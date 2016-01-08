package net.yazsoft.ors.exams;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.security.UsersDao;
import net.yazsoft.frame.utils.Constants;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.examsParameters.ExamsParametersDao;
import net.yazsoft.ors.examsParameters.ExamsParametersTypeDao;
import net.yazsoft.ors.lessons.LessonsDao;
import net.yazsoft.ors.lessons.LessonsDto;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.data.SortEvent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ExamsDao extends BaseGridDao<Exams> implements Serializable{
    private static final Logger logger = Logger.getLogger(ExamsDao.class);
    Exams selected;
    ExamsYear filterYear;
    ExamsSeason filterSeason;
    ExamsSeasonNumber filterSeasonNumber;
    Schools filterSchool;
    Boolean listChanged;
    String sortColumn;
    String sortOrder;

    List<Exams> filteredExams;

    //@Inject SessionInfo sessionInfo;

    @Inject LessonsDao lessonsDao;
    @Inject ExamsAnswerTypeDao examsAnswerTypeDao;
    @Inject ExamsBookletTypeDao examsBookletTypeDao;
    @Inject ExamsFalseTypeDao examsFalseTypeDao;
    @Inject ExamsParametersTypeDao examsParametersTypeDao;
    @Inject ExamsParametersDao examsParametersDao;
    @Inject UsersDao usersDao;

    public ExamsDao() {
        super(Exams.class);
    }

    @Override
    @PostConstruct
    public void init() {
        //filterSchool=Util.getActiveSchool();
        logger.info("EXAMSDAO INIT");
        super.init();
        listChanged=true;
        sortColumn="tid";
        sortOrder="desc";
        //filteredExams=new ArrayList<Exams>();
    }

    public void sortListener(final SortEvent sortEvent) {
        String column=sortEvent.getSortColumn().getColumnKey();
        logger.info("LOG02950: SORT COLUMN :" + column);
        if (sortEvent.isAscending()) {
            sortOrder="asc";
        } else {
            sortOrder="desc";
        }
        if (column.contains("examdate")) sortColumn="date";
        if (column.contains("examname")) sortColumn="nameTr";
        findFilteredExams();
    }

    @Override
    public void delete() {
        for (Users tempuser:getItem().getUsersCollection()) {
            tempuser.setRefActiveExam(null);
            usersDao.update(tempuser);
        }
        super.delete();
    }

    public List<Exams> findFilteredExams() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            if (filterSchool!=null) {
                c.add(Restrictions.eq("refSchool", filterSchool));
            } else {
                if (!Util.getActiveUser().getRefRole().getName().equals(Constants.ROLE_ADMIN)) {
                    c.add(Restrictions.in("refSchool", Util.getActiveUser().getSchoolsCollection()));
                }
            }
            if (filterYear!=null) {
                c.add(Restrictions.eq("refExamYear", filterYear));
            }
            if (filterSeason!=null) {
                c.add(Restrictions.eq("refExamSeason", filterSeason));
            }
            if (filterSeasonNumber!=null) {
                c.add(Restrictions.eq("refExamSeasonNumber", filterSeasonNumber));
            }
            //for (Schools school:Util.getActiveUser().getSchoolsCollection()) {

            //}
            if (sortOrder.equals("asc") ) {
                c.addOrder(Order.asc(sortColumn));
            } else {
                c.addOrder(Order.desc(sortColumn));
            }
            list = c.list();
            filteredExams=list;
            //logger.info("LOG02960: LIST :" + list);
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }

    public Long save() {
        logger.info("SAVINGGGGG");
        //Util.toString(item);
        Long pk=null;
        if (status== Status.UPDATE) {
            update();
        } else {
            pk=create();
        }
        //reset();
        listChanged=true;
        return pk;
    }

    @Override
    public void reset() {
        logger.info("EXAMSDAO RESET");
        super.reset();
        getItem().setRefAnswerType(examsAnswerTypeDao.getById(5L)); //E
        getItem().setRefBookletType(examsBookletTypeDao.getById(2L)); //B
        getItem().setRefFalseType(examsFalseTypeDao.getById(1L)); //0
        lessonsDao.reset();
        listChanged=true;
    }


    @Override
    public Long create() {
        Long pk=null;
        try {
            logger.info("CREATE : " + getItem());
            if (Util.getActiveSchool()==null) {
                throw new Exception("Once kurum aktif etmelisiniz");
            }
            getItem().setActive(Boolean.TRUE);
            getItem().setRefExamType(new ExamsType(1L));
            getItem().setRefSchool(Util.getActiveSchool());
            pk = super.create();
            lessonsDao.DtosToEntities(new Exams(pk));
            ExamsParameters parameter;
            for (ExamsParametersType type:examsParametersTypeDao.getDefaultParameters()){
                parameter=new ExamsParameters();
                parameter.setActive(Boolean.TRUE);
                parameter.setRefParameter(type);
                parameter.setStart(type.getStart());
                parameter.setLength(type.getLength());
                parameter.setRefExam((Exams)getSession().load(Exams.class,pk));
                examsParametersDao.create(parameter);
            }
            reset();
            setItem((Exams)getSession().load(Exams.class,pk));
            setActiveExam();
        } catch (Exception e) {
            logger.error("EXCEPTION", e);
            Util.setFacesMessage("HATA: " +e.getMessage());
        }
        return pk;
    }

    @Override
    public String update() {
        logger.info("UPDATING LESSONS...");
        deleteLessons();
        super.update();
        lessonsDao.DtosToEntities(getItem());
        //reset();
        return null;
    }

    public void deleteLessons() {
        logger.info("DELETING LESSONS...");
        Lessons entity;
        for (LessonsDto dto:lessonsDao.getDeletedDtos()) {
            entity=(Lessons)getSession().get(Lessons.class,dto.getTid());
            getItem().getLessonsCollection().remove(entity);
            lessonsDao.delete(entity);
        }
    }
    @Override
    public void select() {
        try {
            Util.getSessionInfo().setExam(getItem());
            lessonsDao.reset();
            if (lessonsDao != null) {
                lessonsDao.getExamLessons(getItem());
            }
            super.select();
        } catch (Exception e) {
            e.printStackTrace();
            Util.setFacesMessageError(e.getMessage());
        }
    }

    public void setActiveExam() {
        try {
            Util.getSessionInfo().setSchool(getItem().getRefSchool());
            Util.getSessionInfo().setExam(getItem());
        } catch (Exception e) {
            e.printStackTrace();
            Util.setFacesMessageError(e.getMessage());
        }

    }

    public Exams findLastExam(Schools school) {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refSchool", school));
            c.addOrder(Order.desc("date"));
            list = c.list();
            logger.info("LOG02820: " + (Exams)list.get(0));
            if (list.size()>0) {
                return (Exams)list.get(0);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Exams getSelected() {
        return selected;
    }

    public void setSelected(Exams selected) {
        this.selected = selected;
    }

    public ExamsYear getFilterYear() {
        return filterYear;
    }

    public void setFilterYear(ExamsYear filterYear) {
        listChanged=true;
        this.filterYear = filterYear;
    }

    public ExamsSeason getFilterSeason() {
        return filterSeason;
    }

    public void setFilterSeason(ExamsSeason filterSeason) {
        listChanged=true;
        this.filterSeason = filterSeason;
    }

    public ExamsSeasonNumber getFilterSeasonNumber() {
        return filterSeasonNumber;
    }

    public void setFilterSeasonNumber(ExamsSeasonNumber filterSeasonNumber) {
        listChanged=true;
        this.filterSeasonNumber = filterSeasonNumber;
    }

    public List<Exams> getFilteredExams() {
        if (listChanged) {
            filteredExams = findFilteredExams();
            listChanged = false;
        }
        return filteredExams;
    }

    public void setFilteredExams(List<Exams> filteredExams) {
        this.filteredExams = filteredExams;
    }

    public Schools getFilterSchool() {
        return filterSchool;
    }

    public void setFilterSchool(Schools filterSchool) {
        listChanged=true;
        this.filterSchool = filterSchool;
    }
}
