package org.zkoss.reference.developer.spring.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
public class RequestBean {

	private String value = "request bean value"
			+", hashcode:"+hashCode();

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
