package net.yazsoft.frame.utils;

import org.apache.log4j.Logger;

import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/** @author fec */
@Named
@WebServlet("/files/*")
public class FileServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FileServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filename = request.getPathInfo().substring(1);
        //TODO: get folder from web.xml or properties file
        //final File homeDir = new File(System.getProperty("user.home"));
        final File homeDir = new File(Util.getFilesFolder());
        logger.info("HOME DIR : " +homeDir.toString());
        //String dirName="images";
        //File targetFolder = new File(homeDir,dirName);
        //File targetFolder = new File(homeDir);
        File file = new File(homeDir, filename);
        response.setHeader("Content-Type", getServletContext().getMimeType(filename));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }

}
