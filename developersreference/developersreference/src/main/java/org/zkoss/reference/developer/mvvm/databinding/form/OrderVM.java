package org.zkoss.reference.developer.mvvm.databinding.form;
/* OrderVM.java

	Purpose:
		
	Description:
		
	History:
		2011/10/31 Created by Dennis Chen

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */


import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

/**
 * @author dennis
 * 
 */
public class OrderVM {

	//the order list
	ListModelList<Order> orders;
	
	//the selected order
	Order selected;

	public ListModelList<Order> getOrders() {
		if (orders == null) {
			//init the list
			orders = new ListModelList<Order>(getService().list());
		}
		return orders;
	}

	public Order getSelected() {
		return selected;
	}

	@NotifyChange({"selected","validationMessages"})
	public void setSelected(Order selected) {
		this.selected = selected;
		validationMessages.clear();//clear when another order selected
	}

	//action command
	
	@NotifyChange({"selected","orders","validationMessages"})
	@Command
	public void newOrder(){
		Order order = new Order();
		getOrders().add(order);
		selected = order;//select the new one
		validationMessages.clear();//clear message
	}
	
	@NotifyChange({"selected","validationMessages"})
	@Command
	public void saveOrder(){
		getService().save(selected);
		validationMessages.clear();//clear message
	}
	
	
	@NotifyChange({"selected","orders","validationMessages"})
	@Command
	public void deleteOrder(){
		getService().delete(selected);//delete selected
		getOrders().remove(selected);
		selected = null; //clean the selected
		validationMessages.clear();//clear message
	}
	
	//validation messages
	Map<String, String> validationMessages = new HashMap<String,String>();
	
	public Map<String,String> getValidationMessages(){
		return validationMessages;
	}
	

	public OrderService getService() {
		return FakeOrderService.getInstance();
	}
	
	//validators for prompt
	public Validator getPriceValidator(){
		return new Validator(){
			public void validate(ValidationContext ctx) {
				Double price = (Double)ctx.getProperty().getValue();
				if(price==null || price<=0){
					ctx.setInvalid(); // mark invalid
					validationMessages.put("price", "must large than 0");
				}else{
					validationMessages.remove("price");
				}
				//notify messages was changed.
				ctx.getBindContext().getBinder().notifyChange(validationMessages, "price");
			}
		};
	}
	
	public Validator getQuantityValidator(){
		return new Validator(){
			public void validate(ValidationContext ctx) {
				Integer quantity = (Integer)ctx.getProperty().getValue();
				if(quantity==null || quantity<=0){
					ctx.setInvalid();// mark invalid
					validationMessages.put("quantity", "must large than 0");
				}else{
					validationMessages.remove("quantity");
				}
				//notify messages was changed.
				ctx.getBindContext().getBinder().notifyChange(validationMessages, "quantity");
			}
		};
	}
}
