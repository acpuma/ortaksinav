/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.entities;

import java.io.Serializable; import net.yazsoft.frame.hibernate.BaseEntity;

import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductsType.findAll", query = "SELECT p FROM ProductsType p"),
    @NamedQuery(name = "ProductsType.findByTid", query = "SELECT p FROM ProductsType p WHERE p.tid = :tid"),
    @NamedQuery(name = "ProductsType.findByActive", query = "SELECT p FROM ProductsType p WHERE p.active = :active"),
    @NamedQuery(name = "ProductsType.findByVersion", query = "SELECT p FROM ProductsType p WHERE p.version = :version"),
    @NamedQuery(name = "ProductsType.findByNameTr", query = "SELECT p FROM ProductsType p WHERE p.nameTr = :nameTr"),
    @NamedQuery(name = "ProductsType.findByNameEn", query = "SELECT p FROM ProductsType p WHERE p.nameEn = :nameEn"),
    @NamedQuery(name = "ProductsType.findByCreated", query = "SELECT p FROM ProductsType p WHERE p.created = :created"),
    @NamedQuery(name = "ProductsType.findByUpdated", query = "SELECT p FROM ProductsType p WHERE p.updated = :updated")})
public class ProductsType extends BaseEntity implements Serializable {
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
    @OneToMany(mappedBy = "refProductType", fetch = FetchType.LAZY)
    private Collection<Products> productsCollection;

    public ProductsType() {
    }

    public ProductsType(Long tid) {
        this.tid = tid;
    }

    public ProductsType(Long tid, int version, String nameTr) {
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

    public Collection<Products> getProductsCollection() {
        return productsCollection;
    }

    public void setProductsCollection(Collection<Products> productsCollection) {
        this.productsCollection = productsCollection;
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
        if (!(object instanceof ProductsType)) {
            return false;
        }
        ProductsType other = (ProductsType) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}