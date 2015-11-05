/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.users;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

public class UsersDto extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    Long id;
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
    private String username;
    @Size(max = 255)
    @Column(length = 255)
    private String password;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Size(max = 255)
    @Column(length = 255)
    private String name;
    @Size(max = 255)
    @Column(length = 255)
    private String surname;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 45)
    @Column(length = 45)
    private String phone;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(length = 255)
    private String email;
    @Size(max = 255)
    @Column(length = 255)
    private String image;
    @JoinColumn(name = "ref_role", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Roles refRole;
    @JoinColumn(name = "ref_active_exam", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Exams refActiveExam;
    @JoinColumn(name = "ref_image", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Images refImage;
    @JoinColumn(name = "ref_active_school", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Schools refActiveSchool;

    @ManyToMany(mappedBy = "usersCollection", fetch = FetchType.LAZY)
    private Collection<Schools> schoolsCollection;
    @OneToMany(mappedBy = "refUser", fetch = FetchType.LAZY)
    private Collection<Uploads> uploadsCollection;

    @OneToMany(mappedBy = "refUser", fetch = FetchType.LAZY)
    private Collection<ZlogLogin> zlogLoginCollection;
    @OneToMany(mappedBy = "refUser", fetch = FetchType.LAZY)
    private Collection<UsersMenus> usersMenusCollection;
    @OneToMany(mappedBy = "refUser", fetch = FetchType.LAZY)
    private Collection<ResultsSend> resultsSendCollection;

    public UsersDto() {
    }

    public UsersDto(Long tid) {
        this.tid = tid;
    }

    public UsersDto(Long tid, int version, String username) {
        this.tid = tid;
        this.version = version;
        this.username = username;
    }

    public Users toEntity(Users entity) {
        entity.setActive(this.active);
        entity.setPassword(this.password);
        entity.setUsername(this.username);
        entity.setName(this.name);
        entity.setSurname(this.surname);
        entity.setCreated(this.created);
        entity.setEmail(this.email);
        entity.setImage(this.image);
        entity.setPhone(this.phone);
        entity.setTid(this.tid);
        entity.setUpdated(this.updated);
        entity.setVersion(this.version);
        entity.setAccountNonExpired(this.accountNonExpired);
        entity.setAccountNonLocked(this.accountNonLocked);
        entity.setCredentialsNonExpired(this.credentialsNonExpired);
        entity.setRefActiveExam(this.refActiveExam);
        entity.setRefImage(this.refImage);
        entity.setRefRole(this.refRole);
        entity.setResultsSendCollection(this.resultsSendCollection);
        entity.setUploadsCollection(this.uploadsCollection);
        entity.setUsersMenusCollection(this.usersMenusCollection);
        entity.setZlogLoginCollection(this.zlogLoginCollection);
        return entity;
    }

    public Users toEntity() {
        Users entity=new Users();
        return  toEntity(entity);
    }

    public void fromEntity(Users entity) {
        this.tid=entity.getTid();
        this.active=entity.getActive();
        this.password=entity.getPassword();
        this.username=entity.getUsername();
        this.name=entity.getName();
        this.surname=entity.getSurname();
        this.created=entity.getCreated();
        this.email=entity.getEmail();
        this.image=entity.getImage();
        this.phone=entity.getPhone();
        this.updated=entity.getUpdated();
        this.version=entity.getVersion();
        this.accountNonExpired=entity.getAccountNonExpired();
        this.accountNonLocked=entity.getAccountNonLocked();
        this.credentialsNonExpired=entity.getCredentialsNonExpired();
        this.refActiveExam=entity.getRefActiveExam();
        this.refImage=entity.getRefImage();
        this.refRole=entity.getRefRole();
        this.resultsSendCollection=entity.getResultsSendCollection();
        this.uploadsCollection=entity.getUploadsCollection();
        this.usersMenusCollection=entity.getUsersMenusCollection();
        this.zlogLoginCollection=entity.getZlogLoginCollection();
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

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @XmlTransient
    public Collection<Schools> getSchoolsCollection() {
        return schoolsCollection;
    }

    public void setSchoolsCollection(Collection<Schools> schoolsCollection) {
        this.schoolsCollection = schoolsCollection;
    }

    public Roles getRefRole() {
        return refRole;
    }

    public void setRefRole(Roles refRole) {
        this.refRole = refRole;
    }

    @XmlTransient
    public Collection<Uploads> getUploadsCollection() {
        return uploadsCollection;
    }

    public void setUploadsCollection(Collection<Uploads> uploadsCollection) {
        this.uploadsCollection = uploadsCollection;
    }

    public Exams getRefActiveExam() {
        return refActiveExam;
    }

    public void setRefActiveExam(Exams refActiveExam) {
        this.refActiveExam = refActiveExam;
    }

    public Images getRefImage() {
        return refImage;
    }

    public void setRefImage(Images refImage) {
        this.refImage = refImage;
    }

    public Schools getRefActiveSchool() {
        return refActiveSchool;
    }

    public void setRefActiveSchool(Schools refActiveSchool) {
        this.refActiveSchool = refActiveSchool;
    }

    @XmlTransient
    public Collection<ZlogLogin> getZlogLoginCollection() {
        return zlogLoginCollection;
    }

    public void setZlogLoginCollection(Collection<ZlogLogin> zlogLoginCollection) {
        this.zlogLoginCollection = zlogLoginCollection;
    }

    public Collection<UsersMenus> getUsersMenusCollection() {
        return usersMenusCollection;
    }

    public void setUsersMenusCollection(Collection<UsersMenus> usersMenusCollection) {
        this.usersMenusCollection = usersMenusCollection;
    }

    public Collection<ResultsSend> getResultsSendCollection() {
        return resultsSendCollection;
    }

    public void setResultsSendCollection(Collection<ResultsSend> resultsSendCollection) {
        this.resultsSendCollection = resultsSendCollection;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof UsersDto)) {
            return false;
        }
        UsersDto other = (UsersDto) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
