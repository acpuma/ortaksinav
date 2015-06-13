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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Products.findAll", query = "SELECT p FROM Products p"),
    @NamedQuery(name = "Products.findByTid", query = "SELECT p FROM Products p WHERE p.tid = :tid"),
    @NamedQuery(name = "Products.findByActive", query = "SELECT p FROM Products p WHERE p.active = :active"),
    @NamedQuery(name = "Products.findByVersion", query = "SELECT p FROM Products p WHERE p.version = :version"),
    @NamedQuery(name = "Products.findByNameTr", query = "SELECT p FROM Products p WHERE p.nameTr = :nameTr"),
    @NamedQuery(name = "Products.findByNameEn", query = "SELECT p FROM Products p WHERE p.nameEn = :nameEn"),
    @NamedQuery(name = "Products.findByCreated", query = "SELECT p FROM Products p WHERE p.created = :created"),
    @NamedQuery(name = "Products.findByUpdated", query = "SELECT p FROM Products p WHERE p.updated = :updated"),
    @NamedQuery(name = "Products.findByDetailTr", query = "SELECT p FROM Products p WHERE p.detailTr = :detailTr"),
    @NamedQuery(name = "Products.findByDetailEn", query = "SELECT p FROM Products p WHERE p.detailEn = :detailEn"),
    @NamedQuery(name = "Products.findByPrice", query = "SELECT p FROM Products p WHERE p.price = :price"),
    @NamedQuery(name = "Products.findByPriceReal", query = "SELECT p FROM Products p WHERE p.priceReal = :priceReal")})
public class Products extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long tid;
    private Boolean active;
    private Boolean showOrderForm;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private int version;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name_tr", nullable = false, length = 255)
    private String nameTr;
    @Size(max = 255)
    @Column(name = "name_en", length = 255)
    private String nameEn;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Size(max = 5000)
    @Column(name = "detail_tr", length = 5000)
    private String detailTr;
    @Size(max = 5000)
    @Column(name = "detail_en", length = 5000)
    private String detailEn;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 12)
    private Float price;
    @Column(name = "price_real", precision = 12)
    private Float priceReal;
    @JoinColumn(name = "ref_image", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Images refImage;
    @JoinColumn(name = "ref_product_type", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductsType refProductType;
    @JoinColumn(name = "ref_product_category", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductsCategory refProductCategory;

    public Products() {
    }

    public Products(Long tid) {
        this.tid = tid;
    }

    public Products(Long tid, int version, String nameTr) {
        this.tid = tid;
        this.version = version;
        this.nameTr = nameTr;
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

    public String getNameTr() {
        return nameTr;
    }

    public void setNameTr(String nameTr) {
        this.nameTr = nameTr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
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

    public String getDetailTr() {
        return detailTr;
    }

    public void setDetailTr(String detailTr) {
        this.detailTr = detailTr;
    }

    public String getDetailEn() {
        return detailEn;
    }

    public void setDetailEn(String detailEn) {
        this.detailEn = detailEn;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getPriceReal() {
        return priceReal;
    }

    public void setPriceReal(Float priceReal) {
        this.priceReal = priceReal;
    }

    public Images getRefImage() {
        return refImage;
    }

    public void setRefImage(Images refImage) {
        this.refImage = refImage;
    }

    public ProductsType getRefProductType() {
        return refProductType;
    }

    public void setRefProductType(ProductsType refProductType) {
        this.refProductType = refProductType;
    }

    public ProductsCategory getRefProductCategory() {
        return refProductCategory;
    }

    public void setRefProductCategory(ProductsCategory refProductCategory) {
        this.refProductCategory = refProductCategory;
    }

    public Boolean getShowOrderForm() {
        return showOrderForm;
    }

    public void setShowOrderForm(Boolean showOrderForm) {
        this.showOrderForm = showOrderForm;
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
        if (!(object instanceof Products)) {
            return false;
        }
        Products other = (Products) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
