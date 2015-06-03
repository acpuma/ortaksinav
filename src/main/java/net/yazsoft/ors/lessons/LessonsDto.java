package net.yazsoft.ors.lessons;

import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.Lessons;
import net.yazsoft.ors.entities.LessonsName;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class LessonsDto implements Comparable<LessonsDto>,Serializable{
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long tid;
    private boolean active;
    private int version;
    private Date created;
    private Date updated;
    private int rank;
    private int questionCount;
    private Integer start;
    private Exams refExam;
    private LessonsName refLessonName;

    public Lessons toEntity() {
        return toEntity(null);
    }

    public Lessons toEntity(Lessons entity) {
        if ((entity==null) || (entity.getTid()==null)) {
            entity=new Lessons();
        }
        entity.setTid(this.tid);
        entity.setActive(this.active);
        entity.setVersion(this.version);
        entity.setCreated(this.created);
        entity.setUpdated(this.updated);
        entity.setRefExam(this.refExam);
        entity.setRank(this.rank);
        entity.setQuestionCount(this.questionCount);
        entity.setStart(this.start);
        entity.setRefLessonName(this.refLessonName);
        return entity;
    }

    @Override
    public int compareTo(LessonsDto o) {
        return Comparators.START.compare(this, o);
    }

    public static class Comparators {
        public static final Comparator<LessonsDto> START =
                (LessonsDto o1, LessonsDto o2) -> o1.start.compareTo(o2.start);
        //public static final Comparator<Student> AGE =
        // (Student o1, Student o2) -> Integer.compare(o1.age, o2.age);
        //public static final Comparator<Student> NAMEANDAGE =
        // (Student o1, Student o2) -> NAME.thenComparing(AGE).compare(o1, o2);
    }

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

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
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
