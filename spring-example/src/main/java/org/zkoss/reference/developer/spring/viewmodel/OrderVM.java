/* OrderVM.java

	Purpose:
		
	Description:
		
	History:

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.reference.developer.spring.viewmodel;

import java.util.List;

import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.reference.developer.spring.domain.Order;
import org.zkoss.reference.developer.spring.domain.OrderService;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * @author Hawk
 * 
 */
public class OrderVM {

	//the order list
	List<Order> orders;
	
	@WireVariable
	OrderService orderService;
	
	//the selected order
	Order selected;
	
	String confirmMessage;
	
	public List<Order> getOrders() {
		if (orders == null) {
			//init the list
			orders = orderService.list();
		}
		return orders;
	}

	public Order getSelected() {
		return selected;
	}

	@NotifyChange("selected")
	public void setSelected(Order selected) {
		this.selected = selected;
	}
		
}
