package net.yazsoft.ors.weblinks;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.WebLinks;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.ReorderEvent;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class WebLinksDao extends BaseGridDao<WebLinks> implements Serializable{
    private static final Logger logger = Logger.getLogger(WebLinksDao.class);
    WebLinks selected;
    List<WebLinks> links;
    Boolean listChanged=true;

    public void onRowReorder(ReorderEvent event) {
        //Util.setFacesMessage("Row Moved From: " + event.getFromIndex() + ", To:" + event.getToIndex());
        try {
            links.get(event.getFromIndex()).setRank(event.getToIndex());
            logger.info("LOG02290: MOVED : " + links.get(event.getFromIndex()).getName());
            update(links.get(event.getFromIndex()));
            for (int i = 0; i < links.size(); i++) {
                links.get(i).setRank(i + 1);
                update(links.get(i));
            }
            listChanged = true;
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public Long save() {
        listChanged=true;
        getItem().setActive(true);
        return super.save();
    }

    public void delete() {
        super.delete();
        listChanged=true;
    }

    public List<WebLinks> findLinks() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            c.addOrder(Order.asc("rank"));
            list = c.list();
            listChanged=false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public WebLinksDao() {
        super(WebLinks.class);
    }

    public WebLinks getSelected() {
        return selected;
    }

    public void setSelected(WebLinks selected) {
        this.selected = selected;
    }

    public List<WebLinks> getLinks() {
        if (listChanged) {
            links=findLinks();
        }
        return links;
    }

    public void setLinks(List<WebLinks> links) {
        this.links = links;
    }
}
