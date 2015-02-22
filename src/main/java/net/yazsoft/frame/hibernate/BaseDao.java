package net.yazsoft.frame.hibernate;

import net.yazsoft.frame.utils.ReflectionUtil;
import net.yazsoft.frame.utils.Util;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Named
@Transactional
public class BaseDao<T extends BaseEntity> implements Serializable {
    private static final Logger logger = Logger.getLogger(BaseDao.class.getName());

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionFactory sessionFactory;

    Session session;

	protected Class<T> type;

    public BaseDao() {
    }

    public BaseDao(Class<T> type) {
		this.type = type;
	}

	public Long create(final T t) {
		return (Long) getSession().save(t);
	}

	@SuppressWarnings("unchecked")
	public T getById(final Long id) {
		return (T) getSession().get(type, id);
	}

	public T update(final T t) {
		final Calendar now = Calendar.getInstance();
		return (T) getSession().merge(t);
	}

	public void delete(final T t) {
        getSession().delete(t);
	}

	/**
	 * Gets all hibernate with active column true by default
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		final Criteria c = getCriteria();
		c.add(Restrictions.eq("active", true));
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return c.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getListById(final Long id ) {
		final Criteria c = getCriteria();
		c.add(Restrictions.eq("active", true));
		c.add(Restrictions.eq("tid", id));
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return c.list();
	}

	public void hardDelete(final T t) {
		getSession().delete(t);
	}
	
	protected Criteria getCriteria(){
		return getSession().createCriteria(type);
	}

    public List<T> getAll(int first, int pageSize, String sortField,
                        SortOrder sortOrder, Map<String, Object> filters) {
        return load(first,  pageSize,  sortField,
                 sortOrder,  filters);
    }

	public List<T> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
        logger.info("BASEDAO LOAD");
		Criteria c=null;
        List<T> list=null;
        try {
            c = getCriteria();
            getSession().flush();
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));

            // Add sorting if requested
            if (sortField != null && !sortField.trim().isEmpty()) {
                if (sortOrder == SortOrder.ASCENDING) {
                    c.addOrder(Order.asc(sortField));
                } else if (sortOrder == SortOrder.DESCENDING) {
                    c.addOrder(Order.desc(sortField));
                }
            }

            // Add filtering if requested
            if (!filters.isEmpty()) {
                Iterator<Map.Entry<String, Object>> iterator = filters.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();

                    try {

                        Field field = ReflectionUtil.getField(type, entry.getKey());
                        Class<?> clazz = field.getType();

                        if (clazz.equals(Long.class)) {
                            c.add(Restrictions.eq(entry.getKey(), Long.parseLong(entry.getValue().toString())));
                        } else if (clazz.equals(String.class)) {
                            c.add(Restrictions.like(entry.getKey(), entry.getValue().toString(), MatchMode.START));
                        }

                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }


            // Add paging
            c.setMaxResults(pageSize).setFirstResult(first);
            list=c.list();
            for(T item:list) {
                getSession().update(item);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
		
		return list;
	}

	public int rowCount() {
        Integer countInt=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            c.setProjection(Projections.rowCount());

            List list = c.list();
            Long count = (Long) list.get(0);
            countInt=count.intValue();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return countInt;
	}

    public Session getSession() {
        Session session1=sessionFactory.getCurrentSession();
        logger.info(session1.isOpen());
        session=session1;
        return session1;
    }

}
