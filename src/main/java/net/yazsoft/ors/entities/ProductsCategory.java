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
    @NamedQuery(name = "ProductsCategory.findAll", query = "SELECT p FROM ProductsCategory p")})
public class ProductsCategory extends BaseEntity implements Serializable {
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
    private int rank;
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
    @OneToMany(mappedBy = "refProductCategory", fetch = FetchType.LAZY)
    private Collection<Products> productsCollection;
    @OneToMany(mappedBy = "refProductCategory", fetch = FetchType.LAZY)
    private Collection<ProductsCategory> productsCategoryCollection;
    @JoinColumn(name = "ref_product_category", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductsCategory refProductCategory;

    public ProductsCategory() {
    }

    public ProductsCategory(Long tid) {
        this.tid = tid;
    }

    public ProductsCategory(Long tid, int version, String nameTr) {
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

    @XmlTransient
    public Collection<Products> getProductsCollection() {
        return productsCollection;
    }

    public void setProductsCollection(Collection<Products> productsCollection) {
        this.productsCollection = productsCollection;
    }

    @XmlTransient
    public Collection<ProductsCategory> getProductsCategoryCollection() {
        return productsCategoryCollection;
    }

    public void setProductsCategoryCollection(Collection<ProductsCategory> productsCategoryCollection) {
        this.productsCategoryCollection = productsCategoryCollection;
    }

    public ProductsCategory getRefProductCategory() {
        return refProductCategory;
    }

    public void setRefProductCategory(ProductsCategory refProductCategory) {
        this.refProductCategory = refProductCategory;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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
        if (!(object instanceof ProductsCategory)) {
            return false;
        }
        ProductsCategory other = (ProductsCategory) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
