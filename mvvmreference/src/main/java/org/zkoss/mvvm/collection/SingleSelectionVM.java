package org.zkoss.mvvm.collection;

import java.util.List;
import java.util.Set;

import org.zkoss.bind.annotation.Command;
import org.zkoss.mvvm.collection.model.Item;
import org.zkoss.mvvm.collection.model.ItemService;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.ListModelList;

public class SingleSelectionVM {

	protected ItemService itemService = new ItemService();
	protected List<Item> itemList = itemService.getAllItems();
	protected ListModelList<Item> modelList = new ListModelList<>(itemList);
	protected int pickedIndex;
	protected Item pickedItem;
	
	private Set pickedItemSet;
	private int pickedIndex2;

	@Command
	public void showSelection(){
		Notification.show(modelList.getSelection().size() == 0 ? "nothing selected" : modelList.getSelection().iterator().next().toString());
	}

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

	public ListModelList<Item> getModelList() {
		return modelList;
	}
}
