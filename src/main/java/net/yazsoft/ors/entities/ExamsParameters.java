/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.entities;

import java.io.Serializable; import net.yazsoft.frame.hibernate.BaseEntity;
import java.math.BigInteger;
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

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExamsParameters.findAll", query = "SELECT e FROM ExamsParameters e")})
public class ExamsParameters extends BaseEntity implements Serializable {
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
    private BigInteger id;
    @JoinColumn(name = "ref_exam", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Exams refExam;
    @JoinColumn(name = "ref_parameter", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ExamsParametersType refParameter;

    public ExamsParameters() {
    }

    public ExamsParameters(Long tid) {
        this.tid = tid;
    }

    public ExamsParameters(Long tid, boolean active, int version, int start, int length) {
        this.tid = tid;
        this.active = active;
        this.version = version;
        this.start = start;
        this.length = length;
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

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
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
    public int hashCode() {
        int hash = 0;
        hash += (tid != null ? tid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExamsParameters)) {
            return false;
        }
        ExamsParameters other = (ExamsParameters) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
