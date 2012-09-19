package org.zkoss.reference.developer.mvvm.collection;

import java.util.List;

import org.zkoss.reference.developer.mvvm.collection.model.Item;
import org.zkoss.reference.developer.mvvm.collection.model.ItemService;

public class ListboxViewModel {

	private ItemService itemService = new ItemService();
	

	
	public List<Item> getItemList(){
		return itemService.getAllItems();
	}
	
}
