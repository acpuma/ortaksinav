package net.yazsoft.ors.optic;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.OpticsFields;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class OpticFieldsDao extends BaseGridDao<OpticsFields> implements Serializable{

    OpticsFields selected;

    public OpticFieldsDao() {
        super(OpticsFields.class);
    }

    public OpticsFields getSelected() {
        return selected;
    }

    public void setSelected(OpticsFields selected) {
        this.selected = selected;
    }
}
