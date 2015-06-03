package net.yazsoft.ors.exams;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.ExamsYear;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ExamsYearDao extends BaseGridDao<ExamsYear> implements Serializable{

    ExamsYear selected;

    public ExamsYearDao() {
        super(ExamsYear.class);
    }
}
