package net.yazsoft.ors.webslides;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.images.ImagesDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.WebSlides;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.ReorderEvent;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class WebSlidesDao extends BaseGridDao<WebSlides> implements Serializable{
    private static final Logger logger = Logger.getLogger(WebSlidesDao.class);
    WebSlides selected;
    List<WebSlides> slides;
    Boolean listChanged=true;

    @Inject
    ImagesDao imagesDao;


    public void onRowReorder(ReorderEvent event) {
        //Util.setFacesMessage("Row Moved From: " + event.getFromIndex() + ", To:" + event.getToIndex());
        try {
            int size=slides.size();
            slides.get(event.getFromIndex()).setRank(size-event.getToIndex());
            logger.info("LOG02290: MOVED : " + slides.get(event.getFromIndex()).getTitleTr());
            update(slides.get(event.getFromIndex()));
            for (int i = 0; i < slides.size(); i++) {
                slides.get(i).setRank(size - i);
                update(slides.get(i));
            }
            listChanged = true;
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void addSlide() {
        WebSlides temp=new WebSlides();
        temp.setActive(true);
        temp.setTitleTr("Slide");
        temp.setRank(slides.size() + 1);
        create(temp);
        selected=temp;
        listChanged = true;
    }

    public void delete() {
        try {
            if (getItem().getRefImage()!=null) {
                String uploadsFolder = Util.getUploadsFolder();
                String extension = getItem().getRefImage().getExtension();
                String dirName;
                dirName = uploadsFolder + "/images/slide";
                File file = new File(dirName + "/" + getItem().getTid().toString() + "." + extension);
                file.delete();
                imagesDao.delete(getItem().getRefImage());
                logger.info("LOG01740: DELETED : " + file.getName());
            }
            super.delete();
            listChanged = true;
            //Util.setFacesMessage("SLIDE SİLİNDİ");
        } catch (Exception e) {
            logger.error("EXCEPTION: ", e);
            Util.setFacesMessageError(e.getMessage());
            throw e;
        }
    }

    public List<WebSlides> findWebSlides() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.addOrder(Order.desc("rank"));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
            listChanged=false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public WebSlidesDao() {
        super(WebSlides.class);
    }

    public WebSlides getSelected() {
        return selected;
    }

    public void setSelected(WebSlides selected) {
        this.selected = selected;
    }

    public List<WebSlides> getSlides() {
        if (listChanged) {
            slides = findWebSlides();
        }
        return slides;
    }

    public void setSlides(List<WebSlides> slides) {
        this.slides = slides;
    }
}
