package net.yazsoft.ors.exams;

import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.Exams;
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
    @Inject
    ExamsDao examsDao;

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
        return examsDao.load(first, pageSize, multiSortMeta, filters);
    }
}
