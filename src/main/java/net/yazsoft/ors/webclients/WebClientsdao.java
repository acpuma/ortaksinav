package net.yazsoft.ors.webclients;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.WebClients;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.ReorderEvent;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class WebClientsDao extends BaseGridDao<WebClients> implements Serializable{
    private static final Logger logger = Logger.getLogger(WebClientsDao.class);
    WebClients selected;
    List<WebClients> items;
    Boolean itemsChanged=true;
    WebClients center;

    @PostConstruct
    public void init() {

    }

    public WebClients findCenter() {
        WebClients client=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("center", true));
            client = (WebClients) c.uniqueResult();
            //logger.info("LOG02340: CENTER CLIENT : " + client.getName() + " , " + client.getAddress());
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return client;
    }

    public void onRowReorder(ReorderEvent event) {
        //Util.setFacesMessage("Row Moved From: " + event.getFromIndex() + ", To:" + event.getToIndex());
        try {
            items.get(event.getFromIndex()).setRank(event.getToIndex());
            logger.info("LOG02290: MOVED : " + items.get(event.getFromIndex()).getName());
            update(items.get(event.getFromIndex()));
            for (int i = 0; i < items.size(); i++) {
                items.get(i).setRank(i + 1);
                update(items.get(i));
            }
            itemsChanged = true;
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public Long save() {
        itemsChanged=true;
        getItem().setActive(true);
        return super.save();
    }

    public void delete() {
        super.delete();
        itemsChanged=true;
    }

    public List<WebClients> findItems() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            c.addOrder(Order.asc("rank"));
            list = c.list();
            itemsChanged=false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public WebClientsDao() {
        super(WebClients.class);
    }

    public WebClients getSelected() {
        return selected;
    }

    public void setSelected(WebClients selected) {
        this.selected = selected;
    }

    public List<WebClients> getItems() {
        if (itemsChanged) {
            items=findItems();
        }
        return items;
    }

    public void setItems(List<WebClients> items) {
        this.items = items;
    }

    public WebClients getCenter() {
        if (center==null) {
            center=findCenter();
        }
        return center;
    }

    public void setCenter(WebClients center) {
        this.center = center;
    }
}
