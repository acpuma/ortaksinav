package net.yazsoft.ors.lessonsName;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.answers.AnswersSubjectTypeDao;
import net.yazsoft.ors.entities.LessonsGroup;
import net.yazsoft.ors.entities.LessonsName;
import net.yazsoft.ors.lessonsGroup.LessonsGroupDao;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class LessonsNameDao extends BaseGridDao<LessonsName> implements Serializable{
    private static final Logger logger = Logger.getLogger(LessonsNameDao.class);
    LessonsName selected;

    List<LessonsName> names;

    @Inject
    AnswersSubjectTypeDao answersSubjectTypeDao;

    @Override
    public Long save() {
        logger.info("GROUP SAVE status : " + getStatus());
        try {
            getItem().setActive(Boolean.TRUE);
            Long pk=super.save();
            return pk;
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
        return 0L;
    }

    public List findByGroup(LessonsGroup group) {
        logger.info(group);
        List list=null;

        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refLessonGroup", group));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public void onRowSelect(SelectEvent event) {
        selected=(LessonsName) event.getObject();
        answersSubjectTypeDao.setLessonsName(selected);
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            LessonsName lessonsName=(LessonsName) event.getObject();
            logger.info("LOG00080:" + lessonsName.getNameTr());
            DataTable table = (DataTable) event.getSource();
            lessonsName = (LessonsName) table.getRowData();
            logger.info("LOG00080:" + lessonsName.getNameTr());
            super.update(lessonsName);
            //Util.setFacesMessage("Edited :" + lesson+", " +lesson.getQuestionCount());
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
    }

    public void onRowCancel(RowEditEvent event) {
        //Util.setFacesMessage("Edit Cancelled : " + ((LessonsDto) event.getObject()).getTid());
    }

    public List<LessonsName> getNames() {
        //names=findByGroup(lessonsGroupDao.getSelected());
        return names;
    }

    public void setNames(List<LessonsName> names) {
        this.names = names;
    }

    public LessonsNameDao() {
        super(LessonsName.class);
    }

    public LessonsName getSelected() {
        return selected;
    }

    public void setSelected(LessonsName selected) {
        this.selected = selected;
    }
}
