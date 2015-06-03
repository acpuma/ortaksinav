/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.frame.hibernate;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "boolean")
public class BooleanConverter implements Converter {
    static final Logger logger= Logger.getLogger(BooleanConverter.class.getName());

      @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            logger.info("BOOLEAN asString: "+ value);
                if ((int)value==0) {
                    return "FALSE";
                } else {
                    return "TRUE";
                }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(BaseEntityConverter.class.getName()).log(Level.ERROR, null, ex);
        }
        return "FALSE";
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        logger.info("BOOLEAN asObject: "+ value);
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        if ( ((String)value).toLowerCase().equals("true") ) {
            logger.info("CONVERTER TRUE");
            return 1;
        } else {
            logger.info("CONVERTER FALSE");
        }
      
        return 0;
    }

}
