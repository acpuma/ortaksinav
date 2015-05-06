package net.yazsoft.frame.security;

import net.yazsoft.frame.scopes.SessionScoped;
import net.yazsoft.ors.entities.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class SessionInfo implements Serializable{

    Students student;
    Users user;
    Schools school;
    Exams exam;
    SchoolsClass sclass;

    @Inject
    UsersDao usersDao;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Schools getSchool() {
        return school;
    }

    public void setSchool(Schools school) {
        this.school = school;
        if (user!=null) {
            user.setRefActiveSchool(school);
            usersDao.update(user);
        }
    }

    public Exams getExam() {
        return exam;
    }

    public void setExam(Exams exam) {
        this.exam = exam;
        user.setRefActiveExam(exam);
        usersDao.update(user);
    }

    public Students getStudent() {
        return student;
    }

    public void setStudent(Students student) {
        this.student = student;
    }

    public SchoolsClass getSclass() {
        return sclass;
    }

    public void setSclass(SchoolsClass sclass) {
        this.sclass = sclass;
    }
}
