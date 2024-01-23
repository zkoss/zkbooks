package org.zkoss.mvvm.databinding.form;
/* Item.java

	Purpose:
		
	Description:
		
	History:
		2011/10/25 Created by Dennis Chen

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */


import java.util.Calendar;
import java.util.Date;

import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.NotifyChange;

/**
 * @author dennis
 * 
 */
public class Order {

	String id;
	String description;
	Date creationDate;
	Date shippingDate;
	double price;
	int quantity;

	public Order() {
	}	
	
	Order(String id, String description, double price, int quantity, Date creationDate) {
		super();
		this.id = id;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.creationDate = creationDate;
		// default shipping date is 3 day after.
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 3);
		shippingDate = cal.getTime();
	}

	public String getId() {
		return id;
	}

	@NotifyChange
	public void setId(String id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}
	@NotifyChange
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getShippingDate() {
		return shippingDate;
	}
	@NotifyChange
	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}

	public String getDescription() {
		return description;
	}

	@NotifyChange
	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	@NotifyChange
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * should return an Object instead of a primitive for form binding.
	 * if the proxy's getter is expected to return a primitive and a null is returned from the invoke() method of the MethodHandler. Since primitives can't be null, trying to auto-unbox a null would cause a NullPointerException.
	 */
	public Integer getQuantity() {
		return quantity;
	}

	@NotifyChange
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@DependsOn( { "price", "quantity" })
	public double getTotalPrice() {
		return price * quantity;
	}

}
