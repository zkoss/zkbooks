package org.zkoss.reference.developer.hibernate.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.zkoss.reference.developer.hibernate.dao.OrderDao;
import org.zkoss.reference.developer.hibernate.domain.Order;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.AbstractListModel;

/**
 * Advanced implementation for lazy initialization issue under ROD.
 * It queries from a database one page size a time when elements are requested, and
 * store the query result as a cache in an Execution attribute to avoid returning
 * detached objects.
 * 
 * @author Hawk
 *
 */
public class LiveOrderListModel extends AbstractListModel<Order>{

	private static final long serialVersionUID = -7982684413905984053L;
	
	private OrderDao orderDao;
	private Integer totalSize;
	private int pageSize = 30;
	private static final String CACHE_KEY= LiveOrderListModel.class+"_cache";
	
	public LiveOrderListModel(OrderDao orderDao){
		this.orderDao = orderDao;
	}

	/**
	 * query one page size of entity for one execution a time. 
	 */
	@Override
	public Order getElementAt(int index) {
		Map<Integer, Order> cache = getCache();

		Order targetOrder = cache.get(index);
		if (targetOrder == null){
			//if cache doesn't contain target object, query a page starting from the index
			List<Order> pageResult = orderDao.findAll(index, pageSize);
			int indexKey = index;
			for (Order o : pageResult ){
				cache.put(indexKey, o);
				indexKey++;
			}
		}else{
			return targetOrder;
		}

		//get the target after query from database
		targetOrder = cache.get(index);
		if (targetOrder == null){
			//if we still cannot find the target object from database, there is inconsistency in the database
			throw new HibernateException("Element at index "+index+" cannot be found in the database.");
		}else{
			return targetOrder;
		}
	}

	private Map<Integer, Order> getCache(){
		Execution execution = Executions.getCurrent();
		//we only reuse this cache in one execution to avoid accessing detached objects.
		//our filter opens a session during a HTTP request
		Map<Integer, Order> cache = (Map)execution.getAttribute(CACHE_KEY);
		if (cache == null){
			cache = new HashMap<Integer, Order>();
			execution.setAttribute(CACHE_KEY, cache);
		}
		return cache;
	}
	
	@Override
	public int getSize() {
		if (totalSize == null){
			totalSize = orderDao.findAllSize().intValue();
		}
		return totalSize; 
	}
}
