package org.zkoss.reference.developer.responsive;

import org.zkoss.bind.annotation.*;
import org.zkoss.zul.ListModelList;

public class ProportionalColumnsVM extends  ResponsiveGrid{

	private String currentTemplate = "8cols";
	

	@MatchMedia("all and (min-width : 1024px)")
	@NotifyChange({"currentTemplate", "columnModel"})
	public void set8Col(){
		currentTemplate = "8cols";
	}

	@MatchMedia("all and (min-width : 800px) and (max-width : 1023px)")
	@NotifyChange({"currentTemplate", "columnModel"})
	public void set6Col(){
		currentTemplate = "6cols";
	}
	
	@MatchMedia("all and (min-width : 600px) and (max-width : 799px)")
	@NotifyChange({"currentTemplate", "columnModel"})
	public void set4Col(){
		currentTemplate = "4cols";
	}
	
	@MatchMedia("all and (max-width : 599px)")
	@NotifyChange({"currentTemplate", "columnModel"})
	public void set2Col(){
		currentTemplate = "2cols";
	}


	public String getCurrentTemplate() {
		return currentTemplate;
	}
	
}
