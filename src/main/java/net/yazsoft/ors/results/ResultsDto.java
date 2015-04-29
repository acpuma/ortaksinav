/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.results;

import java.io.Serializable; import net.yazsoft.frame.hibernate.BaseEntity;
import net.yazsoft.ors.entities.*;

import java.util.Comparator;
import java.util.Date;
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
import javax.xml.bind.annotation.XmlRootElement;

public class ResultsDto extends BaseEntity implements Comparable<ResultsDto>,Serializable {
    private static final long serialVersionUID = 1L;
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
    @JoinColumn(name = "ref_student", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Students refStudent;
    @JoinColumn(name = "ref_school", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Schools refSchool;

    public void fromEntity(Results entity) {
        this.tid=entity.getTid();
        this.active=entity.getActive();
        this.version=entity.getVersion();
        this.updated=entity.getUpdated();
        this.created=entity.getCreated();
        this.trues=entity.getTrues();
        this.falses=entity.getFalses();
        this.nulls=entity.getNulls();
        this.score=entity.getScore();
        this.rankClass=entity.getRankClass();
        this.rankSchool=entity.getRankSchool();
        this.nets=entity.getNets();
        this.refExam=entity.getRefExam();
        this.refStudent=entity.getRefStudent();
        this.refSchool=entity.getRefSchool();
    }

    public Results toEntity() {
        return toEntity(null);
    }

    public Results toEntity(Results entity) {
        if ((entity==null) || (entity.getTid()==null)) {
            entity=new Results();
        }
        entity.setTid(this.tid);
        entity.setActive(this.active);
        entity.setVersion(this.version);
        entity.setCreated(this.created);
        entity.setUpdated(this.updated);
        entity.setRefExam(this.refExam);
        entity.setRefStudent(this.refStudent);
        entity.setTrues(this.trues);
        entity.setFalses(this.falses);
        entity.setNulls(this.nulls);
        entity.setNets(this.nets);
        entity.setScore(this.score);
        entity.setRankClass(this.rankClass);
        entity.setRankSchool(this.rankSchool);
        entity.setRefSchool(this.refSchool);
        return entity;
    }

    @Override
    public int compareTo(ResultsDto o) {
        return Comparators.SCORE.compare(this, o);
    }

    public static class Comparators {
        public static final Comparator<ResultsDto> SCORE =
                (ResultsDto o1, ResultsDto o2) -> o1.score.compareTo(o2.score);
        //public static final Comparator<Student> AGE =
        // (Student o1, Student o2) -> Integer.compare(o1.age, o2.age);
        //public static final Comparator<Student> NAMEANDAGE =
        // (Student o1, Student o2) -> NAME.thenComparing(AGE).compare(o1, o2);
    }

    public ResultsDto(Results entity) {
        fromEntity(entity);
    }

    public ResultsDto() {
    }

    public ResultsDto(Long tid) {
        this.tid = tid;
    }

    public ResultsDto(Long tid, int version) {
        this.tid = tid;
        this.version = version;
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

    public Exams getRefExam() {
        return refExam;
    }

    public void setRefExam(Exams refExam) {
        this.refExam = refExam;
    }

    public Students getRefStudent() {
        return refStudent;
    }

    public void setRefStudent(Students refStudent) {
        this.refStudent = refStudent;
    }

    public Schools getRefSchool() {
        return refSchool;
    }

    public void setRefSchool(Schools refSchool) {
        this.refSchool = refSchool;
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
        if (!(object instanceof ResultsDto)) {
            return false;
        }
        ResultsDto other = (ResultsDto) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
