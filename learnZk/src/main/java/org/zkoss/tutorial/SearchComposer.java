package org.zkoss.tutorial;


import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.google.api.services.customsearch.model.Result;

@SuppressWarnings("serial")
public class SearchComposer extends SelectorComposer<Window> {


	@Wire("#keywordBox")
	private Textbox keywordBox;
//	@Wire("#result")
//	private Label resultLabel;
	@Wire("#resultGrid")
	private Grid resultGrid;
	
	private CustomSearchService searchService = new CustomSearchService();
	
	@Listen("onClick = #searchButton")
	public void search(){
		resultGrid.setModel(new SimpleListModel<Result>(searchService.search(keywordBox.getValue())));
		
	}
}
