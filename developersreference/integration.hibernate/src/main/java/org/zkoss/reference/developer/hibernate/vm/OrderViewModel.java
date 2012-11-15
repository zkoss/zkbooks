package org.zkoss.reference.developer.hibernate.vm;

import java.util.List;

import org.zkoss.bind.annotation.Init;
import org.zkoss.reference.developer.hibernate.dao.OrderDao;
import org.zkoss.reference.developer.hibernate.domain.Order;

/**
 * 
 * @author Hawk
 *
 */
public class OrderViewModel {

	private OrderDao orderDao = new OrderDao();

	private List<Order> orders ;
	private Order selectedItem;
	
	@Init
	public void init(){
		orders = orderDao.findAll();
		setSelectedItem(orders.get(0));
	}
	
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public Order getSelectedItem() {
		selectedItem = orderDao.load(selectedItem); 
		return selectedItem;
	}
	public void setSelectedItem(Order selectedItem) {
		this.selectedItem = selectedItem;
	}
	
}
