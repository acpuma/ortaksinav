package net.yazsoft.ors.results;

import net.yazsoft.frame.hibernate.BaseGridDao;
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
import java.util.ArrayList;
import java.util.List;

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

    @Inject StudentsAnswersDao studentsAnswersDao;
    @Inject SchoolsClassDao schoolsClassDao;

    @PostConstruct
    public void init() {
        answersViewDto=new ArrayList<>();
        studentsDto=new ArrayList<>();
        answersDto=new ArrayList<>();
        resultsDto=new ArrayList<>();
    }

    public void fillGrids() {
        if (classes==null) {
            classes = schoolsClassDao.findBySchool(Util.getActiveSchool());
        }

        if (lessons==null) {
            lessons = (List)Util.getActiveExam().getLessonsCollection();
        }

        if (answers==null) {
            answers = studentsAnswersDao.findByExam(Util.getActiveExam());
        }

        if (answersDto.isEmpty()){
            for (StudentsAnswers answer:answers) {
                answersDto.add(new StudentsAnswersDto(answer));
            }
        }

        if (results==null) {
            results=findByExam(Util.getActiveExam());
        }

        if (resultsDto.isEmpty()){
            for (Results result:results) {
                resultsDto.add(new ResultsDto(result));
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
}
