package org.zkoss.reference.developer.hibernate.vm;

import java.util.List;

import org.zkoss.bind.annotation.Init;
import org.zkoss.reference.developer.hibernate.dao.SimpleOrderDao;
import org.zkoss.reference.developer.hibernate.domain.Order;

public class OrderViewModel {

	private SimpleOrderDao orderDao = new SimpleOrderDao();

	private List<Order> orders ;
	private Order selectedItem;
	
	@Init
	public void init(){
		orders = orderDao.findAll();
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public Order getSelectedItem() {
		return selectedItem;
	}
	public void setSelectedItem(Order selectedItem) {
		this.selectedItem = selectedItem;
	}
	
	

}
