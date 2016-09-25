package net.yazsoft.ors.schools;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.Results;
import net.yazsoft.ors.entities.Schools;
import net.yazsoft.ors.entities.SchoolsClass;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named
@ViewScoped
public class SchoolsClassDao extends BaseGridDao<SchoolsClass> implements Serializable{
    private static final Logger logger = Logger.getLogger(SchoolsClassDao.class);
    SchoolsClass selected;
    List<SchoolsClass> schoolsClasses;
    List<SchoolsClass> foundClasses;


    @Inject SchoolsClassTypeDao schoolsClassTypeDao;


    public List<SchoolsClass> findByActiveSchool() {
        return findBySchool(Util.getActiveSchool());
    }

    public List findBySchool(Schools school) {
        logger.info("SCHOOL : " + school);
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refSchool", school));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            c.addOrder(Order.asc("name"));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Find class by name from current active schollClasses list
     * @param name class name to find
     * @return
     */
    public SchoolsClass findByNameFromList(String name) {
        if (foundClasses==null) {
            foundClasses=findBySchool(Util.getActiveSchool());
        }
        for (SchoolsClass sclass:foundClasses) {
            if (sclass.getName().equals(name)) {
                return sclass;
            }
        }
        return null;
    }

    //@Transactional
    public SchoolsClass findBySchoolAndName(Schools school,String name) {
        //List list;
        //logger.info("SCHOOL : " + school);
        SchoolsClass temp=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refSchool", school));
            //c.add(Restrictions.eq("name", name.toUpperCase()));
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.sqlRestriction("name = ? collate utf8_turkish_ci", name, new StringType()) );
            //c.add(Restrictions.eq("isDeleted", false));
            //list=c.list();
            //logger.info("LOG02910: LIST : " + list);
            temp = (SchoolsClass)c.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return temp;
    }

    public List<SchoolsClass> findByExam(Exams exam) {
        logger.info("EXAM : " + exam);
        List<Results> list=null;
        try {
            Criteria c = getSession().createCriteria(Results.class);
            c.add(Restrictions.eq("refExam", exam));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = (List<Results>) c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        List<SchoolsClass> schoolsClasses=new ArrayList<>();
        List<Long> classTids=new ArrayList<>();
        for (Results result:list) {
            if ( (result!=null) && (result.getRefStudent()!=null) && (result.getRefStudent().getRefSchoolClass()!=null)
                    && (result.getRefStudent().getRefSchoolClass().getTid()!=null) ) {
                if (!classTids.contains(result.getRefStudent().getRefSchoolClass().getTid())) {
                    classTids.add(result.getRefStudent().getRefSchoolClass().getTid());
                }
            }
        }

        logger.info("LOG01640: CLASS TIDS : " + classTids);
        for (Long classTid:classTids) {
            schoolsClasses.add((SchoolsClass)getSession().load(SchoolsClass.class,classTid));
        }
        BeanComparator fieldComparator=new BeanComparator("name");
        Collections.sort(schoolsClasses, fieldComparator);
        logger.info("LOG01650: EXAM CLASSES : " + schoolsClasses);

        return schoolsClasses;

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

    public List<SchoolsClass> getFoundClasses() {
        return foundClasses;
    }

    public void setFoundClasses(List<SchoolsClass> foundClasses) {
        this.foundClasses = foundClasses;
    }


}
