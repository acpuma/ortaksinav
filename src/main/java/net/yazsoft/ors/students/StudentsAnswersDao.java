package net.yazsoft.ors.students;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.StudentsAnswers;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class StudentsAnswersDao extends BaseGridDao<StudentsAnswers> implements Serializable{
    StudentsAnswers selected;

    public StudentsAnswersDao() {
        super(StudentsAnswers.class);
    }

    public StudentsAnswers getSelected() {
        return selected;
    }

    public void setSelected(StudentsAnswers selected) {
        this.selected = selected;
    }
}
