package org.zkoss.reference.developer.hibernate.vm;

import org.zkoss.reference.developer.hibernate.domain.Order;

/**
 * 
 * @author Hawk
 *
 */
public class RollbackViewModel {

	private Order newOrder;
	

	public Order getNewOrder() {
		return newOrder;
	}

	public void setNewOrder(Order newOrder) {
		this.newOrder = newOrder;
	}
	
	
	

}
