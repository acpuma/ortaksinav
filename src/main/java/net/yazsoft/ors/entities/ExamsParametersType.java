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
    @NamedQuery(name = "ExamsParametersType.findAll", query = "SELECT e FROM ExamsParametersType e"),
    @NamedQuery(name = "ExamsParametersType.findByTid", query = "SELECT e FROM ExamsParametersType e WHERE e.tid = :tid"),
    @NamedQuery(name = "ExamsParametersType.findByActive", query = "SELECT e FROM ExamsParametersType e WHERE e.active = :active"),
    @NamedQuery(name = "ExamsParametersType.findByVersion", query = "SELECT e FROM ExamsParametersType e WHERE e.version = :version"),
    @NamedQuery(name = "ExamsParametersType.findByNameTr", query = "SELECT e FROM ExamsParametersType e WHERE e.nameTr = :nameTr"),
    @NamedQuery(name = "ExamsParametersType.findByNameEn", query = "SELECT e FROM ExamsParametersType e WHERE e.nameEn = :nameEn"),
    @NamedQuery(name = "ExamsParametersType.findByCreated", query = "SELECT e FROM ExamsParametersType e WHERE e.created = :created"),
    @NamedQuery(name = "ExamsParametersType.findByUpdated", query = "SELECT e FROM ExamsParametersType e WHERE e.updated = :updated"),
    @NamedQuery(name = "ExamsParametersType.findByShowDefault", query = "SELECT e FROM ExamsParametersType e WHERE e.showDefault = :showDefault"),
    @NamedQuery(name = "ExamsParametersType.findByLength", query = "SELECT e FROM ExamsParametersType e WHERE e.length = :length"),
    @NamedQuery(name = "ExamsParametersType.findByStart", query = "SELECT e FROM ExamsParametersType e WHERE e.start = :start")})
public class ExamsParametersType extends BaseEntity implements Serializable {
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
    @Column(name = "show_default")
    private Boolean showDefault;
    private Integer length;
    private Integer start;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refParameter", fetch = FetchType.LAZY)
    private Collection<ExamsParameters> examsParametersCollection;

    public ExamsParametersType() {
    }

    public ExamsParametersType(Long tid) {
        this.tid = tid;
    }

    public ExamsParametersType(Long tid, boolean active, int version, String nameTr) {
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

    public Boolean getShowDefault() {
        return showDefault;
    }

    public void setShowDefault(Boolean showDefault) {
        this.showDefault = showDefault;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    @XmlTransient
    public Collection<ExamsParameters> getExamsParametersCollection() {
        return examsParametersCollection;
    }

    public void setExamsParametersCollection(Collection<ExamsParameters> examsParametersCollection) {
        this.examsParametersCollection = examsParametersCollection;
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
        if (!(object instanceof ExamsParametersType)) {
            return false;
        }
        ExamsParametersType other = (ExamsParametersType) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
