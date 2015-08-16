/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.ors.entities;

import java.io.Serializable; import net.yazsoft.frame.hibernate.BaseEntity;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WebArticles.findAll", query = "SELECT a FROM WebArticles a")})
public class WebArticles extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long tid;
    private Boolean active;
    private Boolean publish;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private int version;
    private int rank;
    @Size(max = 500)
    @Column(name = "title_tr", length = 500)
    private String titleTr;
    @Size(max = 500)
    @Column(name = "title_en", length = 500)
    private String titleEn;
    @JoinColumn(name = "ref_content", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Contents refContent;
    @JoinColumn(name = "ref_web_article_type", referencedColumnName = "tid")
    @ManyToOne(fetch = FetchType.LAZY)
    private WebArticlesType refWebArticleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    public WebArticles() {
    }

    public WebArticles(Long tid) {
        this.tid = tid;
    }

    public WebArticles(Long tid, int version, String titleTr) {
        this.tid = tid;
        this.version = version;
        this.titleTr = titleTr;
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


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

    public Contents getRefContent() {
        return refContent;
    }

    public void setRefContent(Contents refContent) {
        this.refContent = refContent;
    }

    public Boolean getPublish() {
        return publish;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public WebArticlesType getRefWebArticleType() {
        return refWebArticleType;
    }

    public void setRefWebArticleType(WebArticlesType refWebArticleType) {
        this.refWebArticleType = refWebArticleType;
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
        if (!(object instanceof WebArticles)) {
            return false;
        }
        WebArticles other = (WebArticles) object;
        if ((this.tid == null && other.tid != null) || (this.tid != null && !this.tid.equals(other.tid))) {
            return false;
        }
        return true;
    }

}
