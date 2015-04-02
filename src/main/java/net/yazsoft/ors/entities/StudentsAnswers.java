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
    @NamedQuery(name = "StudentsAnswers.findAll", query = "SELECT s FROM StudentsAnswers s")})
public class StudentsAnswers extends BaseEntity implements Serializable {
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
    @JoinColumn(name = "ref_exam", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Exams refExam;
    @JoinColumn(name = "ref_lesson", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Lessons refLesson;
    @JoinColumn(name = "ref_student", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Students refStudent;

    public StudentsAnswers() {
    }

    public StudentsAnswers(Long tid) {
        this.tid = tid;
    }

    public StudentsAnswers(Long tid, int version, String answers) {
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
        if (!(object instanceof StudentsAnswers)) {
            return false;
        }
        StudentsAnswers other = (StudentsAnswers) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
