package net.yazsoft.ors.schedule;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.LessonsGroup;
import net.yazsoft.ors.entities.Schedules;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DateUtil;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.component.schedule.Schedule;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class ScheduleDao extends BaseGridDao<Schedules> implements Serializable{
    private static final Logger log = Logger.getLogger(ScheduleDao.class);
    private ScheduleModel eventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private Schedules selected;
    private ArrayList<LessonsGroup> lessonGroups;


    public LessonsGroup findLessonGroup(Date date) {
        LessonsGroup lessonGroup=null;
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        //hour--;

        switch (hour) {
            case 0: case 1: case 2 :case 3:case 4 :case 5 : lessonGroup=lessonGroups.get(0);break;
            case 6: case 7: case 8 :case 9:case 10 :case 11 :lessonGroup=lessonGroups.get(1);break;
            case 12: case 13: case 14 :case 15:case 16 :case 17 : lessonGroup=lessonGroups.get(2);break;
            case 18: case 19: case 20 :case 21:case 22 :case 23 :case -1 : lessonGroup=lessonGroups.get(3);break;
        }
        log.info("GROUPS : " + lessonGroups);
        log.info("HOUR : " + hour + " , GROUP : " + lessonGroup);
        return lessonGroup;

    }
    public void deleteEvent() {
        try {
            log.info("DELETING EVENT : " + event);
            log.info("SELECTED : " + selected);
            if(selected.getTid() != null) {
                delete(selected);
            }
            eventModel.deleteEvent(event);
            getSession().flush();

            //for refresh
            event = new DefaultScheduleEvent();
            eventModel = null;
            selected=new Schedules();
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void addEvent(ActionEvent actionEvent) {
        try {
            if(event.getId() == null) {
                eventModel.addEvent(event);
                selected.setActive(Boolean.TRUE);
                selected.setRefSchool(Util.getActiveSchool());
                selected.setStartDate(event.getStartDate());
                selected.setEndDate(event.getEndDate());
                saveOrUpdate(selected);
            } else {
                selected.setStartDate(event.getStartDate());
                selected.setEndDate(event.getEndDate());
                saveOrUpdate(selected);
                eventModel.updateEvent(event);
            }
            //for refresh
            event = new DefaultScheduleEvent();
            eventModel = null;
            selected=new Schedules();
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    @PostConstruct
    public void init() {
        selected=new Schedules();
        lessonGroups=new ArrayList<>();
        lessonGroups.add(new LessonsGroup());
        lessonGroups.add(new LessonsGroup());
        lessonGroups.add(new LessonsGroup());
        lessonGroups.add(new LessonsGroup());
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        Util.setFacesMessage("Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
    }

    public void findEvents() {
        if (eventModel==null) {
            eventModel = new DefaultScheduleModel();
        }
        for (Schedules schedule:findSchoolSchedules()) {
            if (schedule.getRefLessonName()!=null) {
                log.info("schedule : " + schedule.getRefLessonName().getNameTr() + schedule.getStartDate());
                eventModel.addEvent(new DefaultScheduleEvent(schedule.getRefLessonName().getNameTr(),
                        schedule.getStartDate(), schedule.getEndDate(), schedule));
            } else {
                log.info("schedule : " + schedule.getStartDate() + " lesson name is null" );
            }

        }
    }

    public List<Schedules> findSchoolSchedules() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refSchool",Util.getActiveSchool()));
            list = c.list();
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }

    public List<Schedules> findDaySchedules(Date scheduleDay) {
        List list=null;
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(scheduleDay);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        Date startHour=calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        Date endHour=calendar.getTime();
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refSchool",Util.getActiveSchool()));
            c.add(Restrictions.between("startDate",startHour,endHour));
            c.addOrder(Order.asc("startDate"));
            list = c.list();
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }

    public List<Schedules> findSchedulesByWeek(Date scheduleDay) {
        List list=null;
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(scheduleDay);
        calendar.set(Calendar.DAY_OF_WEEK,calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY,0);
        Date startHour=calendar.getTime();

        calendar.add(Calendar.DAY_OF_WEEK, 7);
        Date endHour=calendar.getTime();
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refSchool",Util.getActiveSchool()));
            c.add(Restrictions.between("startDate",startHour,endHour));
            c.addOrder(Order.asc("startDate"));
            list = c.list();
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }


    public void onEventSelect(SelectEvent e) {
        event = (ScheduleEvent) e.getObject();
        selected = (Schedules)event.getData();
        log.info("EVENT : " + event);
        //log.info("EVENT DATA : " + selected.getRefLessonName().getNameTr());
    }

    public void onDateSelect(SelectEvent e) {
        Date date = (Date) e.getObject();
        event = new DefaultScheduleEvent("", date, Util.oneHourLater(date) );
        selected = new Schedules();
        selected.setRefLessonGroup(findLessonGroup(date));
        log.info("GROUPS : " + lessonGroups);
        if (selected.getRefLessonGroup()!=null) {
            log.info("SELECTED GROUP : " + selected.getRefLessonGroup().getNameTr());
        }
    }

    public void onViewChange(SelectEvent e) {
        //Date date = (Date) e.getObject();
        log.info("VIEW EVENT : " + e);
    }

    public void nextPrevious(ActionEvent event){
        String increment = (String) event.getComponent().getAttributes().get("increment");
        int moveValue = increment.equals("next")?1:-1;
        Calendar cal = Calendar.getInstance();
        UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot();
        Schedule component = (Schedule)view.findComponent("pageForm:schedule");
        cal.setTime((Date)component.getInitialDate());
        cal.add(Calendar.WEEK_OF_MONTH, moveValue);
        component.setInitialDate(cal.getTime());

        for (int i=0; i<4; i++) {
            lessonGroups.set(i,null);
        }
        List<Schedules> weekschedules=findSchedulesByWeek(cal.getTime());
        for (Schedules sched:weekschedules) {
            cal.setTime(sched.getStartDate());
            int hour=cal.get(Calendar.HOUR_OF_DAY);
            switch (hour) {
                case 0: case 1: case 2 :case 3:case 4 :case 5 : lessonGroups.set(0,sched.getRefLessonGroup());break;
                case 6: case 7: case 8 :case 9:case 10 :case 11 :lessonGroups.set(1,sched.getRefLessonGroup());break;
                case 12: case 13: case 14 :case 15:case 16 :case 17 : lessonGroups.set(2,sched.getRefLessonGroup());break;
                case 18: case 19: case 20 :case 21:case 22 :case 23 :case -1 : lessonGroups.set(3,sched.getRefLessonGroup());break;
            }
        }
    }

    public Date getNow() {
        return Util.getNow();
    }

    public Date getScheduleDate() {
        UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot();
        Schedule component = (Schedule)view.findComponent("pageForm:schedule");
        return (Date)component.getInitialDate();
    }

    public ScheduleDao() {
        super(Schedules.class);
    }

    public ScheduleModel getEventModel() {
        if (eventModel==null) {
            findEvents();
        }
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public Schedules getSelected() {
        return selected;
    }

    public void setSelected(Schedules selected) {
        this.selected = selected;
    }

    public ArrayList<LessonsGroup> getLessonGroups() {
        return lessonGroups;
    }

    public void setLessonGroups(ArrayList<LessonsGroup> lessonGroups) {
        this.lessonGroups = lessonGroups;
    }
}
