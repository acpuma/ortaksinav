/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.students;

import java.io.Serializable; import net.yazsoft.frame.hibernate.BaseEntity;
import net.yazsoft.ors.entities.*;

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

public class StudentsDto extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    Long id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    Long tid;
    Boolean active;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    int version;
    @Size(max = 255)
    @Column(length = 255)
    String name;
    @Size(max = 255)
    @Column(length = 255)
    String surname;
    @Temporal(TemporalType.TIMESTAMP)
    Date created;
    @Temporal(TemporalType.TIMESTAMP)
    Date updated;
    @Size(max = 45)
    @Column(length = 45)
    String username;
    @Size(max = 45)
    @Column(length = 45)
    String password;
    @Size(max = 255)
    @Column(length = 255)
    String fullname;
    @Size(max = 1)
    @Column(length = 1)
    String gender;
    @Column(name = "school_no")
    Integer schoolNo;
    @Size(max = 11)
    @Column(length = 11)
    String mernis;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 20)
    @Column(length = 20)
    String phone;
    @OneToMany(mappedBy = "refStudent", fetch = FetchType.LAZY)
    Collection<StudentsAnswers> studentsAnswersCollection;
    @JoinColumn(name = "ref_school", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    Schools refSchool;
    @JoinColumn(name = "ref_school_class", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    SchoolsClass refSchoolClass;
    @OneToMany(mappedBy = "refStudent", fetch = FetchType.LAZY)
    Collection<Results> resultsCollection;

    public Students toEntity() {
        Students entity=new Students();
        entity.setTid(this.tid);
        entity.setActive(this.active);
        entity.setVersion(this.version);
        entity.setName(this.name);
        entity.setSurname(this.surname);
        entity.setCreated(this.created);
        entity.setUpdated(this.updated);
        entity.setUsername(this.username);
        entity.setPassword(this.password);
        entity.setFullname(this.fullname);
        entity.setGender(this.gender);
        entity.setSchoolNo(this.schoolNo);
        entity.setMernis(this.mernis);
        entity.setPhone(this.phone);
        entity.setStudentsAnswersCollection(this.studentsAnswersCollection);
        entity.setRefSchool(this.refSchool);
        entity.setRefSchoolClass(this.refSchoolClass);
        entity.setResultsCollection(this.resultsCollection);
        return entity;
    }

    public StudentsDto(Students entity) {
        fromEntity(entity);
    }
    public void fromEntity(Students entity) {
        this.tid=entity.getTid();
        this.active=entity.getActive();
        this.version=entity.getVersion();
        this.name=entity.getName();
        this.surname=entity.getSurname();
        this.created=entity.getCreated();
        this.updated=entity.getCreated();
        this.username=entity.getUsername();
        this.password=entity.getPassword();
        this.fullname=entity.getFullname();
        this.gender=entity.getGender();
        this.schoolNo=entity.getSchoolNo();
        this.mernis=entity.getMernis();
        this.phone=entity.getPhone();
        this.studentsAnswersCollection=entity.getStudentsAnswersCollection();
        this.refSchool=entity.getRefSchool();
        this.refSchoolClass=entity.getRefSchoolClass();
        this.resultsCollection=entity.getResultsCollection();
    }

    public StudentsDto() {
    }

    public StudentsDto(Long tid) {
        this.tid = tid;
    }

    public StudentsDto(Long tid, int version) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getSchoolNo() {
        return schoolNo;
    }

    public void setSchoolNo(Integer schoolNo) {
        this.schoolNo = schoolNo;
    }

    public String getMernis() {
        return mernis;
    }

    public void setMernis(String mernis) {
        this.mernis = mernis;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @XmlTransient
    public Collection<StudentsAnswers> getStudentsAnswersCollection() {
        return studentsAnswersCollection;
    }

    public void setStudentsAnswersCollection(Collection<StudentsAnswers> studentsAnswersCollection) {
        this.studentsAnswersCollection = studentsAnswersCollection;
    }

    public Schools getRefSchool() {
        return refSchool;
    }

    public void setRefSchool(Schools refSchool) {
        this.refSchool = refSchool;
    }

    public SchoolsClass getRefSchoolClass() {
        return refSchoolClass;
    }

    public void setRefSchoolClass(SchoolsClass refSchoolClass) {
        this.refSchoolClass = refSchoolClass;
    }

    @XmlTransient
    public Collection<Results> getResultsCollection() {
        return resultsCollection;
    }

    public void setResultsCollection(Collection<Results> resultsCollection) {
        this.resultsCollection = resultsCollection;
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
        if (!(object instanceof StudentsDto)) {
            return false;
        }
        StudentsDto other = (StudentsDto) object;
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
}
