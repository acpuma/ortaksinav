package net.yazsoft.ors.answers;

import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.Lessons;

public class AnswersAuto {
    Exams exam;
    Lessons lesson;
    String booklet;
    String answers;

    public Exams getExam() {
        return exam;
    }

    public void setExam(Exams exam) {
        this.exam = exam;
    }

    public Lessons getLesson() {
        return lesson;
    }

    public void setLesson(Lessons lesson) {
        this.lesson = lesson;
    }

    public String getBooklet() {
        return booklet;
    }

    public void setBooklet(String booklet) {
        this.booklet = booklet;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "AnswersAuto{" +
                "exam=" + exam.getNameTr() +
                ", lesson=" + lesson.getRefLessonName().getNameTr() +
                ", booklet='" + booklet + '\'' +
                ", answers='" + answers + '\'' +
                '}';
    }
}
