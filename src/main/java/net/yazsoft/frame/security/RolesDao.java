package net.yazsoft.frame.security;


import net.yazsoft.ors.entities.Roles;
import net.yazsoft.frame.hibernate.BaseDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;

@Named
@Transactional
public class RolesDao extends BaseDao<Roles>{
    @Autowired
    private SessionFactory sessionFactory;

    public RolesDao() {
        super(Roles.class);
    }
}
