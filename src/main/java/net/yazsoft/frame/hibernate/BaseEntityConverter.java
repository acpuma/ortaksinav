package net.yazsoft.frame.hibernate;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.enterprise.inject.Model;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/** * @author fec */

@FacesConverter(forClass = BaseEntity.class) //entityid")
@Model
public class BaseEntityConverter implements Converter {
    static final Logger logger= Logger.getLogger(BaseEntityConverter.class.getName());
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Number id =null;
        try {
            if (value == null || value=="") {
                return "";
            }
            logger.debug("PACKAGE : "+value.getClass().getPackage());

            //TODO : change package according to project
            if (!(value.getClass().getPackage().getName().equals("net.yazsoft.ors.entities"))) {
                logger.info("COMPONENT :" +component.getClientId());
                logger.info("VALUE : " + value);
                throw new ConverterException("Value is not under net.yazsoft.ors.entities package"
                        + " Under: " + value.getClass().getPackage().getName() 
                        + " Value : " + value);
                //return null;
            }
            
            id =(Number) new PropertyDescriptor("tid", value.getClass()).getReadMethod().invoke(value);
            //Number id = value.getTblId();
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(BaseEntityConverter.class.getName()).log(Level.ERROR, null, ex);
        }
        return (id != null) ? id.toString() : null;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
      
        //return value;
        
        if (!value.matches("[0-9]+")) {
            throw new ConverterException("Value is not a valid ID of BaseEntity.");
        }
        logger.debug(">>>>Converter: "+value);
        Class<?> type = component.getValueExpression("value").getType(context.getELContext());
        logger.debug(">>>>---TYPE : " + type); 
        Class classs=type.getClass();
        BaseDao<BaseEntity> dao;
        Long tblid=Long.valueOf(value);
        logger.debug(">>>>---ID : " + tblid);
        dao = new BaseDao((Class<BaseEntity>) type);
        //return baseService.find((Class<BaseEntity<? extends Number>>) type, Long.valueOf(value));
        return dao.getById(tblid);
    }

}
