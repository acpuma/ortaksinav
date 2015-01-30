/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.frame.entities;

import net.yazsoft.frame.hibernate.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Menus.findAll", query = "SELECT m FROM Menus m"),
    @NamedQuery(name = "Menus.findByTid", query = "SELECT m FROM Menus m WHERE m.tid = :tid"),
    @NamedQuery(name = "Menus.findByActive", query = "SELECT m FROM Menus m WHERE m.active = :active"),
    @NamedQuery(name = "Menus.findByVersion", query = "SELECT m FROM Menus m WHERE m.version = :version"),
    @NamedQuery(name = "Menus.findByCreated", query = "SELECT m FROM Menus m WHERE m.created = :created"),
    @NamedQuery(name = "Menus.findByUpdated", query = "SELECT m FROM Menus m WHERE m.updated = :updated"),
    @NamedQuery(name = "Menus.findByNameTr", query = "SELECT m FROM Menus m WHERE m.nameTr = :nameTr"),
    @NamedQuery(name = "Menus.findByNameEn", query = "SELECT m FROM Menus m WHERE m.nameEn = :nameEn"),
    @NamedQuery(name = "Menus.findByImage", query = "SELECT m FROM Menus m WHERE m.image = :image"),
    @NamedQuery(name = "Menus.findByForm", query = "SELECT m FROM Menus m WHERE m.form = :form"),
    @NamedQuery(name = "Menus.findByOrder", query = "SELECT m FROM Menus m WHERE m.order = :order"),
    @NamedQuery(name = "Menus.findByComment", query = "SELECT m FROM Menus m WHERE m.comment = :comment")})
public class Menus extends BaseEntity<Long> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long tid;
    private Integer active;
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
    @Column(name = "name_tr", nullable = false, length = 255)
    private String nameTr;
    @Size(max = 255)
    @Column(name = "name_en", length = 255)
    private String nameEn;
    @Size(max = 255)
    @Column(length = 255)
    private String image;
    @Size(max = 255)
    @Column(length = 255)
    private String form;
    private Integer order;
    @Size(max = 255)
    @Column(length = 255)
    private String comment;
    @OneToMany(mappedBy = "mainId", fetch = FetchType.LAZY)
    private Collection<Menus> menusCollection;
    @JoinColumn(name = "main_id", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menus mainId;
    @JoinColumn(name = "ref_menutype", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private MenusType refMenutype;

    public Menus() {
    }

    public Menus(Long tid) {
        this.tid = tid;
    }

    public Menus(Long tid, int version, String nameTr) {
        this.tid = tid;
        this.version = version;
        this.nameTr = nameTr;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @XmlTransient
    public Collection<Menus> getMenusCollection() {
        return menusCollection;
    }

    public void setMenusCollection(Collection<Menus> menusCollection) {
        this.menusCollection = menusCollection;
    }

    public Menus getMainId() {
        return mainId;
    }

    public void setMainId(Menus mainId) {
        this.mainId = mainId;
    }

    public MenusType getRefMenutype() {
        return refMenutype;
    }

    public void setRefMenutype(MenusType refMenutype) {
        this.refMenutype = refMenutype;
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
        if (!(object instanceof Menus)) {
            return false;
        }
        Menus other = (Menus) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.yazsoft.app.entities.Menus[ tid=" + tid + " ]";
    }

}
