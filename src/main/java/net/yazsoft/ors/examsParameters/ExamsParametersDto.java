package net.yazsoft.ors.examsParameters;

import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.ExamsParametersType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class ExamsParametersDto {
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
    @Column(nullable = false)
    private int start;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private int length;
    private Boolean direction;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @JoinColumn(name = "ref_exam", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Exams refExam;
    @JoinColumn(name = "ref_parameter", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ExamsParametersType refParameter;

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

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Boolean getDirection() {
        return direction;
    }

    public void setDirection(Boolean direction) {
        this.direction = direction;
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

    public ExamsParametersType getRefParameter() {
        return refParameter;
    }

    public void setRefParameter(ExamsParametersType refParameter) {
        this.refParameter = refParameter;
    }

    @Override
    public String toString() {
        return "ExamsParametersDto{" +
                "tid=" + tid +
                ", active=" + active +
                ", version=" + version +
                ", start=" + start +
                ", length=" + length +
                ", direction=" + direction +
                ", created=" + created +
                ", updated=" + updated +
                ", refExam=" + refExam +
                ", refParameter=" + refParameter +
                '}';
    }
}
