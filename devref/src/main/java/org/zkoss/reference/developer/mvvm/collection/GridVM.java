package org.zkoss.reference.developer.mvvm.collection;

import java.util.List;

import org.zkoss.reference.developer.mvvm.collection.model.ItemService;

public class GridVM {

	private ItemService itemService = new ItemService();
	

	
	public List<String> getItemList(){
		return itemService.getItemList();
	}
	
	//for testing
	public List<String> getStaticItemList(){
		
		return itemService.getStaticItemList();
	}
	
	public Integer getNumber(){
		return 10;
	}
	
	public String getNumberString(){
		return "10";
	}
}
