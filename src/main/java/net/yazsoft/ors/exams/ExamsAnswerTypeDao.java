package net.yazsoft.ors.exams;


import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.ExamsAnswerType;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class ExamsAnswerTypeDao extends BaseGridDao<ExamsAnswerType> implements Serializable{

    private static final Logger logger = Logger.getLogger(ExamsAnswerTypeDao.class);

    public ExamsAnswerTypeDao() {
        super(ExamsAnswerType.class);
    }

    public List<ExamsAnswerType> getExamAnswerTypes(Exams exam) {
        logger.info("EXAM : " + exam);
        List list=null;
        if ((exam!=null) && (exam.getRefAnswerType()!=null) ) {
            list=new ArrayList<>();
            for (ExamsAnswerType answerType:getAll()) {
                if (answerType.getTid()<=exam.getRefAnswerType().getTid()){
                    list.add(answerType);
                }
            }
        }
        return list;
    }
}
