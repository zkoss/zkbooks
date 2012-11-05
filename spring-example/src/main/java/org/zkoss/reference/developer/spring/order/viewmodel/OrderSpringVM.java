/* OrderVM.java

	Purpose:
		
	Description:
		
	History:

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.reference.developer.spring.order.viewmodel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.reference.developer.spring.order.domain.Order;
import org.zkoss.reference.developer.spring.order.domain.OrderService;

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

	//action command
	@NotifyChange({"selected","orders"})
	@Command
	public void newOrder(){
		Order order = new Order();
		getOrders().add(order);
		selected = order;//select the new one
	}
	
	@NotifyChange("selected")
	@Command
	public void saveOrder(){
		orderService.save(selected);
	}
	
	@NotifyChange({"selected","orders","confirmMessage"})
	@Command
	public void deleteOrder(){
		orderService.delete(selected);//delete selected
		orders = orderService.list();//refresh
		selected = null; //clean the selected
		confirmMessage = null;
	}


	public String getConfirmMessage(){
		return confirmMessage;
	}
	
	@NotifyChange("confirmMessage")
	@Command
	public void confirmDelete(){
		//set the message to show to user
		confirmMessage = "Do you want to delete "+selected.getId()+" ?";
	}
	
	
	@NotifyChange("confirmMessage")
	@Command
	public void cancelDelete(){
		//clear the message
		confirmMessage = null; 
	}
		
}
