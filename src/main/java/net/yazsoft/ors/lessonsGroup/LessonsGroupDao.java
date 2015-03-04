package net.yazsoft.ors.lessonsGroup;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.LessonsGroup;
import net.yazsoft.ors.entities.LessonsName;
import net.yazsoft.ors.lessonsName.LessonsNameDao;
import org.apache.log4j.Logger;
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

    public void onRowSelect(SelectEvent event) {
        selected=((LessonsGroup) event.getObject());
        logger.info("LOG00020:" + selected);
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
