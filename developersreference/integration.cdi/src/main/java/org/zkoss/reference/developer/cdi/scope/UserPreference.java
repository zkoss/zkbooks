package org.zkoss.reference.developer.cdi.scope;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@SessionScoped @Named
public class UserPreference implements Serializable{
	private String value = "preference setting";

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
