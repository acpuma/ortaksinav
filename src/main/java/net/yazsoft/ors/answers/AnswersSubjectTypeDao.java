package net.yazsoft.ors.answers;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.AnswersSubjectType;
import net.yazsoft.ors.entities.Lessons;
import net.yazsoft.ors.entities.LessonsName;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AnswersSubjectTypeDao extends BaseGridDao<AnswersSubjectType> implements Serializable{
    private static final Logger logger = Logger.getLogger(AnswersSubjectTypeDao.class);
    AnswersSubjectType selected;

    public AnswersSubjectTypeDao() {
        super(AnswersSubjectType.class);
    }

    public List<AnswersSubjectType> getLessonSubjects(LessonsName lessonName) {
        logger.info("LESSON : " + lessonName);
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refLessonName", lessonName));
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

    public AnswersSubjectType getSelected() {
        return selected;
    }

    public void setSelected(AnswersSubjectType selected) {
        this.selected = selected;
    }
}
