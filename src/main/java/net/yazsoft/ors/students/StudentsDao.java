package net.yazsoft.ors.students;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Schools;
import net.yazsoft.ors.entities.Students;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class StudentsDao extends BaseGridDao<Students> implements Serializable{
    private static final Logger logger = Logger.getLogger(StudentsDao.class);
    Students selected;
    List<Students> students;

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

    public Students findBySchoolAndNumber(Schools school,Integer schoolNo) {
        logger.info("SCHOOL : " + school);
        Students temp=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refSchool", school));
            c.add(Restrictions.eq("schoolNo", schoolNo));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            temp = (Students)c.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return temp;
    }

    public StudentsDao() {
        super(Students.class);
    }

    public Students getSelected() {
        return selected;
    }

    public void setSelected(Students selected) {
        this.selected = selected;
    }

    public List<Students> getStudents() {
        return students;
    }

    public void setStudents(List<Students> students) {
        this.students = students;
    }
}
