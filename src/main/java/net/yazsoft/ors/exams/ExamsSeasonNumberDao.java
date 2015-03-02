package net.yazsoft.ors.exams;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.ExamsSeasonNumber;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ExamsSeasonNumberDao extends BaseGridDao<ExamsSeasonNumber> implements Serializable{

    public ExamsSeasonNumberDao() {
        super(ExamsSeasonNumber.class);
    }
}
