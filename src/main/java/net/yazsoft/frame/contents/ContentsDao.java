package net.yazsoft.frame.contents;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Cities;
import net.yazsoft.ors.entities.Contents;
import net.yazsoft.ors.entities.Towns;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@Transactional
public class ContentsDao extends BaseGridDao<Contents> implements Serializable{
    Logger logger= Logger.getLogger(ContentsDao.class);
    Contents selected;
    List<Contents> items;
    Long contentId;

    public void initSelected() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            selected=getById(contentId);
        }
    }

    public ContentsDao() {
        super(Contents.class);
    }

    public Contents getSelected() {
        return selected;
    }

    public void setSelected(Contents selected) {
        this.selected = selected;
    }

    public List<Contents> getItems() {
        return items;
    }

    public void setItems(List<Contents> items) {
        this.items = items;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }
}
