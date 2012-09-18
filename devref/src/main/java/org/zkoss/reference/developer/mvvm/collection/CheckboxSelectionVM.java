package org.zkoss.reference.developer.mvvm.collection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.reference.developer.mvvm.collection.model.Item;
import org.zkoss.reference.developer.mvvm.collection.model.ItemService;
import org.zkoss.zk.ui.event.CheckEvent;

public class CheckboxSelectionVM {

	private ItemService itemService = new ItemService();
	private Set<Item> pickedItemSet = new HashSet<Item>();

	private int pickedIndex;
	private int pickedIndex2;
	private String pickedItem;
	
	public List<Item> getItemList(){
		return itemService.getAllItems();
	}

	@Command
	@NotifyChange("pickedItemSet")
	public void pick(@ContextParam(ContextType.TRIGGER_EVENT) CheckEvent checkEvent, @BindingParam("picked") Item item){
		if (checkEvent.isChecked()){
			pickedItemSet.add(item);
		}else{
			pickedItemSet.remove(item);
		}
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

	public Set getPickedItemSet() {
		return pickedItemSet;
	}

	public void setPickedItemSet(Set pickedItemSet) {
		this.pickedItemSet = pickedItemSet;
	}
	
}
