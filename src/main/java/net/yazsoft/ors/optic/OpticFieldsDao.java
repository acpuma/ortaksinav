package net.yazsoft.ors.optic;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Optics;
import net.yazsoft.ors.entities.OpticsFields;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class OpticFieldsDao extends BaseGridDao<OpticsFields> implements Serializable{

    OpticsFields selected;

    public List<OpticsFields> findFieldsByOptic(Optics optic) {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refOptic", optic));
            c.addOrder(Order.asc("rank"));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
            //listChanged=false;
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }


    public OpticFieldsDao() {
        super(OpticsFields.class);
    }

    public OpticsFields getSelected() {
        return selected;
    }

    public void setSelected(OpticsFields selected) {
        this.selected = selected;
    }


}
