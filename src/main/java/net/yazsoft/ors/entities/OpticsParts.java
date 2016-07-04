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
    @NamedQuery(name = "OpticsParts.findAll", query = "SELECT p FROM OpticsParts p")})
public class OpticsParts extends BaseEntity implements Serializable {
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

    private Integer rank;
    private int x,y,w,h;
    private boolean horizontal;
    private boolean print;
    private boolean printTitle;
    @Size(max = 255)
    @Column(length = 255)
    private String chars;
    @Size(max = 255)
    @Column(length = 255)
    private String title;
    @Size(max = 255)
    @Column(length = 255)
    private String charType;


    @JoinColumn(name = "ref_optic", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Optics refOptic;

    @JoinColumn(name = "ref_field_type", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private OpticsFieldsType refFieldType;


    public OpticsParts() {
    }

    public OpticsParts(Long tid, int version) {
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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public boolean isPrintTitle() {
        return printTitle;
    }

    public void setPrintTitle(boolean printTitle) {
        this.printTitle = printTitle;
    }

    public String getChars() {
        return chars;
    }

    public void setChars(String values) {
        this.chars = values;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCharType() {
        return charType;
    }

    public void setCharType(String valueType) {
        this.charType = valueType;
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

    public boolean isPrint() {
        return print;
    }

    public void setPrint(boolean print) {
        this.print = print;
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
        if (!(object instanceof OpticsParts)) {
            return false;
        }
        OpticsParts other = (OpticsParts) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
