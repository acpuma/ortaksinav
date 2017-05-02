package net.yazsoft.frame.upload;

import net.yazsoft.frame.hibernate.BaseDao;
import net.yazsoft.frame.images.ImagesDao;
import net.yazsoft.frame.images.ImagesTypeDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.security.UsersDao;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.products.ProductsDao;
import net.yazsoft.ors.schools.SchoolsDao;
import net.yazsoft.ors.students.StudentsDao;
import net.yazsoft.ors.weblinks.WebLinksDao;
import net.yazsoft.ors.webslides.WebSlidesDao;
import org.apache.log4j.Logger;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

@Named
@ViewScoped
public class UploadsBean extends BaseDao implements Serializable{
    private static final Logger logger = Logger.getLogger(UploadsBean.class.getName());
    private static final int BUFFER_SIZE = 6124;
    public static final long DAT = 1L;
    public static final long IMAGE = 2L;
    public static final long BOOKLET = 3L;
    public static final long LOGO = 4L;
    public static final long FILE = 5L;
    public static final long FMT = 6L;
    public static final String IMAGE_SCHOOL = "school";
    public static final String IMAGE_USER ="user";
    public static final String IMAGE_STUDENT ="student";
    public static final String IMAGE_SLIDE ="slide";
    public static final String IMAGE_PRODUCT ="product";
    public static final String IMAGE_WEBLINK ="weblink";
    private UploadedFile uploadedFile;
    Schools activeSchool;
    File uploadDirectory;
    Long fileType;
    Albums album;
    UploadsType uploadType;
    String detail;
    String imageType;
    Integer imageWidth;
    Integer imageHeight;
    SchoolsClass schoolClass;
    Students student;

    @Inject UploadsDao uploadsDao;
    @Inject UploadsTypeDao uploadsTypeDao;
    @Inject ImagesDao imagesDao;
    @Inject ImagesTypeDao imagesTypeDao;

    @Inject SchoolsDao schoolsDao;
    @Inject UsersDao usersDao;
    @Inject StudentsDao studentsDao;
    @Inject WebSlidesDao webSlidesDao;
    @Inject ProductsDao productsDao;
    @Inject WebLinksDao webLinksDao;

    @PostConstruct
    public void init() {
        logger.info("UPLOAD CONSTRUCTOR");
        //album=activeSchool.getId();
        fileType=FILE;
    }


    public void handleSchoolClassChange() {
        logger.info("School Class : " +schoolClass);
    }

    @Transactional
    public void handleStudentImages(FileUploadEvent event) {
        try {
            logger.info("SCHOOL CLASs : " + schoolClass);
            if (schoolClass==null) {
                throw new Exception("Sinif seciniz");
            }
            String filename=event.getFile().getFileName();

            logger.info("Stdunet Filename : " + filename);
            Integer schoolNo=Integer.valueOf(filename.substring(0,filename.lastIndexOf(".")).toLowerCase());
            logger.info("STUDENT NO : " + schoolNo);
            List<Students> studentsList=studentsDao.findBySchoolClass(schoolClass);
            logger.info("STUDENTS LIST : " + studentsList);
            for (Students tempStudent:studentsList) {
                if (tempStudent.getSchoolNo().equals(schoolNo)) {
                    student=tempStudent;
                }
            }
            logger.info("STUDENT : " + student );
            if (student==null) {
                logger.error(schoolNo+" nolu ogrenci yok");
                Util.setFacesMessageError(schoolNo+" nolu ogrenci yok");
            } else {
                handleImageUpload(event);
            }
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    @Transactional
    public void deleteClassStudentsImages() {
        try {
            logger.info("SCHOOL CLASs : " + schoolClass);
            if (schoolClass==null) {
                throw new Exception("Sinif seciniz");
            }
            List<Students> studentsList=studentsDao.findBySchoolClass(schoolClass);
            logger.info("STUDENTS LIST : " + studentsList);
            for (Students tempStudent:studentsList) {
                if (tempStudent.getRefImage()!=null) {
                    deleteStudentImage(tempStudent);
                }
            }
            Util.setFacesMessage(schoolClass.getName()+" SINIF FOTOGRAFLARI SILINDI");
        } catch (Exception e) {
            Util.catchException(e);
        }
    }


    @Transactional
    public void handleImageUpload(FileUploadEvent event) {
        logger.info("UPLOADING IMAGE.......");
        fileType=IMAGE;
        Long tid=null;
        switch (imageType){
            case IMAGE_SCHOOL: tid=schoolsDao.getItem().getTid();break;
            case IMAGE_USER: tid=usersDao.getItem().getTid();break;
            case IMAGE_STUDENT:
                if (student!=null) {
                    tid=student.getTid();
                } else {
                    tid = studentsDao.getItem().getTid();
                }
                break;
            case IMAGE_SLIDE: tid=webSlidesDao.getItem().getTid();break;
            case IMAGE_PRODUCT: tid=productsDao.getItem().getTid();break;
            case IMAGE_WEBLINK: tid=webLinksDao.getItem().getTid();break;
        }

        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            getUploadDirectory(null);
            String filenameOriginal=event.getFile().getFileName();

            String extension=UploadsDao.getFileExtension(filenameOriginal);


            InputStream inputStream;
            inputStream = event.getFile().getInputstream();
            BufferedImage bufferedImage= ImageIO.read(event.getFile().getInputstream());
            Integer imgWidth=bufferedImage.getWidth();
            Integer imgHeight=bufferedImage.getHeight();
            if (imageWidth==null) {
                imageWidth=256;
            }

            if (imgWidth>imageWidth) {
                imageHeight=imgHeight*(imageWidth/imgWidth); //scale
                inputStream = Util.imageResize(event.getFile().getInputstream(), extension, imageWidth, imageHeight);
            }
            String filename=tid.toString()+"."+extension.toLowerCase();
            OutputStream out = new FileOutputStream(new File(uploadDirectory,filename));
            int read = 0;
            byte[] bytes = new byte[BUFFER_SIZE];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();

            //Util.setFacesMessage(" RESIM YUKLENDI ");

            ImagesType imagesType=null;
            switch (imageType) {
                case (IMAGE_USER) :
                    imagesType=(ImagesType)getSession().load(ImagesType.class,1L);break;
                case (IMAGE_SCHOOL) :
                    imagesType=(ImagesType)getSession().load(ImagesType.class,2L);break;
                case (IMAGE_STUDENT) :
                    imagesType=(ImagesType)getSession().load(ImagesType.class,3L);break;
                case (IMAGE_SLIDE) :
                    imagesType=(ImagesType)getSession().load(ImagesType.class,4L);break;
                case (IMAGE_PRODUCT) :
                    imagesType=(ImagesType)getSession().load(ImagesType.class,5L);break;
                case (IMAGE_WEBLINK) :
                    imagesType=(ImagesType)getSession().load(ImagesType.class,6L);break;
            }

            Images image=null;
            image=imagesDao.findImageByTidAndType(tid,imagesType);
            if (image==null) {
                image=new Images();
            }
            image.setName(filenameOriginal);
            image.setExtension(extension);
            image.setRefSchool(Util.getActiveSchool());
            image.setActive(true);
            image.setRefTid(tid);
            image.setRefImageType(imagesType);

            imagesDao.saveOrUpdate(image);
            switch (imageType) {
                case (IMAGE_USER) :
                    usersDao.getItem().setRefImage(image); usersDao.update();break;
                case (IMAGE_SCHOOL) :
                    schoolsDao.getItem().setRefImage(image); schoolsDao.update();break;
                case (IMAGE_STUDENT) :
                    if (student==null) {
                        studentsDao.getItem().setRefImage(image);
                        studentsDao.update();
                    } else {
                        student.setRefImage(image);
                        studentsDao.update(student);
                    }
                    break;
                case (IMAGE_SLIDE) :
                    webSlidesDao.getItem().setRefImage(image); webSlidesDao.update();break;
                case (IMAGE_PRODUCT) :
                    productsDao.getItem().setRefImage(image); productsDao.update();break;
                case (IMAGE_WEBLINK) :
                    webLinksDao.getItem().setRefImage(image); webLinksDao.update();break;
            }
        } catch (Exception e) {
            logger.error("EXCEPTION: ", e);
            Util.setFacesMessageError(e.getMessage());
            //throw e;
        }
    }


    public void downloadFile(Uploads upload) throws IOException{
        try {
            //Uploads upload=uploadsDao.getExamBooklet(exam,detail);
            //if (upload!=null) {

            //}
            if (upload==null) {
                logger.info("LOG01490: UPLOAD IS NULL");
                return;
            }
            fileType=upload.getRefUploadType().getTid();

            String uploadsFolder = Util.getUploadsFolder();
            String extension = UploadsDao.getFileExtension(upload.getName());
            String dirName;
            if (fileType.equals(DAT)) {
                dirName = uploadsFolder + "/DAT/" + upload.getRefSchool().getTid().toString();
            } else if (fileType.equals(BOOKLET)) {
                dirName = uploadsFolder + "/BOOKLET/" + upload.getRefSchool().getTid().toString();
            } else if (fileType.equals(FMT)) {
                dirName = uploadsFolder + "/FMT/";
            } else {
                dirName = uploadsFolder + "/files/";
            }

            if ((!fileType.equals(FILE)) && (!fileType.equals(FMT))) {
                dirName = dirName + ("/" + upload.getRefExam().getTid().toString());
            }

            File file = new File(dirName + "/" + upload.getTid().toString() + "." + extension);

            HttpServletResponse httpServletResponse = (HttpServletResponse)
                    FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + upload.getName());

            FacesContext.getCurrentInstance().responseComplete();

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            BufferedInputStream input = null;
            //BufferedOutputStream output = null;
            input = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);

            // Write file contents to response.
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                servletOutputStream.write(buffer, 0, length);
            }

            servletOutputStream.flush();
            servletOutputStream.close();
            input.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            logger.error("EXCEPTION: ", e);
            Util.setFacesMessageError(e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void deleteStudentImage(Students student) {
        try {
            logger.info("DELETING IMAGE.......");
            logger.info("FILE TYPE : " + fileType);
            logger.info("IMAGE TYPE : " + imageType);
            fileType=IMAGE;
            imageType=IMAGE_STUDENT;
            String uploadsFolder = getUploadDirectory(null).toString();
            String dirName;
            Images image=null;
            Long tid=null;
            image = student.getRefImage();
            tid = student.getTid();


            File file = new File(getUploadDirectory(null) + "/" + tid.toString()+"."+image.getExtension());
            if (file.delete()) {
                logger.info(file.getName() + " is deleted!");
            } else {
                logger.info("DELETING : " + file.toString()); // + "/" + image.getTid().toString()+"."+image.getExtension());
                logger.error("Delete operation is failed.");
                Util.setFacesMessageError("Kayit silindi, dosya silineMEdi.");
            }
            student.setRefImage(null);
            studentsDao.update(student);
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    @Transactional
    public void deleteImage() {
        try {
            logger.info("DELETING IMAGE.......");
            logger.info("FILE TYPE : " + fileType);
            logger.info("IMAGE TYPE : " + imageType);
            fileType=IMAGE;
            String uploadsFolder = getUploadDirectory(null).toString();
            String dirName;
            Images image=null;
            Long tid=null;
            switch (imageType) {
                case (IMAGE_USER) : image=usersDao.getItem().getRefImage(); tid=usersDao.getItem().getTid(); break;
                case (IMAGE_SCHOOL) : image = schoolsDao.getItem().getRefImage(); tid=schoolsDao.getItem().getTid(); break;
                case (IMAGE_STUDENT) : image = studentsDao.getItem().getRefImage(); tid=studentsDao.getItem().getTid(); break;
                case (IMAGE_SLIDE) : image = webSlidesDao.getItem().getRefImage(); tid=webSlidesDao.getItem().getTid(); break;
                case (IMAGE_PRODUCT) : image = productsDao.getItem().getRefImage(); tid=productsDao.getItem().getTid(); break;
                case (IMAGE_WEBLINK) : image = webLinksDao.getItem().getRefImage(); tid=webLinksDao.getItem().getTid(); break;
            }

            File file = new File(getUploadDirectory(null) + "/" + tid.toString()+"."+image.getExtension());
            if (file.delete()) {
                logger.info(file.getName() + " is deleted!");
            } else {
                logger.info("DELETING : " + file.toString()); // + "/" + image.getTid().toString()+"."+image.getExtension());
                logger.error("Delete operation is failed.");
                Util.setFacesMessageError("Kayit silindi, dosya silineMEdi.");
            }
            switch (imageType) {
                case (IMAGE_USER) : usersDao.getItem().setRefImage(null); usersDao.update(); break;
                case (IMAGE_SCHOOL) : schoolsDao.getItem().setRefImage(null); schoolsDao.update(); break;
                case (IMAGE_STUDENT) : studentsDao.getItem().setRefImage(null); studentsDao.update(); break;
                case (IMAGE_SLIDE) : webSlidesDao.getItem().setRefImage(null);  webSlidesDao.update(); break;
                case (IMAGE_PRODUCT) : productsDao.getItem().setRefImage(null); productsDao.update(); break;
                case (IMAGE_WEBLINK) : webLinksDao.getItem().setRefImage(null); webLinksDao.update(); break;
            }

        } catch (Exception e) {
            Util.catchException(e);
        }

    }

    @Transactional
    public void deleteUpload(Uploads upload) {
        try {

            if (upload.getRefUploadType()==null) {
                fileType=DAT;
            } else {
                fileType=upload.getRefUploadType().getTid();
            }


            String uploadsFolder = Util.getUploadsFolder();
            String extension=UploadsDao.getFileExtension(upload.getName());
            String dirName;
            if (fileType.equals(DAT)) {
                dirName = uploadsFolder + "/DAT/" + upload.getRefSchool().getTid().toString();
            } else if (fileType.equals(BOOKLET)) {
                dirName = uploadsFolder + "/BOOKLET/" + upload.getRefSchool().getTid().toString();
            } else if (fileType.equals(FMT)) {
                dirName = uploadsFolder + "/FMT/";
            } else {
                dirName = uploadsFolder + "/files/";
            }

            if ((!fileType.equals(FILE)) && (!fileType.equals(FMT)) ) {
                dirName = dirName + ("/" + upload.getRefExam().getTid().toString());
            }

            File file = new File(dirName + "/" + upload.getTid().toString()+"."+extension);
            if (file.delete()) {
                logger.info(file.getName() + " is deleted!");
            } else {
                logger.info("DELETING : " + dirName + "/" + upload.getTid().toString()+"."+extension);
                logger.error("Delete operation is failed.");
                Util.setFacesMessageError("Kayit silindi, dosya silineMEdi.");
            }
            Util.setFacesMessage("Deleted file : " + upload.getName());
            uploadsDao.delete(upload);
            uploadsDao.setUploads(null);
            uploadsDao.setFileUploads(null);
            uploadsDao.setUploadsFmt(null);
            //deleting
        } catch (Exception e) {
            logger.error("EXCEPTION: ", e);
            Util.setFacesMessageError(e.getMessage());
            throw e;
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
        String dirName="";
        File targetFolder;
        if (fileType.equals(DAT)) {
            Util.createDirectory("DAT");
            dirName="/DAT/"+activeSchool.getTid().toString();
        } else if (fileType.equals(BOOKLET)) {
            Util.createDirectory("BOOKLET");
            dirName="/BOOKLET/"+activeSchool.getTid().toString();
        } else if (fileType.equals(IMAGE)) {
            Util.createDirectory("images");
            Util.createDirectory("images/"+imageType);
            dirName="/images/"+imageType;
            if (imageType.equals("student")) {
                dirName="/images/"+imageType+"/"+Util.getActiveSchool().getTid()+"/";
            }
            /*
            switch (imageType) {
                case (IMAGE_SCHOOL) :
                    Util.createDirectory("images/school");
                    dirName="/images/school"; break;
                case (IMAGE_USER) :
                    Util.createDirectory("images/users/");
                    dirName="/images/user"; break;
                case (IMAGE_STUDENT) :
                    Util.createDirectory("images/students/");
                    dirName="/images/student"; break;
            }
            */
        } else if (fileType.equals(FILE)) {
            Util.createDirectory("files");
            dirName="/files/";
        } else if (fileType.equals(FMT)) {
            Util.createDirectory("FMT");
            dirName="/FMT/";
        } else {
            logger.error("LOG02410: UNSUPPORTED UPLOAD TYPE !!!");
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
    public void handleBookletUpload(FileUploadEvent event) {
        fileType=BOOKLET;
        uploadType=uploadsTypeDao.getById(BOOKLET);
        handleFileUpload(event);
    }

    @Transactional
    public void handleDatUpload(FileUploadEvent event) {
        fileType=DAT;
        uploadType=uploadsTypeDao.getById(DAT);
        handleFileUpload(event);
    }

    @Transactional
    public void handleFmtUpload(FileUploadEvent event) {
        fileType=FMT;
        uploadType=uploadsTypeDao.getById(FMT);
        handleFileUpload(event);
    }


    @Transactional
    public void handleFileUpload(FileUploadEvent event) {
        logger.info("UPLOADING FILE.......");
        activeSchool=Util.getActiveSchool();

        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            if ((fileType==FILE) || (fileType==FMT)){
                getUploadDirectory(null);
            } else {
                getUploadDirectory(Util.getActiveExam().getTid().toString());
            }
            String filenameOriginal=event.getFile().getFileName();
            String extension=UploadsDao.getFileExtension(filenameOriginal);

            Long tid=1L;
            Uploads upload=new Uploads();
            upload.setActive(Boolean.TRUE);
            upload.setCreated(Util.getNow());
            upload.setExtension(extension);
            if (fileType!=FILE) {
                upload.setRefSchool(Util.getActiveSchool());
                if (fileType!=FMT) {
                    upload.setRefExam(Util.getActiveExam());
                }
            }
            upload.setRefUser(Util.getActiveUser());
            upload.setName(filenameOriginal);
            upload.setRefUploadType(uploadsTypeDao.getById(fileType));
            upload.setDetail(detail);
            tid=uploadsDao.create(upload);
            detail=null; //for next upload

            InputStream inputStream;
            inputStream = event.getFile().getInputstream();
            if (fileType.equals(IMAGE)) {
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
            uploadsDao.setFileUploads(null);
            uploadsDao.setUploadsFmt(null);
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

    public SchoolsClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolsClass schoolClass) {
        this.schoolClass = schoolClass;
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