package net.yazsoft.ors.students;


import net.yazsoft.frame.scopes.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class AnswersList {
    int no;
    String answerTrue;
    String answerStudent;
    boolean right;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getAnswerTrue() {
        return answerTrue;
    }

    public void setAnswerTrue(String answerTrue) {
        this.answerTrue = answerTrue;
    }

    public String getAnswerStudent() {
        return answerStudent;
    }

    public void setAnswerStudent(String answerStudent) {
        this.answerStudent = answerStudent;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "AnswersList{" +
                "no=" + no +
                ", answerTrue='" + answerTrue + '\'' +
                ", answerStudent='" + answerStudent + '\'' +
                ", right=" + right +
                '}';
    }
}
