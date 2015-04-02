/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.schools;

import java.io.Serializable; import net.yazsoft.frame.hibernate.BaseEntity;
import net.yazsoft.ors.entities.Schools;
import net.yazsoft.ors.entities.SchoolsClass;
import net.yazsoft.ors.entities.Students;

import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


public class SchoolsClassDto extends BaseEntity implements Serializable {
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @OneToMany(mappedBy = "refSchoolClass", fetch = FetchType.LAZY)
    private Collection<Students> studentsCollection;
    @JoinColumn(name = "ref_school", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Schools refSchool;

    public SchoolsClassDto() {
    }

    public SchoolsClassDto(Long tid) {
        this.tid = tid;
    }

    public SchoolsClassDto(Long tid, int version, String name) {
        this.tid = tid;
        this.version = version;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @XmlTransient
    public Collection<Students> getStudentsCollection() {
        return studentsCollection;
    }

    public void setStudentsCollection(Collection<Students> studentsCollection) {
        this.studentsCollection = studentsCollection;
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
        if (!(object instanceof SchoolsClassDto)) {
            return false;
        }
        SchoolsClassDto other = (SchoolsClassDto) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
