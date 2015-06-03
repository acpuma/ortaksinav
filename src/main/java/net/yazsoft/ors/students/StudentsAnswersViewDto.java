package net.yazsoft.ors.students;

import net.yazsoft.ors.entities.Students;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.util.ArrayList;

public class StudentsAnswersViewDto extends StudentsDto{
    @Size(max = 1)
    @Column(length = 1)
    private String booklet;
    private ArrayList<String> answers;

    public StudentsAnswersViewDto(StudentsDto student) {
        answers=new ArrayList<>();
        fromStudentDto(student);
    }
    public void fromStudentDto(StudentsDto student) {
        this.tid=student.getTid();
        this.active=student.getActive();
        this.version=student.getVersion();
        this.name=student.getName();
        this.surname=student.getSurname();
        this.created=student.getCreated();
        this.updated=student.getCreated();
        this.username=student.getUsername();
        this.password=student.getPassword();
        this.fullname=student.getFullname();
        this.gender=student.getGender();
        this.schoolNo=student.getSchoolNo();
        this.mernis=student.getMernis();
        this.phone=student.getPhone();
        this.studentsAnswersCollection=student.getStudentsAnswersCollection();
        this.refSchool=student.getRefSchool();
        this.refSchoolClass=student.getRefSchoolClass();
        this.resultsCollection=student.getResultsCollection();
    }

    public StudentsAnswersViewDto() {
        answers=new ArrayList<>();
    }

    public String getBooklet() {
        return booklet;
    }

    public void setBooklet(String booklet) {
        this.booklet = booklet;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }
}
