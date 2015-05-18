package net.yazsoft.frame.security;


import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.ZlogLogin;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
@Transactional
public class ZlogLoginDao extends BaseGridDao<ZlogLogin>{
    private static final Logger logger = Logger.getLogger(ZlogLoginDao.class);
    public ZlogLogin selected;
    List<ZlogLogin> logs;

    public List<ZlogLogin> findLogs() {
        List list = null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.addOrder(Order.desc("created"));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        logs = list;
        return list;
    }

    public ZlogLoginDao() {
        super(ZlogLogin.class);
    }

    public ZlogLogin getSelected() {
        return selected;
    }

    public void setSelected(ZlogLogin selected) {
        this.selected = selected;
    }

    public List<ZlogLogin> getLogs() {
        if (logs==null) {
            logs=findLogs();
        }
        return logs;
    }

    public void setLogs(List<ZlogLogin> logs) {
        this.logs = logs;
    }
}
