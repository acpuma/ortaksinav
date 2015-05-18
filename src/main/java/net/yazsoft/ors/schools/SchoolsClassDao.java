package net.yazsoft.ors.schools;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Schools;
import net.yazsoft.ors.entities.SchoolsClass;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class SchoolsClassDao extends BaseGridDao<SchoolsClass> implements Serializable{
    private static final Logger logger = Logger.getLogger(SchoolsClassDao.class);
    SchoolsClass selected;
    List<SchoolsClass> schoolsClasses;

    public List findBySchool(Schools school) {
        logger.info("SCHOOL : " + school);
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refSchool", school));
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

    public SchoolsClass findBySchoolAndName(Schools school,String name) {
        logger.info("SCHOOL : " + school);
        SchoolsClass temp=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refSchool", school));
            c.add(Restrictions.eq("name", name));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            temp = (SchoolsClass)c.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return temp;
    }

    public SchoolsClassDao() {
        super(SchoolsClass.class);
    }

    public SchoolsClass getSelected() {
        return selected;
    }

    public void setSelected(SchoolsClass selected) {
        this.selected = selected;
    }

    public List<SchoolsClass> getSchoolsClasses() {
        return schoolsClasses;
    }

    public void setSchoolsClasses(List<SchoolsClass> schoolsClasses) {
        this.schoolsClasses = schoolsClasses;
    }
}
