package net.yazsoft.frame.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.security.UsersDao;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Users;
import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Named
@ViewScoped
public class Report {
    private static final Logger logger = Logger.getLogger(Report.class);
    public static JasperDesign jasperDesign;
    public static JasperPrint jasperPrint;
    public static JasperReport jasperReport;
    public static String reportTemplateUrl = "users.jrxml";

    @Inject UsersDao usersDao;

    public void print() {
        try
        {
            String path = Util.getSession().getServletContext().getRealPath(reportTemplateUrl);
            logger.info("LOG01170: PATH : " + path);
            InputStream input = new FileInputStream(new File(path));

            //get report file and then load into jasperDesign
            jasperDesign = JRXmlLoader.load(input);
            //compile the jasperDesign
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            //fill the ready report with data and parameter
            jasperPrint = JasperFillManager.fillReport(jasperReport, null,
                    new JRBeanCollectionDataSource(
                            findReportData() ));

            JRPdfExporter exporter = new JRPdfExporter();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(baos);

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            //exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos)); //"users.pdf"));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void pdf() throws JRException, IOException{

        HttpServletResponse httpServletResponse=(HttpServletResponse)
                FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename="+"users.pdf");

        FacesContext.getCurrentInstance().responseComplete();

        ServletOutputStream servletOutputStream=httpServletResponse.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        System.out.println("All done the report is done");
        servletOutputStream.flush();
        servletOutputStream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    private static Collection findReportData()
    {

        //declare a list of object
        List<Users> data = new LinkedList<Users>();
        Users p1 = new Users();
        p1.setName("John");
        p1.setSurname("Smith");
        p1.setUsername("jsmith");
        data.add(p1);
        return data;
    }

}
