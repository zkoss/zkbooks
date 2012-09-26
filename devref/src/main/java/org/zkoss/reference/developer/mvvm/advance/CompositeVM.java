package org.zkoss.reference.developer.mvvm.advance;

import org.zkoss.bind.annotation.Command;

public class CompositeVM {

	private String title = "My Title";
	
	
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
