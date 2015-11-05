package net.yazsoft.frame.utils;

import net.yazsoft.frame.scopes.ViewScoped;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.springframework.transaction.annotation.Transactional;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Named
@ViewScoped
public class Excel implements Serializable{

    private static final Logger log = Logger.getLogger(Excel.class);
    private static final int BUFFER_SIZE = 6124;
    Workbook workbook = null;

    public void readExcel(String fileName) {
        try {
            //Create the input stream from the xlsx/xls file
            FileInputStream fis = new FileInputStream(fileName);

            //Create Workbook instance for xlsx/xls file input stream
            workbook = null;
            if (fileName.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (fileName.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            }
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadExcel(String filestr) {
        downloadExcel(filestr,null);
    }

    public void downloadExcel(String filestr,String folder) {
        String filename;
        if (folder==null) {
            filename = Util.getUploadsFolder() + "/" + filestr;
        } else {
            filename = Util.getUploadsFolder() + "/" + folder + "/"+ filestr;
        }
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment;filename="+filestr);

        try {
            File file = new File(filename);
            FileInputStream fileIn = new FileInputStream(file);
            ServletOutputStream out = response.getOutputStream();

            byte[] outputByte = new byte[4096];
            //copy binary contect to output stream
            while(fileIn.read(outputByte, 0, 4096) != -1)
            {
                out.write(outputByte, 0, 4096);
            }
            fileIn.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Transactional
    public void handleFileUpload(FileUploadEvent event, String filename,String folder) {
        log.info("UPLOADING FILE.......");
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            InputStream inputStream;
            inputStream = event.getFile().getInputstream();
            OutputStream out ;
            if (folder==null) {
                out=new FileOutputStream(new File(Util.getUploadsFolder(),"/"+ filename));
            } else {
                out=new FileOutputStream(new File(Util.getUploadsFolder(),"/"+ folder + "/"+ filename));
            }

            int read = 0;
            byte[] bytes = new byte[BUFFER_SIZE];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();

            Util.setFacesMessage("File name: "
                    + event.getFile().getFileName() + " yuklendi ");
            //+ event.getFile().getSize() / 1024 + " Kb Content type: "
            //+ event.getFile().getContentType() + " the file was uploaded.");
        } catch(Exception e){
            Util.setFacesMessageError(" the files were not uploaded : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }
}
