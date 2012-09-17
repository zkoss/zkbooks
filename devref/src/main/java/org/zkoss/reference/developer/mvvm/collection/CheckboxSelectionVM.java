package org.zkoss.reference.developer.mvvm.collection;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.reference.developer.mvvm.collection.model.Item;
import org.zkoss.reference.developer.mvvm.collection.model.ItemService;
import org.zkoss.zk.ui.event.CheckEvent;

public class CheckboxSelectionVM {

	private ItemService itemService = new ItemService();
	private int pickedIndex;
	private int pickedIndex2;
	private String pickedItem;
	private Set pickedItemSet = new TreeSet<String>();
	
	public List<Item> getItemList(){
		return itemService.getAllItems();
	}

	@Command
	public void pick(@ContextParam(ContextType.TRIGGER_EVENT) CheckEvent checkEvent, @BindingParam("index") int index, @BindingParam("picked") String item){
		if (checkEvent.isChecked()){
			//add
		}else{
			//remove
		}
		System.out.println(index+","+item);
		
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
