/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.entities;

import java.io.Serializable; import net.yazsoft.frame.hibernate.BaseEntity;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lessons.findAll", query = "SELECT l FROM Lessons l")})
public class Lessons extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long tid;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private boolean active;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private int version;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private int rank;
    @Basic(optional = false)
    @NotNull
    @Column(name = "question_count", nullable = false)
    private int questionCount;
    private Integer start;
    @OneToMany(mappedBy = "refLesson", fetch = FetchType.LAZY)
    private Collection<StudentsAnswers> studentsAnswersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refLesson", fetch = FetchType.LAZY)
    private Collection<Answers> answersCollection;
    @JoinColumn(name = "ref_exam", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Exams refExam;
    @JoinColumn(name = "ref_lesson_name", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private LessonsName refLessonName;

    public Lessons() {
    }

    public Lessons(Long tid) {
        this.tid = tid;
    }

    public Lessons(Long tid, boolean active, int version, int rank, int questionCount) {
        this.tid = tid;
        this.active = active;
        this.version = version;
        this.rank = rank;
        this.questionCount = questionCount;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public boolean getActive() {
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

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    @XmlTransient
    public Collection<StudentsAnswers> getStudentsAnswersCollection() {
        return studentsAnswersCollection;
    }

    public void setStudentsAnswersCollection(Collection<StudentsAnswers> studentsAnswersCollection) {
        this.studentsAnswersCollection = studentsAnswersCollection;
    }

    @XmlTransient
    public Collection<Answers> getAnswersCollection() {
        return answersCollection;
    }

    public void setAnswersCollection(Collection<Answers> answersCollection) {
        this.answersCollection = answersCollection;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tid != null ? tid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lessons)) {
            return false;
        }
        Lessons other = (Lessons) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
