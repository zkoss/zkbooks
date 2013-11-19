/* Item.java

	Purpose:
		
	Description:
		
	History:
		2011/10/25 Created by Hawk Chen

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.start;

import java.text.DecimalFormat;


/**
 * @author Hawk
 * 
 */
public class Item {

	private int id;
	private String name;
	private String description;
	private double cost;
	private int quantity;

	private static DecimalFormat costFormatter = new DecimalFormat("$ ###,###,###,##0.00");
	
	public Item(int id, String name, String description, double cost, int quantity) {
		super();
		this.name = name;
		this.description = description;
		this.cost = cost;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getCost() {
		return cost;
	}

	public String getFormatedCost(){
		return costFormatter.format(cost);
	}
	public void setCost(double cost) {
		this.cost = cost;
	}

	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getTotalPrice(){
		return cost*quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
