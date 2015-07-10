package net.yazsoft.frame.auth;

import net.yazsoft.frame.hibernate.BaseDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.security.UsersDao;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Schools;
import net.yazsoft.ors.entities.Users;
import net.yazsoft.ors.schools.SchoolsDao;
import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class UsersSchoolsDao extends BaseDao implements Serializable {
    private static final Logger logger = Logger.getLogger(UsersSchoolsDao.class);
    DualListModel<Schools> schools;
    List<Schools> userSchools;
    Users user;
    Boolean listChanged=true;

    @Inject SchoolsDao schoolsDao;
    @Inject UsersDao usersDao;

    @PostConstruct
    public void init() {
        userSchools=new ArrayList<>();
    }

    public void saveUserSchools() {
        try {
            logger.info("LOG02150: SAVING USER SCHOOLS");
            for (Schools item : userSchools) {
                if (!user.getSchoolsCollection().contains(item)) {
                    user.getSchoolsCollection().add(item);
                }
                if (!item.getUsersCollection().contains(user)) {
                    item.getUsersCollection().add(user);
                }
                schoolsDao.saveOrUpdate(item);
            }
            for (Schools item: schools.getSource()) {
                if (user.getSchoolsCollection().contains(item)) {
                    user.getSchoolsCollection().remove(item);
                }
                if (item.getUsersCollection().contains(user)){
                    item.getUsersCollection().remove(user);
                }
                schoolsDao.saveOrUpdate(item);
            }
            usersDao.saveOrUpdate(user);
            logger.info("LOG02160: SAVED : " + user.getSchoolsCollection());
            Util.setFacesMessage("KAYIT EDİLDİ");
            listChanged=false;
        } catch (Exception e) {
            e.printStackTrace();
            Util.setFacesMessageError(e.getMessage());
        }
    }

    public void select() {
        logger.info("LOG02170: SELECTING USER...");
        user=usersDao.getItem();

        List<Schools> sources=schoolsDao.findSchools();
        logger.info("LOG02180: SOURCES : " + sources);
        userSchools=new ArrayList<>(user.getSchoolsCollection());
        for (Schools item:userSchools) {
            if (sources.contains(item)) {
                sources.remove(item);
            }
        }
        schools.setSource(sources);
        schools.setTarget(userSchools);
        logger.info("LOG02130: USER : " + user.getUsername());
        logger.info("LOG02140: USERSCHOOLS : " + userSchools);
    }

    private void findSchools() {
        try {
            if (listChanged==true) {
                userSchools=new ArrayList<>();
                schools=new DualListModel<Schools>(schoolsDao.getAll(),userSchools);
                listChanged=false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Util.setFacesMessageError(e.getMessage());
        }
    }

    private void removeSchool(Schools school) {
        for (int i=0; i<userSchools.size(); i++) {
            if (school.getTid().equals(userSchools.get(i).getTid())) {
                userSchools.remove(i);
            }
        }
    }

    public void onTransfer(TransferEvent event) {
        try {
            logger.info("LOG02090: TRANSFERRING ... ");
            Schools school;
            for (Object eventItem : event.getItems()) {
                //Long itempk = Long.parseLong((String)eventItem);
                //logger.info("LOG02060: PK : " + itempk);
                //school = (Schools) getSession().load(Schools.class, itempk);
                school=(Schools) eventItem;
                if (event.isAdd()) {
                    logger.info("LOG02100: ADDING : " + school);
                    userSchools.add(school);
                } else {
                    logger.info("LOG02110: REMOVING : " + school);
                    removeSchool(school);
                    /*
                    if (userSchools.contains(school)) {
                        logger.info("LOG02120: CONTAINS : "  + school);
                        userSchools.remove(school);
                    }
                    */
                }

            }
            logger.info("LOG02080: SELECTED : " + userSchools);
        } catch (Exception e) {
            e.printStackTrace();
            Util.setFacesMessageError(e.getMessage());
        }

    }

    public DualListModel<Schools> getSchools() {
        findSchools();
        return schools;
    }

    public void setSchools(DualListModel<Schools> schools) {
        this.schools = schools;
    }

    public List<Schools> getUserSchools() {
        return userSchools;
    }

    public void setUserSchools(List<Schools> userSchools) {
        this.userSchools = userSchools;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
