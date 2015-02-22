package net.yazsoft.frame.hibernate;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;


@FacesConverter(forClass = BaseEntity.class) //entityid")
public class BaseEntityConverter implements Converter {
    static final Logger logger= Logger.getLogger(BaseEntityConverter.class.getName());

    //@Autowired
    //BaseDao baseDao;

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
        BaseDao baseDao =null;
        Long tblid=Long.valueOf(value);
        try {
            if (value == null || value.isEmpty()) {
                return null;
            }
            //return value;

            if (!value.matches("[0-9]+")) {
                throw new ConverterException("Value is not a valid ID of BaseEntity.");
            }
            logger.info(">>>>Converter: " + value);
            Class<?> type = component.getValueExpression("value").getType(context.getELContext());
            logger.info(">>>>---TYPE : " + type);
            String lowClassName=UPPER_CAMEL.to(LOWER_CAMEL,type.getSimpleName());

            WebApplicationContext webAppContext = FacesContextUtils
                    .getWebApplicationContext(FacesContext.getCurrentInstance());
            baseDao=(BaseDao)webAppContext.getBean(lowClassName+"Dao");
            logger.info("BASEDAO : " +baseDao);
            logger.info(">>>>---ID : " + tblid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return baseDao.getById(tblid);
    }

}
