package net.yazsoft.frame.security;

import net.yazsoft.frame.entities.UserRoles;
import net.yazsoft.frame.hibernate.BaseDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserRolesDao extends BaseDao<UserRoles>{
    @Autowired
    private SessionFactory sessionFactory;

    public UserRolesDao() {
        super(UserRoles.class);
    }

}
