package net.yazsoft.ors.results;

import net.sf.jasperreports.engine.JRParameter;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.report.Report;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.schools.SchoolsClassDao;
import net.yazsoft.ors.schools.SchoolsClassDto;
import net.yazsoft.ors.students.*;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class ResultsDao extends BaseGridDao<Results> implements Serializable{
    private static final Logger logger = Logger.getLogger(ResultsDao.class);
    Results selected;
    List<StudentsAnswersViewDto> answersViewDto;
    List<Students> students;
    List<StudentsDto> studentsDto;
    List<StudentsAnswers> answers;
    List<StudentsAnswersDto> answersDto;
    List<SchoolsClass> classes;
    List<Lessons> lessons;
    List<Results> results;
    List<ResultsDto> resultsDto;
    Lessons selectedLesson;
    List<SchoolsClass> selectedClasses;

    @Inject StudentsAnswersDao studentsAnswersDao;
    @Inject SchoolsClassDao schoolsClassDao;
    @Inject Report report;

    @PostConstruct
    public void init() {
        answersViewDto=new ArrayList<>();
        studentsDto=new ArrayList<>();
        answersDto=new ArrayList<>();
        resultsDto=new ArrayList<>();
        selectedClasses=new ArrayList<>();
    }

    public void reportLesson() {
        if (selectedLesson==null) {
            Util.setFacesMessageError("Ders Seciniz");
            return;
        }
        logger.info("LOG01600: REPORT LESSON : " + selectedLesson.getRefLessonName().getNameTr());
        Map<String, Object> params = new HashMap<>();
        params.put("pSchoolName", Util.getActiveSchool().getName());
        params.put("pexam", Util.getActiveExam().getTid());
        params.put("plesson", selectedLesson.getTid());
        params.put("pLessonName", selectedLesson.getRefLessonName().getNameTr());
        params.put("pdate",Util.getActiveExam().getDate());
        Date now=Calendar.getInstance(new Locale("TR")).getTime();
        params.put("pnow",now);
        if (Util.getActiveSchool().getRefTown()!=null) {
            params.put("pilce",Util.getActiveSchool().getRefTown().getName().toUpperCase());
        }
        params.put("pyil",Util.getActiveExam().getRefExamYear().getName());
        params.put("pdonem",Util.getActiveExam().getRefExamSeason().getNameTr());
        params.put("psinavno",Util.getActiveExam().getRefExamSeasonNumber().getName());
        params.put("plogo","http://www.ortaksinav.com.tr/images/school/"+Util.getActiveSchool().getTid()
                + "."+ Util.getActiveSchool().getRefImage().getExtension());

        Locale trlocale= Locale.forLanguageTag("tr-TR");
        params.put(JRParameter.REPORT_LOCALE, trlocale);
        report.pdf("repLesson", params, selectedLesson.getRefLessonName().getNameTr());
    }

    public void reportLessonAverage() {
        if (selectedLesson==null) {
            Util.setFacesMessageError("Ders Seciniz");
            return;
        }
        if (selectedClasses==null) {
            Util.setFacesMessageError("Sinif Seciniz");
            return;
        }

        logger.info("LOG01610: selelectedClasses : " + selectedClasses);
        //logger.info("LOG01610: CLASSESSTR : " + selectedClassesStr);
        logger.info("LOG01600: REPORT LESSON : " + selectedLesson.getRefLessonName().getNameTr());

        Map<String, Object> params = new HashMap<>();
        params.put("pSchoolName", Util.getActiveSchool().getName());
        params.put("pexam", Util.getActiveExam().getTid());
        params.put("plesson", selectedLesson.getTid());
        params.put("pLessonName", selectedLesson.getRefLessonName().getNameTr());
        params.put("pclasses",selectedClasses);
        params.put("pclassesstr",selectedClasses.toString());
        params.put("pdate",Util.getActiveExam().getDate());
        Date now=Calendar.getInstance(new Locale("TR")).getTime();
        params.put("pnow",now);
        params.put("pil",Util.getActiveSchool().getRefCity().getName().toUpperCase());
        if (Util.getActiveSchool().getRefTown()!=null) {
            params.put("pilce", Util.getActiveSchool().getRefTown().getName().toUpperCase());
        }
        params.put("pyil",Util.getActiveExam().getRefExamYear().getName());
        params.put("pdonem",Util.getActiveExam().getRefExamSeason().getNameTr());
        params.put("psinavno",Util.getActiveExam().getRefExamSeasonNumber().getName());
        params.put("plogo","http://www.ortaksinav.com.tr/images/school/"+Util.getActiveSchool().getTid()
                + "."+ Util.getActiveSchool().getRefImage().getExtension());
        Locale trlocale= Locale.forLanguageTag("tr-TR");
        params.put(JRParameter.REPORT_LOCALE, trlocale);
        report.pdf("repLessonOrtalama", params, selectedLesson.getRefLessonName().getNameTr()+"Ortalama");
    }

    public void fillGrids() {
        if (Util.getActiveExam()!=null) {
            if (classes == null) {
                classes = schoolsClassDao.findByExam(Util.getActiveExam());
            }

            if (lessons == null) {
                lessons = (List) Util.getActiveExam().getLessonsCollection();
            }

            if (answers == null) {
                answers = studentsAnswersDao.findByExam(Util.getActiveExam());
            }

            if (answersDto.isEmpty()) {
                for (StudentsAnswers answer : answers) {
                    answersDto.add(new StudentsAnswersDto(answer));
                }
            }

            if (results == null) {
                results = findByExam(Util.getActiveExam());
            }

            if (resultsDto.isEmpty()) {
                for (Results result : results) {
                    resultsDto.add(new ResultsDto(result));
                }
            }
        }
    }

    public void DtosToEntities(List<ResultsDto> dtos) {
        Results entity;
        for (ResultsDto dto:dtos) {
            if (dto.getTid()!=null) {
                entity=(Results)getSession().get(Results.class,dto.getTid());
            } else {
                entity=new Results();
            }
            entity=dto.toEntity(entity);
            entity.setRefExam(Util.getActiveExam());
            entity.setRefSchool(Util.getActiveSchool());
            entity.setActive(Boolean.TRUE);
            //logger.info("RESULT ENTITY :" + entity);
            saveOrUpdate(entity);
        }
    }

    public List findByExam(Exams exam) {
        logger.info("EXAM : " + exam);
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refExam", exam));
            c.add(Restrictions.eq("active", true));
            //c.addOrder(Order.desc("score"));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List findByStudent(Students student) {
        logger.info("STUDENT  : " + student + " : " + student.getFullname());
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refStudent", student));
            c.add(Restrictions.eq("active", true));
            //c.addOrder(Order.desc("score"));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public Results findByExamAndStudent(Exams exam,Students student) {
        logger.info("STUDENT  : " + student + " : " + student.getFullname());
        Results temp=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refStudent", student));
            c.add(Restrictions.eq("refExam", exam));
            c.add(Restrictions.eq("active", true));
            //c.addOrder(Order.desc("score"));
            //c.add(Restrictions.eq("isDeleted", false));
            temp = (Results)c.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return temp;
    }

    public List findStudentExams(Students student) {
        List list=new ArrayList<>();
        ArrayList<Results> studentResults;
        try {
            studentResults=(ArrayList<Results>)findByStudent(student);
            for (Results result:studentResults) {
                list.add(result.getRefExam());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public void select() {
        super.select();
        //Util.getSessionInfo().setExam(getItem().getRefExam());
            answers = studentsAnswersDao.findByExamAndStudent(getItem().getRefExam(),Util.getActiveStudent());
            answersDto.clear();
            for (StudentsAnswers answer:answers) {
                answersDto.add(new StudentsAnswersDto(answer));
            }
    }

    public ResultsDao() {
        super(Results.class);
    }

    public Results getSelected() {
        return selected;
    }

    public void setSelected(Results selected) {
        this.selected = selected;
    }

    public List<StudentsAnswersViewDto> getAnswersViewDto() {

        return answersViewDto;
    }

    public void setAnswersViewDto(List<StudentsAnswersViewDto> answersViewDto) {
        this.answersViewDto = answersViewDto;
    }

    public List<StudentsAnswersDto> getAnswersDto() {
        fillGrids();
        return answersDto;
    }

    public void setAnswersDto(List<StudentsAnswersDto> answersDto) {
        this.answersDto = answersDto;
    }

    public List<SchoolsClass> getClasses() {
        return classes;
    }

    public void setClasses(List<SchoolsClass> classes) {
        this.classes = classes;
    }

    public List<Lessons> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lessons> lessons) {
        this.lessons = lessons;
    }

    public List<ResultsDto> getResultsDto() {
        fillGrids();
        return resultsDto;
    }

    public void setResultsDto(List<ResultsDto> resultsDto) {
        this.resultsDto = resultsDto;
    }

    public List<Results> getResults() {
        if (results==null) {
            results=findByStudent(Util.getActiveStudent());
        }
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public List<SchoolsClass> getSelectedClasses() {
        return selectedClasses;
    }

    public void setSelectedClasses(List<SchoolsClass> selectedClasses) {
        this.selectedClasses = selectedClasses;
    }

    public Lessons getSelectedLesson() {
        return selectedLesson;
    }

    public void setSelectedLesson(Lessons selectedLesson) {
        this.selectedLesson = selectedLesson;
    }
}
