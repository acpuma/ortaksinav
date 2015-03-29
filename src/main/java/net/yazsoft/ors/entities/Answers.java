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
    @NamedQuery(name = "Answers.findAll", query = "SELECT a FROM Answers a")})
public class Answers extends BaseEntity implements Serializable {
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
    @Column(nullable = false)
    private int score;
    @Size(max = 1)
    @Column(length = 1)
    private String ansA;
    @Size(max = 1)
    @Column(length = 1)
    private String ansB;
    @Size(max = 1)
    @Column(length = 1)
    private String ansC;
    @Size(max = 1)
    @Column(length = 1)
    private String ansD;
    @Size(max = 1)
    @Column(length = 1)
    private String ansE;
    @Size(max = 1)
    @Column(length = 1)
    private String ansF;
    @Size(max = 1)
    @Column(length = 1)
    private String ansG;
    @Size(max = 1)
    @Column(length = 1)
    private String ansH;
    @JoinColumn(name = "ref_answer_cancel", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AnswersCancelType refAnswerCancel;
    @JoinColumn(name = "ref_answer_question", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AnswersQuestionType refAnswerQuestion;
    @JoinColumn(name = "ref_answer_subject", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private AnswersSubjectType refAnswerSubject;
    @JoinColumn(name = "ref_lesson", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Lessons refLesson;

    public Answers() {
    }

    public Answers(Long tid) {
        this.tid = tid;
    }

    public Answers(Long tid, boolean active, int version, int rank, int score) {
        this.tid = tid;
        this.active = active;
        this.version = version;
        this.rank = rank;
        this.score = score;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAnsA() {
        return ansA;
    }

    public void setAnsA(String ansA) {
        this.ansA = ansA;
    }

    public String getAnsB() {
        return ansB;
    }

    public void setAnsB(String ansB) {
        this.ansB = ansB;
    }

    public String getAnsC() {
        return ansC;
    }

    public void setAnsC(String ansC) {
        this.ansC = ansC;
    }

    public String getAnsD() {
        return ansD;
    }

    public void setAnsD(String ansD) {
        this.ansD = ansD;
    }

    public String getAnsE() {
        return ansE;
    }

    public void setAnsE(String ansE) {
        this.ansE = ansE;
    }

    public String getAnsF() {
        return ansF;
    }

    public void setAnsF(String ansF) {
        this.ansF = ansF;
    }

    public String getAnsG() {
        return ansG;
    }

    public void setAnsG(String ansG) {
        this.ansG = ansG;
    }

    public String getAnsH() {
        return ansH;
    }

    public void setAnsH(String ansH) {
        this.ansH = ansH;
    }

    public AnswersCancelType getRefAnswerCancel() {
        return refAnswerCancel;
    }

    public void setRefAnswerCancel(AnswersCancelType refAnswerCancel) {
        this.refAnswerCancel = refAnswerCancel;
    }

    public AnswersQuestionType getRefAnswerQuestion() {
        return refAnswerQuestion;
    }

    public void setRefAnswerQuestion(AnswersQuestionType refAnswerQuestion) {
        this.refAnswerQuestion = refAnswerQuestion;
    }

    public AnswersSubjectType getRefAnswerSubject() {
        return refAnswerSubject;
    }

    public void setRefAnswerSubject(AnswersSubjectType refAnswerSubject) {
        this.refAnswerSubject = refAnswerSubject;
    }

    public Lessons getRefLesson() {
        return refLesson;
    }

    public void setRefLesson(Lessons refLesson) {
        this.refLesson = refLesson;
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
        if (!(object instanceof Answers)) {
            return false;
        }
        Answers other = (Answers) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
