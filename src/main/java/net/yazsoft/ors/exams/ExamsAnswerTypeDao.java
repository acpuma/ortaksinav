package net.yazsoft.ors.exams;


import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.ExamsAnswerType;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ExamsAnswerTypeDao extends BaseGridDao<ExamsAnswerType> implements Serializable{
    public ExamsAnswerTypeDao() {
        super(ExamsAnswerType.class);
    }
}
