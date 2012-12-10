package org.zkoss.reference.developer.hibernate.model;

import java.util.LinkedList;
import java.util.List;

import org.zkoss.reference.developer.hibernate.dao.OrderDao;
import org.zkoss.reference.developer.hibernate.domain.Order;
import org.zkoss.zul.AbstractListModel;

/**
 * Simple solution for lazy initialization issue under Render On Demand.
 * It's simple but has worse performance for redundant queries. 
 * @author Hawk
 *
 */
public class OrderListModel extends AbstractListModel<Order>{

	private static final long serialVersionUID = -7982684413905984053L;
	
	private OrderDao orderDao;
	List<Order> orderList = new LinkedList<Order>();
	
	public OrderListModel(List<Order> orders,OrderDao orderDao){
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
