package org.zkoss.reference.developer.mvvm.collection;

import java.util.List;

import org.zkoss.reference.developer.mvvm.collection.model.Item;
import org.zkoss.reference.developer.mvvm.collection.model.ItemService;

public class ItemsVM {

	private ItemService itemService = new ItemService();
	private List<Item> itemList = itemService.getAllItems();
	
	public List<Item> getItemList(){
		return itemList;
	}
	

	
	//for testing
	
	public Integer getNumber(){
		return 10;
	}
	
	public String getNumberString(){
		return "10";
	}
}
