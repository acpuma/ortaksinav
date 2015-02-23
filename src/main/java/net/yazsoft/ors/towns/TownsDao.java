package net.yazsoft.ors.towns;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Cities;
import net.yazsoft.ors.entities.Towns;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@Transactional
public class TownsDao extends BaseGridDao<Towns> implements Serializable{
    Logger logger= Logger.getLogger(TownsDao.class);
    Towns selected;
    List<Towns> towns;

    public List findByCity(Cities city) {
        logger.info(city);
        List list=null;

        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refCity", city));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public TownsDao() {
        super(Towns.class);
    }

    public Towns getSelected() {
        return selected;
    }

    public void setSelected(Towns selected) {
        this.selected = selected;
    }

    public List<Towns> getTowns() {
        return towns;
    }

    public void setTowns(List<Towns> towns) {
        this.towns = towns;
    }
}
