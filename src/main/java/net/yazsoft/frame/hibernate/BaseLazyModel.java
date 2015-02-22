package net.yazsoft.frame.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public  class BaseLazyModel<T extends BaseEntity>   extends LazyDataModel<T> implements Serializable{
    private static final Logger logger= Logger.getLogger(BaseLazyModel.class);

    @Autowired
    private SessionFactory sessionFactory;
    protected Class<T> type;

    public BaseLazyModel() {
    }

    public BaseLazyModel(Class<T> type) {
        this.type = type;
    }

    @Override
    public T getRowData(String string) {
        List<T> cars = (List<T>) getWrappedData();
        for (T car : cars) {
            if ((car.hashCode() + "").equals(string)) {
                return car;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(T t) {
        return t.hashCode();
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField,
                        SortOrder sortOrder, Map<String, Object> filters) {
        setRowCount(count(type));
        return getAll(first, pageSize, sortField, sortOrder, filters);
    }

    private Session getSession(SessionFactory sessionFactory) {
        return sessionFactory.getCurrentSession();
    }

    @Transactional
    public Integer count(Class type) {
        Long messageCount;
        try {
            Criteria query = getSession(sessionFactory).createCriteria(type);
            query.add(Restrictions.eq("active", 1));
            query.setProjection(Projections.rowCount());
            List list = query.list();
            messageCount=(Long) list.get(0);
        } catch (HibernateException ex) {
            logger.error("COUNT ERROR ", ex);
            throw ex;
        }
        return messageCount.intValue();
    }

    @Transactional
    public List<T> getAll(int first, int pageSize, String sortField, SortOrder sortOrder,
                          Map<String, Object> filters) {
        Session session = null;
        List list=null;
        try {
            session = getSession(sessionFactory);
            Criteria criteria = session.createCriteria(type);
            if (sortField != null && !sortField.isEmpty()) {
                if (sortOrder.name().equals("ASCENDING")) {
                    criteria.addOrder(Order.asc(sortField));
                } else {
                    criteria.addOrder(Order.desc(sortField));
                }
            }
            if (!filters.isEmpty()) {
                Iterator<Map.Entry<String, Object>> iterator = filters.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();
                    criteria = criteria.add(Restrictions.like(entry.getKey(),
                            (String) entry.getValue(), MatchMode.START));
                }
            }
            criteria.setMaxResults(pageSize);
            criteria.setFirstResult(first);
            criteria.add(Restrictions.eq("active", 1));
            list= criteria.list();
        } catch (HibernateException ex) {
            logger.error("GETALL HATA ", ex);
            throw ex;
        }
        return list;
    }

}
