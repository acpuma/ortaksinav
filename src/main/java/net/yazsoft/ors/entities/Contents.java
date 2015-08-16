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
    @NamedQuery(name = "Contents.findAll", query = "SELECT c FROM Contents c")})
public class Contents extends BaseEntity implements Serializable {
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
    @Column(name = "ref_tid")
    private Long refTid;
    @Lob
    @Size(max = 16777215)
    @Column(name = "content_tr",length = 16777215)
    private String contentTr;
    @Lob
    @Size(max = 16777215)
    @Column(name = "content_en",length = 16777215)
    private String contentEn;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @JoinColumn(name = "ref_content_type", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private ContentsType refContentType;
    @OneToMany(mappedBy = "refContent", fetch = FetchType.LAZY)
    private Collection<Menus> menusCollection;
    @OneToMany(mappedBy = "refContent", fetch = FetchType.LAZY)
    private Collection<WebArticles> webArticlesCollection;

    public Contents() {
    }

    public Contents(Long tid) {
        this.tid = tid;
    }

    public Contents(Long tid, int version, String content) {
        this.tid = tid;
        this.version = version;
        this.contentTr = content;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getContentTr() {
        return contentTr;
    }

    public void setContentTr(String contentTr) {
        this.contentTr = contentTr;
    }

    public String getContentEn() {
        return contentEn;
    }

    public void setContentEn(String contentEn) {
        this.contentEn = contentEn;
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

    public Collection<Menus> getMenusCollection() {
        return menusCollection;
    }

    public void setMenusCollection(Collection<Menus> menusCollection) {
        this.menusCollection = menusCollection;
    }

    public Long getRefTid() {
        return refTid;
    }

    public void setRefTid(Long refTid) {
        this.refTid = refTid;
    }

    public ContentsType getRefContentType() {
        return refContentType;
    }

    public void setRefContentType(ContentsType refContentType) {
        this.refContentType = refContentType;
    }

    public Collection<WebArticles> getWebArticlesCollection() {
        return webArticlesCollection;
    }

    public void setWebArticlesCollection(Collection<WebArticles> webArticlesCollection) {
        this.webArticlesCollection = webArticlesCollection;
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
        if (!(object instanceof Contents)) {
            return false;
        }
        Contents other = (Contents) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
