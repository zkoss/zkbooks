package org.zkoss.reference.developer.spring.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class UserPreference {
	private String value = "user preference value";

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
