package net.yazsoft.ors.results;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.lessons.LessonsDao;
import net.yazsoft.ors.schools.SchoolsClassDao;
import net.yazsoft.ors.students.StudentsAnswersDao;
import net.yazsoft.ors.students.StudentsDao;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class ResultsViewDao extends BaseGridDao implements Serializable{
    private static final Logger logger = Logger.getLogger(ResultsViewDao.class);
    List<ResultsViewDto> resultsViewDtos;
    List<Students> students;
    List<Results> results;
    List<StudentsAnswers> answers;
    List<Lessons> lessons;

    @Inject StudentsDao studentsDao;
    @Inject ResultsDao resultsDao;
    @Inject StudentsAnswersDao studentsAnswersDao;
    @Inject SchoolsClassDao schoolsClassDao;
    @Inject LessonsDao lessonsDao;

    @PostConstruct
    public void init() {
    }

    public void fillByExam(Exams exam) {
        resultsViewDtos=new ArrayList<>();
        try {
            logger.info("LOG01210:FILLING GRID");
            //students=studentsDao.findBySchoolClass(sclass);
            results=resultsDao.findByExam(exam);
            logger.info("LOG01220: Results :" + results);
            answers=studentsAnswersDao.findByExam(exam);
            logger.info("LOG01230: Answers : " + answers);
            ResultsViewDto dto;
            for(Results result:results) {
                dto=new ResultsViewDto();
                dto.setTid(result.getTid());
                dto.setSchoolClass(result.getRefStudent().getRefSchoolClass());
                dto.setStudent(result.getRefStudent());
                dto.setResult(result);
                dto.setAnswersList(findAnswers(result.getRefStudent()));
                resultsViewDtos.add(dto);
                for (StudentsAnswers answer:dto.getAnswersList()) {

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    Results findResult(Exams exam,Students student) {
        for (Results result:results) {
            if (result.getRefExam().equals(exam)
                    && result.getRefStudent().equals(student)) {
                return result;
            }
        }
        return null;
    }

    List<StudentsAnswers> findAnswers(Students student) {
        List list=new ArrayList<>();
        for (StudentsAnswers answer:answers) {
            if (answer.getRefStudent().equals(student)) {
                list.add(answer);
            }
        }
        return list;
    }

    public List<ResultsViewDto> getResultsViewDtos() {
        if (resultsViewDtos==null) {
            fillByExam(Util.getActiveExam());
        }
        return resultsViewDtos;
    }

    public void setResultsViewDtos(List<ResultsViewDto> resultsViewDtos) {
        this.resultsViewDtos = resultsViewDtos;
    }


    private List<ColumnModel> columns;;

    static public class ColumnModel implements Serializable {
        private String header;
        private String property;
        public ColumnModel(String header, String property) {
            this.header = header;
            this.property = property;
        }
        public String getHeader() {
            return header;
        }
        public String getProperty() {
            return property;
        }
    }

    public void populateColumns() {
        lessons=lessonsDao.getExamLessons(Util.getActiveExam());
        columns= new ArrayList<ColumnModel>();
        String lessonName;

        for (Lessons lesson:lessons) {
            lessonName=lesson.getRefLessonName().getNameTr();
            String[] columnKeys = new String[]{"D","Y","N","P","SS","OS"};
            for(String columnKey : columnKeys) {
                columns.add(new ColumnModel(columnKey.toUpperCase(), columnKey));
            }
            //columns.add(new ColumnModel(lessonName, lessonName));
        }
    }

    public List<ColumnModel> getColumns() {
        if (columns==null || columns.isEmpty()) {
            populateColumns();
        }
        return columns;
    }

    public void setColumns(List<ColumnModel> columns) {
        this.columns = columns;
    }
}
