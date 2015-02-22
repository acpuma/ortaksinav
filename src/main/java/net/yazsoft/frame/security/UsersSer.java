package net.yazsoft.frame.security;

import net.yazsoft.frame.hibernate.BaseSer;
import net.yazsoft.ors.entities.Users;
import net.yazsoft.ors.users.UsersLazyModel;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@Transactional
public class UsersSer extends BaseSer<Users> {
    private Logger logger= Logger.getLogger(UsersSer.class);

    @Inject
    UsersDao usersDao;

    @Inject
    private UsersLazyModel lazyModel;

    @PostConstruct
    public void init() {
        //lazyModel=usersDao.getLazyModel();
    }

    public Users findByUsername(final String username) {
        return usersDao.findByUserName(username);
    }

    public LazyDataModel<Users> getLazyModel() {
        return lazyModel;
    }
}
