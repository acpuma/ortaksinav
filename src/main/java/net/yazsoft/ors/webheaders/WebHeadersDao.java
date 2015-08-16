package net.yazsoft.ors.webheaders;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.WebHeaders;
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
public class WebHeadersDao extends BaseGridDao<WebHeaders> implements Serializable{
    private static final Logger logger = Logger.getLogger(WebHeadersDao.class);
    WebHeaders selected;
    List<WebHeaders> headers;
    Boolean listChanged=true;

    public void onRowReorder(ReorderEvent event) {
        //Util.setFacesMessage("Row Moved From: " + event.getFromIndex() + ", To:" + event.getToIndex());
        try {
            headers.get(event.getFromIndex()).setRank(event.getToIndex());
            logger.info("LOG02290: MOVED : " + headers.get(event.getFromIndex()).getTitleTr());
            update(headers.get(event.getFromIndex()));
            for (int i = 0; i < headers.size(); i++) {
                headers.get(i).setRank(i+1);
                update(headers.get(i));
            }
            listChanged = true;
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void delete() {
        super.delete();
        listChanged=true;
    }

    public Long save() {
        listChanged=true;
        return super.save();
    }

    public void addHeader() {
        WebHeaders temp=new WebHeaders();
        temp.setActive(true);
        temp.setRank(headers.size() + 1);
        temp.setTitleTr("Baslik");
        temp.setSubtitleTr("Yorum");
        create(temp);
        selected=temp;
        listChanged=true;
    }

    public List<WebHeaders> findWebHeaders() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.addOrder(Order.asc("rank"));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
            listChanged=false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public WebHeadersDao() {
        super(WebHeaders.class);
    }

    public WebHeaders getSelected() {
        return selected;
    }

    public void setSelected(WebHeaders selected) {
        this.selected = selected;
    }

    public List<WebHeaders> getHeaders() {
        if (listChanged) {
            headers = findWebHeaders();
        }
        return headers;
    }

    public void setHeaders(List<WebHeaders> headers) {
        this.headers = headers;
    }
}
