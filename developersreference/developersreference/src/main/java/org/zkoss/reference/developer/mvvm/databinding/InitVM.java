package org.zkoss.reference.developer.mvvm.databinding;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Init;

public class InitVM {

	private String value="my-test-value";
	
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
}
