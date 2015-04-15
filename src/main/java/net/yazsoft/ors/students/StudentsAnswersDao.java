package net.yazsoft.ors.students;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.StudentsAnswers;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
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
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
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
