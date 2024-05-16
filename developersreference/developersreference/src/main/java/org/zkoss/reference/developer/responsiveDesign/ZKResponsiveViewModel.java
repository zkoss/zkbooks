package org.zkoss.reference.developer.responsiveDesign;

import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.MatchMedia;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

public class ZKResponsiveViewModel {

	//UI template name
	private String layoutState = "bigLayout";
	//Selection model
	private ListModelList<String> selectionModel;
	
	@Init
	public void init(){
		//initializing the selection model
		selectionModel = new ListModelList<String>();
		selectionModel.add("choice 1");
		selectionModel.add("choice 2");
		selectionModel.add("choice 3");
	}

	@NotifyChange("layoutState")
	@MatchMedia("all and (min-width: 1200px)")
	public void handleBigLayout(){
		layoutState = "bigLayout";
	}
	
	@NotifyChange("layoutState")
	@MatchMedia("all and (max-width: 1199px)")
	public void handleSmallLayout(){
		layoutState = "smallLayout";
	}

	public String getLayoutState() {
		return layoutState;
	}

	public ListModelList<String> getSelectionModel() {
		return selectionModel;
	}
	
}
