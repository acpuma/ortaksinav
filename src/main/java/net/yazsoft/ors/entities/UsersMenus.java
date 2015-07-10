/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.entities;

import java.io.Serializable; import net.yazsoft.frame.hibernate.BaseEntity;
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

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsersMenus.findAll", query = "SELECT um FROM UsersMenus um")})
public class UsersMenus extends BaseEntity implements Serializable {
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
    private Boolean psuper;
    private Boolean padd;
    private Boolean pupdate;
    private Boolean pdelete;
    private Boolean preport;

    @JoinColumn(name = "ref_menu", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menus refMenu;
    @JoinColumn(name = "ref_user", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users refUser;

    public UsersMenus() {
    }

    public UsersMenus(Long tid) {
        this.tid = tid;
    }

    public UsersMenus(Long tid, int version) {
        this.tid = tid;
        this.version = version;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
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

    public Menus getRefMenu() {
        return refMenu;
    }

    public void setRefMenu(Menus refMenu) {
        this.refMenu = refMenu;
    }

    public Users getRefUser() {
        return refUser;
    }

    public void setRefUser(Users refUser) {
        this.refUser = refUser;
    }

    public Boolean getPsuper() {
        return psuper;
    }

    public void setPsuper(Boolean psuper) {
        this.psuper = psuper;
    }

    public Boolean getPadd() {
        return padd;
    }

    public void setPadd(Boolean padd) {
        this.padd = padd;
    }

    public Boolean getPupdate() {
        return pupdate;
    }

    public void setPupdate(Boolean pupdate) {
        this.pupdate = pupdate;
    }

    public Boolean getPdelete() {
        return pdelete;
    }

    public void setPdelete(Boolean pdelete) {
        this.pdelete = pdelete;
    }

    public Boolean getPreport() {
        return preport;
    }

    public void setPreport(Boolean preport) {
        this.preport = preport;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        if (!(object instanceof UsersMenus)) {
            return false;
        }
        UsersMenus other = (UsersMenus) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }


}
