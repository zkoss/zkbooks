package org.zkoss.reference.developer.mvvm.collection.model;

import java.util.LinkedList;

public class ItemService {

	private LinkedList<Item> itemList = new LinkedList<Item>();
	private static int DEFAULT_SIZE = 10;
	private int id = 0; //last global id 
	
	public ItemService(int maxSize){
		for (int i = 0 ; i< maxSize ; i++){
			itemList.add(new Item(id++, "Item "+i));
		}
	}
	
	public ItemService(){
		this(DEFAULT_SIZE);
	}
	
	public LinkedList<Item> getAllItems(){
		return itemList;
	}
	
	public void add(Item item){
		item.setId(id++);
		itemList.add(item);
	}
	
	public void delete(Item item){
		int index = itemList.indexOf(item);
		if (index != -1){
			itemList.remove(index);
		}
	}
}
	
