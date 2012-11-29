package org.zkoss.reference.developer.hibernate.vm;

import java.util.List;

import org.zkoss.bind.annotation.Init;
import org.zkoss.reference.developer.hibernate.dao.OrderDao;
import org.zkoss.reference.developer.hibernate.domain.Order;
import org.zkoss.reference.developer.hibernate.model.LiveOrderListModel;
import org.zkoss.reference.developer.hibernate.model.OrderListModel;

/**
 * @author Hawk
 *
 */
public class RodViewModel {

	private OrderDao orderDao = new OrderDao(); 
	
	private List<Order> orders ;
	
	private OrderListModel orderListModel;
	
	private LiveOrderListModel liveOrderListModel;
	
	@Init
	public void init(){
		orders = orderDao.findAll();
		orderListModel = new OrderListModel(orders, orderDao);
		liveOrderListModel = new LiveOrderListModel(orderDao);
	}
	
	
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}


	public OrderListModel getOrderListModel() {
		return orderListModel;
	}


	public void setOrderListModel(OrderListModel orderListModel) {
		this.orderListModel = orderListModel;
	}


	public LiveOrderListModel getLiveOrderListModel() {
		return liveOrderListModel;
	}


	public void setLiveOrderListModel(LiveOrderListModel liveOrderListModel) {
		this.liveOrderListModel = liveOrderListModel;
	}

}
