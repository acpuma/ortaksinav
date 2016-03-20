/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.entities;

import java.io.Serializable; import net.yazsoft.frame.hibernate.BaseEntity;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Distributes.findAll", query = "SELECT r FROM Distributes r")})
public class Distributes extends BaseEntity implements Serializable {
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
    private int distributeRank; // student distribute rank
    private String room;
    private int roomRank;
    private String name;
    private String surname;
    private String mernis;
    private String schoolNo;
    private String className;
    private String lesson1;
    private String lesson2;
    private String lesson3;
    private String lesson4;


    @JoinColumn(name = "ref_school", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Schools refSchool;

    @JoinColumn(name = "ref_distribute_name", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private DistributesNames refDistributeName;

    public Distributes() {
    }

    public Distributes(Long tid) {
        this.tid = tid;
    }

    public Distributes(Long tid, int version) {
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

    public Schools getRefSchool() {
        return refSchool;
    }

    public void setRefSchool(Schools refSchool) {
        this.refSchool = refSchool;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getRoomRank() {
        return roomRank;
    }

    public void setRoomRank(int roomRank) {
        this.roomRank = roomRank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMernis() {
        return mernis;
    }

    public void setMernis(String mernis) {
        this.mernis = mernis;
    }

    public String getSchoolNo() {
        return schoolNo;
    }

    public void setSchoolNo(String schoolNo) {
        this.schoolNo = schoolNo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLesson1() {
        return lesson1;
    }

    public void setLesson1(String lesson1) {
        this.lesson1 = lesson1;
    }

    public String getLesson2() {
        return lesson2;
    }

    public void setLesson2(String lesson2) {
        this.lesson2 = lesson2;
    }

    public String getLesson3() {
        return lesson3;
    }

    public void setLesson3(String lesson3) {
        this.lesson3 = lesson3;
    }

    public String getLesson4() {
        return lesson4;
    }

    public void setLesson4(String lesson4) {
        this.lesson4 = lesson4;
    }

    public int getDistributeRank() {
        return distributeRank;
    }

    public void setDistributeRank(int distributeRank) {
        this.distributeRank = distributeRank;
    }

    public DistributesNames getRefDistributeName() {
        return refDistributeName;
    }

    public void setRefDistributeName(DistributesNames refDistributeName) {
        this.refDistributeName = refDistributeName;
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
        if (!(object instanceof Distributes)) {
            return false;
        }
        Distributes other = (Distributes) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
