package net.yazsoft.ors.weblinks;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.WebLinks;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class WebLinksDao extends BaseGridDao<WebLinks> implements Serializable{
    WebLinks selected;

    public WebLinksDao() {
        super(WebLinks.class);
    }

    public WebLinks getSelected() {
        return selected;
    }

    public void setSelected(WebLinks selected) {
        this.selected = selected;
    }
}
