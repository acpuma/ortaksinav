/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.schools;

import java.io.Serializable; import net.yazsoft.frame.hibernate.BaseEntity;
import net.yazsoft.ors.entities.Schools;
import net.yazsoft.ors.entities.SchoolsClass;
import net.yazsoft.ors.entities.SchoolsClassType;
import net.yazsoft.ors.entities.Students;

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


public class SchoolsClassDto extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private SchoolsClass entity;
    private Long id;
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String name;
    private String roomName;
    private int capacity=0;
    private Boolean bUseRoom=false;
    private Boolean bIncludeStudents=false;
    private int studentsSize;
    private String lesson1;
    private String lesson2;
    private String lesson3;
    private String lesson4;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @OneToMany(mappedBy = "refSchoolClass", fetch = FetchType.LAZY)
    private Collection<Students> studentsCollection;
    @JoinColumn(name = "ref_school", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Schools refSchool;
    @JoinColumn(name = "ref_school_class_type", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private SchoolsClassType refSchoolClassType;

    public SchoolsClass toEntity(){
        return toEntity(null);
    }

    public SchoolsClass toEntity(SchoolsClass schoolsClass){
        if (schoolsClass==null) {
            schoolsClass=new SchoolsClass();
        }
        schoolsClass.setTid(this.tid);
        schoolsClass.setVersion(this.version);
        schoolsClass.setActive(this.active);
        schoolsClass.setName(this.name);
        schoolsClass.setCreated(this.created);
        schoolsClass.setUpdated(this.updated);
        //tempClass.setStudentsCollection(this.studentsCollection);
        schoolsClass.setRefSchool(this.refSchool);
        schoolsClass.setRoomName(this.roomName);
        schoolsClass.setCapacity(this.capacity);
        schoolsClass.setbIncludeStudents(this.bIncludeStudents);
        schoolsClass.setbUseRoom(this.bUseRoom);
        schoolsClass.setRefSchoolClassType(this.refSchoolClassType);
        entity=schoolsClass;
        return schoolsClass;
    }

    public SchoolsClassDto(SchoolsClass schoolsClass) {
        fromEntity(schoolsClass);
    }

    public void fromEntity(SchoolsClass schoolsClass) {
        entity=schoolsClass;
        this.tid=schoolsClass.getTid();
        this.version=schoolsClass.getVersion();
        this.active=schoolsClass.getActive();
        this.name=schoolsClass.getName();
        this.created=schoolsClass.getCreated();
        this.updated=schoolsClass.getUpdated();
        this.studentsCollection=schoolsClass.getStudentsCollection();
        this.refSchool=schoolsClass.getRefSchool();
        this.roomName=schoolsClass.getRoomName();
        this.capacity=schoolsClass.getCapacity();
        this.bIncludeStudents=schoolsClass.getbIncludeStudents();
        this.bUseRoom=schoolsClass.getbUseRoom();
        this.refSchoolClassType=schoolsClass.getRefSchoolClassType();
    }

    public SchoolsClassDto() {
    }

    public SchoolsClassDto(Long tid) {
        this.tid = tid;
    }

    public SchoolsClassDto(Long tid, int version, String name) {
        this.tid = tid;
        this.version = version;
        this.name = name;
    }

    public SchoolsClass getEntity() {
        return entity;
    }

    public void setEntity(SchoolsClass entity) {
        this.entity = entity;
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

    @XmlTransient
    public Collection<Students> getStudentsCollection() {
        return studentsCollection;
    }

    public void setStudentsCollection(Collection<Students> studentsCollection) {
        this.studentsCollection = studentsCollection;
    }

    public Schools getRefSchool() {
        return refSchool;
    }

    public void setRefSchool(Schools refSchool) {
        this.refSchool = refSchool;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
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
        if (!(object instanceof SchoolsClassDto)) {
            return false;
        }
        SchoolsClassDto other = (SchoolsClassDto) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Boolean getbIncludeStudents() {
        return bIncludeStudents;
    }

    public void setbIncludeStudents(Boolean bIncludeStudents) {
        this.bIncludeStudents = bIncludeStudents;
    }

    public Boolean getbUseRoom() {
        return bUseRoom;
    }

    public void setbUseRoom(Boolean bUseRoom) {
        this.bUseRoom = bUseRoom;
    }

    public int getStudentsSize() {
        return getStudentsCollection().size();
    }

    public void setStudentsSize(int studentsSize) {
        this.studentsSize = studentsSize;
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

    public SchoolsClassType getRefSchoolClassType() {
        return refSchoolClassType;
    }

    public void setRefSchoolClassType(SchoolsClassType refSchoolClassType) {
        this.refSchoolClassType = refSchoolClassType;
    }
}
