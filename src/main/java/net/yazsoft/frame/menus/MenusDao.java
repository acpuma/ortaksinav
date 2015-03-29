/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.frame.menus;

import net.yazsoft.frame.hibernate.BaseDao;
import net.yazsoft.ors.entities.Menus;
import net.yazsoft.ors.entities.MenusType;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import net.yazsoft.frame.scopes.*;

@Named
@ViewScoped
public class MenusDao extends BaseDao<Menus> implements Serializable{

    @Inject
    protected SessionFactory sessionFactory;

    public MenusDao() {
        super(Menus.class);
    }

    public List<Menus> getSubmenus(Long menuid) {
        final Session session = sessionFactory.getCurrentSession();
        Criteria query;
        List list=null;
        try {
            session.flush();
            //session.clear(); //clear cache
            query = session.createCriteria(Menus.class);
            query.add(Restrictions.eq("mainId", new Menus(menuid)));
            query.add(Restrictions.eq("active", 1));
            query.addOrder(Order.asc("order"));
            //query.setProjection(Projections.rowCount());
            list = query.list();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return list;
    }
    
    public List<Menus> getMenus(Long menutype) {
        Criteria query;
        List list=null;
        final Session session = sessionFactory.getCurrentSession();
        try {
            session.flush();
            //session.clear(); //clear cache
            query = session.createCriteria(Menus.class);
            query.add(Restrictions.eq("refMenutype", new MenusType(menutype)));
            query.add(Restrictions.eq("active", 1));
            query.addOrder(Order.asc("order"));
            //query.setProjection(Projections.rowCount());
            list = query.list();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return list;
    }

}
