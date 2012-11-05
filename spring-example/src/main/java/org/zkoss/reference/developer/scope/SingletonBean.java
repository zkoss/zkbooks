package org.zkoss.reference.developer.scope;

import org.springframework.stereotype.Component;

@Component
public class SingletonBean {
	private String value = "singleton bean value"
			+", hashcode:"+hashCode();

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
