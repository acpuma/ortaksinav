package net.yazsoft.ors.distribute;

import net.yazsoft.ors.entities.SchoolsClassType;
import net.yazsoft.ors.entities.Students;

import java.util.List;

/**
 * Created by fec on 13/04/16.
 */
public class ClassTypeStudents {
    List<Students> students;
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
}
