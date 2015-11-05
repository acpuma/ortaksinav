package net.yazsoft.ors.exams;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.ExamsSeason;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ExamsSeasonDao extends BaseGridDao<ExamsSeason> implements Serializable{
    ExamsSeason selected;

    List<ExamsSeason> items;

    public void checkboxChange(ExamsSeason season) {
        try {
            update(season);
            //Util.setFacesMessage("CHANGED");
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public List<ExamsSeason> findItems() {
        if (items==null) {
            final Criteria c = getCriteria();
            c.add( Restrictions.eq("active", true) );
            c.addOrder(Order.desc("current"));
            items = c.list();
        }
        return items;
    }

    public ExamsSeason findDefaultSeason() {
        ExamsSeason season=null;
        final Criteria c = getCriteria();
        c.add( Restrictions.eq("current",true) );
        season = (ExamsSeason) c.uniqueResult();
        return season;
    }

    public ExamsSeasonDao() {
        super(ExamsSeason.class);
    }

    public ExamsSeason getSelected() {
        return selected;
    }

    public void setSelected(ExamsSeason selected) {
        this.selected = selected;
    }

    public List<ExamsSeason> getItems() {
        if (items==null) {
            items=findItems();
        }
        return items;
    }

    public void setItems(List<ExamsSeason> items) {
        this.items = items;
    }
}
