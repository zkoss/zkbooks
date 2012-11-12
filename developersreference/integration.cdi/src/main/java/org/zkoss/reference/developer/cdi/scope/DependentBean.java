package org.zkoss.reference.developer.cdi.scope;

import javax.inject.Named;

@Named
public class DependentBean {
	private String value = "Dependent bean value";

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public int getHashCode(){
		return hashCode();
	}
}
