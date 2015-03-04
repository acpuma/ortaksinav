package net.yazsoft.ors.lessonsName;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.LessonsGroup;
import net.yazsoft.ors.entities.LessonsName;
import net.yazsoft.ors.lessonsGroup.LessonsGroupDao;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class LessonsNameDao extends BaseGridDao<LessonsName> implements Serializable{
    private static final Logger logger = Logger.getLogger(LessonsNameDao.class);
    LessonsName selected;

    List<LessonsName> names;

    public List findByGroup(LessonsGroup group) {
        logger.info(group);
        List list=null;

        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refLessonGroup", group));
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

    public List<LessonsName> getNames() {
        //names=findByGroup(lessonsGroupDao.getSelected());
        return names;
    }

    public void setNames(List<LessonsName> names) {
        this.names = names;
    }

    public LessonsNameDao() {
        super(LessonsName.class);
    }

    public LessonsName getSelected() {
        return selected;
    }

    public void setSelected(LessonsName selected) {
        this.selected = selected;
    }
}
