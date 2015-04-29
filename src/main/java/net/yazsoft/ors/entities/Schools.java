/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.entities;

import java.io.Serializable; import net.yazsoft.frame.hibernate.BaseEntity;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
    @NamedQuery(name = "Schools.findAll", query = "SELECT s FROM Schools s")})
public class Schools extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long tid;
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
    private Boolean active;
    @Size(max = 50)
    @Column(name = "meb_code", length = 50)
    private String mebCode;
    @JoinTable(name = "UsersSchools", joinColumns = {
        @JoinColumn(name = "ref_school", referencedColumnName = "tid", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "ref_user", referencedColumnName = "tid", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Users> usersCollection;
    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<Uploads> uploadsCollection;
    @OneToMany(mappedBy = "refActiveSchool", fetch = FetchType.LAZY)
    private Collection<Users> usersCollection1;
    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<Albums> albumsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<Exams> examsCollection;
    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<Students> studentsCollection;
    @JoinColumn(name = "ref_city", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cities refCity;
    @JoinColumn(name = "ref_town", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Towns refTown;
    @OneToMany(mappedBy = "refSchools", fetch = FetchType.LAZY)
    private Collection<Images> imagesCollection;
    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<SchoolsClass> schoolsClassCollection;
    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<Results> resultsCollection;
    @OneToMany(mappedBy = "refSchool", fetch = FetchType.LAZY)
    private Collection<StudentsAnswers> studentsAnswersCollection;

    public Schools() {
    }

    public Schools(Long tid) {
        this.tid = tid;
    }

    public Schools(Long tid, int version, String name) {
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getMebCode() {
        return mebCode;
    }

    public void setMebCode(String mebCode) {
        this.mebCode = mebCode;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    @XmlTransient
    public Collection<Uploads> getUploadsCollection() {
        return uploadsCollection;
    }

    public void setUploadsCollection(Collection<Uploads> uploadsCollection) {
        this.uploadsCollection = uploadsCollection;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection1() {
        return usersCollection1;
    }

    public void setUsersCollection1(Collection<Users> usersCollection1) {
        this.usersCollection1 = usersCollection1;
    }

    @XmlTransient
    public Collection<Albums> getAlbumsCollection() {
        return albumsCollection;
    }

    public void setAlbumsCollection(Collection<Albums> albumsCollection) {
        this.albumsCollection = albumsCollection;
    }

    @XmlTransient
    public Collection<Exams> getExamsCollection() {
        return examsCollection;
    }

    public void setExamsCollection(Collection<Exams> examsCollection) {
        this.examsCollection = examsCollection;
    }

    @XmlTransient
    public Collection<Students> getStudentsCollection() {
        return studentsCollection;
    }

    public void setStudentsCollection(Collection<Students> studentsCollection) {
        this.studentsCollection = studentsCollection;
    }

    public Cities getRefCity() {
        return refCity;
    }

    public void setRefCity(Cities refCity) {
        this.refCity = refCity;
    }

    public Towns getRefTown() {
        return refTown;
    }

    public void setRefTown(Towns refTown) {
        this.refTown = refTown;
    }

    @XmlTransient
    public Collection<Images> getImagesCollection() {
        return imagesCollection;
    }

    public void setImagesCollection(Collection<Images> imagesCollection) {
        this.imagesCollection = imagesCollection;
    }

    @XmlTransient
    public Collection<SchoolsClass> getSchoolsClassCollection() {
        return schoolsClassCollection;
    }

    public void setSchoolsClassCollection(Collection<SchoolsClass> schoolsClassCollection) {
        this.schoolsClassCollection = schoolsClassCollection;
    }

    @XmlTransient
    public Collection<Results> getResultsCollection() {
        return resultsCollection;
    }

    public void setResultsCollection(Collection<Results> resultsCollection) {
        this.resultsCollection = resultsCollection;
    }

    @XmlTransient
    public Collection<StudentsAnswers> getStudentsAnswersCollection() {
        return studentsAnswersCollection;
    }

    public void setStudentsAnswersCollection(Collection<StudentsAnswers> studentsAnswersCollection) {
        this.studentsAnswersCollection = studentsAnswersCollection;
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
        if (!(object instanceof Schools)) {
            return false;
        }
        Schools other = (Schools) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
