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
import org.zkoss.reference.developer.hibernate.model.ReloadListModel;

/**
 * @author Hawk
 *
 */
public class RodViewModel {

	private OrderDao orderDao = new OrderDao(); 
	
	private List<Order> orders ;
	
	private ReloadListModel reloadOrderList;
	
	@Init
	public void init(){
		orders = orderDao.findAll();
		if (orders.size() < 100){
			//save large amount of orders
			for (int i =0 ; i < 100 ; i++){
				Order order = new Order(null, Order.PROCESSING, Calendar.getInstance().getTime(), "auto created "+i);
				int numOfItems = new Random().nextInt(5)+1;
				for (int n=0 ; n < numOfItems ; n++){
					OrderItem item = new OrderItem(null,  0, "auto item", n+10, n);
					order.getItems().add(item);
					item.setOrder(order);
				}
				orderDao.save(order);
			}
			orders = orderDao.findAll();
		}
		
		reloadOrderList = new ReloadListModel(orders);
	}
	
	
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}


	public ReloadListModel getReloadOrderList() {
		return reloadOrderList;
	}


	public void setReloadOrderList(ReloadListModel reloadOrderList) {
		this.reloadOrderList = reloadOrderList;
	}

}
