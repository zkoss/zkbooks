package org.zkoss.reference.developer.mvvm.collection;

import java.util.List;
import java.util.Set;

import org.zkoss.reference.developer.mvvm.collection.model.ItemService;

public class ListboxSelectionVM {

	private ItemService itemService = new ItemService();
	private int pickedIndex;
	private int pickedIndex2;
	private String pickedItem;
	private Set pickedItemSet;
	
	public List<String> getItemList(){
		return itemService.getItemList();
	}

	public Set getPickedItemSet() {
		return pickedItemSet;
	}

	public void setPickedItemSet(Set pickedItemSet) {
		this.pickedItemSet = pickedItemSet;
	}

	public int getPickedIndex() {
		return pickedIndex;
	}

	public void setPickedIndex(int pickedIndex) {
		this.pickedIndex = pickedIndex;
	}

	public String getPickedItem() {
		return pickedItem;
	}

	public void setPickedItem(String pickedItem) {
		this.pickedItem = pickedItem;
	}

	public int getPickedIndex2() {
		return pickedIndex2;
	}

	public void setPickedIndex2(int pickedIndex2) {
		this.pickedIndex2 = pickedIndex2;
	}
	
}
