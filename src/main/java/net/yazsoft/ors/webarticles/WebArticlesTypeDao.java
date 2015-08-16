package net.yazsoft.ors.webarticles;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.WebArticlesType;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class WebArticlesTypeDao extends BaseGridDao<WebArticlesType> implements Serializable{
    WebArticlesType selected;

    public WebArticlesTypeDao() {
        super(WebArticlesType.class);
    }

    public WebArticlesType getSelected() {
        return selected;
    }

    public void setSelected(WebArticlesType selected) {
        this.selected = selected;
    }
}
