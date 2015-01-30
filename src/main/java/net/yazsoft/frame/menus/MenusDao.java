/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.frame.menus;

import net.yazsoft.frame.entities.Menus;
import net.yazsoft.frame.entities.MenusType;
import net.yazsoft.frame.hibernate.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class MenusDao extends BaseDao<Menus> implements Serializable{

    @Autowired
    protected SessionFactory sessionFactory;

    public MenusDao() {
        super(Menus.class);
    }
    
    public List<Menus> getSubmenus(Long menuid) {
        final Session session = sessionFactory.getCurrentSession();
        Criteria query;
        List list=null;
        try {
            session.clear(); //clear cache
            query = session.createCriteria(Menus.class);
            query.add(Restrictions.eq("mainId", new Menus(menuid)));
            query.add(Restrictions.eq("active", 1));
            query.addOrder(Order.asc("order"));
            //query.setProjection(Projections.rowCount());
            list = query.setCacheable(false).list();
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
            session.clear(); //clear cache
            query = session.createCriteria(Menus.class);
            query.add(Restrictions.eq("refMenutype", new MenusType(menutype)));
            query.add(Restrictions.eq("active", 1));
            query.addOrder(Order.asc("order"));
            //query.setProjection(Projections.rowCount());
            list = query.setCacheable(false).list();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return list;
    }

}
