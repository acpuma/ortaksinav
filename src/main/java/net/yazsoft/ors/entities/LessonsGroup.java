/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.entities;

import java.io.Serializable; import net.yazsoft.frame.hibernate.BaseEntity;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LessonsGroup.findAll", query = "SELECT l FROM LessonsGroup l")})
public class LessonsGroup extends BaseEntity implements Serializable {
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

    @JoinColumn(name = "ref_school_class_type", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private SchoolsClassType refSchoolClassType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refLessonGroup", fetch = FetchType.LAZY)
    private Collection<LessonsName> lessonsNameCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refLessonGroup", fetch = FetchType.LAZY)
    private Collection<Schedules> schedulesCollection;

    public LessonsGroup() {
    }

    public LessonsGroup(Long tid) {
        this.tid = tid;
    }

    public LessonsGroup(Long tid, boolean active, int version, String nameTr) {
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

    @XmlTransient
    public Collection<LessonsName> getLessonsNameCollection() {
        return lessonsNameCollection;
    }

    public void setLessonsNameCollection(Collection<LessonsName> lessonsNameCollection) {
        this.lessonsNameCollection = lessonsNameCollection;
    }

    public Collection<Schedules> getSchedulesCollection() {
        return schedulesCollection;
    }

    public void setSchedulesCollection(Collection<Schedules> schedulesCollection) {
        this.schedulesCollection = schedulesCollection;
    }


    public SchoolsClassType getRefSchoolClassType() {
        return refSchoolClassType;
    }

    public void setRefSchoolClassType(SchoolsClassType refSchoolClassType) {
        this.refSchoolClassType = refSchoolClassType;
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
        if (!(object instanceof LessonsGroup)) {
            return false;
        }
        LessonsGroup other = (LessonsGroup) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
