package net.yazsoft.ors.examsParameters;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.ExamsParametersType;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ExamsParametersTypeDao extends BaseGridDao<ExamsParametersType> implements Serializable{
    private static final Logger logger = Logger.getLogger(ExamsParametersTypeDao.class);
    ExamsParametersType selected;
    List<ExamsParametersType> parametersTypes;

    public ExamsParametersTypeDao() {
        super(ExamsParametersType.class);
    }

    public List<ExamsParametersType> getDefaultParameters() {
        List list = null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("showDefault", true));
            c.add(Restrictions.eq("active", true));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        List<ExamsParametersType> parameters = list;

        logger.info("DEFAULT PARAMETERS :" + parameters);
        return list;
    }

    public void onRowSelect(SelectEvent event) {
        selected=(ExamsParametersType) event.getObject();
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            ExamsParametersType parameter=(ExamsParametersType) event.getObject();
            DataTable table = (DataTable) event.getSource();
            parameter = (ExamsParametersType) table.getRowData();
            super.update(parameter);
            //Util.setFacesMessage("Edited :" + lesson+", " +lesson.getQuestionCount());
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
    }

    public void onRowCancel(RowEditEvent event) {
        //Util.setFacesMessage("Edit Cancelled : " + ((LessonsDto) event.getObject()).getTid());
    }

    public void addParameterType() {
        getItem().setActive(Boolean.TRUE);
        super.save();
        //getLessonSubjects(lessonsName); //reload
    }

    public void deleteParameterType(ExamsParametersType parameter) {
        try {
            super.delete(parameter);
            //getLessonSubjects(lessonsName); //reload
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
    }


    public ExamsParametersType getSelected() {
        return selected;
    }

    public void setSelected(ExamsParametersType selected) {
        this.selected = selected;
    }

    public List<ExamsParametersType> getParametersTypes() {
        return parametersTypes;
    }

    public void setParametersTypes(List<ExamsParametersType> parametersTypes) {
        this.parametersTypes = parametersTypes;
    }
}
