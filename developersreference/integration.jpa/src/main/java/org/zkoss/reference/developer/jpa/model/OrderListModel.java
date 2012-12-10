package org.zkoss.reference.developer.jpa.model;

import java.util.LinkedList;
import java.util.List;

import org.zkoss.reference.developer.jpa.dao.SpringOrderDao;
import org.zkoss.reference.developer.jpa.domain.Order;
import org.zkoss.zul.AbstractListModel;

/**
 * Simple solution for lazy initialization issue under Render On Demand.
 * It's simple but has worse performance for redundant queries. 
 * @author Hawk
 *
 */
public class OrderListModel extends AbstractListModel<Order>{

	private static final long serialVersionUID = -7982684413905984053L;
	
	private SpringOrderDao orderDao;
	List<Order> orderList = new LinkedList<Order>();
	
	public OrderListModel(List<Order> orders,SpringOrderDao orderDao){
		this.orderList = orders;
		this.orderDao = orderDao;
	}
	
	@Override
	public Order getElementAt(int index) {
		//throw a runtime exception if orderDao does not find target object
		Order renewOrder = orderDao.reload(orderList.get(index));
		return renewOrder;
	}

	@Override
	public int getSize() {
		return orderList.size();
	}
}
