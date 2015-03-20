package net.yazsoft.ors.examsParameters;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.ExamsParametersType;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ExamsParametersTypeDao extends BaseGridDao<ExamsParametersType> implements Serializable{
    ExamsParametersType selected;

    public ExamsParametersTypeDao() {
        super(ExamsParametersType.class);
    }

    public ExamsParametersType getSelected() {
        return selected;
    }

    public void setSelected(ExamsParametersType selected) {
        this.selected = selected;
    }
}
