package net.yazsoft.ors.exams;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.ExamsYear;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ExamsYearDao extends BaseGridDao<ExamsYear> implements Serializable{

    private static final Logger log = Logger.getLogger(ExamsYearDao.class);
    ExamsYear selected;
    List<ExamsYear> items;
    List<ExamsYear> itemsActive;

    @Inject ExamsDao examsDao;

    public ExamsYear findDefaultYear() {
        ExamsYear year=null;
        final Criteria c = getCriteria();
        c.add( Restrictions.eq("current",true) );
        year = (ExamsYear) c.uniqueResult();
        return year;
    }

    //Update aktif/pasif year
    public void checkboxChange(ExamsYear year) {
        try {
            update(year);
            //Util.setFacesMessage("CHANGED");
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    @PostConstruct
    public void init() {
        setItem(new ExamsYear());
    }

    public void delete(ExamsYear year) {
        try {
            super.delete(year);
            items = null;
        }catch (Exception e) {
            Util.catchException(e);
        }
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

    public void onRowSelect(SelectEvent event) {
        selected=(ExamsYear) event.getObject();
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            ExamsYear yearName=(ExamsYear) event.getObject();
            log.info("LOG00080:" + yearName.getName());
            DataTable table = (DataTable) event.getSource();
            yearName = (ExamsYear) table.getRowData();
            log.info("LOG00080:" + yearName.getName());
            super.update(yearName);
            //Util.setFacesMessage("Edited :" + lesson+", " +lesson.getQuestionCount());
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
    }

    public void onRowCancel(RowEditEvent event) {
        //Util.setFacesMessage("Edit Cancelled : " + ((LessonsDto) event.getObject()).getTid());
    }

    public List<ExamsYear> getItemsActive() {
        if (itemsActive==null) {
            final Criteria c = getCriteria();
            c.add( Restrictions.eq("active",true) );
            c.addOrder(Order.desc("current"));
            c.addOrder(Order.desc("name"));
            itemsActive = c.list();
        }
        return itemsActive;
    }

    public void setItemsActive(List<ExamsYear> itemsActive) {
        this.itemsActive = itemsActive;
    }

    public List<ExamsYear> getAll() {
        List<ExamsYear> all;
        final Criteria c = getCriteria();
        all = c.list();
        if (all!=null) {
            log.info("ALL COUNT :" + all.size());
        }
        return all;
    }

    public ExamsYearDao() {
        super(ExamsYear.class);
    }

    public List<ExamsYear> getItems() {
        if (items==null) {
            items=getAll();
        }
        return items;
    }

    public void setItems(List<ExamsYear> items) {
        this.items = items;
    }
}
