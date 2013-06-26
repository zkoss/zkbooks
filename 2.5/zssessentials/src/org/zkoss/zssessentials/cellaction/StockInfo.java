/* StockInfo.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Oct 8, 2007 9:34:48 AM     2007, Created by Dennis.Chen
}}IS_NOTE

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zssessentials.cellaction;

/**
 * @author Dennis.Chen
 *
 */
public class StockInfo {

	String label;
	double prviousPrice;
	double currentPrice;
	double ratio;
	
	
	
	public StockInfo(String label, double price) {
		this(label,price,price);
	}
	
	public StockInfo(String label, double currentPrice, double prviousPrice) {
		super();
		this.label = label;
		this.currentPrice = currentPrice;
		this.prviousPrice = prviousPrice;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public double getPrviousPrice() {
		return prviousPrice;
	}
	public void setPrviousPrice(double prviousPrice) {
		this.prviousPrice = prviousPrice;
	}
	public double getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
}
