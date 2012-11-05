package org.zkoss.reference.developer.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class SessionBean {
	private String value = "session bean value"
			+", hashcode:"+hashCode();

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
