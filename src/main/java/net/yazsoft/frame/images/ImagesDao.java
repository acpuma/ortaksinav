package net.yazsoft.frame.images;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Images;
import net.yazsoft.ors.entities.ImagesType;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ImagesDao extends BaseGridDao<Images> implements Serializable{
    private static final Logger logger = Logger.getLogger(ImagesDao.class);
    Images selected;


    public Images findImageByTidAndType(Long tid,ImagesType imageType) {
        logger.info("LOG01720: TID , IMAGETYPE : " + tid + "," + imageType);
        Images temp=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refTid", tid));
            c.add(Restrictions.eq("refImageType", imageType));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            temp = (Images) c.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return temp;
    }

    public ImagesDao() {
        super(Images.class);
    }

    public Images getSelected() {
        return selected;
    }

    public void setSelected(Images selected) {
        this.selected = selected;
    }
}
