package org.zkoss.reference.developer.cdi.scope;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped @Named
public class RequestBean {
	private String value = "Request bean value";

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
