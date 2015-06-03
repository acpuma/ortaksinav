package net.yazsoft.ors.examsParameters;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.ExamsParameters;
import net.yazsoft.ors.entities.ExamsParametersType;
import net.yazsoft.ors.exams.ExamsDao;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class ExamsParametersDao extends BaseGridDao<ExamsParameters> implements Serializable{
    private static final Logger logger = Logger.getLogger(ExamsParametersDao.class);
    ExamsParameters selected;

    List<ExamsParametersDto> parametersDtos;
    List<ExamsParametersDto> deletedDtos;

    List<ExamsParameters> parameters;

    Exams exam;

    public ExamsParametersDao() {
        super(ExamsParameters.class);
    }

    @PostConstruct
    public void init() {
        parametersDtos=new ArrayList<>();
        deletedDtos=new ArrayList<>();
        parameters=new ArrayList<>();
        getItem().setRefParameter(new ExamsParametersType());
        getItem().setRefExam(new Exams());
    }

    @Override
    public Long create() {
        try {
            logger.info("CREATE PARAMETERS: " + parametersDtos);
            logger.info("CREATE EXAM : " + exam);
            getItem().setActive(Boolean.TRUE);
            DtosToEntities(exam);
            reset();
        } catch (Exception e) {
            logger.error("EXCEPTION", e);
            Util.setFacesMessage("HATA: " +e.getMessage());
        }
        return null;
    }


    public void DtosToEntities(Exams exam) {
        logger.info("DtosToEntities Exam:" + exam);
        ExamsParameters entity;
        for (ExamsParametersDto dto:parametersDtos) {
            entity = new ExamsParameters();
            logger.info("parameter tid : " +dto.getTid());
            if (dto.getTid()!=null) {
                entity=(ExamsParameters)getSession().get(ExamsParameters.class,dto.getTid());
            }
            entity.setRefExam(exam);
            entity.setRefParameter(dto.getRefParameter());
            entity.setStart(dto.getStart());
            entity.setLength(dto.getLength());
            entity.setDirection(dto.getDirection());
            entity.setActive(Boolean.TRUE);
            saveOrUpdate(entity);
        }
        deleteEntites();
    }

    public void addParameter() {
        logger.info("PAREMETERS :" + parametersDtos);
        int maxrank=0;
        if (parametersDtos==null) {
            parametersDtos = new ArrayList<ExamsParametersDto>();
        }
        ExamsParametersDto newdto=new ExamsParametersDto();
        parametersDtos.add(newdto);
        logger.info("LESSONS :" + parametersDtos);
    }

    public void delete(ExamsParametersDto deleteDto) {
        logger.info("REMOVING : " + deleteDto);
        parametersDtos.remove(deleteDto);
        if (deleteDto.getTid()!=null) {
            deletedDtos.add(deleteDto);
        }
        //Util.setFacesMessage(deleteDto+" SILINDI");
    }

    public void deleteEntites() {
        ExamsParameters entity;
        for (ExamsParametersDto dto:deletedDtos) {
            entity = new ExamsParameters();
            entity=(ExamsParameters)getSession().get(ExamsParameters.class,dto.getTid());
            delete(entity);
        }
    }


    public void onRowEdit(RowEditEvent event) {
        try {

            logger.info("EDITING ROW...");
            ExamsParametersDto dto=(ExamsParametersDto) event.getObject();
            logger.info("LOG00080:" + dto.getStart());
            DataTable table = (DataTable) event.getSource();
            dto = (ExamsParametersDto) table.getRowData();
            logger.info("LOG00080:" + dto.getStart());
            logger.info("LOG00100:SELECTED " + dto.getStart());

            //super.update(lesson);
            //Util.setFacesMessage("Edited :" + lesson+", " +lesson.getQuestionCount());
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
    }

    public void onRowCancel(RowEditEvent event) {
        //Util.setFacesMessage("Edit Cancelled : " + ((ExamsParametersDto) event.getObject()).getTid());
    }

    public List<ExamsParameters> getExamParameters(Exams exam) {
        logger.info("EXAM : " + exam);
        List list = null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refExam", exam));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            c.addOrder(Order.asc("start"));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        List<ExamsParameters> parameters = list;
        for (ExamsParameters parameter : parameters) {
            ExamsParametersDto dto = new ExamsParametersDto();
            dto.setTid(parameter.getTid());
            dto.setRefExam(parameter.getRefExam());
            dto.setRefParameter(parameter.getRefParameter());
            dto.setStart(parameter.getStart());
            dto.setLength(parameter.getLength());
            parametersDtos.add(dto);
        }
        logger.info("PARAMETERS :" + parametersDtos);
        return list;
    }

    public ExamsParameters getSelected() {
        return selected;
    }

    public void setSelected(ExamsParameters selected) {
        this.selected = selected;
    }

    public List<ExamsParametersDto> getParametersDtos() {
        return parametersDtos;
    }

    public void setParametersDtos(List<ExamsParametersDto> parametersDtos) {
        this.parametersDtos = parametersDtos;
    }

    public List<ExamsParameters> getParameters() {
        return parameters;
    }

    public void setParameters(List<ExamsParameters> parameters) {
        this.parameters = parameters;
    }

    public Exams getExam() {
        return exam;
    }

    public void setExam(Exams exam) {
        logger.info("SETTING EXAM : " + exam);
        if (exam!=null) {
            init();
            getExamParameters(exam);
        }
        this.exam = exam;
    }
}
