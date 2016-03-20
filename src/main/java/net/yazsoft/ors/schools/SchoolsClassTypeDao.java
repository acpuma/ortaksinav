package net.yazsoft.ors.schools;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.ExamsBookletType;
import net.yazsoft.ors.entities.SchoolsClassType;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class SchoolsClassTypeDao extends BaseGridDao<SchoolsClassType> implements Serializable{
    private static final Logger log = Logger.getLogger(SchoolsClassTypeDao.class);
    SchoolsClassType selected;

    public List<SchoolsClassType> items;



    @PostConstruct
    public void init() {
        setItem(new SchoolsClassType());
    }

    public void delete(SchoolsClassType classType) {
        try {
            super.delete(classType);
            items = null;
        }catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void onRowSelect(SelectEvent event) {
        selected=(SchoolsClassType) event.getObject();
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            SchoolsClassType classType=(SchoolsClassType) event.getObject();
            log.info(classType.getName());
            DataTable table = (DataTable) event.getSource();
            classType = (SchoolsClassType) table.getRowData();
            log.info(classType.getName());
            super.update(classType);
            //Util.setFacesMessage("Edited :" + lesson+", " +lesson.getQuestionCount());
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
    }

    public void onRowCancel(RowEditEvent event) {
        //Util.setFacesMessage("Edit Cancelled : " + ((LessonsDto) event.getObject()).getTid());
    }

    public Long save() {
        Long pk=null;
        try {
            getItem().setActive(true);
            pk = super.save();
            items = null;
        } catch (Exception e) {
            Util.catchException(e);
        }
        return pk;
    }

    public List<SchoolsClassType> findItems() {
        if (items==null) {
            final Criteria c = getCriteria();
            c.add( Restrictions.eq("active", true) );
            c.addOrder(Order.desc("name"));
            items = c.list();
        }
        return items;
    }

    public void classTypeChanged() {

    }



    public List<SchoolsClassType> getItems() {
        if (items==null) {
            items=findItems();
        }
        return items;
    }

    public void setItems(List<SchoolsClassType> items) {
        this.items = items;
    }

    public SchoolsClassTypeDao() {
        super(SchoolsClassType.class);
    }

    public SchoolsClassType getSelected() {
        return selected;
    }
    public void setSelected(SchoolsClassType selected) {
        this.selected = selected;
    }
}
