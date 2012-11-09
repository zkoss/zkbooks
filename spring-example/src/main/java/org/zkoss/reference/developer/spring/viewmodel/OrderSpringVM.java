/* OrderVM.java

	Purpose:
		
	Description:
		
	History:

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.reference.developer.spring.viewmodel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.reference.developer.spring.domain.Order;
import org.zkoss.reference.developer.spring.domain.OrderService;

/**
 * @author Hawk
 * 
 */
@Component("orderVm")
@Scope("prototype")
public class OrderSpringVM {

	//the order list
	List<Order> orders;
	
	@Autowired
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
