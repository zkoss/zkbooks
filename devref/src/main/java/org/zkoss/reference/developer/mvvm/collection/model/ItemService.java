package org.zkoss.reference.developer.mvvm.collection.model;

import java.util.LinkedList;
import java.util.List;

public class ItemService {

	private LinkedList<String> itemList = new LinkedList<String>();
	private static List<String> sItemList = new ItemService().getItemList();
	
	public ItemService(){
		for (int i = 0 ; i< 10 ; i++){
			itemList.add("Item "+i);
		}
	}
	
	public LinkedList<String> getItemList(){
		return itemList;
	}
	
	public List<String> getStaticItemList(){
		return sItemList;
	}
}
	
