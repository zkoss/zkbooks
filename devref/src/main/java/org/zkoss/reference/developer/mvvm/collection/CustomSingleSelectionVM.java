package org.zkoss.reference.developer.mvvm.collection;

import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.reference.developer.mvvm.collection.model.Item;
import org.zkoss.reference.developer.mvvm.collection.model.ItemService;
 
public class CustomSingleSelectionVM {

	private ItemService itemService = new ItemService();
	private List<Item> itemList = itemService.getAllItems();
	private Item pickedItem = itemList.get(0);
	
	@NotifyChange("pickedItem")
	@Command
	public void select(@BindingParam("item") Item item){
		pickedItem = item;
	}
	
	public List<Item> getItemList(){
		return itemList;
	}

	public Item getPickedItem() {
		return pickedItem;
	}

	public void setPickedItem(Item pickedItem) {
		this.pickedItem = pickedItem;
	}
	
}
