package net.yazsoft.ors.operations;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.upload.UploadsBean;
import net.yazsoft.frame.upload.UploadsDao;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.examsParameters.ExamsParametersDao;
import net.yazsoft.ors.examsParameters.ExamsParametersDto;
import net.yazsoft.ors.examsParameters.ExamsParametersTypeDao;
import net.yazsoft.ors.lessons.LessonsDao;
import net.yazsoft.ors.lessons.LessonsDto;
import net.yazsoft.ors.schools.SchoolsClassDao;
import net.yazsoft.ors.students.StudentsAnswersDao;
import net.yazsoft.ors.students.StudentsDao;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class OperationsDao extends BaseGridDao<Results> implements Serializable{
    private static final Logger logger = Logger.getLogger(OperationsDao.class);
    Results selected;
    Uploads upload;
    List<SchoolsClass> classes;
    List<String> classesList;

    @Inject UploadsDao uploadsDao;
    @Inject UploadsBean uploadsBean;
    @Inject ExamsParametersDao examsParametersDao;
    @Inject ExamsParametersTypeDao examsParametersTypeDao;
    @Inject LessonsDao lessonsDao;
    @Inject StudentsDao studentsDao;
    @Inject StudentsAnswersDao studentsAnswersDao;
    @Inject SchoolsClassDao schoolsClassDao;

    @Transactional
    public void transfer() {
        logger.info("TRANSFERING ....");
        classes=schoolsClassDao.findBySchool(Util.getActiveSchool());
        classesList=new ArrayList<>();
        for(SchoolsClass sclass:classes) {
            classesList.add(sclass.getName());
        }
        upload=uploadsDao.getSelected();
        try {
            examsParametersDao.getExamParameters(Util.getActiveExam());
            lessonsDao.getExamLessons(Util.getActiveExam());

            Path upload_path = Paths.get(UploadsDao.getUploadedFilePath(upload));
            Charset charset = Charset.forName("ISO-8859-9");
            List<String> lines = Files.readAllLines(upload_path, charset);
            String parameterName;
            String parameterValue;
            String lessonName;
            String lessonAnswers;
            String booklet=null;
            Integer schoolNo=null;
            Students student;
            StudentsAnswers answers;
            SchoolsClass schoolsClass;
            //get classes and create new ones
            for (String line:lines) {
                for(ExamsParametersDto parameter:examsParametersDao.getParametersDtos()) {
                    if (parameter.getRefParameter().getTid().intValue() == 3) {
                        parameterValue = line.substring(parameter.getStart() - 1,
                                parameter.getStart() + parameter.getLength() - 1).trim();
                        schoolsClass = schoolsClassDao.findBySchoolAndName(Util.getActiveSchool(), parameterValue);
                        if (schoolsClass == null) {
                            SchoolsClass tempClass = new SchoolsClass();
                            tempClass.setName(parameterValue);
                            tempClass.setRefSchool(Util.getActiveSchool());
                            tempClass.setActive(Boolean.TRUE);
                            schoolsClassDao.saveOrUpdate(tempClass);
                        }
                    }
                }
            }
            for (String line : lines) {
                //Find schoolno
                for(ExamsParametersDto parameter:examsParametersDao.getParametersDtos()){
                    if (parameter.getRefParameter().getTid().intValue()==2) {
                        parameterValue=line.substring(parameter.getStart()-1,
                                parameter.getStart()+parameter.getLength()-1).trim();
                        schoolNo=Integer.valueOf(parameterValue);
                    }
                }

                Students tempStudent=studentsDao.findBySchoolAndNumber(Util.getActiveSchool(),schoolNo);
                student=new Students();

                //Get parameters
                for(ExamsParametersDto parameter:examsParametersDao.getParametersDtos()){
                    //logger.info("PARAMETER :" + parameter);
                    parameterValue=line.substring(parameter.getStart()-1,
                            parameter.getStart()+parameter.getLength()-1).trim();
                    parameterName=parameter.getRefParameter().getNameTr();
                    student.setRefSchool(Util.getActiveSchool());
                    switch (parameter.getRefParameter().getTid().intValue()){
                        case 1: student.setFullname(parameterValue);break;
                        case 2: schoolNo=Integer.valueOf(parameterValue);break;
                        case 3:
                            schoolsClass=schoolsClassDao.findBySchoolAndName(Util.getActiveSchool(), parameterValue);
                            student.setRefSchoolClass(schoolsClass);
                            break;
                        case 4: student.setName(parameterValue);break;
                        case 5: student.setSurname(parameterValue);break;
                        case 6: student.setGender(parameterValue);break;
                        case 7: booklet=parameterValue;break;
                        case 8: student.setMernis(parameterValue);break;
                        case 10:student.setPhone(parameterValue);break;
                        default:break;
                    }
                    logger.info(parameterName + " : " + parameterValue);
                }
                student.setActive(Boolean.TRUE);

                if (tempStudent==null) {
                    student.setSchoolNo(schoolNo);
                    studentsDao.saveOrUpdate(student);
                } else {
                    //studentsDao.update(student);
                }

                for(LessonsDto lesson:lessonsDao.getLessonsDtos()) {
                    lessonName=lesson.getRefLessonName().getNameTr();
                    lessonAnswers=line.substring(lesson.getStart() - 1,
                            lesson.getStart() + lesson.getQuestionCount());
                    logger.info(lessonName + " : " + lessonAnswers);
                    answers=new StudentsAnswers();
                    answers.setRefExam(Util.getActiveExam());
                    answers.setRefLesson(lessonsDao.getById(lesson.getTid()));
                    answers.setBooklet(booklet);
                    answers.setAnswers(lessonAnswers);
                    answers.setActive(Boolean.TRUE);
                    answers.setRefStudent(student);
                    studentsAnswersDao.saveOrUpdate(answers);
                }
            }

            examsParametersDao.getParametersDtos().clear();
            lessonsDao.getLessonsDtos().clear();

        } catch (IOException e) {
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
}
