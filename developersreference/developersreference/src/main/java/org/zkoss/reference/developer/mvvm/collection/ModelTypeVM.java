package org.zkoss.reference.developer.mvvm.collection;

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
		pickedItem = new Item();
		itemService = new ItemService(100);
		itemList = itemService.getAllItems();  //return a java.util.List
		itemListModel = new ListModelList<Item>(itemService.getAllItems());
	}
	
	@Command @NotifyChange({"itemList","pickedItem"})
	public void add(){
		itemList.add(pickedItem);
		pickedItem = new Item();
	}
	
	@Command  @NotifyChange({"itemList","pickedItem"})
	public void delete(){
		int index = itemList.indexOf(pickedItem);
		if (index != -1){
			itemList.remove(index);
			pickedItem = new Item();
		}
		
	}
	
	@Command @NotifyChange("pickedItem")
	public void update(){
		//data binding directly modifies target object's property in the item list  
	}
	
	@Command @NotifyChange("pickedItem")
	public void reset(){
		pickedItem = new Item();
	}

	public ListModelList<Item> getItemListModel() {
		return itemListModel;
	}

	public void setItemListModel(ListModelList<Item> itemListModel) {
		this.itemListModel = itemListModel;
	}
	
	@Command @NotifyChange("pickedItem")
	public void modelAdd(){
		itemListModel.add(pickedItem);
		pickedItem = new Item();
	}
	
//	@NotifyChange({"pickedItem","itemListModel"}) this cause reload and render whole model
	@NotifyChange("pickedItem")
	@Command  
	public void modelDelete(){
		int index = itemListModel.indexOf(pickedItem);
		if (index != -1){
			itemListModel.remove(index);
			pickedItem = new Item();
		}
		
	}	
	
	@Command @NotifyChange("pickedItem")
	public void modelUpdate(){
		update();
	}
	
	@Command @NotifyChange("pickedItem")
	public void modelReset(){
		reset();
	}	
}
