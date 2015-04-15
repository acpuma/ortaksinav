package net.yazsoft.frame.upload;

import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Albums;
import net.yazsoft.ors.entities.Schools;
import net.yazsoft.ors.entities.Uploads;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import java.awt.image.BufferedImage;
import java.io.*;

@Named
@ViewScoped
public class UploadsBean implements Serializable{
    private static final Logger logger = Logger.getLogger(UploadsBean.class.getName());
    private static final int BUFFER_SIZE = 6124;
    private UploadedFile uploadedFile;
    Schools activeSchool;
    File uploadDirectory;
    String fileType;
    Albums album;

    @Inject UploadsDao uploadsDao;
    @Inject UploadsTypeDao uploadsTypeDao;

    @PostConstruct
    public void init() {
        logger.info("UPLOAD CONSTRUCTOR");
        //album=activeSchool.getId();
        fileType="DAT";
    }

    @Transactional
    public void deleteUpload(Uploads upload) {
        try {
            uploadsDao.delete(upload);

            File deleteFile=new File(upload.getTid().toString()+".DAT");

            String uploadsFolder = Util.getUploadsFolder();
            String extension=UploadsDao.getFileExtension(upload.getName());
            String dirName = uploadsFolder + "/DAT/" + upload.getRefSchool().getTid().toString();
            dirName = dirName + ("/" + upload.getRefExam().getTid().toString());

            File file = new File(dirName + "/" + upload.getTid().toString()+"."+extension);
            if (file.delete()) {
                logger.info(file.getName() + " is deleted!");
            } else {
                logger.info("DELETING : " + dirName + "/" + upload.getTid().toString()+"."+extension);
                logger.info("Delete operation is failed.");
            }
            uploadsDao.setUploads(null);
            //deleting
            Util.setFacesMessage("Deleted file id : " );
        } catch (Exception e) {
            logger.error("EXCEPTION: ", e);
            Util.setFacesMessageError(e.getMessage());
        }
        //ImagesDao imageDao=new ImagesDao();
    }

    /**
     * Creates and return the directory
     * @param subdir if the directory will include a subdir like albumid directory under images folder
     *               if no subdirectory send null
     * @return UploadDirectory name
     */
    public File getUploadDirectory(String subdir) {
        String dirName;
        File targetFolder;
        if (fileType.equals("DAT")) {
            Util.createDirectory("DAT");
            dirName="/DAT/"+activeSchool.getTid().toString();
        } else {
            dirName="/files";
        }
        targetFolder=Util.createDirectory(dirName);

        // if the  directory does not exist, create it
        if (subdir!=null) {
            dirName = dirName + ("/" +subdir);
            targetFolder=Util.createDirectory(dirName);
        }
        uploadDirectory=targetFolder;
        return targetFolder;
    }



    @Transactional
    public void handleFileUpload(FileUploadEvent event) {
        logger.info("UPLOADING FILE.......");
        activeSchool=Util.getActiveSchool();

        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            getUploadDirectory(Util.getActiveExam().getTid().toString());
            String filenameOriginal=event.getFile().getFileName();
            String extension=UploadsDao.getFileExtension(filenameOriginal);

            Long tid=1L;
            Uploads upload=new Uploads();
            upload.setActive(Boolean.TRUE);
            upload.setRefSchool(Util.getActiveSchool());
            upload.setRefExam(Util.getActiveExam());
            upload.setRefUser(Util.getActiveUser());
            upload.setName(filenameOriginal);
            upload.setRefUploadType(uploadsTypeDao.getById(1L)); //DAT
            tid=uploadsDao.create(upload);

            InputStream inputStream;
            inputStream = event.getFile().getInputstream();
            if (fileType.equals("IMAGE")) {
                BufferedImage bufferedImage= ImageIO.read(event.getFile().getInputstream());
                Integer imageWidth=bufferedImage.getWidth();
                if (imageWidth>1600) {
                    inputStream = Util.imageResize(event.getFile().getInputstream(), extension, 1600, 800);
                }
            }
            String filename=tid.toString()+"."+extension;
            OutputStream out = new FileOutputStream(new File(uploadDirectory,filename));
            int read = 0;
            byte[] bytes = new byte[BUFFER_SIZE];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();

            uploadsDao.setUploads(null); //for refresh grid

            Util.setFacesMessage("ID : " + tid.toString() + " ,file name: "
                    + event.getFile().getFileName() + " File size: "
                    + event.getFile().getSize() / 1024 + " Kb Content type: "
                    + event.getFile().getContentType() + " the file was uploaded.");
        } catch(Exception e){
            Util.setFacesMessageError(" the files were not uploaded : " + e.getMessage());
            e.printStackTrace();
        }
    }



    @PreDestroy 
    public void Destroy() {
        //if uploadedImages are not saved into an album, then they should be deleted before destroying view
        logger.info("DESTROY VIEW. DELETING unsaved UPLOADED IMAGES");
    }
    
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public Albums getAlbum() {
        return album;
    }

    public void setAlbum(Albums album) {
        this.album = album;
    }
}


//CREATING THUMBNAIL
            /*
            dirName=dirName+("/SMALL");
            targetFolder = new File(imageFolder,dirName);
            // if the SMALL directory does not exist, create it
            if (!targetFolder.exists()) {
              //logger.info("creating directory: " + targetFolder.toString());
              boolean dirCreated = targetFolder.mkdir();

               if(dirCreated) {
                 logger.info("DIR created");
               }
            }

            inputStream = Util.imageThumbnail(event.getFile().getInputstream(),extension);
            out = new FileOutputStream(new File(targetFolder,filename));
            read = 0;
            bytes = new byte[BUFFER_SIZE];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();
            */