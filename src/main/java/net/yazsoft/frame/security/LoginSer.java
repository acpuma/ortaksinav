package net.yazsoft.frame.security;


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
public class LoginSer implements Serializable{
    private static final Logger logger = Logger.getLogger(LoginSer.class);
    private String username;
    private String password;
    private boolean rememberMe = false;
    private boolean loggedIn = false;

    @Inject
    private AuthenticationManager authenticationManager;

    public String login() throws IOException {
        try {
            Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(
                    this.getUsername(), this.getPassword());
            Authentication result = authenticationManager
                    .authenticate(authenticationRequest);

            SecurityContextHolder.getContext().setAuthentication(result);

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

            if (savedRequest != null) {
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
