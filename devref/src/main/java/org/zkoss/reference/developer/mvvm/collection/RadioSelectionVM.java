package org.zkoss.reference.developer.mvvm.collection;

import java.util.List;
import java.util.Set;

import org.zkoss.reference.developer.mvvm.collection.model.Item;
import org.zkoss.reference.developer.mvvm.collection.model.ItemService;

public class RadioSelectionVM {

	private ItemService itemService = new ItemService();
	private int pickedIndex;
	private int pickedIndex2;
	private Item pickedItem = itemService.getAllItems().get(1);
	private String pickedItemName = "Item 1";
	private Set<Item> pickedItemSet;
	
	public List<Item> getItemList(){
		return itemService.getAllItems();
	}

	public Set<Item> getPickedItemSet() {
		return pickedItemSet;
	}

	public void setPickedItemSet(Set<Item> pickedItemSet) {
		this.pickedItemSet = pickedItemSet;
	}

	public int getPickedIndex() {
		return pickedIndex;
	}

	public void setPickedIndex(int pickedIndex) {
		this.pickedIndex = pickedIndex;
	}

	public Item getPickedItem() {
		return pickedItem;
	}

	public void setPickedItem(Item pickedItem) {
		this.pickedItem = pickedItem;
	}

	public int getPickedIndex2() {
		return pickedIndex2;
	}

	public void setPickedIndex2(int pickedIndex2) {
		this.pickedIndex2 = pickedIndex2;
	}

	public String getPickedItemName() {
		return pickedItemName;
	}

	public void setPickedItemName(String pickedItemName) {
		this.pickedItemName = pickedItemName;
	}
	
}
