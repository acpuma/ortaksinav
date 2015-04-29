package net.yazsoft.ors.students;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.SchoolsClass;
import net.yazsoft.ors.entities.Students;
import net.yazsoft.ors.entities.StudentsAnswers;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class StudentsAnswersDao extends BaseGridDao<StudentsAnswers> implements Serializable{
    private static final Logger logger = Logger.getLogger(StudentsAnswersDao.class);
    StudentsAnswers selected;

    public List findByExam(Exams exam) {
        logger.info("EXAM : " + exam);
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refExam", exam));
            c.add(Restrictions.eq("active", true));
            //c.addOrder(Order.desc("score"));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List findExamClasses(Exams exam) {
        logger.info("EXAM : " + exam);
        List<StudentsAnswers> list=null;
        List<SchoolsClass> classes=new ArrayList<>();
        try {
            list=findByExam(exam);
            for (StudentsAnswers answer:list) {
                if (!classes.contains(answer.getRefStudent().getRefSchoolClass())) {
                    classes.add(answer.getRefStudent().getRefSchoolClass());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return classes;
    }

    public List findExamStudents(Exams exam) {
        logger.info("EXAM : " + exam);
        List<StudentsAnswers> list=null;
        List<Students> students=new ArrayList<>();
        try {
            list=findByExam(exam);
            for (StudentsAnswers answer:list) {
                if (!students.contains(answer.getRefStudent())) {
                    students.add(answer.getRefStudent());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    public void deleteExamAnswers(Exams exam) {
        String hql = "delete from StudentsAnswers where refExam= :exam";
        getSession().createQuery(hql).setLong("exam", exam.getTid()).executeUpdate();
    }

    public StudentsAnswersDao() {
        super(StudentsAnswers.class);
    }

    public StudentsAnswers getSelected() {
        return selected;
    }

    public void setSelected(StudentsAnswers selected) {
        this.selected = selected;
    }
}
