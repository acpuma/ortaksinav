package net.yazsoft.ors.distribute;

import net.yazsoft.ors.entities.SchoolsClassType;
import net.yazsoft.ors.entities.Students;

import java.util.List;

public class ClassTypeStudents {
    List<Students> students;
    List<Integer> booklets;
    SchoolsClassType classType;

    public List<Students> getStudents() {
        return students;
    }

    public void setStudents(List<Students> students) {
        this.students = students;
    }

    public SchoolsClassType getClassType() {
        return classType;
    }

    public void setClassType(SchoolsClassType classType) {
        this.classType = classType;
    }

    public List<Integer> getBooklets() {
        return booklets;
    }

    public void setBooklets(List<Integer> booklets) {
        this.booklets = booklets;
    }
}
