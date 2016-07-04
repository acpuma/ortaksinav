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

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Optics.findAll", query = "SELECT o FROM Optics o")})
public class Optics extends BaseEntity implements Serializable {
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String name;

    private Integer marginx;
    private Integer marginy;
    private Integer ratio;
    private Integer ratiop;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "refOptic",fetch = FetchType.LAZY,orphanRemoval = true)
    private Collection<OpticsFields> opticsFieldsCollection;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "refOptic",fetch = FetchType.LAZY)
    private Collection<OpticsParts> opticsPartsCollection;

    @JoinColumn(name = "ref_school", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Schools refSchool;

    public Optics() {
    }

    public Optics(Long tid) {
        this.tid = tid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<OpticsFields> getOpticsFieldsCollection() {
        return opticsFieldsCollection;
    }

    public void setOpticsFieldsCollection(Collection<OpticsFields> opticsFieldsCollection) {
        this.opticsFieldsCollection = opticsFieldsCollection;
    }

    public Integer getMarginx() {
        return marginx;
    }

    public void setMarginx(Integer marginx) {
        this.marginx = marginx;
    }

    public Integer getMarginy() {
        return marginy;
    }

    public void setMarginy(Integer marginy) {
        this.marginy = marginy;
    }

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    public Collection<OpticsParts> getOpticsPartsCollection() {
        return opticsPartsCollection;
    }

    public void setOpticsPartsCollection(Collection<OpticsParts> opticsPartsCollection) {
        this.opticsPartsCollection = opticsPartsCollection;
    }

    public Schools getRefSchool() {
        return refSchool;
    }

    public void setRefSchool(Schools refSchool) {
        this.refSchool = refSchool;
    }

    public Integer getRatiop() {
        return ratiop;
    }

    public void setRatiop(Integer ratiop) {
        this.ratiop = ratiop;
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
        if (!(object instanceof Optics)) {
            return false;
        }
        Optics other = (Optics) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
