package org.zkoss.reference.developer.mvvm.collection;

import java.util.Calendar;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.reference.developer.mvvm.collection.model.Item;
import org.zkoss.reference.developer.mvvm.collection.model.ItemService;
import org.zkoss.zul.ListModelList;

public class ModelTypeVM extends SingleSelectionVM{

	private ListModelList<Item> itemListModel;
	
	@Init
	public void init(){
		itemService = new ItemService(100);
		itemList = itemService.getAllItems();
	}
	
	@Command @NotifyChange({"pickedItem","itemList"})
	public void add(){
		itemList.add(pickedItem);
	}
	
	@Command  @NotifyChange({"pickedItem","itemList"})
	public void delete(){
		int index = itemList.indexOf(pickedItem);
		if (index != -1){
			itemList.remove(index);
			pickedItem = new Item();
		}
		
	}
	
	@Command @NotifyChange("pickedItem")
	public void update(){
		//directly modify target object's content, no need more actions 
	}
	
	@Command @NotifyChange("pickedItem")
	public void reset(){
		pickedItem = new Item();
	}
	
	
}
