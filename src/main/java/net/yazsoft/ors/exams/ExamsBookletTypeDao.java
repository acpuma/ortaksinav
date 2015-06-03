package net.yazsoft.ors.exams;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.ExamsBookletType;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ExamsBookletTypeDao extends BaseGridDao<ExamsBookletType> implements Serializable{
    public ExamsBookletTypeDao() {
        super(ExamsBookletType.class);
    }
}
