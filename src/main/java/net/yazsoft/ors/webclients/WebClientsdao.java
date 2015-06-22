package net.yazsoft.ors.webclients;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.WebClients;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class WebClientsdao extends BaseGridDao<WebClients> implements Serializable{
    WebClients selected;

    public WebClientsdao() {
        super(WebClients.class);
    }

    public WebClients getSelected() {
        return selected;
    }

    public void setSelected(WebClients selected) {
        this.selected = selected;
    }
}
