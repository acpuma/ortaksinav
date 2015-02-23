package net.yazsoft.ors.lessons;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.Lessons;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class LessonsDao extends BaseGridDao<Lessons> implements Serializable{

    Lessons selected;

    public LessonsDao() {
        super(Lessons.class);
    }

    public Lessons getSelected() {
        return selected;
    }

    public void setSelected(Lessons selected) {
        this.selected = selected;
    }
}
