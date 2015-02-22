package net.yazsoft.frame.utils;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class ErrorHandler {
    public String getStatusCode(){
        String val = String.valueOf((Integer)FacesContext.getCurrentInstance().getExternalContext().
                getRequestMap().get("javax.servlet.error.status_code"));
        return val;
    }

    public String getMessage(){
        String val =  (String)FacesContext.getCurrentInstance().getExternalContext().
                getRequestMap().get("javax.servlet.error.message");
        return val;
    }

    public String getExceptionType(){
        String item=null;
        Object val = FacesContext.getCurrentInstance().getExternalContext().
                getRequestMap().get("javax.servlet.error.exception_type");
        if (val!=null) {item=val.toString();}
        return item;
    }

    public String getException(){
        String item=null;
        Object val = (Exception)FacesContext.getCurrentInstance().getExternalContext().
                getRequestMap().get("javax.servlet.error.exception");
        if (val!=null) {item=val.toString();}
        return item;
    }

    public String getRequestURI(){
        return (String)FacesContext.getCurrentInstance().getExternalContext().
                getRequestMap().get("javax.servlet.error.request_uri");
    }

    public String getServletName(){
        return (String) FacesContext.getCurrentInstance().getExternalContext().
                getRequestMap().get("javax.servlet.error.servlet_name");
    }

}
