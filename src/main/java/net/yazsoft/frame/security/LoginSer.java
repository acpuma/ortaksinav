package net.yazsoft.frame.security;


import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.Users;
import net.yazsoft.ors.exams.ExamsDao;
import net.yazsoft.ors.schools.SchoolsDao;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Named
@ViewScoped
public class LoginSer implements Serializable{
    private static final Logger logger = Logger.getLogger(LoginSer.class);
    private String username="a";
    private String password; //="b";
    private boolean rememberMe = false;
    private boolean loggedIn = false;

    @Inject
    private AuthenticationManager authenticationManager;

    @Inject SessionInfo sessionInfo;
    @Inject UsersDao usersDao;
    @Inject SchoolsDao schoolsDao;
    @Inject ExamsDao examsDao;

    public String login() throws IOException {
        try {
            logger.info("user / pass : " + this.getUsername() + " / " + this.getPassword());
            Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(
                    this.getUsername(), this.getPassword());
            Authentication result = authenticationManager
                    .authenticate(authenticationRequest);

            SecurityContextHolder.getContext().setAuthentication(result);
            Users user=usersDao.findByUserName(this.getUsername());
            sessionInfo.setUser(user);
            sessionInfo.setSchool(user.getRefActiveSchool());
            sessionInfo.setExam(user.getRefActiveExam());

            // restore the request before the login-redirect, if any.
            RequestCache requestCache = new HttpSessionRequestCache();
            HttpServletRequest request = (HttpServletRequest) FacesContext
                    .getCurrentInstance().getExternalContext().getRequest();
            HttpServletResponse response = (HttpServletResponse) FacesContext
                    .getCurrentInstance().getExternalContext().getResponse();
            SavedRequest savedRequest = requestCache.getRequest(request,
                    response);
            ExternalContext ec = FacesContext.getCurrentInstance()
                    .getExternalContext();
            if (savedRequest!=null) {
                logger.info("LOGIN REQUEST PAGE : " + savedRequest.getRedirectUrl());
            }
            if ((savedRequest != null) && (!savedRequest.getRedirectUrl().contains("login"))) {
                // redirect to the page requested before login
                ec.redirect(savedRequest.getRedirectUrl());
            } else {
                // login page requested directly, redirect to index after login
                //ec.redirect("/index.html");
                return "/";
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Bad credentials", username);
        FacesContext.getCurrentInstance().addMessage(null, message);
        return null;
    }

    public void logout() throws IOException {
        logger.info("Loggin out");
        SecurityContextHolder.clearContext();

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        ec.redirect(ec.getRequestContextPath() + "/index.html");
    }

    public String cancel() {
        this.username = "";
        this.password = "";
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
