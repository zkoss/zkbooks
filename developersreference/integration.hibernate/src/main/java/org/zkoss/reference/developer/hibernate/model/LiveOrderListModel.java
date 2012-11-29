package org.zkoss.reference.developer.hibernate.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.reference.developer.hibernate.dao.OrderDao;
import org.zkoss.reference.developer.hibernate.domain.Order;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.AbstractListModel;

/**
 * 
 * @author Hawk
 *
 */
public class LiveOrderListModel extends AbstractListModel<Order>{

	private static final long serialVersionUID = -7982684413905984053L;
	
	private OrderDao orderDao;
	
	private int pageSize = 30;
	private final String CACHE_KEY= "cache";
	
	public LiveOrderListModel(OrderDao orderDao){
		this.orderDao = orderDao;
	}

	/**
	 * query one page size of entity for one execution a time. 
	 */
	@Override
	public Order getElementAt(int index) {
		Map<Integer, Order> cache = getCache();
		if (!cache.containsKey(index)){
			//if cache doesn't contain target object, query a page starting from the index
			List<Order> pageResult = orderDao.findAll(index, pageSize);
			int indexKey = index;
			for (Order o : pageResult ){
				cache.put(indexKey, o);
				indexKey++;
			}
		}
		return cache.get(index);
	}

	private Map<Integer, Order> getCache(){
		Execution execution = Executions.getCurrent();
		//we only reuse this cache in one execution to avoid accessing detached objects.
		//our filter open a session during a HTTP request
		Map<Integer, Order> cache = (Map)execution.getAttribute(CACHE_KEY);
		if (cache == null){
			cache = new HashMap<Integer, Order>();
			execution.setAttribute(CACHE_KEY, cache);
		}
		return cache;
	}
	
	@Override
	public int getSize() {
		return orderDao.findAllSize().intValue();
	}
}
