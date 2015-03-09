package net.yazsoft.frame.security;

import net.yazsoft.frame.scopes.SessionScoped;
import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.Schools;
import net.yazsoft.ors.entities.Users;

import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class SessionInfo implements Serializable{

    Users user;
    Schools school;
    Exams exam;

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
    }

    public Exams getExam() {
        return exam;
    }

    public void setExam(Exams exam) {
        this.exam = exam;
    }
}
