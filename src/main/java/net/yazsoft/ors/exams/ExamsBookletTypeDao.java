package net.yazsoft.ors.exams;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.ExamsBookletType;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ExamsBookletTypeDao extends BaseGridDao<ExamsBookletType> implements Serializable{

    private static final Logger log = Logger.getLogger(ExamsBookletTypeDao.class);

    public List<ExamsBookletType> findUntilBookletType(ExamsBookletType bookletType) {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.le("tid",bookletType.getTid()));
            list=c.list();
            log.info("BOOKLETTYPES : " + list);
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }
    public ExamsBookletTypeDao() {
        super(ExamsBookletType.class);
    }
}
