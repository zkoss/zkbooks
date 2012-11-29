package org.zkoss.reference.developer.hibernate.vm;

import java.util.Calendar;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.reference.developer.hibernate.dao.OrderDao;
import org.zkoss.reference.developer.hibernate.domain.Order;

/**
 * Test when a exception happens during DAO save an object, filter will perform rollback.
 * @author Hawk
 *
 */
public class RollbackViewModel {

	private OrderDao orderDao = new OrderDao(); 
	
	private List<Order> orders ;
	private boolean error= false;
	
	@Init
	public void init(){
//		orders = orderDao.findAll();
		orders = orderDao.findAllNewSession();
	}
	
	@Command @NotifyChange("orders")
	public void add(){
		Order newOrder = new Order();
		newOrder.setDescription("auto add "+Calendar.getInstance().getTime());
		if (error){
			orderDao.errorSave(newOrder);
		}else{
			orderDao.save(newOrder);
		}
		orders = orderDao.findAll();
	}
	
	@Command @NotifyChange("orders")
	public void noTransactionalAdd(){
		Order newOrder = new Order();
		newOrder.setDescription("auto add "+Calendar.getInstance().getTime());
		orderDao.saveInNewSession(newOrder);
//		orders = orderDao.findAll();
	}
	
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}
}
