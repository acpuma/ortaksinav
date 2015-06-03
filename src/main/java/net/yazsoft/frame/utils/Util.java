package net.yazsoft.frame.utils;

import net.yazsoft.frame.security.SessionInfo;
import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.Schools;
import net.yazsoft.ors.entities.Students;
import net.yazsoft.ors.entities.Users;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.imgscalr.Scalr;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;


public class Util implements Serializable{
    static final Logger logger= Logger.getLogger(Util.class.getName());
//    @Inject SettingsDao settings;
    //TODO: Get from db
    static String HOMEDIR=System.getProperty("user.home");
    //static final String HOMEDIR="/home/admin/";

    public Util() {
    }

    public static String getHomedir() {
        if (HOMEDIR.indexOf("root")>-1 ) {
            HOMEDIR="/home/admin/";
        }
        logger.info("LOG01590: HOMEDIR : " + HOMEDIR);
        return HOMEDIR;
    }

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }

    public static String getUserName() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return session.getAttribute("username").toString();
    }

    /*
    public static Users getUserInfo() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return (Users) session.getAttribute("sessionInfo");
    }
    */

    public static SessionInfo getSessionInfo() {
        HttpSession session=getSession();
        if (session==null) return null;
        return (SessionInfo) session.getAttribute("sessionInfo");
    }

    public static String getUserId() {
        HttpSession session = getSession();
        if (session != null) {
            return (String) session.getAttribute("userid");
        } else {
            return null;
        }
    }

    public static Schools getActiveSchool() {
        if (getSessionInfo()!=null) {
            return getSessionInfo().getSchool();
        } else {
            return null;
        }
    }
    public static Exams getActiveExam() {
        return getSessionInfo().getExam();
    }
    public static Users getActiveUser() {
        return getSessionInfo().getUser();
    }
    public static Students getActiveStudent() {return  getSessionInfo().getStudent();}
    
    public static Locale getLocale() {
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }
    public static String getMessage(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
        String message = bundle.getString(key);
        return message;
    }
    
    public static String getLocalColumn(Object entity,String column) {
        String columnValue=null;
        try {
            columnValue = new PropertyDescriptor(column+toProperCase(getLocale().toString()), entity.getClass())
                    .getReadMethod().invoke(entity).toString();
        } catch (NullPointerException ne) {
            columnValue="Null";
            Logger.getLogger(Util.class.getName()).log(Level.ERROR, null, ne);
        } catch (IntrospectionException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.ERROR, null, ex);
        }
        return columnValue;
    }
    
    public static void setLocalColumn(Object entity,String column,Object value) {
        try {
            new PropertyDescriptor("name_"+getLocale().toString().toLowerCase(), entity.getClass())
                    .getWriteMethod().invoke(value);
        } catch (NullPointerException ne) {
            Logger.getLogger(Util.class.getName()).log(Level.ERROR, null, ne);
        } catch (IntrospectionException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.ERROR, null, ex);
        }
    }
    
    public static InputStream imageResize(InputStream userImage,String extension,Integer width, Integer height) {
        BufferedImage resizeImageJpg;
        InputStream is;
        try {
            BufferedImage originalImage = ImageIO.read(userImage);
            int type = originalImage.getType() == 0 ? BufferedImage.SCALE_SMOOTH : originalImage.getType();
            logger.info(type);
            resizeImageJpg = Scalr.resize(originalImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH,
                    width, height, Scalr.OP_ANTIALIAS);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(resizeImageJpg, extension, os);
            is = new ByteArrayInputStream(os.toByteArray());
            return is;
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        return null;
    }
    
    public static InputStream imageThumbnail(InputStream userImage,String extension) {
        return imageResize(userImage,extension, 256, 256);
    }
    
    
    
    
    public static String toString(Object beanInstance) {
        if (beanInstance==null) {
            logger.info(beanInstance + " IS NULL");
            return beanInstance + " IS NULL";
        } else {
            StringBuilder beanContent=new StringBuilder();
            beanContent.append("\n ----  ");
            beanContent.append("BEAN : ").append(beanInstance.getClass().getName());
            beanContent.append("  ---- \n");
            try {
                                     BeanInfo info = Introspector.getBeanInfo(beanInstance.getClass());
                    for (PropertyDescriptor pd:info.getPropertyDescriptors()) {
                            if (pd.getPropertyType()!=Class.class){
                                    String propertyName=pd.getName();
                                    beanContent.append(propertyName).append(" : ");
                                    Method mget=pd.getReadMethod();
                                    String propertyValue=new String();
                                    if (pd.getPropertyType()==int.class) {
                                            propertyValue=String.valueOf(mget.invoke(beanInstance));
                                    } else if (pd.getPropertyType()==Integer.class) {
                                            propertyValue=String.valueOf(mget.invoke(beanInstance));
                                    } else if (pd.getPropertyType()==Long.class) {
                                            propertyValue=String.valueOf(mget.invoke(beanInstance));
                                    } else if (pd.getPropertyType()==Boolean.class) {
                                            propertyValue=String.valueOf(mget.invoke(beanInstance));
                                    } else if (pd.getPropertyType()==String.class) {
                                            propertyValue=(String)mget.invoke(beanInstance);
                                    } else if (pd.getPropertyType()==Double.class) {
                                            propertyValue=String.valueOf(mget.invoke(beanInstance));
                                    } else if (pd.getPropertyType()==Float.class) {
                                            propertyValue=String.valueOf(mget.invoke(beanInstance));
                                    } else if (pd.getPropertyType()==BigDecimal.class) {
                                            propertyValue=String.valueOf(mget.invoke(beanInstance));
                                    } else if (pd.getPropertyType()==Class.class){
                                            propertyValue=pd.getPropertyType().getName();
                                    } else if (pd.getPropertyType()==Date.class){
                                            Date newDate=(Date)mget.invoke(beanInstance);
                                            if (newDate==null) {
                                                    propertyValue="null";
                                            } else {
                                                    propertyValue=newDate.toString();
                                            }
                                    } else if (pd.getPropertyType()==Timestamp.class){
                                            Timestamp newTimestamp=(Timestamp)mget.invoke(beanInstance);
                                            if (newTimestamp==null) {
                                                    propertyValue="null";
                                            } else {
                                                    propertyValue=newTimestamp.toString();
                                            }
                                    } else if (pd.getPropertyType()==Locale.class) {
                                            propertyValue=String.valueOf(mget.invoke(beanInstance));
                                    } else {
                                            propertyValue="property type to get value NOT DEFINED : ";
                                            propertyValue+=pd.getPropertyType().getName();
                                    }
                                    beanContent.append(propertyValue).append("\n");
                            }
                    }
            } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
            }
            beanContent.append("     ---------------- o 0 o ---------------       ");
            //logger.info(beanContent.toString());
            return beanContent.toString();
        }
    }
    
    
    public static void setFacesMessage(String message) {
        FacesMessage msg = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null,msg);
    }

    public static void setFacesMessageError(String message) {
        FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
        FacesContext.getCurrentInstance().addMessage(null, error);
    }
    
    public static void moveFile(String filename,String sourceFolder, String targetFolderString) {
        try{
 
    	   File afile =new File(sourceFolder+"/"+filename);
           logger.info("MOVING : "+sourceFolder+"/"+filename);
           logger.info("TO : "+targetFolderString+"/"+filename);
           
            // if the TARGET directory does not exist, create it
            File targetFolder = new File(targetFolderString);
            if (!targetFolder.exists()) {
                //logger.info("creating directory: " + targetFolder.toString());
                boolean dirCreated = targetFolder.mkdir();

                if (dirCreated) {
                    logger.info("TARGET FOLDER created");
                }
            }
            
    	   if(afile.renameTo(new File(targetFolderString + "/"+filename))){
    		logger.info("File is moved successful!");
    	   }else{
    		logger.info("File is failed to move!");
    	   }
 
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    /*
    public static void moveAppImage(AppImage image,Long sourceAlbum,Long targetAlbum) {
        logger.log(Level.INFO, "MOVING IMAGE : "+image.getTblId()
                +" source : " + sourceAlbum
                +" target:"+ targetAlbum); //, new Object[]{image.getTblId(), sourceAlbum, targetAlbum});
        moveFile(image.getTblId()+"."+image.getExtension(),
                    Util.getImagesFolder()+"/APP_"+image.getRefApp().getTblId()+"/ALBUM_"+sourceAlbum.toString(),
                    Util.getImagesFolder()+"/APP_"+image.getRefApp().getTblId()+"/ALBUM_"+targetAlbum.toString());
        //move thumbnail of the image
        moveFile(image.getTblId()+"."+image.getExtension(),
                    Util.getImagesFolder()+"/APP_"+image.getRefApp().getTblId()+"/ALBUM_"+sourceAlbum.toString()+"/SMALL",
                    Util.getImagesFolder()+"/APP_"+image.getRefApp().getTblId()+"/ALBUM_"+targetAlbum.toString()+"/SMALL");
    }
    */
    
    public static String getUploadsFolder() {
        //File homeDir = new File(System.getProperty("user.home"));
        File homeDir = new File(getHomedir());
        //String imagesFolder=settings.getSetting("ImagesFolder");
        //File homeDir = new File(imagesFolder);
        
        //logger.info("HOME DIR : " +homeDir.toString());
        String dirName="uploads";
        File targetFolder = new File(homeDir,dirName);

        // if the IMAGES directory does not exist, create it
        if (!targetFolder.exists()) {
          //logger.info("creating directory: " + targetFolder.toString());
          boolean dirCreated = targetFolder.mkdir();  

           if(dirCreated) {    
             logger.info("DIR created");  
           }
        }
        return homeDir+"/"+dirName;
    }

    public static String getImagesFolder() {
        //File homeDir = new File(System.getProperty("user.home"));
        File homeDir = new File(getUploadsFolder());
        //String imagesFolder=settings.getSetting("ImagesFolder");
        //File homeDir = new File(imagesFolder);

        //logger.info("HOME DIR : " +homeDir.toString());
        String dirName="images";
        File targetFolder = new File(homeDir,dirName);

        // if the IMAGES directory does not exist, create it
        if (!targetFolder.exists()) {
            //logger.info("creating directory: " + targetFolder.toString());
            boolean dirCreated = targetFolder.mkdir();

            if(dirCreated) {
                logger.info("DIR created");
            }
        }
        return homeDir+"/"+dirName;
    }

    public static File createDirectory(String newDirectory) {
        String uploadsFolder=Util.getUploadsFolder();
        File targetFolder = new File(uploadsFolder, newDirectory);
        if (!targetFolder.exists()) {
            boolean dirCreated = targetFolder.mkdir();
            if (dirCreated) {
                logger.info("DIR created : " + targetFolder);
            }
        }
        return targetFolder;
    }
    
    public static String toCamelCase(String s) {
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    public static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase()
                + s.substring(1).toLowerCase();
    }
}
