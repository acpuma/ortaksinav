package net.yazsoft.frame.hibernate;

import net.yazsoft.ors.entities.Users;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.*;
import java.util.Date;

/** * @author fec */
@Entity
@Table(name = "ZL_REVINFO")
@RevisionEntity(HibRevisionListener.class)
public class HibRevisionEntity extends DefaultRevisionEntity {

    @Id
    private Long tid;
    
    @ManyToOne
    @JoinColumn(name = "ref_user")
    private Users user;
    
    private Date update_date;

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }
    
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }
}
