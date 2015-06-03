package net.yazsoft.frame.security;


import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.Roles;
import net.yazsoft.frame.hibernate.BaseDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;

@Named
@ViewScoped
@Transactional
public class RolesDao extends BaseDao<Roles>{
    @Autowired
    private SessionFactory sessionFactory;

    public RolesDao() {
        super(Roles.class);
    }
}
