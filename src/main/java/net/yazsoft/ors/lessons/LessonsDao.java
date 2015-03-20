package net.yazsoft.ors.lessons;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.Lessons;
import net.yazsoft.ors.entities.LessonsName;
import net.yazsoft.ors.exams.ExamsDao;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Transactional
public class LessonsDao extends BaseGridDao<Lessons> implements Serializable{
    private static final Logger logger = Logger.getLogger(LessonsDao.class);
    Lessons selected;
    LessonsDto selectedDto;

    List<LessonsDto> lessonsDtos;
    List<LessonsDto> deletedDtos;

    Integer sumQuestionCount;

    @PostConstruct
    public void init() {
        //exam=new Exams(1L);
        //getExamLessons();
        lessonsDtos=new ArrayList<LessonsDto>();
        deletedDtos=new ArrayList<LessonsDto>();
        getItem().setRefExam(new Exams());
        getItem().setRefLessonName(new LessonsName());
    }

    public void DtosToEntities(Exams exam) {
        Lessons entity;
        for (LessonsDto dto:lessonsDtos) {
            entity = new Lessons();
            logger.info("lesson tid : " +dto.getTid());
            if (dto.getTid()!=null) {
                entity=(Lessons)getSession().get(Lessons.class,dto.getTid());
            }
            entity.setRefLessonName(dto.getRefLessonName());
            entity.setQuestionCount(dto.getQuestionCount());
            entity.setRank(dto.getRank());
            entity.setRefExam(exam);
            entity.setActive(Boolean.TRUE);
            entity.setStart(dto.getStart());
            saveOrUpdate(entity);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            logger.info("EDITING ROW...");
            LessonsDto lesson=(LessonsDto) event.getObject();
            logger.info("LOG00080:" + lesson.getQuestionCount());
            DataTable table = (DataTable) event.getSource();
            lesson = (LessonsDto) table.getRowData();
            logger.info("LOG00080:" + lesson.getQuestionCount());
            logger.info("LOG00100:SELECTED " + lesson.getQuestionCount());
            //super.update(lesson);
            //Util.setFacesMessage("Edited :" + lesson+", " +lesson.getQuestionCount());
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
    }

    public void addLesson() {
        logger.info("LESSONS :" + lessonsDtos);
        int maxrank=0;
        if (lessonsDtos==null) {
            lessonsDtos=new ArrayList<LessonsDto>();
        } else {
            ArrayList<Integer> numbers;
            for (LessonsDto dto:lessonsDtos) {
                if (maxrank<dto.getRank())
                maxrank=dto.getRank();
            }
        }
        LessonsDto newdto=new LessonsDto();
        newdto.setRank(maxrank+1);
        lessonsDtos.add(newdto);
        logger.info("LESSONS :" + lessonsDtos);
    }

    public void delete(LessonsDto deleteDto) {
        logger.info("REMOVING : " + deleteDto);
        lessonsDtos.remove(deleteDto);
        if (deleteDto.getTid()!=null) {
            deletedDtos.add(deleteDto);
        }
        //Util.setFacesMessage(deleteDto+" SILINDI");
    }

    public void deleteEntites() {
        Lessons entity;
        for (LessonsDto dto:deletedDtos) {
            entity = new Lessons();
            entity=(Lessons)getSession().get(Lessons.class,dto.getTid());
        }
    }

    public List<Lessons> getExamLessons(Exams exam) {
        logger.info("EXAM : "+ exam);
        List list=null;

        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refExam", exam));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        List<Lessons> lessons=list;
        for (Lessons lesson:lessons) {
            LessonsDto dto=new LessonsDto();
            dto.setTid(lesson.getTid());
            dto.setRank(lesson.getRank());
            dto.setQuestionCount(lesson.getQuestionCount());
            dto.setRefLessonName(lesson.getRefLessonName());
            dto.setRefExam(lesson.getRefExam());
            if (lesson.getStart()!=null) {
                dto.setStart(lesson.getStart());
            }
            lessonsDtos.add(dto);
        }
        logger.info("LESSONS :" + lessonsDtos);
        return list;
    }


    public void onRowCancel(RowEditEvent event) {
        //Util.setFacesMessage("Edit Cancelled : " + ((LessonsDto) event.getObject()).getTid());
    }

    public LessonsDao() {
        super(Lessons.class);
    }

    public Lessons getSelected() {
        return selected;
    }

    public void setSelected(Lessons selected) {
        this.selected = selected;
    }

    public List<LessonsDto> getLessonsDtos() {
        return lessonsDtos;
    }

    public void setLessonsDtos(List<LessonsDto> lessonsDtos) {
        this.lessonsDtos = lessonsDtos;
    }

    public LessonsDto getSelectedDto() {
        return selectedDto;
    }

    public void setSelectedDto(LessonsDto selectedDto) {
        this.selectedDto = selectedDto;
    }

    public List<LessonsDto> getDeletedDtos() {
        return deletedDtos;
    }

    public void setDeletedDtos(List<LessonsDto> deletedDtos) {
        this.deletedDtos = deletedDtos;
    }

    public Integer getSumQuestionCount() {
        Integer sum=0;
        for (LessonsDto dto:lessonsDtos) {
            sum=sum+dto.getQuestionCount();
        }
        return sum;
    }


}
