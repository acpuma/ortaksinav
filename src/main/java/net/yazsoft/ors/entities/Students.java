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
    @NamedQuery(name = "Students.findAll", query = "SELECT s FROM Students s"),
    @NamedQuery(name = "Students.findByTid", query = "SELECT s FROM Students s WHERE s.tid = :tid"),
    @NamedQuery(name = "Students.findByActive", query = "SELECT s FROM Students s WHERE s.active = :active"),
    @NamedQuery(name = "Students.findByVersion", query = "SELECT s FROM Students s WHERE s.version = :version"),
    @NamedQuery(name = "Students.findByName", query = "SELECT s FROM Students s WHERE s.name = :name"),
    @NamedQuery(name = "Students.findBySurname", query = "SELECT s FROM Students s WHERE s.surname = :surname"),
    @NamedQuery(name = "Students.findByCreated", query = "SELECT s FROM Students s WHERE s.created = :created"),
    @NamedQuery(name = "Students.findByUpdated", query = "SELECT s FROM Students s WHERE s.updated = :updated"),
    @NamedQuery(name = "Students.findByUsername", query = "SELECT s FROM Students s WHERE s.username = :username"),
    @NamedQuery(name = "Students.findByPassword", query = "SELECT s FROM Students s WHERE s.password = :password"),
    @NamedQuery(name = "Students.findByFullname", query = "SELECT s FROM Students s WHERE s.fullname = :fullname"),
    @NamedQuery(name = "Students.findByGender", query = "SELECT s FROM Students s WHERE s.gender = :gender"),
    @NamedQuery(name = "Students.findBySchoolNo", query = "SELECT s FROM Students s WHERE s.schoolNo = :schoolNo"),
    @NamedQuery(name = "Students.findByMernis", query = "SELECT s FROM Students s WHERE s.mernis = :mernis"),
    @NamedQuery(name = "Students.findByPhone", query = "SELECT s FROM Students s WHERE s.phone = :phone")})
public class Students extends BaseEntity implements Serializable {
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
    @Size(max = 255)
    @Column(length = 255)
    private String name;
    @Size(max = 255)
    @Column(length = 255)
    private String surname;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Size(max = 255)
    @Column(length = 255)
    private String username;
    @Size(max = 255)
    @Column(length = 255)
    private String password;
    @Size(max = 255)
    @Column(length = 255)
    private String fullname;
    @Size(max = 1)
    @Column(length = 1)
    private String gender;
    @Column(name = "school_no")
    private Integer schoolNo;
    @Size(max = 11)
    @Column(length = 11)
    private String mernis;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 20)
    @Column(length = 20)
    private String phone;
    @OneToMany(mappedBy = "refStudent", fetch = FetchType.LAZY)
    private Collection<StudentsAnswers> studentsAnswersCollection;
    @JoinColumn(name = "ref_school", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Schools refSchool;
    @JoinColumn(name = "ref_school_class", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private SchoolsClass refSchoolClass;
    @OneToMany(mappedBy = "refStudent", fetch = FetchType.LAZY)
    private Collection<ZlogLogin> zlogLoginCollection;
    @OneToMany(mappedBy = "refStudent", fetch = FetchType.LAZY)
    private Collection<Results> resultsCollection;
    @JoinColumn(name = "ref_image", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Images refImage;

    public Students() {
    }

    public Students(Long tid) {
        this.tid = tid;
    }

    public Students(Long tid, int version) {
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
    public Collection<ZlogLogin> getZlogLoginCollection() {
        return zlogLoginCollection;
    }

    public void setZlogLoginCollection(Collection<ZlogLogin> zlogLoginCollection) {
        this.zlogLoginCollection = zlogLoginCollection;
    }

    @XmlTransient
    public Collection<Results> getResultsCollection() {
        return resultsCollection;
    }

    public void setResultsCollection(Collection<Results> resultsCollection) {
        this.resultsCollection = resultsCollection;
    }

    public Images getRefImage() {
        return refImage;
    }

    public void setRefImage(Images refImage) {
        this.refImage = refImage;
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
        if (!(object instanceof Students)) {
            return false;
        }
        Students other = (Students) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
