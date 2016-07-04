/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.entities;

import java.io.Serializable;
import net.yazsoft.frame.hibernate.BaseEntity;

import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpticFieldsType.findAll", query = "SELECT t FROM OpticsFieldsType t")})
public class OpticsFieldsType extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long tid;
    private Boolean active;
    private Boolean addDefault;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private int version;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String name;
    @Size(min = 1, max = 255)
    @Column(length = 255)
    private String nameDist;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    private Float leftx;
    private Float topy;
    private Float fontsize;
    private int rank;

    @OneToMany(mappedBy = "refFieldType", fetch = FetchType.LAZY)
    private Collection<OpticsFields> fieldsCollection;

    @OneToMany(mappedBy = "refFieldType", fetch = FetchType.LAZY)
    private Collection<OpticsParts> partsCollection;

    public OpticsFieldsType() {
    }

    public OpticsFieldsType(Long tid) {
        this.tid = tid;
    }

    public OpticsFieldsType(Long tid, int version, String name) {
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

    public Float getLeftx() {
        return leftx;
    }

    public void setLeftx(Float leftx) {
        this.leftx = leftx;
    }

    public Float getTopy() {
        return topy;
    }

    public void setTopy(Float topy) {
        this.topy = topy;
    }

    public Collection<OpticsFields> getFieldsCollection() {
        return fieldsCollection;
    }

    public void setFieldsCollection(Collection<OpticsFields> fieldsCollection) {
        this.fieldsCollection = fieldsCollection;
    }

    public Float getFontsize() {
        return fontsize;
    }

    public void setFontsize(Float fontsize) {
        this.fontsize = fontsize;
    }

    public Boolean getAddDefault() {
        return addDefault;
    }

    public void setAddDefault(Boolean addDefault) {
        this.addDefault = addDefault;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getNameDist() {
        return nameDist;
    }

    public void setNameDist(String nameDist) {
        this.nameDist = nameDist;
    }

    public Collection<OpticsParts> getPartsCollection() {
        return partsCollection;
    }

    public void setPartsCollection(Collection<OpticsParts> partsCollection) {
        this.partsCollection = partsCollection;
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
        if (!(object instanceof OpticsFieldsType)) {
            return false;
        }
        OpticsFieldsType other = (OpticsFieldsType) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
