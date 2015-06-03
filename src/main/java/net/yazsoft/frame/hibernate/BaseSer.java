package net.yazsoft.frame.hibernate;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Repository
public class BaseSer<T extends BaseEntity> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	protected BaseDao<T> baseDao;

    public Long create(final T t) {
		if (t == null) {
			throw new RuntimeException("Model cannot be null");
		}
		return baseDao.create(t);
	}

	public T getById(final Long id) {
		if (id == null) {
			throw new RuntimeException("Id cannot be null");
		}
		return baseDao.getById(id);
	}

	public T update(final T t) {
		if (t == null) {
			throw new RuntimeException("Model cannot be null");
		}
		return baseDao.update(t);
	}

	public void delete(final T t) {
		if (t == null) {
			throw new RuntimeException("Model cannot be null");
		}
		baseDao.delete(t);
	}

	public List<T> getAll() {
		return baseDao.getAll();
	}

	public void hardDelete(final T t) {
		if (t == null) {
			throw new RuntimeException("Model cannot be null");
		}
		baseDao.delete(t);
	}
	
	public List<T> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		return this.baseDao.load(first, pageSize, sortField, sortOrder, filters);
	}
	
	public long rowCount(){
		return this.baseDao.rowCount();
	}

}
