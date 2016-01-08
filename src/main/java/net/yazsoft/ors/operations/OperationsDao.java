package net.yazsoft.ors.operations;

import com.sun.faces.util.CollectionsUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.upload.UploadsBean;
import net.yazsoft.frame.upload.UploadsDao;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.answers.AnswersDao;
import net.yazsoft.ors.answers.AnswersDto;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.examsParameters.ExamsParametersDao;
import net.yazsoft.ors.examsParameters.ExamsParametersDto;
import net.yazsoft.ors.examsParameters.ExamsParametersTypeDao;
import net.yazsoft.ors.lessons.LessonsDao;
import net.yazsoft.ors.lessons.LessonsDto;
import net.yazsoft.ors.results.ResultsDao;
import net.yazsoft.ors.results.ResultsDto;
import net.yazsoft.ors.schools.SchoolsClassDao;
import net.yazsoft.ors.schools.SchoolsClassDto;
import net.yazsoft.ors.students.*;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class OperationsDao extends BaseGridDao<Results> implements Serializable{
    private static final Logger logger = Logger.getLogger(OperationsDao.class);
    Results selected;
    Uploads upload;
    List<SchoolsClass> classes;
    List<String> classesList;
    List<SchoolsClassDto> classesDto;
    List<SchoolsClassDto> newClassesDto;
    List<Students> students;
    List<StudentsDto> studentsDto;
    List<StudentsDto> newStudentsDto;
    List<StudentsAnswers> answers;
    List<StudentsAnswersDto> answersDto;
    List<StudentsAnswersDto> newAnswersDto;
    List<StudentsAnswersViewDto> answersViewDto;
    List<Results> results;
    List<ResultsDto> resultsDtos;
    List<String> lines;
    Boolean deleteOld=Boolean.FALSE;
    Boolean autoAddAnswers=Boolean.FALSE;

    Boolean updateFields=Boolean.FALSE;
    Boolean updateFullname=Boolean.FALSE;
    Boolean updateNameSurname=Boolean.FALSE;
    Boolean updateGender=Boolean.FALSE;
    Boolean updatePhone=Boolean.FALSE;
    Boolean updateMernis=Boolean.FALSE;

    Integer lessonsCount;
    Map<Lessons,Map<Integer,String>> lessonAnswerMap;
    String fileContent;

    @Inject UploadsDao uploadsDao;
    @Inject UploadsBean uploadsBean;
    @Inject ExamsParametersDao examsParametersDao;
    @Inject ExamsParametersTypeDao examsParametersTypeDao;
    @Inject LessonsDao lessonsDao;
    @Inject StudentsDao studentsDao;
    @Inject StudentsAnswersDao studentsAnswersDao;
    @Inject SchoolsClassDao schoolsClassDao;
    @Inject AnswersDao answersDao;
    @Inject ResultsDao resultsDao;
    @Inject private BCryptPasswordEncoder encoder;

    @PostConstruct
    public void init() {
        reset();
    }

    public void reset() {
        classes=new ArrayList<>();
        classesList = new ArrayList<>();
        classesDto = new ArrayList<>();
        newClassesDto = new ArrayList<>();
        students = new ArrayList<>();
        studentsDto = new ArrayList<>();
        newStudentsDto=new ArrayList<>();
        answers=new ArrayList<>();
        answersDto=new ArrayList<>();
        newAnswersDto=new ArrayList<>();
        answersViewDto=new ArrayList<>();
        results=new ArrayList<>();
        resultsDtos=new ArrayList<>();

        lines = new ArrayList<>();
        lessonAnswerMap=new LinkedHashMap<>();
        //TODO: Get parameter ids from db
    }

    public void fillGrids() {
        classesDto.clear();
        classes = schoolsClassDao.findBySchool(Util.getActiveSchool());
        for (SchoolsClass sclass : classes) {
            classesList.add(sclass.getName());
            classesDto.add(new SchoolsClassDto(sclass));
        }

        lessonsCount=lessonsDao.getLessonsDtos().size();
        logger.info("LESSON COUNT: " + lessonsCount);

        //get students from db and add to dtolist
        studentsDto.clear();
        students=studentsDao.findBySchool(Util.getActiveSchool());
        for (Students student:students) {
            studentsDto.add(new StudentsDto(student));
        }

        /*
        for (StudentsDto dto:studentsDto) {
            answersViewDto.add(new StudentsAnswersViewDto(dto));
        }
        */

        //get answers from and add to dtolist
        answersDto.clear();
        answers=studentsAnswersDao.findByExam(Util.getActiveExam());
        for (StudentsAnswers answer:answers) {
            answersDto.add(new StudentsAnswersDto(answer));
        }
        logger.info("LOG01540: STUDENT ANSWERS COUNT : " + answersDto.size());

        //get results
        resultsDtos.clear();
        results=resultsDao.findByExam(Util.getActiveExam());
        for (Results result:results) {
            resultsDtos.add(new ResultsDto(result));
        }
        logger.info("RESULTS COUNT : " + results.size());
    }

    public void show() {
        lines=readUpload();
    }


    @Transactional
    public void transfer() {
        logger.info("TRANSFERING ....");
        List<Integer> schoolNos=null;
        List<String> mernisNos=null;
        try {
            reset();
            upload=uploadsDao.getSelected();
            logger.info("UPLOAD : " + upload);
            lines=readUpload();
            examsParametersDao.getExamParameters(Util.getActiveExam());
            lessonsDao.getExamLessons(Util.getActiveExam());

            fillGrids();
            prepareClasses();
            getAnswersAutomatic();

            //get schoolsNos/mernisNos from db and from dat
            if (Util.getActiveSchool().getUseMernis().equals(Boolean.TRUE)) {
                mernisNos = getMernisNos(studentsDto);
            } else {
                schoolNos = getSchoolNos(studentsDto);
            }

            String parameterName;
            String parameterValue;
            String lessonName;
            String lessonAnswers = null;
            String booklet=null;
            Integer schoolNo=null;
            String mernisNo=null;
            StudentsDto studentDto=null;
            StudentsAnswersDto answer;
            SchoolsClass schoolsClass;
            StudentsAnswersViewDto answerView=null;
            //get classes and create new ones

            for (String line : lines) {
                //find schoolno first
                try {
                    String nameSurname = null;
                    for (ExamsParametersDto parameter : examsParametersDao.getParametersDtos()) {
                        if (parameter.getRefParameter().getTid().intValue() == 1) { //ad soyad
                            parameterValue = line.substring(parameter.getStart() - 1,
                                    parameter.getStart() + parameter.getLength() - 1).trim();
                            if ((parameterValue != null) && (!parameterValue.equals(""))) {
                                nameSurname = parameterValue;
                            }
                        }
                        if (parameter.getRefParameter().getTid().intValue() == 2) {
                            parameterValue = line.substring(parameter.getStart() - 1,
                                    parameter.getStart() + parameter.getLength() - 1).trim();
                            if ((parameterValue != null) && (!parameterValue.equals(""))) {
                                try {
                                    schoolNo = Integer.valueOf(parameterValue);
                                } catch (Exception e) {
                                    //Util.setFacesMessageError("OKUL NO HATASI. SATIR : " + line + e.getMessage());
                                    Util.catchException(e);
                                }
                            }
                        }
                        if (parameter.getRefParameter().getTid().intValue() == 8) {
                            parameterValue = line.substring(parameter.getStart() - 1,
                                    parameter.getStart() + parameter.getLength() - 1).trim();
                            if ((parameterValue != null) && (!parameterValue.equals(""))) {
                                mernisNo = parameterValue;
                            }
                        }
                    }
                    //logger.info("CEVAPMI? : "+line.substring(0,5));
                    if (!nameSurname.equals("CEVAP")) {
                        Boolean isnew = false;
                        //if schoolno not exists add new student
                        if (Util.getActiveSchool().getUseMernis().equals(true)) {
                            if (!mernisNos.contains(mernisNo)) isnew = true;
                        } else {
                            if (!schoolNos.contains(schoolNo)) isnew = true;
                        }
                        if (isnew) {
                            studentDto = new StudentsDto();
                            studentDto.setSchoolNo(schoolNo);
                            studentDto.setMernis(mernisNo);
                            studentDto.setActive(Boolean.TRUE);
                            studentDto.setVersion(0);
                            studentDto.setRefSchool(Util.getActiveSchool());

                            //Get parameters
                            for (ExamsParametersDto parameter : examsParametersDao.getParametersDtos()) {
                                //logger.info("PARAMETER :" + parameter);
                                parameterValue = line.substring(parameter.getStart() - 1,
                                        parameter.getStart() + parameter.getLength() - 1).trim();
                                parameterName = parameter.getRefParameter().getNameTr();
                                switch (parameter.getRefParameter().getTid().intValue()) {
                                    case 1:
                                        studentDto.setFullname(parameterValue);
                                        break;
                                    //case 2: schoolNo=Integer.valueOf(parameterValue);break; //already setted
                                    case 3:
                                        schoolsClass = findSchoolClassByName(parameterValue);
                                        studentDto.setRefSchoolClass(schoolsClass);
                                        break;
                                    case 4:
                                        studentDto.setName(parameterValue);
                                        break;
                                    case 5:
                                        studentDto.setSurname(parameterValue);
                                        break;
                                    case 6:
                                        studentDto.setGender(parameterValue);
                                        break;
                                    case 7:
                                        booklet = parameterValue;
                                        break;
                                    case 8:
                                        studentDto.setMernis(parameterValue);
                                        break;
                                    case 9:
                                        studentDto.setPhone(parameterValue);
                                        break;
                                    default:
                                        break;
                                }
                                //logger.info(parameterName + " : " + parameterValue);
                            }

                        /*
                        String credit=Util.getActiveSchool().getMebCode().concat(schoolNo.toString());
                        studentDto.setUsername(credit);
                        studentDto.setPassword(encoder.encode(credit));
                         */
                            studentsDto.add(studentDto);
                            newStudentsDto.add(studentDto);
                            answerView = new StudentsAnswersViewDto(studentDto);
                            answersViewDto.add(answerView);
                        } else {

                            if (Util.getActiveSchool().getUseMernis().equals(true)) {
                                studentDto = findStudentByMernis(mernisNo);
                            } else {
                                studentDto = findStudentBySchoolno(schoolNo);
                            }
                            //get booklet
                            for (ExamsParametersDto parameter : examsParametersDao.getParametersDtos()) {
                                parameterValue = line.substring(parameter.getStart() - 1,
                                        parameter.getStart() + parameter.getLength() - 1).trim();
                                switch (parameter.getRefParameter().getTid().intValue()) {
                                    case 1:
                                        studentDto.setFullname(parameterValue);
                                        break;
                                    case 4:
                                        studentDto.setName(parameterValue);
                                        break;
                                    case 5:
                                        studentDto.setSurname(parameterValue);
                                        break;
                                    case 6:
                                        studentDto.setGender(parameterValue);
                                        break;
                                    case 7:
                                        booklet = parameterValue;
                                        break;
                                    case 9:
                                        studentDto.setPhone(parameterValue);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            answerView = new StudentsAnswersViewDto(studentDto);
                            answersViewDto.add(answerView);
                            //answerView = findStudentAnswerView(schoolNo);
                        }
                        answerView.setBooklet(booklet);

                        //Students tempStudent=studentsDao.findBySchoolAndNumber(Util.getActiveSchool(),schoolNo);

                        //ADD ANSWERS TO DB
                        Students studentEntity = null;
                        if (studentDto != null) {
                            studentEntity = studentDto.toEntity();
                        }
                        int i = 0;
                        for (LessonsDto lesson : lessonsDao.getLessonsDtos()) {
                            i++;
                            lessonName = lesson.getRefLessonName().getNameTr();
                            //if (line.length() > lesson.getStart() + lesson.getQuestionCount()) {
                            lessonAnswers = line.substring(lesson.getStart() - 1,
                                    lesson.getStart() + lesson.getQuestionCount() - 1);
                            //}
                            //if (lessonAnswers==null){
                            //logger.info("LESSON : " + lessonName + " : " + lessonAnswers);
                            //logger.info("LINE : " + line);
                            //logger.info("LINE LENGTH : " + line.length());
                            //}
                            answer = null;
                            if ((studentDto.getTid() != null) && (lesson.getTid() != null)) {
                                answer = findAnswer((Students) getSession().load(Students.class, studentDto.getTid()),
                                        (Lessons) getSession().load(Lessons.class, lesson.getTid()));
                                if (answer != null) {
                                    answer.setBooklet(booklet);
                                }
                            }
                            if (answer == null) {
                                answer = new StudentsAnswersDto();
                                answer.setRefSchool(Util.getActiveSchool());
                                answer.setRefExam(Util.getActiveExam());
                                answer.setRefLesson(lessonsDao.getById(lesson.getTid()));
                                answer.setBooklet(booklet);
                                answer.setAnswers(lessonAnswers);
                                answer.setActive(Boolean.TRUE);
                                if (studentDto != null) {
                                    answer.setRefStudent(studentEntity);
                                }
                                answersDto.add(answer);
                                newAnswersDto.add(answer);
                            } else {
                                answer.setAnswers(lessonAnswers);
                                if (studentDto != null) {
                                    answer.setRefStudent(studentEntity);
                                }
                            }
                            answerView.getAnswers().add(lessonAnswers);
                            //studentsAnswersDao.saveOrUpdate(answers);
                        }
                        //logger.info("ANSWERS : " + answerView.getAnswers());
                    }
                } catch (Exception e) {
                    logger.error("LOG02930: ERROR LINE : " + line );
                    logger.error("LOG02940: " + e.getMessage());
                    Util.setFacesMessageError("HATA SATIRI : " + line);
                    Util.catchException(e);
                }
            }

            logger.info("STUDENTS COUNT : " + studentsDto.size());
            logger.info("NEW STUDENTS : " + newStudentsDto.size());
            logger.info("ANSWERS COUNT : " + answersDto.size());
            logger.info("NEW ANSWERS COUNT : " + newAnswersDto.size());
            logger.info("ANSWER VIEW COUNT : " + answersViewDto.size());
            examsParametersDao.getParametersDtos().clear();
            lessonsDao.getLessonsDtos().clear();
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }


    @Transactional
    public void confirmTransfer() {
        logger.info("DELETE OLD : " + deleteOld);
        Long tid=null;
        try {
            if (autoAddAnswers) {
                answersDao.saveAutoAnswers(Util.getActiveExam());
            }

            //CREATE NEW CLASSES
            for (SchoolsClassDto classDto:newClassesDto) {
                schoolsClassDao.saveOrUpdate(classDto.toEntity());
                //logger.info("LOG02900: SAVED CLASS : " + classtid + " : " + classDto.getName());
            }
            getSession().flush();
            getSession().clear();

            //CREATE OR UPDATE STUDENT INFO
            SchoolsClass schoolsClass=null;
            Students stuEntity=null;
            for (StudentsDto studentDto: studentsDto) {
                //set school class for student
                schoolsClass=null;
                stuEntity=null;
                if ((studentDto.getRefSchoolClass()!=null)
                        && (studentDto.getRefSchoolClass().getName()!=null)
                        && (!studentDto.getRefSchoolClass().getName().equals("")) ) {
                    schoolsClass = schoolsClassDao.findBySchoolAndName(Util.getActiveSchool(),
                            studentDto.getRefSchoolClass().getName());
                }
                if (schoolsClass!=null) {
                    studentDto.setRefSchoolClass(schoolsClass);
                }

                if (studentDto.getTid()==null) {
                    studentDto = studentsDao.prepareCredits(studentDto);
                    studentsDao.saveOrUpdate(studentDto.toEntity());
                } else {
                    if (updateFields) {
                        if ( (updateMernis) && (Util.getActiveSchool().getUseMernis()) ) {
                            studentDto = studentsDao.prepareCredits(studentDto);
                        }
                        stuEntity=(Students)getSession().load(Students.class,studentDto.getTid());
                        studentsDao.updateStudentFields(studentDto.toEntity(stuEntity),updateNameSurname,updateFullname,
                                updateGender,updatePhone,updateMernis);
                    }
                }
            }
            //getSession().flush();
            //getSession().clear();

            //CREATE STUDENT ANSWERS
            if (deleteOld) {
                studentsAnswersDao.deleteExamAnswers(Util.getActiveExam());
                for (StudentsAnswersDto dto : answersDto) {
                    dto.setRefSchool(Util.getActiveSchool());
                    //find saved student and set it to dto
                    stuEntity=studentsDao.findByMernis(dto.getRefStudent().getMernis());
                    dto.setRefStudent(stuEntity);
                    studentsAnswersDao.create(dto.toEntity());
                }
            } else {
                for (StudentsAnswersDto dto : newAnswersDto) {
                    stuEntity=studentsDao.findByMernis(dto.getRefStudent().getMernis());
                    dto.setRefStudent(stuEntity);
                    dto.setRefSchool(Util.getActiveSchool());
                    studentsAnswersDao.saveOrUpdate(dto.toEntity());
                }
            }
            getSession().flush();
            getSession().clear();

            if ((upload.getProcessed()==null) || (!upload.getProcessed().equals(Boolean.TRUE) )){
                upload.setProcessed(Boolean.TRUE);
                uploadsDao.update(upload);
            }
            reset();
            fillGrids();
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void count() {
        try {
            fillGrids();
            Float trues,falses,nulls;
            Float nets;
            Float score;
            Float questionScore;
            Lessons lesson;
            Map<Integer,String> answersMap;
            Integer cancelCount;

            Integer falseType=Integer.parseInt(Util.getActiveExam().getRefFalseType().getName());
            logger.info("FALSETYPE : " + falseType);
            //find trues,falses,nulls,score
            logger.info("LOG01530: ANSWERS COUNT : " + answersDto.size());
            for (StudentsAnswersDto dto:answersDto) {
                trues=0f; falses=0f; nulls=0f; nets=0f;
                lesson=dto.getRefLesson();
                cancelCount=answersDao.findCancelCount(lesson);
                //logger.info("LOG02830: CANCELCOUNT : " + cancelCount);
                //for (Lessons lesson:Util.getActiveExam().getLessonsCollection()){
                //Map<Integer,String> answersMap=answersDao.getLessonAnswersMap(Util.getActiveExam(),lesson);
                answersMap=getLessonAnswersMap(lesson);
                logger.info("answersMap : " + answersMap);
                //StudentsAnswersDto dto=answersDto.get(0);
                //logger.info("studentAnswers : " + dto.getTid() + ", " + dto.getRefStudent() + ", " + dto.getAnswers());
                String answers = ""; //real answers
                switch (dto.getBooklet()) {
                    case "A": answers = answersMap.get(0);break;
                    case "B": answers = answersMap.get(1);break;
                    case "C": answers = answersMap.get(2);break;
                    case "D": answers = answersMap.get(3);break;
                    case "E": answers = answersMap.get(4);break;
                    case "F": answers = answersMap.get(5);break;
                    case "G": answers = answersMap.get(6);break;
                    case "H": answers = answersMap.get(7);break;
                }
                for (int i = 0; i < lesson.getQuestionCount(); i++) {
                    if (answers.length()>i) {
                        if (answers.charAt(i)=='X') { //Herkes icin dogru
                            trues++;
                        } else if (answers.charAt(i) == 'Y') { //herkes icin yanlis
                            falses++;
                        }else if (answers.charAt(i)=='Z') { //Iptal edilmis
                            //    do nothing
                        } else if (dto.getAnswers().charAt(i) == ' ') {
                            nulls++;
                        } else {
                            if (answers.charAt(i) == dto.getAnswers().charAt(i)) {
                                trues++;
                            } else {
                                falses++;
                            }
                        }
                    }
                }

                if (falseType.equals(0)) {
                    nets = (float) (trues);
                } else {
                    nets = (float) ((float)trues - ((float)falses/falseType));
                }
                if (nets<0) nets=0f;

                score= ( (float) 100/(lesson.getQuestionCount()-cancelCount) ) * nets;

                //if all trues, score is 100
                if (lesson.getQuestionCount()-cancelCount==trues) {
                    score=(float)100;
                }
                logger.info("LESSON RESULT : trues:" + trues + ", falses : " + falses
                        + ", nulls : " + nulls + ", nets : " + nets + " ,score : " + score );
                dto.setTrues(trues.intValue());
                dto.setFalses(falses.intValue());
                dto.setNulls(nulls.intValue());
                dto.setNets(nets);
                dto.setScore( Util.round(score,2) );
            }
            Collections.sort(answersDto);
            Collections.reverse(answersDto);

            int totalQuestionCount=0;
            int totalCancelCount=0;
            for (Lessons lesson1:Util.getActiveExam().getLessonsCollection()){
                //getSession().refresh(lesson1);
                totalQuestionCount+=lesson1.getQuestionCount();
                int lessonCancelCount=answersDao.findCancelCount(lesson1);
                totalCancelCount += lessonCancelCount;
                logger.info("LOG02840: LESSON / cancelcount : " + lesson1.getRefLessonName().getNameTr()
                        + " / " + lessonCancelCount);
            }
            logger.info("TOTAL QUESTION COUNT : " + totalQuestionCount);
            logger.info("TOTAL CANCEL COUNT : " + totalCancelCount);

            //get only students with results
            List<Students> resultStudents=new ArrayList<>();
            for (Students student:students) {
                if (!findStudentAnswers(student).isEmpty()){
                    resultStudents.add(student);
                }
            }
            logger.info("RESULTS STUDENTS COUNT : " + resultStudents.size());

            ResultsDto result;
            Boolean newresult=false;
            for (Students student:resultStudents) {
                newresult=false;
                result=findStudentResult(student);
                if (result==null) {
                    result=new ResultsDto();
                    newresult=true;
                    logger.info("LOG01580: NEW RESULT");
                }
                List<StudentsAnswersDto> studentsAnswersDtos=findStudentAnswers(student);
                score=0f; trues=0f; falses=0f; nulls=0f; nets=0f;
                for (StudentsAnswersDto dto:studentsAnswersDtos) {
                    //score+=dto.getScore();
                    trues+=dto.getTrues();
                    falses+=dto.getFalses();
                    nulls+=dto.getNulls();
                    nets+=dto.getNets();
                }
                if (falseType.equals(0)) {
                    nets = (float) (trues);
                } else {
                    nets = (float) ((float)trues - ((float)falses/falseType));
                }
                if (nets<0) nets=0f;

                //score=score/lessonsCount;
                int totalcount=totalQuestionCount-totalCancelCount;
                score = ( (float) 100 / (totalQuestionCount-totalCancelCount) ) * nets;
                logger.info("EXAM RESULT : trues:" + trues + ", falses : " + falses
                        + ", nulls : " + nulls + ", nets : " + nets + " ,score : " + score + ", qc:" +totalQuestionCount
                        + " , totalCount : " + totalcount);

                //if all trues, set score 100
                if (totalQuestionCount-totalCancelCount==trues) {
                    score = (float) 100;
                }
                result.setRefStudent(student);
                result.setRefExam(Util.getActiveExam());
                result.setScore( Util.round(score,2) );
                result.setTrues(trues.intValue());
                result.setFalses(falses.intValue());
                result.setNulls(nulls.intValue());
                result.setNets(nets);
                result.setRefSchool(Util.getActiveSchool());
                if (newresult==true) {
                    resultsDtos.add(result);
                }
            }
            Collections.sort(resultsDtos);
            Collections.reverse(resultsDtos);
            logger.info("LOG01550: RESULTSDTOS COUNT : " + resultsDtos.size() );

            //sort by score all students in the exam
            int i = 1;
            int j=1;
            ResultsDto oldResultDto=new ResultsDto();
            for (ResultsDto dto : resultsDtos) {
                j++;
                if (i>1) {
                    if (!dto.getScore().equals(oldResultDto.getScore())){
                        i=j;
                    }
                } else {
                    i++;
                }
                dto.setRankSchool(i - 1);
                oldResultDto=dto;
            }

            //sort students by score in the class
            for (SchoolsClass schoolsClass:classes) {
                logger.info("SCHOOLCLASS : " + schoolsClass);
                List<ResultsDto>  classStudentsDto= resultsDtos.stream()
                        .filter(a -> a.getRefStudent().getRefSchoolClass().equals(schoolsClass)).collect(Collectors.toList());
                logger.info("class students : " + classStudentsDto );
                Collections.sort(classStudentsDto);
                Collections.reverse(classStudentsDto);
                i=1; j=1;
                oldResultDto=new ResultsDto();
                for (ResultsDto dto:classStudentsDto) {
                    j++;
                    if (i>1) {
                        if (!dto.getScore().equals(oldResultDto.getScore())) {
                            i=j;
                        }
                    } else { //index=1
                        i++;
                    }
                    dto.setRankClass(i-1);
                    if (dto.getTid()!=null) {
                        findResultDto(dto.getTid()).setRankClass(i - 1);
                    }
                    oldResultDto=dto;
                }
            }

            resultsDao.DtosToEntities(resultsDtos);

            //find ranks
            for (Lessons lesson1:Util.getActiveExam().getLessonsCollection()) {
                List<StudentsAnswersDto>  lessonStudents= answersDto.stream()
                        .filter(p -> p.getRefLesson().equals(lesson1)).collect(Collectors.toList());
                Collections.sort(lessonStudents);
                Collections.reverse(lessonStudents);

                //sort students by score in the class
                for (SchoolsClass schoolsClass:classes) {
                    //logger.info("SCHOOLCLASS : " + schoolsClass);
                    List<StudentsAnswersDto>  classStudentsDto= lessonStudents.stream()
                            .filter(a -> a.getRefStudent().getRefSchoolClass().equals(schoolsClass)).collect(Collectors.toList());
                    //logger.info("class students : " + classStudentsDto );
                    Collections.sort(classStudentsDto);
                    Collections.reverse(classStudentsDto);
                    i=1; j=1;
                    StudentsAnswersDto oldDto=new StudentsAnswersDto();
                    StudentsAnswersDto realDto;
                    for (StudentsAnswersDto dto:classStudentsDto) {
                        //dto.setRankClass(index);
                        dto.setRankClass(i);
                        //logger.info("INDEX : " + index);
                        realDto=answersDto.get(answersDto.indexOf(dto));
                        j++;
                        if (i>1) {
                            if (!realDto.getScore().equals(oldDto.getScore())) {
                                i=j;
                            }
                        } else { //index=1
                            i++;
                        }
                        answersDto.get(answersDto.indexOf(dto)).setRankClass(i-1);
                        oldDto=dto;
                    }
                }

                //sort students by score in the exam
                Collections.sort(answersDto, StudentsAnswersDto.Comparators.SCORE);
                i = 1; j=1;
                StudentsAnswersDto oldDto=new StudentsAnswersDto();
                StudentsAnswersDto realDto;
                for (StudentsAnswersDto dto : lessonStudents) {
                    dto.setRankSchool(i);
                    realDto=answersDto.get(answersDto.indexOf(dto));
                    j++;
                    if (i>1) {
                        if (!realDto.getScore().equals(oldDto.getScore())){
                            i=j;
                        }
                    } else {
                        i++;
                    }
                    answersDto.get(answersDto.indexOf(dto)).setRankSchool(i-1);
                    oldDto=dto;
                }
            }

            //save
            StudentsAnswers answer;
            for (StudentsAnswersDto dto : answersDto) {
                answer = (StudentsAnswers) getSession().load(StudentsAnswers.class, dto.getTid());
                studentsAnswersDao.saveOrUpdate(dto.toEntity(answer));
            }

        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }


    public Map<Integer,String> getLessonAnswersMap(Lessons lesson1) {
        Map<Integer,String> answersMap;
        if (!lessonAnswerMap.containsKey(lesson1)) {
            answersMap=answersDao.getLessonAnswersMap(Util.getActiveExam(),lesson1);
            lessonAnswerMap.put(lesson1,answersMap);
            //logger.info("NEW LESSON : " + lesson1);
        } else {
            answersMap=lessonAnswerMap.get(lesson1);
            //logger.info("OLD LESSON ! : " + lesson1);
        }
        //logger.info("lessonAnswerMap : " + lessonAnswerMap);
        return answersMap;
    }

    public ResultsDto findResultDto(Long resultid) {
        for (ResultsDto dto:resultsDtos) {
            if ( (dto.getTid()!=null) && (dto.getTid().equals(resultid)) ) {
                return dto;
            }
        }
        return null;
    }





    public void prepareClasses() {
        try {
            logger.info("PREPARING CLASSES ...");
            //List<String> lines = readUpload();

            examsParametersDao.getExamParameters(Util.getActiveExam());
            logger.info("CLASSES FROM DB:" + classes);


            SchoolsClass schoolsClass;
            SchoolsClassDto schoolsClassDto;
            String parameterName;
            String parameterValue;

            for (String line : lines) {
                for (ExamsParametersDto parameter : examsParametersDao.getParametersDtos()) {
                    //if cevap continue next
                    if (parameter.getRefParameter().getTid().intValue() == 1) { //ad soyad
                        parameterValue = line.substring(parameter.getStart() - 1,
                                parameter.getStart() + parameter.getLength() - 1).trim();
                        if ((parameterValue != null) && (!parameterValue.equals(""))) {
                            if (!parameterValue.equals("CEVAP")) {
                                continue;
                            }
                        }
                    }
                    if (parameter.getRefParameter().getTid().intValue() == 3) {
                        //TODO: concat class and branch
                        parameterValue = line.substring(parameter.getStart() - 1,
                                parameter.getStart() + parameter.getLength() - 1).trim();
                        //schoolsClass = schoolsClassDao.findBySchoolAndName(Util.getActiveSchool(), parameterValue);
                        if ( (parameterValue!=null)
                                && (!parameterValue.equals(""))
                                && (!classesList.contains(parameterValue)) ) {
                            SchoolsClassDto tempClass = new SchoolsClassDto();
                            tempClass.setName(parameterValue);
                            tempClass.setRefSchool(Util.getActiveSchool());
                            tempClass.setActive(Boolean.TRUE);
                            //schoolsClassDao.saveOrUpdate(tempClass);
                            classesDto.add(tempClass);
                            newClassesDto.add(tempClass);
                            classesList.add(parameterValue);
                            logger.info("LOG02890: ADDING CLASS : " + parameterValue);
                        }
                    }
                }
            }
            logger.info("CLASSES : " + classesDto);
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }


    public void getAnswersAutomatic() {
        String lessonAnswers;
        String parameterValue=null;
        //String parameterName;
        String booklet="";
        try {
            answersDao.getAnswersAutos().clear();
            for (String line : lines) {

                    //get booklet
                    String nameSurname=null;
                    for (ExamsParametersDto parameter : examsParametersDao.getParametersDtos()) {
                        //logger.info("LOG02810: " + parameter.getRefParameter().getNameTr());
                        //logger.info("LOG02790: " + (parameter.getStart() - 1));
                        //logger.info("LOG02800: " + (parameter.getStart() + parameter.getLength() - 1));
                        if (parameter.getRefParameter().getTid().intValue() == 1) { //ad soyad
                            parameterValue = line.substring(parameter.getStart() - 1,
                                    parameter.getStart() + parameter.getLength() - 1).trim();
                            if ((parameterValue != null) && (!parameterValue.equals(""))) {
                                nameSurname = parameterValue;
                            }
                        }


                        if ( (parameter.getStart()+parameter.getLength()-1) < line.length() ) {
                            parameterValue = line.substring(parameter.getStart() - 1,
                                    parameter.getStart() + parameter.getLength() - 1).trim();
                        }
                        if (parameter.getRefParameter().getTid().intValue() == 7) { //booklet
                            booklet = parameterValue;
                        }

                    }
                    /*
                    booklet=line.substring(6,7);
                    */

                if (nameSurname.equals("CEVAP")) {
                    logger.info("BOOKLET : " + booklet);
                    List<LessonsDto> lessonsDtos=lessonsDao.getLessonsDtos();
                    logger.info("LESSONS : " + lessonsDtos);
                    for (LessonsDto lesson : lessonsDtos) {
                        //if (line.length() > (lesson.getStart() + lesson.getQuestionCount())) {
                            lessonAnswers = line.substring(lesson.getStart() - 1,
                                    lesson.getStart() + lesson.getQuestionCount() - 1 );
                            answersDao.prepareAutoAnswers(Util.getActiveExam(),
                                    lessonsDao.getById(lesson.getTid()), booklet, lessonAnswers);
                        //}
                    }


                    /*
                    for (ExamsParametersDto parameter : examsParametersDao.getParametersDtos()) {
                        //logger.info("PARAMETER :" + parameter);
                        parameterValue = line.substring(parameter.getStart() - 1,
                                parameter.getStart() + parameter.getLength() - 1).trim();
                        parameterName = parameter.getRefParameter().getNameTr();
                        //studentDto.setRefSchool(Util.getActiveSchool());
                        if (parameter.getRefParameter().getTid().intValue() == 1) { //fullname
                            if (parameterValue.equals("CEVAP")) {
                                logger.info("Lesson size : " + lessonsDao.getLessonsDtos().size());
                                for (LessonsDto lesson : lessonsDao.getLessonsDtos()) {
                                    if (line.length() > (lesson.getStart() + lesson.getQuestionCount())) {
                                        lessonAnswers = line.substring(lesson.getStart() - 1,
                                                lesson.getStart() + lesson.getQuestionCount());
                                        answersDao.prepareAutoAnswers(Util.getActiveExam(),
                                                lessonsDao.getById(lesson.getTid()), booklet, lessonAnswers);
                                    }
                                }
                            }
                        }
                    }
                    */
                }
            }

        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }

    }

    private StudentsDto findStudentBySchoolno(Integer schoolNo) {
        for (StudentsDto dto:studentsDto) {
            if (dto.getSchoolNo().equals(schoolNo)) {
                return dto;
            }
        }
        return null;
    }

    private StudentsDto findStudentByMernis(String mernisNo) {
        for (StudentsDto dto:studentsDto) {
            if (dto.getMernis().equals(mernisNo)) {
                return dto;
            }
        }
        return null;
    }
    private StudentsAnswersViewDto findStudentAnswerView(Integer schoolNo) {
        for (StudentsAnswersViewDto dto:answersViewDto) {
            if (dto.getSchoolNo().equals(schoolNo)) {
                return dto;
            }
        }
        return null;
    }

    private StudentsAnswersDto findAnswer(Students student,Lessons lesson) {
        for (StudentsAnswersDto answer:answersDto) {
            if ( (answer.getRefStudent().equals(student))
                    && (answer.getRefLesson().equals(lesson))
                    )
                return answer;
        }
        return null;
    }

    private List<StudentsAnswersDto> findStudentAnswers(Students student) {
        List<StudentsAnswersDto> studentsAnswersDtos=new ArrayList<>();
        for (StudentsAnswersDto answer:answersDto) {
            if (answer.getRefStudent().equals(student)) {
                studentsAnswersDtos.add(answer);
            }
        }
        return studentsAnswersDtos;
    }


    private SchoolsClass findSchoolClassByName(String name) {
        for (SchoolsClassDto dto:classesDto) {
            if (dto.getName().equals(name)) {
                return dto.toEntity();
            }
        }
        return null;
    }

    private ResultsDto findStudentResult(Students student) {
        for (ResultsDto result:resultsDtos) {
            if ((student!=null) && (result.getRefStudent()!=null)) {
                if ( (result.getRefStudent().getTid().equals(student.getTid())) ) {
                    logger.info("LOG01570: RESULT : " + result);
                    return result;
                }
            }
        }
        logger.info("LOG01560: RESULT NULL");
        return null;
    }

    public List<Integer> getSchoolNos(List<StudentsDto> studentsDto) {
        List<Integer> nos=new ArrayList<>();
        for (StudentsDto dto:studentsDto) {
            nos.add(dto.getSchoolNo());
        }
        /*
        String parameterValue;
        Integer schoolNo;
        for (String line : lines) {
            for (ExamsParametersDto parameter : examsParametersDao.getParametersDtos()) {
                if (parameter.getRefParameter().getTid().intValue() == 2) {
                    parameterValue = line.substring(parameter.getStart() - 1,
                            parameter.getStart() + parameter.getLength() - 1).trim();
                    schoolNo = Integer.valueOf(parameterValue);
                    nos.add(schoolNo);
                }
            }
        }
        */
        return nos;

    }

    /**
     * Finds list of mernis nos from studentsdto list
     * @param studentsDto
     * @return list of mernis nos
     */
    public List<String> getMernisNos(List<StudentsDto> studentsDto) {
        List<String> nos=new ArrayList<>();
        for (StudentsDto dto:studentsDto) {
            nos.add(dto.getMernis());
        }
        return nos;

    }

    private List<String> readUpload() {
        logger.info("READING FILE ....");
        upload=uploadsDao.getSelected();
        List<String> lines=null;
        try {
            Path upload_path = Paths.get(UploadsDao.getUploadedFilePath(upload));
            Charset charset = Charset.forName("ISO-8859-9");
            lines = Files.readAllLines(upload_path, charset);
            byte[] content=Files.readAllBytes(upload_path);
            fileContent=new String(content,"ISO-8859-9");
            logger.info("LOG01660: FILE : " + fileContent);
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
        return lines;
    }

    public void updateUpload() {
        try {
            Path upload_path = Paths.get(UploadsDao.getUploadedFilePath(upload));
            Files.write(upload_path,fileContent.getBytes("ISO-8859-9"));
            Util.setFacesMessage("KAYIT EDILDI");
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }





    public OperationsDao() {
        super(Results.class);
    }

    public Results getSelected() {
        return selected;
    }

    public void setSelected(Results selected) {
        this.selected = selected;
    }

    public List<SchoolsClassDto> getClassesDto() {
        return classesDto;
    }

    public void setClassesDto(List<SchoolsClassDto> classesDto) {
        this.classesDto = classesDto;
    }

    public List<StudentsDto> getStudentsDto() {
        return studentsDto;
    }

    public void setStudentsDto(List<StudentsDto> studentsDto) {
        this.studentsDto = studentsDto;
    }

    public List<StudentsAnswersDto> getAnswersDto() {
        return answersDto;
    }

    public void setAnswersDto(List<StudentsAnswersDto> answersDto) {
        this.answersDto = answersDto;
    }

    public Boolean getDeleteOld() {
        return deleteOld;
    }

    public void setDeleteOld(Boolean deleteOld) {
        this.deleteOld = deleteOld;
    }

    public List<StudentsAnswersViewDto> getAnswersViewDto() {
        return answersViewDto;
    }

    public void setAnswersViewDto(List<StudentsAnswersViewDto> answersViewDto) {
        this.answersViewDto = answersViewDto;
    }

    public Integer getLessonsCount() {
        return lessonsCount;
    }

    public void setLessonsCount(Integer lessonsCount) {
        this.lessonsCount = lessonsCount;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public Boolean getAutoAddAnswers() {
        return autoAddAnswers;
    }

    public void setAutoAddAnswers(Boolean autoAddAnswers) {
        this.autoAddAnswers = autoAddAnswers;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public Boolean getUpdateFields() {
        return updateFields;
    }

    public void setUpdateFields(Boolean updateFields) {
        this.updateFields = updateFields;
    }

    public Boolean getUpdateFullname() {
        return updateFullname;
    }

    public void setUpdateFullname(Boolean updateFullname) {
        this.updateFullname = updateFullname;
    }

    public Boolean getUpdateNameSurname() {
        return updateNameSurname;
    }

    public void setUpdateNameSurname(Boolean updateNameSurname) {
        this.updateNameSurname = updateNameSurname;
    }

    public Boolean getUpdateGender() {
        return updateGender;
    }

    public void setUpdateGender(Boolean updateGender) {
        this.updateGender = updateGender;
    }

    public Boolean getUpdatePhone() {
        return updatePhone;
    }

    public void setUpdatePhone(Boolean updatePhone) {
        this.updatePhone = updatePhone;
    }

    public Boolean getUpdateMernis() {
        return updateMernis;
    }

    public void setUpdateMernis(Boolean updateMernis) {
        this.updateMernis = updateMernis;
    }
}
