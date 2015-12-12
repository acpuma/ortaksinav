package net.yazsoft.ors.schools;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.security.UsersDao;
import net.yazsoft.frame.security.ZlogLoginDao;
import net.yazsoft.frame.utils.Constants;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.deleteRecords.DeleteRecordsDao;
import net.yazsoft.ors.entities.Schools;
import net.yazsoft.ors.entities.Users;
import net.yazsoft.ors.entities.ZlogLogin;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Transactional
public class SchoolsDao extends BaseGridDao<Schools> implements Serializable{
    private static final Logger logger = Logger.getLogger(SchoolsDao.class);
    Schools selected;
    List<Schools> userSchools;

    @Inject UsersDao usersDao;
    @Inject
    @Lazy
    DeleteRecordsDao deleteRecordsDao;
    @Inject ZlogLoginDao zlogLoginDao;

    public SchoolsDao() {
        super(Schools.class);
    }

    @Override
    public void delete() {
        deleteRecordsDao.setSchool(getItem());
        deleteRecordsDao.deleteAllSchool();
        for (Users tempuser:getItem().getUsersCollection()) {
            tempuser.setRefActiveExam(null);
            tempuser.setRefActiveSchool(null);
            usersDao.update(tempuser);
        }
        for (ZlogLogin log:getItem().getZlogLoginCollection()) {
            log.setRefSchool(null);
            zlogLoginDao.update(log);
        }
        for (ZlogLogin log:getItem().getZlogLoginCollection()) {
            log.setRefSchool(null);
            log.setRefStudent(null);
            zlogLoginDao.update(log);
        }
        super.delete();
    }

    @Transactional
    @Override
    public Long create() {
        getItem().setActive(Boolean.TRUE);
        getItem().setUseMernis(Boolean.FALSE);
        Long pk=super.create();
        Schools newschool=getById(pk);
        newschool.setUsersCollection(usersDao.findAdmins());
        saveOrUpdate(newschool);
        return pk;
    }

    public List<Schools> findActiveUserSchools() {
        Users user=Util.getActiveUser();
        if (user.getRefRole().getName().equals(Constants.ROLE_ADMIN)) {
            return getAll();
        }
        return new ArrayList<>(Util.getActiveUser().getSchoolsCollection());
    }

    public List<Schools> findSchools() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public Schools getSelected() {
        return selected;
    }

    public void setSelected(Schools selected) {
        this.selected = selected;
    }

}
