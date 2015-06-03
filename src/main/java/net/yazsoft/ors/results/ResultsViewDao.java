package net.yazsoft.ors.results;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfTable;
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
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
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
    List<String> classes;
    Integer lessonCount;


    @Inject StudentsDao studentsDao;
    @Inject ResultsDao resultsDao;
    @Inject StudentsAnswersDao studentsAnswersDao;
    @Inject SchoolsClassDao schoolsClassDao;
    @Inject LessonsDao lessonsDao;

    @PostConstruct
    public void init() {
    }

    public void postProcessPDF(Object document) {
        Document table=(Document) document;
        table.setMargins(0,0,0,0);

    }

    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.setPageSize(PageSize.A4.rotate());
        //pdf.setMargins(0,0,0,0);
        pdf.open();

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String logo = servletContext.getRealPath("") + File.separator + "resources"
                + File.separator + "images" +File.separator + "orslogo.png";

        Image image=Image.getInstance(logo);
        image.scaleAbsolute(50, 50);
        pdf.add(image);

        Paragraph p = new Paragraph(Util.getActiveSchool().getName().toUpperCase());
        p.setAlignment(Element.ALIGN_CENTER);
        pdf.add(p);
        p = new Paragraph(" Tarih : "+ Util.getActiveExam().getDate().toString());
        p.setAlignment(Element.ALIGN_LEFT);
        pdf.add(p);
    }

    public void fillByExam(Exams exam) {
        resultsViewDtos=new ArrayList<>();
        classes=new ArrayList<>();
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
                dto.setSchoolClass(result.getRefStudent().getRefSchoolClass().getName());
                if (!classes.contains(dto.getSchoolClass())){
                    classes.add(dto.getSchoolClass());
                }
                dto.setStudent(result.getRefStudent());
                dto.setResult(result);
                dto.setAnswersList(findAnswers(result.getRefStudent()));
                resultsViewDtos.add(dto);
                //for (StudentsAnswers answer:dto.getAnswersList()) {

                //}
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
            if (answer.getRefStudent().getTid().equals(student.getTid())) {
                list.add(answer);
            }
        }
        logger.info("LOG01390: STUDENT : " + student + ", ANSWER LIST : " + list);
        return list;
    }

    public List<ResultsViewDto> getResultsViewDtos() {
        logger.info("LOG01370: GETTING RESULTVIEWDTOS");
        if ( resultsViewDtos == null) {
            logger.info("LOG01380: RESULTVIEWDTO NULL, FILLING...");
            logger.info("LOG01360: LESSON COUNT : " + lessonCount);
            if ( lessons == null ) {
                lessons = lessonsDao.getExamLessons(Util.getActiveExam());
                lessonCount = lessons.size();
                logger.info("LOG01360: LESSON COUNT : " + lessonCount);
            }
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
            String[] columnKeys = new String[]{"D","Y","N","P"};
            String[] columnProperties = new String[]{"trues","falses","nets","score"};
            for(int i=0; i< columnKeys.length; i++) {
                String columnKey=columnKeys[i];
                String columnProperty=columnProperties[i];
                columns.add(new ColumnModel(columnKey.toUpperCase(), columnProperty));
            }
            //columns.add(new ColumnModel(lessonName, lessonName));
        }
    }

    public List<ColumnModel> getColumns() {
        if (columns==null || columns.isEmpty()) {
            //populateColumns();
        }
        return columns;
    }

    public void setColumns(List<ColumnModel> columns) {
        this.columns = columns;
    }


    public List<Lessons> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lessons> lessons) {
        this.lessons = lessons;
    }

    public Integer getLessonCount() {
        return lessonCount;
    }

    public void setLessonCount(Integer lessonCount) {
        this.lessonCount = lessonCount;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }
}
