package net.yazsoft.ors.webcomments;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.WebComments;
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
public class WebCommentsDao extends BaseGridDao<WebComments> implements Serializable{
    private static final Logger logger = Logger.getLogger(WebCommentsDao.class);
    WebComments selected;
    List<WebComments> comments;
    Boolean listChanged=true;

    public void onRowReorder(ReorderEvent event) {
        //Util.setFacesMessage("Row Moved From: " + event.getFromIndex() + ", To:" + event.getToIndex());
        try {
            int size=comments.size();
            comments.get(event.getFromIndex()).setRank(size-event.getToIndex());
            logger.info("LOG02290: MOVED : " + comments.get(event.getFromIndex()).getName());
            update(comments.get(event.getFromIndex()));
            for (int i = 0; i < comments.size(); i++) {
                comments.get(i).setRank(size - i);
                update(comments.get(i));
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

    public void addComment() {
        WebComments temp=new WebComments();
        temp.setActive(true);
        temp.setRank(comments.size()+1);
        temp.setTitleTr("Baslik");
        temp.setCommentTr("Yorum");
        temp.setDate(Util.getNow());
        create(temp);
        selected=temp;
        listChanged=true;
    }

    public List<WebComments> findWebComments() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.addOrder(Order.desc("rank"));
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


    public WebCommentsDao() {
        super(WebComments.class);
    }

    public WebComments getSelected() {
        return selected;
    }

    public void setSelected(WebComments selected) {
        this.selected = selected;
    }

    public List<WebComments> getComments() {
        if (listChanged) {
            comments = findWebComments();
        }
        return comments;
    }

    public void setComments(List<WebComments> comments) {
        this.comments = comments;
    }
}
