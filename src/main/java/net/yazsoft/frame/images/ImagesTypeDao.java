package net.yazsoft.frame.images;

import net.yazsoft.frame.hibernate.BaseDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.ImagesType;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ImagesTypeDao extends BaseDao<ImagesType> implements Serializable{
    public ImagesTypeDao() {
        super(ImagesType.class);
    }
}
