/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.entities;

import java.io.Serializable; import net.yazsoft.frame.hibernate.BaseEntity;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

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
    @Size(max = 45)
    @Column(length = 45)
    private String number;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ref_exam_year", nullable = false)
    private long refExamYear;
    @Column(name = "answer_count")
    private Integer answerCount;
    @Column(name = "booklet_count")
    private Integer bookletCount;
    @Column(name = "false_delete")
    private Integer falseDelete;
    @JoinColumn(name = "ref_exam_type", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ExamsType refExamType;
    @JoinColumn(name = "ref_school", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Schools refSchool;
    @JoinColumn(name = "ref_exam_season", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ExamsSeason refExamSeason;

    public Exams() {
    }

    public Exams(Long tid) {
        this.tid = tid;
    }

    public Exams(Long tid, boolean active, int version, String nameTr, long refExamYear) {
        this.tid = tid;
        this.active = active;
        this.version = version;
        this.nameTr = nameTr;
        this.refExamYear = refExamYear;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getRefExamYear() {
        return refExamYear;
    }

    public void setRefExamYear(long refExamYear) {
        this.refExamYear = refExamYear;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public Integer getBookletCount() {
        return bookletCount;
    }

    public void setBookletCount(Integer bookletCount) {
        this.bookletCount = bookletCount;
    }

    public Integer getFalseDelete() {
        return falseDelete;
    }

    public void setFalseDelete(Integer falseDelete) {
        this.falseDelete = falseDelete;
    }

    public ExamsType getRefExamType() {
        return refExamType;
    }

    public void setRefExamType(ExamsType refExamType) {
        this.refExamType = refExamType;
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

    @Override
    public String toString() {
        return "net.yazsoft.ors.entities.Exams[ tid=" + tid + " ]";
    }

}
