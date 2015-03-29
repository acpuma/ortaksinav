package net.yazsoft.ors.lessonsGroup;

import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.LessonsGroup;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class LessonsGroupLazy extends LazyDataModel<LessonsGroup>{
    @Inject
    LessonsGroupDao lessonsGroupDao;

    @Override
    public LessonsGroup getRowData(String rowKey) {
        return lessonsGroupDao.getById(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(LessonsGroup lessonsGroup) {
        return lessonsGroup.getTid();
    }

    @Override
    public List<LessonsGroup> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                              Map<String,Object> filters) {
        this.setRowCount(lessonsGroupDao.rowCount());// TODO make safe cast
        return lessonsGroupDao.load(first, pageSize, sortField, sortOrder, filters);
    }

}
