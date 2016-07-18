package net.yazsoft.ors.optic;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.OpticsFields;
import net.yazsoft.ors.entities.OpticsParts;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class OpticsPartsDao extends BaseGridDao<OpticsParts> implements Serializable{

    OpticsParts selected;

    public OpticsPartsDao() {
        super(OpticsParts.class);
    }

    public OpticsParts getSelected() {
        return selected;
    }

    public void setSelected(OpticsParts selected) {
        this.selected = selected;
    }
}
