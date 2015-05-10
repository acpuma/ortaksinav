package net.yazsoft.ors.results;

import net.yazsoft.ors.entities.Results;
import net.yazsoft.ors.entities.SchoolsClass;
import net.yazsoft.ors.entities.Students;
import net.yazsoft.ors.entities.StudentsAnswers;

import java.io.Serializable;
import java.util.List;

public class ResultsViewDto implements Serializable{
    Long tid;
    SchoolsClass schoolClass;
    Students student;
    Results result;
    List<StudentsAnswers> answersList;

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Students getStudent() {
        return student;
    }

    public void setStudent(Students student) {
        this.student = student;
    }

    public Results getResult() {
        return result;
    }

    public void setResult(Results result) {
        this.result = result;
    }

    public List<StudentsAnswers> getAnswersList() {
        return answersList;
    }

    public void setAnswersList(List<StudentsAnswers> answersList) {
        this.answersList = answersList;
    }

    public SchoolsClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolsClass schoolClass) {
        this.schoolClass = schoolClass;
    }
}
