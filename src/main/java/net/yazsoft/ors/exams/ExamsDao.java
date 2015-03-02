package net.yazsoft.ors.exams;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.ExamsType;
import net.yazsoft.ors.entities.Lessons;
import net.yazsoft.ors.lessons.LessonsDao;
import net.yazsoft.ors.lessons.LessonsDto;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ExamsDao extends BaseGridDao<Exams> implements Serializable{
    private static final Logger logger = Logger.getLogger(ExamsDao.class);
    Exams selected;

    @Inject
    LessonsDao lessonsDao;

    public ExamsDao() {
        super(Exams.class);
    }

    @Override
    public Long create() {
        logger.info("CREATE : " + getItem());
        getItem().setActive(Boolean.TRUE);
        getItem().setRefExamType(new ExamsType(1L));
        Long pk=super.create();
        lessonsDao.DtosToEntities(new Exams(pk));
        reset();
        return pk;
    }

    @Override
    public String update() {
        logger.info("UPDATING LESSONS...");
        deleteLessons();
        super.update();
        lessonsDao.DtosToEntities(getItem());
        reset();
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
        lessonsDao.getExamLessons(getItem());
        super.select();
    }

    public Exams getSelected() {
        return selected;
    }

    public void setSelected(Exams selected) {
        this.selected = selected;
    }
}
