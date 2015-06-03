package net.yazsoft.ors.exams;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.ExamsSeason;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ExamsSeasonDao extends BaseGridDao<ExamsSeason> implements Serializable{
    ExamsSeason selected;

    public ExamsSeasonDao() {
        super(ExamsSeason.class);
    }

    public ExamsSeason getSelected() {
        return selected;
    }

    public void setSelected(ExamsSeason selected) {
        this.selected = selected;
    }
}
