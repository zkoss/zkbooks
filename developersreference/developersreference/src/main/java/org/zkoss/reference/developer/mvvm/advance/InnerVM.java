package org.zkoss.reference.developer.mvvm.advance;

import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;


public class InnerVM {

	private String typeFromOuter;
	
	@Init
	public void init(@ExecutionArgParam("type") String type){
		typeFromOuter = type;
	}

	public String getTypeFromOuter() {
		return typeFromOuter;
	}

	public void setTypeFromOuter(String typeFromOuter) {
		this.typeFromOuter = typeFromOuter;
	}
	

}
