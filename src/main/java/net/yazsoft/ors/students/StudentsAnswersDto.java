/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.students;

import java.io.Serializable;

import net.yazsoft.frame.hibernate.BaseDao;
import net.yazsoft.frame.hibernate.BaseEntity;
import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.Lessons;
import net.yazsoft.ors.entities.Students;
import net.yazsoft.ors.entities.StudentsAnswers;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.Date;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

public class StudentsAnswersDto extends BaseEntity implements Comparable<StudentsAnswersDto>,Serializable {
    private static final Logger logger = Logger.getLogger(StudentsAnswersDto.class);
    private static final long serialVersionUID = 1L;
    private Long id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long tid;
    private Boolean active;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private int version;
    @Size(max = 1)
    @Column(length = 1)
    private String booklet;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String answers;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    private Integer trues;
    private Integer falses;
    private Integer nulls;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 12)
    private Float nets;
    @Column(name = "rank_school")
    private Integer rankSchool;
    @Column(name = "rank_class")
    private Integer rankClass;
    @Column(precision = 12)
    private Float score;
    @JoinColumn(name = "ref_exam", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Exams refExam;
    @JoinColumn(name = "ref_lesson", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Lessons refLesson;
    @JoinColumn(name = "ref_student", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Students refStudent;

    @Override
    public int compareTo(StudentsAnswersDto o) {
        return Comparators.SCORE.compare(this, o);
    }

    public static class Comparators {
        public static final Comparator<StudentsAnswersDto> SCORE = (StudentsAnswersDto o1, StudentsAnswersDto o2) -> o1.score.compareTo(o2.score);
        //public static final Comparator<Student> AGE = (Student o1, Student o2) -> Integer.compare(o1.age, o2.age);
        //public static final Comparator<Student> NAMEANDAGE = (Student o1, Student o2) -> NAME.thenComparing(AGE).compare(o1, o2);
    }

    public StudentsAnswers toEntity() {
        return toEntity(null);
    }

    public StudentsAnswers toEntity(StudentsAnswers entity) {
        if (entity==null) {
            entity = new StudentsAnswers();
        }
        entity.setTid(this.tid);
        entity.setActive(this.active);
        entity.setVersion(this.version);
        entity.setCreated(this.created);
        entity.setUpdated(this.updated);
        entity.setBooklet(this.booklet);
        entity.setAnswers(this.answers);
        entity.setRefExam(this.refExam);
        entity.setRefLesson(this.refLesson);
        entity.setRefStudent(this.refStudent);
        entity.setTrues(this.trues);
        entity.setFalses(this.falses);
        entity.setNulls(this.nulls);
        entity.setNets(this.nets);
        entity.setRankSchool(this.rankSchool);
        entity.setRankClass(this.rankClass);
        entity.setScore(this.score);
        return entity;
    }

    public void fromEntity(StudentsAnswers entity) {
        this.tid=entity.getTid();
        this.active = entity.getActive();
        this.version = entity.getVersion();
        this.created = entity.getCreated();
        this.updated = entity.getUpdated();
        this.booklet = entity.getBooklet();
        this.answers = entity.getAnswers();
        this.refExam = entity.getRefExam();
        this.refLesson = entity.getRefLesson();
        this.refStudent = entity.getRefStudent();
        this.trues= entity.getTrues();
        this.falses=entity.getFalses();
        this.nulls=entity.getNulls();
        this.nets=entity.getNets();
        this.rankSchool=entity.getRankSchool();
        this.rankClass=entity.getRankClass();
        this.score=entity.getScore();
    }




    public StudentsAnswersDto(StudentsAnswers entity) {
        fromEntity(entity);
    }

    public StudentsAnswersDto() {
    }

    public StudentsAnswersDto(Long tid) {
        this.tid = tid;
    }

    public StudentsAnswersDto(Long tid, int version, String answers) {
        this.tid = tid;
        this.version = version;
        this.answers = answers;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public Exams getRefExam() {
        return refExam;
    }

    public void setRefExam(Exams refExam) {
        this.refExam = refExam;
    }

    public Lessons getRefLesson() {
        return refLesson;
    }

    public void setRefLesson(Lessons refLesson) {
        this.refLesson = refLesson;
    }

    public Students getRefStudent() {
        return refStudent;
    }

    public void setRefStudent(Students refStudent) {
        this.refStudent = refStudent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tid != null ? tid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StudentsAnswersDto)) {
            return false;
        }
        StudentsAnswersDto other = (StudentsAnswersDto) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTrues() {
        return trues;
    }

    public void setTrues(Integer trues) {
        this.trues = trues;
    }

    public Integer getFalses() {
        return falses;
    }

    public void setFalses(Integer falses) {
        this.falses = falses;
    }

    public Integer getNulls() {
        return nulls;
    }

    public void setNulls(Integer nulls) {
        this.nulls = nulls;
    }

    public Float getNets() {
        return nets;
    }

    public void setNets(Float nets) {
        this.nets = nets;
    }

    public Integer getRankSchool() {
        return rankSchool;
    }

    public void setRankSchool(Integer rankSchool) {
        this.rankSchool = rankSchool;
    }

    public Integer getRankClass() {
        return rankClass;
    }

    public void setRankClass(Integer rankClass) {
        this.rankClass = rankClass;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }


}
