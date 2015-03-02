package net.yazsoft.frame.hibernate;

import net.yazsoft.frame.utils.Util;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* @author acpuma */

@Transactional
public abstract class BaseGridDao<T extends BaseEntity> extends BaseDao<T> {
    private static final Logger logger = Logger.getLogger(BaseGridDao.class.getName());
    T item;
    public enum Status {CREATE, READ,UPDATE,DELETE};
    protected Status status= Status.CREATE;
    boolean disabledelete=true; //for delete button

    //LazyDataModel<T> lazyModel;

    //@Autowired
    //BaseLazyModel<T> baseLazyModel;

    public BaseGridDao() {}

    public BaseGridDao(Class<T> type) {
        super(type);
        try {
            item=type.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error(ex);
        }
    }

    @PostConstruct
    public void postConstruct(){
        logger.info("BASEGRIDDAO CONSTRUCTOR");
    }
     
    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public void init() {}
    
    public Long save() {
        logger.info("SAVINGGGGG");
        //Util.toString(item);
        Long pk=null;
        if (status== Status.UPDATE) {
            update();
        } else {
            pk=create();
        }
        reset();
        return pk;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    public void reset() {
        try {
            item=type.newInstance();
            logger.info(FacesContext.getCurrentInstance().getViewRoot().getViewMap());
            //destroying view beans
            //FacesContext.getCurrentInstance().getViewRoot().getViewMap().clear();
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error(ex);
        }
        status= Status.CREATE;
        disabledelete=true;
        init();
    }
    
    public void select() {
        status= Status.UPDATE;
        disabledelete=false;
    }
    
    public Long create() {
        Long pk=null;
        try{
            pk=(Long)create(item);
            logger.info("PK : " + pk);
            Util.setFacesMessage("Kayit Edildi");
        }catch(Exception ex){
            logger.error("CREATE EXCEPTION",ex);
            Util.setFacesMessage("KAYITTA HATA : " + ex.getMessage());
        }
        return pk;
    }
    
    public String update() {
        T result=null;
        try{
            result=update(item);//update(item);
            Util.setFacesMessage("KAYIT GUNCELLENDI");
        }catch(HibernateException ex){
            logger.error("GÜNCELLEMEDE HATA",ex);
            Util.setFacesMessage(ex.getMessage());
            throw ex;
        }
        return null;
    }
    
    public void delete() {
        delete(item);
        reset();
        Util.setFacesMessage("SİLİNDİ");
    }

    public boolean isDisabledelete() {
        return disabledelete;
    }

    public void setDisabledelete(boolean disabledelete) {
        this.disabledelete = disabledelete;
    }


    public void initLazyModel() {
        logger.info("INITLAZYMODEL");
        //lazyModel = new BaseLazyModel<>(type);

        /*
        lazyModel = new LazyDataModel<T>() {


            @Override
            public T getRowData(String string) {
                List<T> cars = (List<T>) getWrappedData();
                for (T car : cars) {
                    if ((car.hashCode() + "").equals(string)) {
                        return car;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(T t) {
                return t.hashCode();
            }

            @Transactional
            @Override
            public List<T> load(int first, int pageSize, String sortField,
                                SortOrder sortOrder, Map<String, Object> filters) {
                setRowCount(rowCount());
                return getAll(first, pageSize, sortField, sortOrder, filters);
            }
        };
        */

        //Util.toString(lazyModel);
    }

    /*
    public LazyDataModel<T> getLazyModel() {
        if (lazyModel==null)
            initLazyModel();
        return lazyModel;
    }
    */



}
