package org.zkoss.reference.developer.jpa.vm;

import java.util.List;

import org.zkoss.bind.annotation.Init;
import org.zkoss.reference.developer.jpa.dao.SpringOrderDao;
import org.zkoss.reference.developer.jpa.domain.Order;
import org.zkoss.reference.developer.jpa.model.LiveOrderListModel;
import org.zkoss.reference.developer.jpa.model.OrderListModel;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * @author Hawk
 *
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class RodViewModel {

	@WireVariable
	private SpringOrderDao springOrderDao; 
	
	private List<Order> orders ;
	
	private OrderListModel orderListModel;
	
	private LiveOrderListModel liveOrderListModel;
	
	@Init
	public void init(){
		orders = springOrderDao.queryAll();
		orderListModel = new OrderListModel(orders, springOrderDao);
		liveOrderListModel = new LiveOrderListModel(springOrderDao);
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
