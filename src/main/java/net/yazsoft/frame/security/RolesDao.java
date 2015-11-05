package net.yazsoft.frame.security;


import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Roles;
import net.yazsoft.frame.hibernate.BaseDao;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;

@Named
@ViewScoped
@Transactional
public class RolesDao extends BaseDao<Roles>{
    private static final Logger log = Logger.getLogger(RolesDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public Roles findByName(String name) {
        Roles temp=null;
        try {
            Criteria c = getCriteria();
            //c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("name", name));
            temp = (Roles)c.uniqueResult();
        } catch (Exception e) {
            Util.catchException(e);
        }
        return temp;
    }

    public RolesDao() {
        super(Roles.class);
    }
}
