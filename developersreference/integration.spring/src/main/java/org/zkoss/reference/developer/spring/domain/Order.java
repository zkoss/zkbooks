/* Item.java

	Purpose:
		
	Description:
		
	History:
		2011/10/25 Created by Dennis Chen

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.reference.developer.spring.domain;

import java.util.Date;

import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.NotifyChange;

/**
 * @author Hawk
 */
public class Order {

	Integer id;
	String description;
	Date creationDate;
	Date shippingDate;
	double price;
	int quantity;
	
	public Order(Integer id,String description, double price, int quantity,Date creationDate,Date shippingDate){
		this.id= id;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.creationDate = creationDate;
		this.shippingDate = shippingDate;
	}

	public Integer getId() {
		return id;
	}

	@NotifyChange
	public void setId(Integer id) {
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

	public double getPrice() {
		return price;
	}

	@NotifyChange
	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	@NotifyChange
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@DependsOn({ "price", "quantity" })
	public double getTotalPrice() {
		return price * quantity;
	}

}
