package net.yazsoft.ors.lessons;

import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.LessonsName;

import java.util.Date;

public class LessonsDto {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long tid;
    private boolean active;
    private int version;
    private Date created;
    private Date updated;
    private int rank;
    private int questionCount;
    private int start;
    private Exams refExam;
    private LessonsName refLessonName;

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public Exams getRefExam() {
        return refExam;
    }

    public void setRefExam(Exams refExam) {
        this.refExam = refExam;
    }

    public LessonsName getRefLessonName() {
        return refLessonName;
    }

    public void setRefLessonName(LessonsName refLessonName) {
        this.refLessonName = refLessonName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "LessonsDto{" +
                "id=" + id +
                ", tid=" + tid +
                ", active=" + active +
                ", version=" + version +
                ", created=" + created +
                ", updated=" + updated +
                ", rank=" + rank +
                ", questionCount=" + questionCount +
                ", start=" + start +
                ", refExam=" + refExam +
                ", refLessonName=" + refLessonName +
                '}';
    }
}
