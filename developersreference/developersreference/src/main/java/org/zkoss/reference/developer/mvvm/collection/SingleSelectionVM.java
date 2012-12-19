package org.zkoss.reference.developer.mvvm.collection;

import java.util.List;
import java.util.Set;

import org.zkoss.reference.developer.mvvm.collection.model.Item;
import org.zkoss.reference.developer.mvvm.collection.model.ItemService;

public class SingleSelectionVM {

	protected ItemService itemService = new ItemService();
	protected List<Item> itemList = itemService.getAllItems();
	protected int pickedIndex;
	protected Item pickedItem;
	
	private Set pickedItemSet;
	private int pickedIndex2;
	
	public List<Item> getItemList(){
		return itemList;
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

	//for multiple selections
	public int getPickedIndex2() {
		return pickedIndex2;
	}

	public void setPickedIndex2(int pickedIndex2) {
		this.pickedIndex2 = pickedIndex2;
	}

	public Set getPickedItemSet() {
		return pickedItemSet;
	}
	
	public void setPickedItemSet(Set pickedItemSet) {
		this.pickedItemSet = pickedItemSet;
	}
	
}
