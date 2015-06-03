package net.yazsoft.ors.answers;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.AnswersSubjectType;
import net.yazsoft.ors.entities.LessonsName;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AnswersSubjectTypeDao extends BaseGridDao<AnswersSubjectType> implements Serializable{
    private static final Logger logger = Logger.getLogger(AnswersSubjectTypeDao.class);
    AnswersSubjectType selected;
    LessonsName lessonsName;
    List<AnswersSubjectType> subjectTypes;

    public AnswersSubjectTypeDao() {
        super(AnswersSubjectType.class);
    }

    public List<AnswersSubjectType> getLessonSubjects(LessonsName lessonName) {
        logger.info("LESSON : " + lessonName);
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refLessonName", lessonName));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        subjectTypes=list;
        return list;
    }

    public void onRowSelect(SelectEvent event) {
        selected=(AnswersSubjectType) event.getObject();
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            AnswersSubjectType subject=(AnswersSubjectType) event.getObject();
            DataTable table = (DataTable) event.getSource();
            subject = (AnswersSubjectType) table.getRowData();
            super.update(subject);
            //Util.setFacesMessage("Edited :" + lesson+", " +lesson.getQuestionCount());
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
    }

    public void onRowCancel(RowEditEvent event) {
        //Util.setFacesMessage("Edit Cancelled : " + ((LessonsDto) event.getObject()).getTid());
    }

    public void addSubjectType() {
        getItem().setActive(Boolean.TRUE);
        super.save();
        getLessonSubjects(lessonsName); //reload
    }

    public void deleteSubjectType(AnswersSubjectType subject) {
        try {
            super.delete(subject);
            getLessonSubjects(lessonsName); //reload
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
    }

    public void setLessonsName(LessonsName lessonsName) {
        this.lessonsName = lessonsName;
        getLessonSubjects(lessonsName);
    }


    public AnswersSubjectType getSelected() {
        return selected;
    }

    public void setSelected(AnswersSubjectType selected) {
        this.selected = selected;
    }

    public LessonsName getLessonsName() {
        return lessonsName;
    }

    public List<AnswersSubjectType> getSubjectTypes() {
        return subjectTypes;
    }

    public void setSubjectTypes(List<AnswersSubjectType> subjectTypes) {
        this.subjectTypes = subjectTypes;
    }
}
