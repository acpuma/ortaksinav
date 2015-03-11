package net.yazsoft.ors.lessonsGroup;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.LessonsGroup;
import net.yazsoft.ors.entities.LessonsName;
import net.yazsoft.ors.lessonsName.LessonsNameDao;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class LessonsGroupDao extends BaseGridDao<LessonsGroup> implements Serializable{
    private static final Logger logger = Logger.getLogger(LessonsGroupDao.class);

    LessonsGroup selected;

    @Inject
    LessonsNameDao lessonsNameDao;

    List<LessonsName> lessonsNames;

    public void handleGroupChange() {
        logger.info("GROUP : "+selected);
        if(selected !=null)
            lessonsNames = lessonsNameDao.findByGroup(selected);
        else
            lessonsNames = new ArrayList<LessonsName>();
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            LessonsGroup lessonsGroup=(LessonsGroup) event.getObject();
            logger.info("LOG00080:" + lessonsGroup.getNameTr());
            DataTable table = (DataTable) event.getSource();
            lessonsGroup = (LessonsGroup) table.getRowData();
            logger.info("LOG00080:" + lessonsGroup.getNameTr());
            super.update(lessonsGroup);
            //Util.setFacesMessage("Edited :" + lesson+", " +lesson.getQuestionCount());
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
    }

    public void onRowCancel(RowEditEvent event) {
        //Util.setFacesMessage("Edit Cancelled : " + ((LessonsDto) event.getObject()).getTid());
    }

    public void addLessonName() {
        lessonsNameDao.save();
        handleGroupChange();
    }

    public void deleteLessonName(LessonsName lessonsName) {
        try {
            lessonsNameDao.delete(lessonsName);
            handleGroupChange();
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
    }

    public String remove(LessonsGroup lessonsGroup) {
        logger.info("DELETING : " + lessonsGroup);
        try {
            super.delete(lessonsGroup);
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
        return "";
    }

    @Override
    public Long save() {
        logger.info("GROUP SAVE status : " + getStatus());
        try {
            getItem().setActive(Boolean.TRUE);
            return super.save();
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
        return 0L;
    }

    public void setStatus() {
        setStatus(Status.UPDATE);
    }

    public void onRowSelect(SelectEvent event) {
        selected=((LessonsGroup) event.getObject());
        logger.info("ONROWSELECT :" + selected);
        setStatus(Status.UPDATE);
        handleGroupChange();
    }
    public LessonsGroupDao() {
        super(LessonsGroup.class);
    }

    public LessonsGroup getSelected() {
        return selected;
    }

    public void setSelected(LessonsGroup selected) {
        this.selected = selected;
    }

    public List<LessonsName> getLessonsNames() {
        return lessonsNames;
    }

    public void setLessonsNames(List<LessonsName> lessonsNames) {
        this.lessonsNames = lessonsNames;
    }

}
