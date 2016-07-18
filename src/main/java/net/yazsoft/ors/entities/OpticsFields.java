/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.entities;

import java.io.Serializable;
import net.yazsoft.frame.hibernate.BaseEntity;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpticsFields.findAll", query = "SELECT f FROM OpticsFields f")})
public class OpticsFields extends BaseEntity implements Serializable {
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
    private Float leftx;
    private Float topy;
    private Float fontsize;
    @Size(max = 2000)
    @Column(length = 2000)
    private String value1;
    @Size(max = 2000)
    @Column(length = 2000)
    private String value2;

    @JoinColumn(name = "ref_optic", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Optics refOptic;

    @JoinColumn(name = "ref_field_type", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private OpticsFieldsType refFieldType;


    public OpticsFields() {
    }

    public OpticsFields(Long tid, int version) {
        this.tid = tid;
        this.version = version;
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

    public Float getFontsize() {
        return fontsize;
    }

    public void setFontsize(Float fontsize) {
        this.fontsize = fontsize;
    }

    public Optics getRefOptic() {
        return refOptic;
    }

    public void setRefOptic(Optics refOptic) {
        this.refOptic = refOptic;
    }

    public OpticsFieldsType getRefFieldType() {
        return refFieldType;
    }

    public void setRefFieldType(OpticsFieldsType refFieldType) {
        this.refFieldType = refFieldType;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
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
        if (!(object instanceof OpticsFields)) {
            return false;
        }
        OpticsFields other = (OpticsFields) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
