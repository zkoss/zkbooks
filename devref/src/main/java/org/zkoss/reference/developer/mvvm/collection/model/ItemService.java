package org.zkoss.reference.developer.mvvm.collection.model;

import java.util.LinkedList;
import java.util.List;

public class ItemService {

	private LinkedList<Item> itemList = new LinkedList<Item>();
	private static List<Item> sItemList = new ItemService().getAllItems();
	private static int DEFAULT_SIZE = 10;
	
	public ItemService(int maxSize){
		for (int i = 0 ; i< maxSize ; i++){
			itemList.add(new Item("Item "+i));
		}
	}
	
	public ItemService(){
		this(DEFAULT_SIZE);
	}
	
	public LinkedList<Item> getAllItems(){
		return itemList;
	}
	
	public List<Item> getStaticItemList(){
		return sItemList;
	}
}
	
