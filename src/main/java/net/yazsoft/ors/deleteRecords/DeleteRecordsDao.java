package net.yazsoft.ors.deleteRecords;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.Schools;
import net.yazsoft.ors.entities.SchoolsClass;
import net.yazsoft.ors.entities.Students;
import net.yazsoft.ors.schools.SchoolsClassDao;
import net.yazsoft.ors.students.StudentsAnswersDao;
import net.yazsoft.ors.students.StudentsDao;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class DeleteRecordsDao extends BaseGridDao implements Serializable{
    Schools school;
    Exams exam;
    List<Students> students;
    List<SchoolsClass> classes;

    @Inject StudentsAnswersDao studentsAnswersDao;
    @Inject StudentsDao studentsDao;
    @Inject SchoolsClassDao schoolsClassDao;

    @PostConstruct
    public void init() {
    }

    public void deleteAllExam() {
        deleteResultsExam();
        deleteStudentsAnswersExam();
        deleteStudentsExam();
        deleteClassesExam();
        getSession().flush();
        getSession().clear();
    }

    public void deleteAllSchool() {
        deleteResultsSchool();
        deleteStudentsAnswersSchool();
        deleteStudentsSchool();
        deleteClassesSchool();
        getSession().flush();
        getSession().clear();
    }


    public void deleteClassesExam() {
        getSchool();
        getClasses();
        try {
            for (SchoolsClass schoolsClass:classes){
                schoolsClassDao.delete(schoolsClass);
            }
            Util.setFacesMessage("Silindi");
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteClassesSchool() {
        getSchool();
        try {
            String hql = "delete from SchoolsClass where refSchool= :school";
            getSession().createQuery(hql).setLong("school", school.getTid()).executeUpdate();
            Util.setFacesMessage("Silindi");
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteStudentsExam() {
        getExam();
        getStudents();
        try {
            for (Students student:students){
                studentsDao.delete(student);
            }
            Util.setFacesMessage("Silindi");
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteStudentsSchool() {
        getSchool();
        try {
            String hql = "delete from Students where refSchool= :school";
            getSession().createQuery(hql).setLong("school", school.getTid()).executeUpdate();
            Util.setFacesMessage("Silindi");
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteStudentsAnswersExam() {
        getExam();
        getStudents();
        try {
            String hql = "delete from StudentsAnswers where refExam= :exam";
            getSession().createQuery(hql).setLong("exam", exam.getTid()).executeUpdate();
            Util.setFacesMessage("Silindi");
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteStudentsAnswersSchool() {
        getSchool();
        getStudents();
        try {
            String hql = "delete from StudentsAnswers where refSchool = :school";
            getSession().createQuery(hql).setLong("school", school.getTid()).executeUpdate();
            Util.setFacesMessage("Silindi");
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteResultsExam() {
        getExam();
        try {
            String hql = "delete from Results where refExam = :exam";
            getSession().createQuery(hql).setLong("exam", exam.getTid()).executeUpdate();
            hql = "update StudentsAnswers a set trues=NULL,falses=NULL,nets=NULL,score=NULL," +
                    "rankClass=NULL,rankSchool=NULL,a.nulls=NULL where refExam = :exam";
            getSession().createQuery(hql).setLong("exam", exam.getTid()).executeUpdate();
            Util.setFacesMessage("Silindi");
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteResultsSchool() {
        getSchool();
        try {
            String hql = "delete from Results where refSchool = :school";
            getSession().createQuery(hql).setLong("school", school.getTid()).executeUpdate();
            hql = "update StudentsAnswers a set trues=NULL,falses=NULL,nets=NULL,score=NULL," +
                    "rankClass=NULL,rankSchool=NULL,a.nulls=NULL where refSchool = :school";
            getSession().createQuery(hql).setLong("school", school.getTid()).executeUpdate();
            Util.setFacesMessage("Silindi");
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Students> getStudents() {
        if (students==null) {
            students=studentsAnswersDao.findExamStudents(exam);
        }
        return students;
    }

    public void setStudents(List<Students> students) {
        this.students = students;
    }

    public List<SchoolsClass> getClasses() {
        if (classes==null) {
            classes=studentsAnswersDao.findExamClasses(exam);
        }
        return classes;
    }

    public void setClasses(List<SchoolsClass> classes) {
        this.classes = classes;
    }

    public Schools getSchool() {
        if (school==null) {
            school=Util.getActiveSchool();
        }
        return school;
    }

    public void setSchool(Schools school) {
        this.school = school;
    }

    public Exams getExam() {
        if (exam==null) {
            exam=Util.getActiveExam();
        }
        return exam;
    }

    public void setExam(Exams exam) {
        this.exam = exam;
    }
}
