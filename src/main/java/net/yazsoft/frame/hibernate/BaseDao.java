package net.yazsoft.frame.hibernate;

import net.yazsoft.frame.utils.ReflectionUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class BaseDao<T extends BaseEntity> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	protected SessionFactory sessionFactory;

	final protected Class<T> type;
	
	public enum SortOrder {
	    ASCENDING,
	    DESCENDING,
	    UNSORTED;
	}
	

	/**
	 * Sets generic type class for this Dao. By setting this we will be able to
	 * call methods that use Criteria without explicit class parameters. <br>
	 * <br>
	 * Note that this method is not optional! You have to use this.
	 * 
	 * <br>
	 * <br>
	 * 
	 * <strong>If not used:</strong> <br>
	 * <code>
	 * 	session.createCriteria(clazz); // clazz should come as method parameter
	 * </code>
	 * <br>
	 * <br>
	 * 
	 * <strong>If used:</strong> <br>
	 * <code>
	 * 	session.createCriteria(this.type); // no need for clazz parameter in method
	 * </code>
	 */
//	@SuppressWarnings("unchecked")
//	public BaseDAO() {
//		this.type = (Class<T>) ((ParameterizedType) getClass()
//				.getGenericSuperclass()).getActualTypeArguments()[0];
//	}


	public BaseDao() {
		type=null;
	}
	public BaseDao(Class<T> type) {
		this.type = type;
	}

	public Long create(final T t) {
		final Session session = sessionFactory.getCurrentSession();
		return (Long) session.save(t);
	}

	@SuppressWarnings("unchecked")
	public T getById(final Long id) {
		final Session session = sessionFactory.getCurrentSession();
		return (T) session.get(type, id);
	}

	public T update(final T t) {
		final Session session = sessionFactory.getCurrentSession();
		final Calendar now = Calendar.getInstance();
		return (T) session.merge(t);
	}

	public T delete(final T t) {
		final Session session = sessionFactory.getCurrentSession();
		return (T) session.merge(t);
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
		final Session session = sessionFactory.getCurrentSession();
		session.delete(t);
	}
	
	protected Criteria getCriteria(){
		final Session session = sessionFactory.getCurrentSession();
		return session.createCriteria(type);
	}
	
	public List<T> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		
		Criteria c = getCriteria();
		c.add(Restrictions.eq("active", true));
		c.add(Restrictions.eq("isDeleted", false));
		
		// Add sorting if requested
		if (sortField != null && !sortField.trim().isEmpty()) {
            if (sortOrder == SortOrder.ASCENDING) {
                c.addOrder(Order.asc(sortField));
            } else if(sortOrder == SortOrder.DESCENDING) {
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

					if(clazz.equals(Long.class)){
						c.add(Restrictions.eq(entry.getKey(), Long.parseLong(entry.getValue().toString())));
					}
					else if(clazz.equals(String.class)){
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
		
		return c.list();
	}

	public long rowCount() {
		Criteria  c = getCriteria();
		c.add(Restrictions.eq("active", true));
		c.add(Restrictions.eq("isDeleted", false));
        c.setProjection(Projections.rowCount());
        
        List list = c.list();
        return (long) list.get(0);
	}
	
}
