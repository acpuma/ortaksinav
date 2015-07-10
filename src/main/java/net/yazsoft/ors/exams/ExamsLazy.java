package net.yazsoft.ors.exams;

import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Exams;
import net.yazsoft.ors.entities.Schools;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class ExamsLazy extends LazyDataModel<Exams> {
    private static final Logger logger = Logger.getLogger(ExamsLazy.class);

    @Inject ExamsDao examsDao;

    @Override
    public Exams getRowData(String rowKey) {
        return examsDao.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(Exams row) {
        return row.getTid();
    }

    @Override
    public List<Exams> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                              Map<String,Object> filters) {
        this.setRowCount(examsDao.rowCount());// TODO make safe cast
        return examsDao.load(first, pageSize, sortField, sortOrder, filters);
    }

    @Override
    public List<Exams> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String,Object> filters) {
        this.setRowCount(examsDao.rowCount());
        multiSortMeta=new ArrayList<>();
        SortMeta sortMeta=new SortMeta();
        sortMeta.setSortField("date");
        sortMeta.setSortOrder(SortOrder.DESCENDING);
        multiSortMeta.add(sortMeta);
        /*
        sortMeta.setSortField("refExamYear");
        sortMeta.setSortOrder(SortOrder.DESCENDING);
        multiSortMeta.add(sortMeta);
        sortMeta=new SortMeta();
        sortMeta.setSortField("refExamSeason");
        sortMeta.setSortOrder(SortOrder.DESCENDING);
        multiSortMeta.add(sortMeta);
        sortMeta=new SortMeta();
        sortMeta.setSortField("refExamSeasonNumber");
        sortMeta.setSortOrder(SortOrder.DESCENDING);
        multiSortMeta.add(sortMeta);
        */
        /*
        if (!filters.containsKey("refSchool")) {
            filters.put("refSchool", Util.getActiveSchool().getTid());
        }
        */
        for (Schools school:Util.getActiveUser().getSchoolsCollection()) {
            filters.put("refSchool",school);
        }

        if (examsDao.getFilterSchool()!=null) {
            filters.put("refSchool",examsDao.getFilterSchool());
        }
        if (examsDao.getFilterYear()!=null) {
            filters.put("refExamYear",examsDao.getFilterYear());
        }
        if (examsDao.getFilterSeason()!=null) {
            filters.put("refExamSeason",examsDao.getFilterSeason());
        }
        if (examsDao.getFilterSeasonNumber()!=null) {
            filters.put("refExamSeasonNumber",examsDao.getFilterSeasonNumber());
        }
        logger.info("EXAMS FILTERS : " + filters);
        //filters.clear();
        return examsDao.load(first, pageSize, multiSortMeta, filters);
    }
}
