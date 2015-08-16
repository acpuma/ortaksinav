package net.yazsoft.frame.menus;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Menus;
import net.yazsoft.ors.entities.Users;
import net.yazsoft.ors.entities.UsersMenus;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AuthsDao extends BaseGridDao<UsersMenus> implements Serializable{
    private static final Logger logger = Logger.getLogger(AuthsDao.class);

    UsersMenus auths;

    /*
    public void findAuths(Long menuid) {
        UsersMenus temp=null;
        Menus menu=null;
        Users user=Util.getActiveUser();
        try {
            menu = (Menus) getSession().load(Menus.class,menuid);
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refUser", user));
            c.add(Restrictions.eq("refMenu", menu));
            temp = (UsersMenus) c.uniqueResult();
            auths=temp;
            logger.info("LOG02440: PERMISSIONS / audrs ; "+ auths + " / " + auths.getPadd()
                    +auths.getPupdate()+auths.getPdelete()+auths.getPreport()+auths.getPreport() );
        } catch (Exception e) {
            Util.catchException(e);
        }
    }
    */

    public Menus findMenu(String form) {
        Menus menu=null;
        try {
            logger.info("LOG02450: FINDMENU FORM : " + form);
            Criteria c = getSession().createCriteria(Menus.class);
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("form", form));
            //c.add(Restrictions.eq("refMenu", menu));
            menu = (Menus) c.uniqueResult();
            logger.info("LOG02460: FINDMENU MENU : " + menu);
        } catch (Exception e) {
            Util.catchException(e);
        }
        return menu;
    }

    public void findAuths(String form) {
        UsersMenus temp=null;
        Users user=Util.getActiveUser();
        try {
            Menus menu = findMenu(form);
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refUser", user));
            c.add(Restrictions.eq("refMenu", menu));
            temp = (UsersMenus) c.uniqueResult();
            auths=temp;
            logger.info("LOG02440: PERMISSIONS / audrs ; "+ auths + " / " + auths.getPadd()
                    +auths.getPupdate()+auths.getPdelete()+auths.getPreport()+auths.getPreport() );
        } catch (Exception e) {
            Util.catchException(e);
        }
    }


    public AuthsDao() {
        super(UsersMenus.class);
    }

    public UsersMenus getAuths() {
        return auths;
    }

    public void setAuths(UsersMenus auths) {
        this.auths = auths;
    }
}
