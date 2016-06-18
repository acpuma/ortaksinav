package net.yazsoft.frame.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.yazsoft.frame.hibernate.BaseDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.security.UsersDao;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Users;
import org.apache.log4j.Logger;
import org.hibernate.mapping.Component;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.util.*;

@Named
@ViewScoped
public class Report extends BaseDao{
    private static final Logger logger = Logger.getLogger(Report.class);

    @Inject DataSource dataSource;

    public void pdf(String jasperFile, Map<String, Object> params,String outputFile)  {
        Connection connection=null;
        try {
            if (outputFile==null) outputFile="rapor";
            String path = Util.getSession().getServletContext().getRealPath("/WEB-INF/jasper/"+jasperFile + ".jasper");
            logger.info("LOG01170: PATH : " + path);
            InputStream input = new FileInputStream(new File(path));

            HttpServletResponse response = (HttpServletResponse)
                    FacesContext.getCurrentInstance().getExternalContext().getResponse();

            connection=dataSource.getConnection();

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(input);
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperReport, params, connection);

            response.setContentType("application/x-pdf");
            response.setHeader("Content-disposition", "inline; filename="+outputFile+".pdf");

            final OutputStream outStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
            //outStream.flush();
            //outStream.close();
            FacesContext.getCurrentInstance().responseComplete();

        } catch(Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if ((connection != null) && (!connection.isClosed())) {
                    connection.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                Util.setFacesMessage(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void pdfFile(String jasperFile, Map<String, Object> params,String outputFile)  {
        Connection connection=null;
        try {
            if (outputFile==null) outputFile="rapor";
            String path = Util.getSession().getServletContext().getRealPath("/WEB-INF/jasper/"+jasperFile + ".jasper");
            logger.info("LOG01170: PATH : " + path);
            InputStream input = new FileInputStream(new File(path));

            connection=dataSource.getConnection();

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(input);
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperReport, params, connection);
            String outfile=Util.getUploadsFolder()+"/temp/"+outputFile+ ".pdf";
            OutputStream output = new FileOutputStream(new File(outfile));

            JasperExportManager.exportReportToPdfStream(jasperPrint, output);
            //JasperExportManager.exportReportToPdfStream(jasperPr;
            output.flush();
            output.close();
            logger.info("REPORT FINISHEDDDD");
        } catch(Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if ((connection != null) && (!connection.isClosed())) {
                    connection.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                Util.setFacesMessage(e.getMessage());
                e.printStackTrace();
            }
        }
    }


}
