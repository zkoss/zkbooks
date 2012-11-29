package org.zkoss.reference.developer.hibernate.vm;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.reference.developer.hibernate.dao.OrderDao;
import org.zkoss.reference.developer.hibernate.domain.Order;
import org.zkoss.reference.developer.hibernate.domain.OrderItem;
import org.zkoss.reference.developer.hibernate.model.OrderListModel;

/**
 * @author Hawk
 *
 */
public class RodViewModel {

	private OrderDao orderDao = new OrderDao(); 
	
	private List<Order> orders ;
	
	private OrderListModel reloadOrderList;
	
	@Init
	public void init(){
		orders = orderDao.findAll();
		reloadOrderList = new OrderListModel(orders, orderDao);
	}
	
	
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}


	public OrderListModel getReloadOrderList() {
		return reloadOrderList;
	}


	public void setReloadOrderList(OrderListModel reloadOrderList) {
		this.reloadOrderList = reloadOrderList;
	}

}
