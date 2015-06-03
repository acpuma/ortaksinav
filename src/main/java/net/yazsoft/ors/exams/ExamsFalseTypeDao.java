package net.yazsoft.ors.exams;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.ExamsFalseType;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ExamsFalseTypeDao extends BaseGridDao<ExamsFalseType> implements Serializable{
    public ExamsFalseTypeDao() {
        super(ExamsFalseType.class);
    }
}
