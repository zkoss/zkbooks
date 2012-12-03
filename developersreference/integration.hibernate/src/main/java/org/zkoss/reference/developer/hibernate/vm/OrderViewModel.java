package org.zkoss.reference.developer.hibernate.vm;

import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
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
		if (!orders.isEmpty()){
			setSelectedItem(orders.get(0));
		}
	}
	
	public Order getSelectedItem() {
		if (selectedItem!=null){
			selectedItem = orderDao.reload(selectedItem);
		}
		return selectedItem;
	}
	public void setSelectedItem(Order selectedItem) {
		this.selectedItem = selectedItem;
	}

	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Command @NotifyChange({"selectedItem","orders"})
	public void delete(){
		if (selectedItem!=null){
			orderDao.delete(selectedItem);
			selectedItem = null;
			orders = orderDao.findAll();
		}
	}
}
