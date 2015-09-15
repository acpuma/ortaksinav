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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Exams.findAll", query = "SELECT e FROM Exams e")})
public class Exams extends BaseEntity implements Serializable {
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name_tr", nullable = false, length = 255)
    private String nameTr;
    @Size(max = 255)
    @Column(name = "name_en", length = 255)
    private String nameEn;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    private Integer time;
    @Size(max = 45)
    @Column(length = 45)
    private String number;
    @OneToMany(mappedBy = "refExam", fetch = FetchType.LAZY)
    private Collection<Uploads> uploadsCollection;
    @OneToMany(mappedBy = "refActiveExam", fetch = FetchType.LAZY)
    private Collection<Users> usersCollection;
    @OneToMany(mappedBy = "refExam", fetch = FetchType.LAZY)
    private Collection<StudentsAnswers> studentsAnswersCollection;
    @JoinColumn(name = "ref_answer_type", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private ExamsAnswerType refAnswerType;
    @JoinColumn(name = "ref_booklet_type", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private ExamsBookletType refBookletType;
    @JoinColumn(name = "ref_exam_season_number", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private ExamsSeasonNumber refExamSeasonNumber;
    @JoinColumn(name = "ref_exam_type", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ExamsType refExamType;
    @JoinColumn(name = "ref_exam_year", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ExamsYear refExamYear;
    @JoinColumn(name = "ref_false_type", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private ExamsFalseType refFalseType;
    @JoinColumn(name = "ref_school", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Schools refSchool;
    @JoinColumn(name = "ref_exam_season", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ExamsSeason refExamSeason;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refExam", fetch = FetchType.LAZY)
    private Collection<ExamsParameters> examsParametersCollection;
    @OneToMany(mappedBy = "refExam", fetch = FetchType.LAZY)
    private Collection<Answers> answersCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refExam", fetch = FetchType.LAZY)
    private Collection<Lessons> lessonsCollection;
    @OneToMany(mappedBy = "refExam", fetch = FetchType.LAZY)
    private Collection<Results> resultsCollection;
    @OneToMany(mappedBy = "refExam", fetch = FetchType.LAZY)
    private Collection<ResultsSend> resultsSendCollection;

    public Exams() {
    }

    public Exams(Long tid) {
        this.tid = tid;
    }

    public Exams(Long tid, boolean active, int version, String nameTr) {
        this.tid = tid;
        this.active = active;
        this.version = version;
        this.nameTr = nameTr;
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

    public String getNameTr() {
        return nameTr;
    }

    public void setNameTr(String nameTr) {
        this.nameTr = nameTr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @XmlTransient
    public Collection<Uploads> getUploadsCollection() {
        return uploadsCollection;
    }

    public void setUploadsCollection(Collection<Uploads> uploadsCollection) {
        this.uploadsCollection = uploadsCollection;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    @XmlTransient
    public Collection<StudentsAnswers> getStudentsAnswersCollection() {
        return studentsAnswersCollection;
    }

    public void setStudentsAnswersCollection(Collection<StudentsAnswers> studentsAnswersCollection) {
        this.studentsAnswersCollection = studentsAnswersCollection;
    }

    public ExamsAnswerType getRefAnswerType() {
        return refAnswerType;
    }

    public void setRefAnswerType(ExamsAnswerType refAnswerType) {
        this.refAnswerType = refAnswerType;
    }

    public ExamsBookletType getRefBookletType() {
        return refBookletType;
    }

    public void setRefBookletType(ExamsBookletType refBookletType) {
        this.refBookletType = refBookletType;
    }

    public ExamsSeasonNumber getRefExamSeasonNumber() {
        return refExamSeasonNumber;
    }

    public void setRefExamSeasonNumber(ExamsSeasonNumber refExamSeasonNumber) {
        this.refExamSeasonNumber = refExamSeasonNumber;
    }

    public ExamsType getRefExamType() {
        return refExamType;
    }

    public void setRefExamType(ExamsType refExamType) {
        this.refExamType = refExamType;
    }

    public ExamsYear getRefExamYear() {
        return refExamYear;
    }

    public void setRefExamYear(ExamsYear refExamYear) {
        this.refExamYear = refExamYear;
    }

    public ExamsFalseType getRefFalseType() {
        return refFalseType;
    }

    public void setRefFalseType(ExamsFalseType refFalseType) {
        this.refFalseType = refFalseType;
    }

    public Schools getRefSchool() {
        return refSchool;
    }

    public void setRefSchool(Schools refSchool) {
        this.refSchool = refSchool;
    }

    public ExamsSeason getRefExamSeason() {
        return refExamSeason;
    }

    public void setRefExamSeason(ExamsSeason refExamSeason) {
        this.refExamSeason = refExamSeason;
    }

    @XmlTransient
    public Collection<ExamsParameters> getExamsParametersCollection() {
        return examsParametersCollection;
    }

    public void setExamsParametersCollection(Collection<ExamsParameters> examsParametersCollection) {
        this.examsParametersCollection = examsParametersCollection;
    }

    @XmlTransient
    public Collection<Answers> getAnswersCollection() {
        return answersCollection;
    }

    public void setAnswersCollection(Collection<Answers> answersCollection) {
        this.answersCollection = answersCollection;
    }

    @XmlTransient
    public Collection<Lessons> getLessonsCollection() {
        return lessonsCollection;
    }

    public void setLessonsCollection(Collection<Lessons> lessonsCollection) {
        this.lessonsCollection = lessonsCollection;
    }

    @XmlTransient
    public Collection<Results> getResultsCollection() {
        return resultsCollection;
    }

    public void setResultsCollection(Collection<Results> resultsCollection) {
        this.resultsCollection = resultsCollection;
    }

    public Collection<ResultsSend> getResultsSendCollection() {
        return resultsSendCollection;
    }

    public void setResultsSendCollection(Collection<ResultsSend> resultsSendCollection) {
        this.resultsSendCollection = resultsSendCollection;
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
        if (!(object instanceof Exams)) {
            return false;
        }
        Exams other = (Exams) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
