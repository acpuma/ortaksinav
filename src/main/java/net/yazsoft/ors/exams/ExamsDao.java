package net.yazsoft.ors.exams;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.security.SessionInfo;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.examsParameters.ExamsParametersDao;
import net.yazsoft.ors.examsParameters.ExamsParametersTypeDao;
import net.yazsoft.ors.lessons.LessonsDao;
import net.yazsoft.ors.lessons.LessonsDto;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ExamsDao extends BaseGridDao<Exams> implements Serializable{
    private static final Logger logger = Logger.getLogger(ExamsDao.class);
    Exams selected;
    ExamsYear filterYear;
    ExamsSeason filterSeason;
    ExamsSeasonNumber filterSeasonNumber;

    List<Exams> filteredExams;

    //@Inject
    SessionInfo sessionInfo;

    @Inject LessonsDao lessonsDao;
    @Inject ExamsAnswerTypeDao examsAnswerTypeDao;
    @Inject ExamsBookletTypeDao examsBookletTypeDao;
    @Inject ExamsFalseTypeDao examsFalseTypeDao;
    @Inject ExamsParametersTypeDao examsParametersTypeDao;
    @Inject ExamsParametersDao examsParametersDao;

    public ExamsDao() {
        super(Exams.class);
    }

    @Override
    @PostConstruct
    public void init() {
        logger.info("EXAMSDAO INIT");
        super.init();

        //filteredExams=new ArrayList<Exams>();
    }

    public Long save() {
        logger.info("SAVINGGGGG");
        //Util.toString(item);
        Long pk=null;
        if (status== Status.UPDATE) {
            update();
        } else {
            pk=create();
        }
        //reset();
        return pk;
    }

    @Override
    public void reset() {
        logger.info("EXAMSDAO RESET");
        super.reset();
        getItem().setRefAnswerType(examsAnswerTypeDao.getById(5L)); //E
        getItem().setRefBookletType(examsBookletTypeDao.getById(2L)); //B
        getItem().setRefFalseType(examsFalseTypeDao.getById(1L)); //0
        lessonsDao.reset();
    }


    @Override
    public Long create() {
        Long pk=null;
        try {
            logger.info("CREATE : " + getItem());
            getItem().setActive(Boolean.TRUE);
            getItem().setRefExamType(new ExamsType(1L));
            getItem().setRefSchool(Util.getActiveSchool());
            pk = super.create();
            lessonsDao.DtosToEntities(new Exams(pk));
            ExamsParameters parameter;
            for (ExamsParametersType type:examsParametersTypeDao.getDefaultParameters()){
                parameter=new ExamsParameters();
                parameter.setActive(Boolean.TRUE);
                parameter.setRefParameter(type);
                parameter.setRefExam((Exams)getSession().load(Exams.class,pk));
                examsParametersDao.create(parameter);
            }
            reset();
        } catch (Exception e) {
            logger.error("EXCEPTION", e);
            Util.setFacesMessage("HATA: " +e.getMessage());
        }
        return pk;
    }

    @Override
    public String update() {
        logger.info("UPDATING LESSONS...");
        deleteLessons();
        super.update();
        lessonsDao.DtosToEntities(getItem());
        //reset();
        return null;
    }

    public void deleteLessons() {
        logger.info("DELETING LESSONS...");
        Lessons entity;
        for (LessonsDto dto:lessonsDao.getDeletedDtos()) {
            entity=(Lessons)getSession().get(Lessons.class,dto.getTid());
            getItem().getLessonsCollection().remove(entity);
            lessonsDao.delete(entity);
        }
    }
    @Override
    public void select() {
        lessonsDao.reset();
        if (lessonsDao!=null) {
            lessonsDao.getExamLessons(getItem());
        }
        super.select();
    }

    public Exams getSelected() {
        return selected;
    }

    public void setSelected(Exams selected) {
        this.selected = selected;
    }

    public ExamsYear getFilterYear() {
        return filterYear;
    }

    public void setFilterYear(ExamsYear filterYear) {
        this.filterYear = filterYear;
    }

    public ExamsSeason getFilterSeason() {
        return filterSeason;
    }

    public void setFilterSeason(ExamsSeason filterSeason) {
        this.filterSeason = filterSeason;
    }

    public ExamsSeasonNumber getFilterSeasonNumber() {
        return filterSeasonNumber;
    }

    public void setFilterSeasonNumber(ExamsSeasonNumber filterSeasonNumber) {
        this.filterSeasonNumber = filterSeasonNumber;
    }

    public List<Exams> getFilteredExams() {
        return filteredExams;
    }

    public void setFilteredExams(List<Exams> filteredExams) {
        this.filteredExams = filteredExams;
    }
}
