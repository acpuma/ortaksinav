package net.yazsoft.frame.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author fec
 */
public class BaseEntityInterceptor extends EmptyInterceptor {
    private static final Logger logger = Logger.getLogger(BaseEntityInterceptor.class.getName());
    private static final long serialVersionUID = 1L;

    @Override
    public boolean onSave(Object entity,
                          Serializable id,
                          Object[] state,
                          String[] propertyNames,
                          Type[] types) {
        logger.info(" --------- BASE ENTITY INTERCEPTOR ONSAVE -------");
        boolean returnbool=false;
        if ( entity instanceof BaseEntity ) {
            for ( int i=0; i<propertyNames.length; i++ ) {
                if ( "createdDate".equals( propertyNames[i]) ) {
                    state[i] = new Date();
                    returnbool=true;
                }
                if ( "version".equals( propertyNames[i] ) ) {
                    state[i] = 1;
                    returnbool=true;
                }
                if ( "active".equals( propertyNames[i] ) ) {
                    state[i] = 1;
                    returnbool=true;
                }
            }
        }
        return returnbool;
    }
    
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] state, Object[] previousState, String[] propertyNames, Type[] types) {
        boolean returnbool=false;
        if ( entity instanceof BaseEntity ) {
            for ( int i=0; i<propertyNames.length; i++ ) {
                if ( "upd".equals( propertyNames[i] ) ) {
                    state[i] = new Date();
                    returnbool=true;
                }
            }
        }
        
        return returnbool;
    }
    
}
