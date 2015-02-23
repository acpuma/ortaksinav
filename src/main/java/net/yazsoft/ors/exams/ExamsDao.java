package net.yazsoft.ors.exams;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.Exams;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ExamsDao extends BaseGridDao<Exams> implements Serializable{
    Exams selected;

    public ExamsDao() {
        super(Exams.class);
    }

    public Exams getSelected() {
        return selected;
    }

    public void setSelected(Exams selected) {
        this.selected = selected;
    }
}
