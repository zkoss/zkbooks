package org.zkoss.reference.developer.mvvm.advance;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

public class CompositeVM {

	private String name = "John";
	private String title = "My Title";
	
	
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
	
	@Command
	public void print(){
		System.out.println(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
