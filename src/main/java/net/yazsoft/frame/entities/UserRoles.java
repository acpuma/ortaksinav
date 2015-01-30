/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.frame.entities;

import net.yazsoft.frame.hibernate.BaseEntity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserRoles.findAll", query = "SELECT u FROM UserRoles u")})
public class UserRoles extends BaseEntity<Long> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(nullable = false)
    private Long tid;
    @JoinColumn(name = "user", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users user;
    @JoinColumn(name = "role", referencedColumnName = "tid", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Roles role;

    public UserRoles() {
    }

    public UserRoles(Long tid) {
        this.tid = tid;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
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
        if (!(object instanceof UserRoles)) {
            return false;
        }
        UserRoles other = (UserRoles) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.yazsoft.ors.hibernate.UserRoles[ tid=" + tid + " ]";
    }

}
