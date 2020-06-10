package org.zkoss.mvvm.collection;

import java.util.List;

import org.zkoss.mvvm.collection.model.Item;
import org.zkoss.mvvm.collection.model.ItemService;
import org.zkoss.zul.ListModelList;

public class ItemsVM {

	private ItemService itemService = new ItemService();
	private List<Item> itemList = new ListModelList<>(itemService.getAllItems());
	
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
