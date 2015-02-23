package net.yazsoft.ors.lessonsGroup;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.LessonsGroup;
import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class LessonsGroupDao extends BaseGridDao<LessonsGroup> implements Serializable{
    private static final Logger logger = Logger.getLogger(LessonsGroupDao.class);
    LessonsGroup selected;


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
}
