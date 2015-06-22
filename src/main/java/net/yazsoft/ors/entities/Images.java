/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.entities;

import java.io.Serializable; import net.yazsoft.frame.hibernate.BaseEntity;
import java.math.BigInteger;
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
    @NamedQuery(name = "Images.findAll", query = "SELECT i FROM Images i")})
public class Images extends BaseEntity implements Serializable {
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Size(max = 1000)
    @Column(name = "title_tr", length = 1000)
    private String titleTr;
    @Size(max = 1000)
    @Column(name = "title_en", length = 1000)
    private String titleEn;
    @Size(max = 255)
    @Column(length = 255)
    private String extension;
    @Column(name = "ref_tid")
    private Long refTid;
    @OneToMany(mappedBy = "refImage", fetch = FetchType.LAZY)
    private Collection<Products> productsCollection;
    @OneToMany(mappedBy = "refImage", fetch = FetchType.LAZY)
    private Collection<Users> usersCollection;
    @OneToMany(mappedBy = "refImage", fetch = FetchType.LAZY)
    private Collection<Albums> albumsCollection;
    @OneToMany(mappedBy = "refImage", fetch = FetchType.LAZY)
    private Collection<WebLinks> webLinksCollection;
    @OneToMany(mappedBy = "refImage", fetch = FetchType.LAZY)
    private Collection<Students> studentsCollection;
    @OneToMany(mappedBy = "refImage", fetch = FetchType.LAZY)
    private Collection<WebClients> webClientsCollection;
    @OneToMany(mappedBy = "refImage", fetch = FetchType.LAZY)
    private Collection<Schools> schoolsCollection;
    @JoinColumn(name = "ref_album", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Albums refAlbum;
    @JoinColumn(name = "ref_image_type", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private ImagesType refImageType;
    @JoinColumn(name = "ref_school", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Schools refSchool;
    @OneToMany(mappedBy = "refImage", fetch = FetchType.LAZY)
    private Collection<WebCompanies> webCompaniesCollection;
    @OneToMany(mappedBy = "refImage", fetch = FetchType.LAZY)
    private Collection<WebSlides> webSlidesCollection;

    public Images() {
    }

    public Images(Long tid) {
        this.tid = tid;
    }

    public Images(Long tid, int version, String name) {
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

    public String getTitleTr() {
        return titleTr;
    }

    public void setTitleTr(String titleTr) {
        this.titleTr = titleTr;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getRefTid() {
        return refTid;
    }

    public void setRefTid(Long refTid) {
        this.refTid = refTid;
    }

    @XmlTransient
    public Collection<Products> getProductsCollection() {
        return productsCollection;
    }

    public void setProductsCollection(Collection<Products> productsCollection) {
        this.productsCollection = productsCollection;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    @XmlTransient
    public Collection<Albums> getAlbumsCollection() {
        return albumsCollection;
    }

    public void setAlbumsCollection(Collection<Albums> albumsCollection) {
        this.albumsCollection = albumsCollection;
    }

    @XmlTransient
    public Collection<WebLinks> getWebLinksCollection() {
        return webLinksCollection;
    }

    public void setWebLinksCollection(Collection<WebLinks> webLinksCollection) {
        this.webLinksCollection = webLinksCollection;
    }

    @XmlTransient
    public Collection<Students> getStudentsCollection() {
        return studentsCollection;
    }

    public void setStudentsCollection(Collection<Students> studentsCollection) {
        this.studentsCollection = studentsCollection;
    }

    @XmlTransient
    public Collection<WebClients> getWebClientsCollection() {
        return webClientsCollection;
    }

    public void setWebClientsCollection(Collection<WebClients> webClientsCollection) {
        this.webClientsCollection = webClientsCollection;
    }

    @XmlTransient
    public Collection<Schools> getSchoolsCollection() {
        return schoolsCollection;
    }

    public void setSchoolsCollection(Collection<Schools> schoolsCollection) {
        this.schoolsCollection = schoolsCollection;
    }

    public Albums getRefAlbum() {
        return refAlbum;
    }

    public void setRefAlbum(Albums refAlbum) {
        this.refAlbum = refAlbum;
    }

    public ImagesType getRefImageType() {
        return refImageType;
    }

    public void setRefImageType(ImagesType refImageType) {
        this.refImageType = refImageType;
    }

    public Schools getRefSchool() {
        return refSchool;
    }

    public void setRefSchool(Schools refSchool) {
        this.refSchool = refSchool;
    }

    @XmlTransient
    public Collection<WebCompanies> getWebCompaniesCollection() {
        return webCompaniesCollection;
    }

    public void setWebCompaniesCollection(Collection<WebCompanies> webCompaniesCollection) {
        this.webCompaniesCollection = webCompaniesCollection;
    }

    @XmlTransient
    public Collection<WebSlides> getWebSlidesCollection() {
        return webSlidesCollection;
    }

    public void setWebSlidesCollection(Collection<WebSlides> webSlidesCollection) {
        this.webSlidesCollection = webSlidesCollection;
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
        if (!(object instanceof Images)) {
            return false;
        }
        Images other = (Images) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
