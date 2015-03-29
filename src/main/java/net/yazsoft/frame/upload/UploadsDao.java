package net.yazsoft.frame.upload;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.Uploads;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UploadsDao extends BaseGridDao<Uploads> implements Serializable{
    private static final Logger logger = Logger.getLogger(UploadsDao.class);
    Uploads selected;

    List<Uploads> uploads;

    @PostConstruct
    public void init() {

    }

    public List<Uploads> getExamUploads(Exams exam) {
        logger.info("UPLOADS, EXAM : " + exam);
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refExam", exam));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        uploads=list;
        return list;
    }

    public UploadsDao() {
        super(Uploads.class);
    }

    public Uploads getSelected() {
        return selected;
    }

    public void setSelected(Uploads selected) {
        this.selected = selected;
    }

    public List<Uploads> getUploads() {
        if (uploads==null) {
            uploads=getExamUploads(Util.getActiveExam());
        }
        return uploads;
    }

    public void setUploads(List<Uploads> uploads) {
        this.uploads = uploads;
    }
}
