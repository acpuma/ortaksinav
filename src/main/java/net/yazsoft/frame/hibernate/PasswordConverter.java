package net.yazsoft.frame.hibernate;


import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "password")
public class PasswordConverter implements Converter{
    Logger logger=Logger.getLogger(PasswordConverter.class);

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String password=encoder.encode(value);
        logger.info("pass: " + value + " encrypt : " + password);
        return password;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return null;
    }


}
