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
    @NamedQuery(name = "MenusType.findAll", query = "SELECT m FROM MenusType m"),
    @NamedQuery(name = "MenusType.findByTid", query = "SELECT m FROM MenusType m WHERE m.tid = :tid"),
    @NamedQuery(name = "MenusType.findByActive", query = "SELECT m FROM MenusType m WHERE m.active = :active"),
    @NamedQuery(name = "MenusType.findByVersion", query = "SELECT m FROM MenusType m WHERE m.version = :version"),
    @NamedQuery(name = "MenusType.findByName", query = "SELECT m FROM MenusType m WHERE m.name = :name"),
    @NamedQuery(name = "MenusType.findByCreated", query = "SELECT m FROM MenusType m WHERE m.created = :created"),
    @NamedQuery(name = "MenusType.findByUpdated", query = "SELECT m FROM MenusType m WHERE m.updated = :updated")})
public class MenusType extends BaseEntity<Long> implements Serializable {
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @OneToMany(mappedBy = "refMenutype", fetch = FetchType.LAZY)
    private Collection<Menus> menusCollection;

    public MenusType() {
    }

    public MenusType(Long tid) {
        this.tid = tid;
    }

    public MenusType(Long tid, int version, String name) {
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
    public Collection<Menus> getMenusCollection() {
        return menusCollection;
    }

    public void setMenusCollection(Collection<Menus> menusCollection) {
        this.menusCollection = menusCollection;
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
        if (!(object instanceof MenusType)) {
            return false;
        }
        MenusType other = (MenusType) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.yazsoft.app.entities.MenusType[ tid=" + tid + " ]";
    }

}
