package net.yazsoft.frame.upload;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.Uploads;
import net.yazsoft.ors.entities.UploadsType;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.SelectEvent;

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
    List<Uploads> fileUploads;
    List<Uploads> uploadsFmt;

    @PostConstruct
    public void init() {

    }

    public Uploads getExamBooklet(Exams exam,String booklet) {
        logger.info("UPLOADS, EXAM : " + exam);
        Uploads upload=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("detail", booklet));
            c.add(Restrictions.eq("refExam", exam));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
             upload = (Uploads) c.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        logger.info("LOG01500: BOOKLET UPLOAD : " + upload);
        return upload;
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
    public List<Uploads> findFileUploads() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refUploadType",
                    (UploadsType) getSession().load(UploadsType.class, UploadsBean.FILE) ) );
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        fileUploads=list;
        return list;
    }

    public List<Uploads> findFmtUploads() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refUploadType",
                    (UploadsType) getSession().load(UploadsType.class, UploadsBean.FMT) ) );
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refSchool", Util.getActiveSchool()));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        fileUploads=list;
        return list;
    }

    public void onRowSelect(SelectEvent event) {
        selected=(Uploads) event.getObject();
        logger.info("LOG02420: SELECTED : " + selected);
    }

    public static String getUploadedFilePath(Uploads upload){
        if (upload==null) {
            logger.error("ERROR getUploadedFilePath: upload is null ");
            return null;
        }
        logger.info("UploadedFilePath file: " + upload.getName() );
        String filepath=null;
        filepath=Util.getUploadsFolder();
        filepath=filepath.concat("/DAT/" + upload.getRefSchool().getTid().toString());
        filepath=filepath.concat("/" + upload.getRefExam().getTid().toString());
        filepath=filepath.concat("/" + upload.getTid() + "." + getFileExtension(upload.getName()));
        logger.info("UPLOAD FILE PATH : " + filepath);
        return filepath;
    }

    public static String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
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

    public List<Uploads> getFileUploads() {
        if (fileUploads==null) {
            fileUploads=findFileUploads();
        }
        return fileUploads;
    }

    public void setFileUploads(List<Uploads> fileUploads) {
        this.fileUploads = fileUploads;
    }

    public List<Uploads> getUploadsFmt() {
        if (uploadsFmt==null) {
            uploadsFmt=findFmtUploads();
        }
        return uploadsFmt;
    }

    public void setUploadsFmt(List<Uploads> uploadsFmt) {
        this.uploadsFmt = uploadsFmt;
    }
}
