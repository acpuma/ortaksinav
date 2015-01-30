package net.yazsoft.frame.security;


import java.util.ArrayList;
import java.util.List;

import net.yazsoft.frame.hibernate.BaseDao;
import net.yazsoft.frame.entities.Users;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class UsersDao extends BaseDao<Users>{

    @Autowired
    private SessionFactory sessionFactory;

    public UsersDao() {
        super(Users.class);
    }

    @SuppressWarnings("unchecked")
    public Users findByUserName(String username) {

        List<Users> users = new ArrayList<Users>();

        users = sessionFactory.getCurrentSession()
                .createQuery("from Users where username=?")
                .setParameter(0, username)
                .list();

        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }

    }



}