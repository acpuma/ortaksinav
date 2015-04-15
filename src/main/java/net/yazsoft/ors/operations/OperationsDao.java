package net.yazsoft.ors.operations;

import com.sun.faces.util.CollectionsUtils;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.upload.UploadsBean;
import net.yazsoft.frame.upload.UploadsDao;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.answers.AnswersDao;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.examsParameters.ExamsParametersDao;
import net.yazsoft.ors.examsParameters.ExamsParametersDto;
import net.yazsoft.ors.examsParameters.ExamsParametersTypeDao;
import net.yazsoft.ors.lessons.LessonsDao;
import net.yazsoft.ors.lessons.LessonsDto;
import net.yazsoft.ors.schools.SchoolsClassDao;
import net.yazsoft.ors.schools.SchoolsClassDto;
import net.yazsoft.ors.students.*;
import org.apache.log4j.Logger;
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
    List<String> lines;
    Boolean deleteOld;
    Integer lessonsCount;

    @Inject UploadsDao uploadsDao;
    @Inject UploadsBean uploadsBean;
    @Inject ExamsParametersDao examsParametersDao;
    @Inject ExamsParametersTypeDao examsParametersTypeDao;
    @Inject LessonsDao lessonsDao;
    @Inject StudentsDao studentsDao;
    @Inject StudentsAnswersDao studentsAnswersDao;
    @Inject SchoolsClassDao schoolsClassDao;
    @Inject AnswersDao answersDao;

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
        lines = new ArrayList<>();
        //TODO: Get parameter ids from db
    }

    public void count() {
        try {
            fillGrids();
            Integer trues=0,falses=0,nulls=0;
            Float nets;
            Float score;
            Float questionScore;
            Lessons lesson;
            //Map<Long,>
            for (StudentsAnswersDto dto:answersDto) {
                Integer falseType=Integer.parseInt(Util.getActiveExam().getRefFalseType().getName());
                logger.info("FALSETYPE : " + falseType);
                trues=0; falses=0; nulls=0; nets=0f;
                lesson=dto.getRefLesson();
            //for (Lessons lesson:Util.getActiveExam().getLessonsCollection()){
                Map<Integer,String> answersMap=answersDao.getLessonAnswersMap(Util.getActiveExam(),lesson);
                logger.info("answersMap : " + answersMap);
                //StudentsAnswersDto dto=answersDto.get(0);
                    logger.info("studentAnswers : " + dto.getTid() + ", " + dto.getRefStudent() + ", " + dto.getAnswers());
                    String answers = "";
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
                            if (answers.charAt(i) == ' ') {
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

                    score= (100/lesson.getQuestionCount()) * nets;
                    logger.info("LESSON RESULT : trues:" + trues + ", falses : " + falses
                            + ", nulls : " + nulls + ", nets : " + nets + " ,score : " + score);
                    dto.setTrues(trues);
                    dto.setFalses(falses);
                    dto.setNulls(nulls);
                    dto.setNets(nets);
                    dto.setScore(score);
            }

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
                    int index=1;
                    for (StudentsAnswersDto dto:classStudentsDto) {
                        //dto.setRankClass(index);
                        answersDto.get(answersDto.indexOf(dto)).setRankClass(index);
                        index++;
                    }
                }

                //sort students by score in the exam
                //Collections.sort(answersDto, StudentsAnswersDto.Comparators.SCORE);
                int i = 1;
                for (StudentsAnswersDto dto : lessonStudents) {
                    //dto.setRankSchool(i);
                    answersDto.get(answersDto.indexOf(dto)).setRankSchool(i);
                    i++;
                }
            }
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

    @Transactional
    public void confirmTransfer() {
        logger.info("DELETE OLD : " + deleteOld);
        Long tid=null;
        try {
            if (deleteOld) {
                studentsAnswersDao.deleteExamAnswers(Util.getActiveExam());
                for (StudentsAnswersDto dto : answersDto) {
                    schoolsClassDao.saveOrUpdate(dto.getRefStudent().getRefSchoolClass());
                    studentsDao.saveOrUpdate(dto.getRefStudent());
                    tid = studentsAnswersDao.create(dto.toEntity());
                    dto.setTid(tid);
                }
            } else {
                for (StudentsAnswersDto dto : newAnswersDto) {
                    SchoolsClass schoolsClass=schoolsClassDao.findBySchoolAndName(Util.getActiveSchool(),
                            dto.getRefStudent().getRefSchoolClass().getName());
                    if (schoolsClass==null) {
                        schoolsClassDao.saveOrUpdate(dto.getRefStudent().getRefSchoolClass());
                    } else {
                        dto.getRefStudent().setRefSchoolClass(schoolsClass);
                    }
                    studentsDao.saveOrUpdate(dto.getRefStudent());
                    tid = studentsAnswersDao.create(dto.toEntity());
                    dto.setTid(tid);
                }
            }
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
                    if (parameter.getRefParameter().getTid().intValue() == 3) {
                        //TODO: concat class and branch
                        parameterValue = line.substring(parameter.getStart() - 1,
                                parameter.getStart() + parameter.getLength() - 1).trim();
                        //schoolsClass = schoolsClassDao.findBySchoolAndName(Util.getActiveSchool(), parameterValue);
                        if (!classesList.contains(parameterValue)) {
                            SchoolsClassDto tempClass = new SchoolsClassDto();
                            tempClass.setName(parameterValue);
                            tempClass.setRefSchool(Util.getActiveSchool());
                            tempClass.setActive(Boolean.TRUE);
                            //schoolsClassDao.saveOrUpdate(tempClass);
                            classesDto.add(tempClass);
                            newClassesDto.add(tempClass);
                            classesList.add(parameterValue);
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

    public void fillGrids() {
        classes = schoolsClassDao.findBySchool(Util.getActiveSchool());
        for (SchoolsClass sclass : classes) {
            classesList.add(sclass.getName());
            classesDto.add(new SchoolsClassDto(sclass));
        }

        //get students from db and add to dtolist
        students=studentsDao.findBySchool(Util.getActiveSchool());
        for (Students student:students) {
            studentsDto.add(new StudentsDto(student));
        }

        lessonsCount=lessonsDao.getLessonsDtos().size();
        logger.info("LESSON COUNT: " + lessonsCount);
        for (StudentsDto dto:studentsDto) {
            answersViewDto.add(new StudentsAnswersViewDto(dto));
        }

        //get answers from and add to dtolist
        answers=studentsAnswersDao.findByExam(Util.getActiveExam());
        for (StudentsAnswers answer:answers) {
            answersDto.add(new StudentsAnswersDto(answer));
        }
    }

    public void show() {
        lines=readUpload();
    }

    @Transactional
    public void transfer() {
        logger.info("TRANSFERING ....");
        List<Integer> schoolNos;
        try {
            reset();
            upload=uploadsDao.getSelected();
            logger.info("UPLOAD : " + upload);
            lines=readUpload();
            examsParametersDao.getExamParameters(Util.getActiveExam());
            lessonsDao.getExamLessons(Util.getActiveExam());

            fillGrids();
            prepareClasses();

            //get schoolsNos from db and from dat
            schoolNos=getSchoolNos(studentsDto);

            String parameterName;
            String parameterValue;
            String lessonName;
            String lessonAnswers;
            String booklet=null;
            Integer schoolNo=null;
            StudentsDto studentDto=null;
            StudentsAnswersDto answer;
            SchoolsClass schoolsClass;
            StudentsAnswersViewDto answerView=null;
            //get classes and create new ones

            for (String line : lines) {
                //find schoolno first
                for(ExamsParametersDto parameter:examsParametersDao.getParametersDtos()){
                    if (parameter.getRefParameter().getTid().intValue() == 2) {
                        parameterValue = line.substring(parameter.getStart() - 1,
                                parameter.getStart() + parameter.getLength() - 1).trim();
                        schoolNo = Integer.valueOf(parameterValue);
                    }
                }

                //if schoolno not exists add new student
                if (!schoolNos.contains(schoolNo)) {
                    studentDto=new StudentsDto();
                    studentDto.setSchoolNo(schoolNo);
                    studentDto.setActive(Boolean.TRUE);

                    //Get parameters
                    for(ExamsParametersDto parameter:examsParametersDao.getParametersDtos()){
                        //logger.info("PARAMETER :" + parameter);
                        parameterValue=line.substring(parameter.getStart()-1,
                                parameter.getStart()+parameter.getLength()-1).trim();
                        parameterName=parameter.getRefParameter().getNameTr();
                        studentDto.setRefSchool(Util.getActiveSchool());
                        switch (parameter.getRefParameter().getTid().intValue()){
                            case 1: studentDto.setFullname(parameterValue);break;
                            //case 2: schoolNo=Integer.valueOf(parameterValue);break; //already setted
                            case 3:
                                schoolsClass=findSchoolClassByName(parameterValue);
                                studentDto.setRefSchoolClass(schoolsClass);
                                break;

                            case 4: studentDto.setName(parameterValue);break;
                            case 5: studentDto.setSurname(parameterValue);break;
                            case 6: studentDto.setGender(parameterValue);break;
                            case 7: booklet=parameterValue;break;
                            case 8: studentDto.setMernis(parameterValue);break;
                            case 10:studentDto.setPhone(parameterValue);break;
                            default:break;
                        }
                        //logger.info(parameterName + " : " + parameterValue);
                    }
                    studentsDto.add(studentDto);
                    newStudentsDto.add(studentDto);
                    answerView=new StudentsAnswersViewDto(studentDto);
                    answersViewDto.add(answerView);
                } else {
                    //get booklet
                    for(ExamsParametersDto parameter:examsParametersDao.getParametersDtos()){
                        parameterValue=line.substring(parameter.getStart()-1,
                                parameter.getStart()+parameter.getLength()-1).trim();
                        switch (parameter.getRefParameter().getTid().intValue()){
                            case 7: booklet=parameterValue;break;
                            default:break;
                        }

                    }
                    studentDto=findStudent(schoolNo);
                    answerView=findStudentAnswerView(schoolNo);
                }
                answerView.setBooklet(booklet);

                //Students tempStudent=studentsDao.findBySchoolAndNumber(Util.getActiveSchool(),schoolNo);

                //ADD ANSWERS TO DB
                Students studentEntity=null;
                if (studentDto!=null) {
                    studentEntity = studentDto.toEntity();
                }
                int i=0;
                for(LessonsDto lesson:lessonsDao.getLessonsDtos()) {
                    i++;
                    lessonName=lesson.getRefLessonName().getNameTr();
                    lessonAnswers=line.substring(lesson.getStart() - 1,
                            lesson.getStart() + lesson.getQuestionCount());
                    //logger.info(lessonName + " : " + lessonAnswers);
                    answer=null;
                    if ((studentDto.getTid()!=null) && (lesson.getTid()!=null)) {
                        answer = findAnswer((Students) getSession().load(Students.class, studentDto.getTid()),
                                (Lessons) getSession().load(Lessons.class, lesson.getTid()));
                        answer.setBooklet(booklet);
                    }
                    if (answer==null){
                        answer=new StudentsAnswersDto();
                        answer.setRefExam(Util.getActiveExam());
                        answer.setRefLesson(lessonsDao.getById(lesson.getTid()));
                        answer.setBooklet(booklet);
                        answer.setAnswers(lessonAnswers);
                        answer.setActive(Boolean.TRUE);
                        if (studentDto!=null) {
                            answer.setRefStudent(studentEntity);
                        }
                        answersDto.add(answer);
                        newAnswersDto.add(answer);
                    } else {
                        answer.setAnswers(lessonAnswers);
                    }
                    answerView.getAnswers().add(lessonAnswers);
                    //studentsAnswersDao.saveOrUpdate(answers);
                }
                //logger.info("ANSWERS : " + answerView.getAnswers());
            }

            logger.info("STUDENTS COUNT : " + studentsDto.size());
            logger.info("NEW STUDENTS : " + newStudentsDto.size());
            logger.info("ANSWERS COUNT : " + answersDto.size());
            logger.info("NEW ANSWERS COUNT : " + newAnswersDto.size());
            examsParametersDao.getParametersDtos().clear();
            lessonsDao.getLessonsDtos().clear();
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }

    private StudentsDto findStudent(Integer schoolNo) {
        for (StudentsDto dto:studentsDto) {
            if (dto.getSchoolNo().equals(schoolNo)) {
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

    private SchoolsClass findSchoolClassByName(String name) {
        for (SchoolsClassDto dto:classesDto) {
            if (dto.getName().equals(name)) {
                return dto.toEntity();
            }
        }
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

    private List<String> readUpload() {
        logger.info("READING FILE ....");
        upload=uploadsDao.getSelected();
        List<String> lines=null;
        try {
            Path upload_path = Paths.get(UploadsDao.getUploadedFilePath(upload));
            Charset charset = Charset.forName("ISO-8859-9");
            lines = Files.readAllLines(upload_path, charset);
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
        return lines;
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
}
