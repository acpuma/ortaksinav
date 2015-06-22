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
    @NamedQuery(name = "Products.findAll", query = "SELECT p FROM Products p")})
public class Products extends BaseEntity implements Serializable {
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
    private Boolean showOrderForm;
    private Boolean showShop;
    @JoinColumn(name = "ref_image", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Images refImage;
    @JoinColumn(name = "ref_product_category", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductsCategory refProductCategory;
    @JoinColumn(name = "ref_product_type", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductsType refProductType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "refProduct", fetch = FetchType.LAZY)
    private Collection<OrderFormProducts> orderFormProductsCollection;

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

    public Boolean getShowOrderForm() {
        return showOrderForm;
    }

    public void setShowOrderForm(Boolean showOrderForm) {
        this.showOrderForm = showOrderForm;
    }

    public Boolean getShowShop() {
        return showShop;
    }

    public void setShowShop(Boolean showShop) {
        this.showShop = showShop;
    }

    public Images getRefImage() {
        return refImage;
    }

    public void setRefImage(Images refImage) {
        this.refImage = refImage;
    }

    public ProductsCategory getRefProductCategory() {
        return refProductCategory;
    }

    public void setRefProductCategory(ProductsCategory refProductCategory) {
        this.refProductCategory = refProductCategory;
    }

    public ProductsType getRefProductType() {
        return refProductType;
    }

    public void setRefProductType(ProductsType refProductType) {
        this.refProductType = refProductType;
    }

    @XmlTransient
    public Collection<OrderFormProducts> getOrderFormProductsCollection() {
        return orderFormProductsCollection;
    }

    public void setOrderFormProductsCollection(Collection<OrderFormProducts> orderFormProductsCollection) {
        this.orderFormProductsCollection = orderFormProductsCollection;
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
