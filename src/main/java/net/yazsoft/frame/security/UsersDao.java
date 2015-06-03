package net.yazsoft.frame.security;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Roles;
import net.yazsoft.ors.entities.Schools;
import net.yazsoft.ors.entities.Users;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Named
@ViewScoped
@Transactional
public class UsersDao extends BaseGridDao<Users> {
    Logger logger= Logger.getLogger(UsersDao.class);

    Users selected;

    @Inject RolesDao rolesDao;

    public UsersDao() {
        super(Users.class);
    }

    @Override
    @PostConstruct
    public void init() {
        getItem().setRolesCollection(new HashSet<Roles>());
        getItem().setSchoolsCollection(new HashSet<Schools>());
    }

    @Override
    public Long create() {
        logger.info("CREATING USER");
        getItem().setActive(Boolean.TRUE);
        getItem().setAccountNonExpired(Boolean.TRUE);
        getItem().setAccountNonLocked(Boolean.TRUE);
        getItem().setCredentialsNonExpired(Boolean.TRUE);
        if ((getItem().getPassword()==null) || getItem().getPassword().equals("")) {
            Util.setFacesMessage("SIFRE alanini doldurunuz");
            return null;
        }

        //Collection<Roles> roles=new HashSet<>();
        //roles.add(rolesDao.getById(3L));
        getItem().getRolesCollection().add(new Roles(1L)); //RolesCollection(roles);
        Long pk=super.create();
        //rolesDao.getById(3L).getUsersCollection().add(getItem());
        //rolesDao.update();
        //getSession().save(roles);
        return pk;
    }

    public void checkUserExists() {
        logger.info("username : "+getItem().getUsername());
        Users user=findByUserName(getItem().getUsername());
        if (user!=null) {
            Util.setFacesMessage("KULLANICI ADI KULLANILIYOR");
        }
    }

    @SuppressWarnings("unchecked")
    public Users findByUserName(String username) {

        List<Users> users;

        users = getSession()
                .createQuery("from Users where username=?")
                .setParameter(0, username)
                .list();

        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }

    }

    public Users getSelected() {
        return selected;
    }

    public void setSelected(Users selected) {
        this.selected = selected;
    }
}