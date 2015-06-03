package net.yazsoft.frame.upload;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.UploadsType;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class UploadsTypeDao extends BaseGridDao<UploadsType> implements Serializable{
    UploadsType selected;

    public UploadsTypeDao() {
        super(UploadsType.class);
    }

    public UploadsType getSelected() {
        return selected;
    }

    public void setSelected(UploadsType selected) {
        this.selected = selected;
    }
}
