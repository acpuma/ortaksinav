package net.yazsoft.frame.hibernate;

import org.apache.log4j.Logger;

import java.io.Serializable;


public abstract class BaseEntity<T extends Long> implements Serializable{
    static final Logger logger= Logger.getLogger(BaseEntity.class.getName());
    private static final long serialVersionUID = 1L;
    /*
    private Date created_date =new Date();
    private Integer active=1;
    private Date upd = new Date();
    private static final long serialVersionUID = 1L;
    @Version
    protected Integer version;
    */
    
    public abstract T getTid();
    public abstract void setTid(T id);
    /*
    
    public abstract Date getCreatedDate(); 
    public abstract void setCreatedDate(Date createdDate);
    
    public abstract Date getUpd();
    public abstract void setUpd(Date upd);
    
    public abstract Integer getVersion();
    public abstract void setVersion(Integer version);
    
    public abstract Integer getActive();
    public abstract void setActive(Integer active);
    */
    public BaseEntity(){
        //logger.info("BASE ENTITY CONSTRUCTOR");
        /*
        setActive(1);
        setVersion(1);
        setUpd(new Date());
        setCreatedDate(new Date());
        */
    }
    

    /*
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    @Column(nullable = false)
    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getUpd() {
        return upd;
    }

    public void setUpd(Date upd) {
        this.upd = upd;
    }

    @Version
    @Column(nullable = false)
    public Integer getVersion() {
        return version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    */

    @Override
    public String toString() {
        if (getTid()==null) {
            return null;
        }
        return getTid().toString();
    }
}

