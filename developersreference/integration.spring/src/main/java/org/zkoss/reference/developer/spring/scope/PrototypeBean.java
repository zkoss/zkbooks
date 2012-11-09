package org.zkoss.reference.developer.spring.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PrototypeBean {

	private String value = "prototype bean value"
			+", hashcode:"+hashCode();

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
