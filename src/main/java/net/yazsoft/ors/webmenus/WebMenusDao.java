package net.yazsoft.ors.webmenus;

import net.yazsoft.frame.contents.ContentsDao;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Constants;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Contents;
import net.yazsoft.ors.entities.ContentsType;
import net.yazsoft.ors.entities.Menus;
import net.yazsoft.ors.entities.MenusType;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.ReorderEvent;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class WebMenusDao extends BaseGridDao<Menus> implements Serializable{
    private static final Logger logger = Logger.getLogger(WebMenusDao.class);
    Menus selected;
    List<Menus> menus;
    Boolean listChanged=true;
    Contents content;

    Long menuId;

    public void initSelected() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            selected=getById(menuId);
        }
    }

    @Inject ContentsDao contentsDao;

    @PostConstruct
    public void init() {
        content=new Contents();
        getItem().setRefContent(content);
        getItem().setForm("/index");
    }

    public void onRowReorder(ReorderEvent event) {
        //Util.setFacesMessage("Row Moved From: " + event.getFromIndex() + ", To:" + event.getToIndex());
        try {
            menus.get(event.getFromIndex()).setRank(event.getToIndex());
            logger.info("LOG02290: MOVED : " + menus.get(event.getFromIndex()).getNameTr());
            update(menus.get(event.getFromIndex()));
            for (int i = 0; i < menus.size(); i++) {
                menus.get(i).setRank(i + 1);
                update(menus.get(i));
            }
            listChanged = true;
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    @Transactional
    public Long save() {
        Long pk=null;
        try {
            listChanged = true;
            content.setActive(true);
            if (getItem().getTid() == null) {
                getItem().setActive(true);
                getItem().setMainId((Menus) getSession().load(Menus.class, 1L));
                getItem().setRefMenutype((MenusType) getSession().load(MenusType.class, 2L));
                getItem().setRank(menus.size() + 1);
                content.setRefContentType((ContentsType) getSession().load(
                        ContentsType.class, Constants.CONTENT_MENU));
            }

            if (getItem().getRefContent() == null) {
                if (content.getContentTr() != null) {
                    //logger.info("LOG02310: SAVING CONTENT");
                    contentsDao.saveOrUpdate(content);
                }
                getItem().setRefContent(content);
            } else {
                //logger.info("LOG02310: SAVING CONTENT");
                contentsDao.saveOrUpdate(getItem().getRefContent());
            }

            getItem().setActive(true);
            //pk = super.save();
            if (status== Status.UPDATE) {
                update();
            } else {
                pk=create();
            }
            if (content.getRefTid() == null) {
                content.setRefTid(pk);
                //contentsDao.update();
            }
            reset();
        } catch (Exception e) {
            Util.catchException(e);
        }
        return pk;
    }

    @Transactional
    public void delete() {
        contentsDao.delete(getItem().getRefContent());
        super.delete();
        listChanged=true;
    }

    public List<Menus> findMenus() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refMenutype",getSession().load(MenusType.class,2L)));
            //c.add(Restrictions.eq("isDeleted", false));
            c.addOrder(Order.asc("rank"));
            list = c.list();
            listChanged=false;
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }

    public WebMenusDao() {
        super(Menus.class);
    }

    public Menus getSelected() {
        return selected;
    }

    public void setSelected(Menus selected) {
        this.selected = selected;
    }

    public List<Menus> getMenus() {
        if (listChanged) {
            menus=findMenus();
        }
        return menus;
    }

    public void setMenus(List<Menus> menus) {
        this.menus = menus;
    }

    public Contents getContent() {
        if (getItem().getRefContent()!=null) {
            return getItem().getRefContent();
        }
        return content;
    }

    public void setContent(Contents content) {
        this.content = content;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
