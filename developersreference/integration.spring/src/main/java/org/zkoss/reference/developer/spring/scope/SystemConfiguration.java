package org.zkoss.reference.developer.spring.scope;

import org.springframework.stereotype.Component;

@Component //singleton by default
public class SystemConfiguration {
	private String value = "my configuration " + System.currentTimeMillis();

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
