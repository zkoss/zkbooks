package org.zkoss.reference.developer.mvvm.advance;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

public class CompositeVM {

	String name = "John";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	

	@Command
	@NotifyChange("name")
	public void reset() {
		name = "Lin";
	}

}
