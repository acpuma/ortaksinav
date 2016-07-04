package net.yazsoft.ors.optic;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.OpticsFieldsType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class OpticFieldsTypeDao extends BaseGridDao<OpticsFieldsType> implements Serializable{
    private OpticsFieldsType selected;

    private List<OpticsFieldsType> items;

    public  List<OpticsFieldsType> findDefaultFields() {
        List list=null;
        try {
            Criteria c=getCriteria();
            c.add(Restrictions.eq("active",Boolean.TRUE));
            c.add(Restrictions.eq("addDefault",Boolean.TRUE));
            c.addOrder(Order.asc("rank"));
            list=c.list();
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }

    public  List<OpticsFieldsType> findFields() {
        List list=null;
        try {
            Criteria c=getCriteria();
            c.add(Restrictions.eq("active",Boolean.TRUE));
            c.addOrder(Order.asc("rank"));
            list=c.list();
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }


    public OpticFieldsTypeDao() {
        super(OpticsFieldsType.class);
    }

    public OpticsFieldsType getSelected() {
        return selected;
    }

    public void setSelected(OpticsFieldsType selected) {
        this.selected = selected;
    }

    public List<OpticsFieldsType> getItems() {
        if (items==null) {
            items = findFields();
        }
        return items;
    }

    public void setItems(List<OpticsFieldsType> items) {
        this.items = items;
    }
}
