package net.yazsoft.ors.lessonsName;

import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.LessonsName;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class LessonsNameLazy extends LazyDataModel<LessonsName>{
    @Inject
    LessonsNameDao lessonsNameDao;

    @Override
    public LessonsName getRowData(String rowKey) {
        return lessonsNameDao.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(LessonsName lessonsName) {
        return lessonsName.getTid();
    }

    @Override
    public List<LessonsName> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                              Map<String,Object> filters) {
        this.setRowCount(lessonsNameDao.rowCount());// TODO make safe cast
        return lessonsNameDao.load(first, pageSize, sortField, sortOrder, filters);
    }
}
