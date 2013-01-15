package org.zkoss.reference.developer.mvvm.databinding;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Init;

public class InitVM {

	private String value="my-test-value";
	private int price= 14;
	
	@Init
	public void init(@BindingParam("arg1") String arg1){
		System.out.println("arg1: "+arg1);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}
