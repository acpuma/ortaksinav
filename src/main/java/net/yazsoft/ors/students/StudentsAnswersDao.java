package net.yazsoft.ors.students;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.upload.UploadsBean;
import net.yazsoft.frame.upload.UploadsDao;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.answers.AnswersDao;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.exams.ExamsDao;
import net.yazsoft.ors.exams.ExamsSeasonDao;
import net.yazsoft.ors.exams.ExamsYearDao;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;

import javax.inject.Inject;
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
    List studentAnswers;
    ExamsYear filterYear;

    @Inject AnswersDao answersDao;
    @Inject UploadsBean uploadsBean;
    @Inject UploadsDao uploadsDao;
    @Inject ExamsDao examsDao;
    @Inject ExamsYearDao examsYearDao;
    @Inject ExamsSeasonDao examsSeasonDao;

    public Boolean getBookletReady() {
        Uploads upload=uploadsDao.getExamBooklet(getItem().getRefExam(),getItem().getBooklet());
        if (upload!=null) {
            logger.info("LOG01510:BOOKLET READY");
            return Boolean.TRUE;
        }
        logger.info("LOG01520:BOOKLET NOT READY");
        return Boolean.FALSE;
    }

    public void downloadBooklet() {
        try {
            Uploads upload=uploadsDao.getExamBooklet(getItem().getRefExam(),getItem().getBooklet());
            uploadsBean.downloadFile(upload);
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    public String getTrueAnswers() {
        String answers="";
        List<Answers> answersList=answersDao.getLessonAnswers(getItem().getRefExam(),getItem().getRefLesson());
        logger.info("LOG01400: ANSWERSLIST :" + answersList);
        for (Answers answer:answersList) {
            char booklet=getItem().getBooklet().toUpperCase().charAt(0);
            logger.info("LOG01420: BOOKLET : " + booklet);
            logger.info("LOG01430: answer : " + answer.getAnsA());
            switch (booklet) {
                case 'A' : answers=answers.concat(answer.getAnsA()); break;
                case 'B' : answers=answers.concat(answer.getAnsB()); break;
                case 'C' : answers=answers.concat(answer.getAnsC()); break;
                case 'D' : answers=answers.concat(answer.getAnsD()); break;
                case 'E' : answers=answers.concat(answer.getAnsE()); break;
            }
        }
        logger.info("LOG01410: ANSWERS : " + answers);
        return answers;
    }

    /**
     * For showing in the student homepage
     * @param student
     * @return
     */
    public List findByStudent(Students student) {
        logger.info("STUDENT : " + student);
        logger.info("LOG02710: YEAR / SEASON : " + examsDao.getFilterYear() + " / " + examsDao.getFilterSeason());
        List list=null;
        try {
            if (examsDao.getFilterYear()==null) {
                examsDao.setFilterYear(examsYearDao.findDefaultYear());
                examsDao.setFilterSeason(examsSeasonDao.findDefaultSeason());
            }
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refStudent", student));
            c.add(Restrictions.eq("active", true));
            DetachedCriteria subquery = DetachedCriteria.forClass(Exams.class, "exam")
                    // Filter the Subquery
                    .add(Restrictions.eq("refExamYear", examsDao.getFilterYear()))
                    .add(Restrictions.eq("refExamSeason", examsDao.getFilterSeason()))
                            // SELECT The exam Id
                    .setProjection(Projections.property("exam.tid"));
            //c.add(Restrictions.eq("refExam.refExamSeason",examsDao.getFilterSeason()));
            //c.add(Restrictions.eq("refExam.refExamYear",examsDao.getFilterYear()));
            //c.addOrder(Order.desc(""));
            //c.add(Restrictions.eq("isDeleted", false));
            c.add(Subqueries.propertyIn("refExam", subquery));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        studentAnswers=list;
        return list;
    }

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

    public List findByExamAndStudent(Exams exam,Students student) {
        logger.info("EXAM : " + exam+ ", STUDENT : " + student);
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refExam", exam));
            c.add(Restrictions.eq("refStudent", student));
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

    public List findByExamAndLessonAndClass(Exams exam,Lessons lesson,SchoolsClass sclass) {
        logger.info("EXAM : " + exam);
        List list=null;
        try {
            DetachedCriteria subquery = DetachedCriteria.forClass(Students.class, "stu")
                    // Filter the Subquery
                    .add(Restrictions.eq("refSchoolClass", sclass))
                            // SELECT The User Id
                    .setProjection(Projections.property("stu.tid"));
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refExam", exam));
            c.add(Restrictions.eq("refLesson", lesson));
            //c.add(Restrictions.eq("refStudent.refSchoolClass", sclass));
            c.add(Subqueries.propertyIn("refStudent", subquery));
            c.add(Restrictions.eq("active", true));
            //c.addOrder(Order.desc("score"));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
            logger.info("LOG02570: LIST COUNT : " + list.size());
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
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

    public List getStudentAnswers() {
        //if (studentAnswers==null) {
            findByStudent(Util.getActiveStudent());
        //}
        return studentAnswers;
    }

    public void setStudentAnswers(List studentAnswers) {
        this.studentAnswers = studentAnswers;
    }

    public ExamsYear getFilterYear() {
        return filterYear;
    }

    public void setFilterYear(ExamsYear filterYear) {
        this.filterYear = filterYear;
    }
}
