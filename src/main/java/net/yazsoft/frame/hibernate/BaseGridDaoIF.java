package net.yazsoft.frame.hibernate;

import org.primefaces.model.LazyDataModel;

/**
 * Created by fec on 2/9/15.
 */
public interface BaseGridDaoIF<T extends BaseEntity> {
    public LazyDataModel<T> getLazyModel();
    public void initLazyModel();
}
