package org.zkoss.reference.developer.cdi.scope;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped @Named
public class SystemConfiguration implements Serializable{
	private String value = "system configuration value";

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
