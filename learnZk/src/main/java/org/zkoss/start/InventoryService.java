/* FakeSearchService.java

	Purpose:
		
	Description:
		
	History:
		2011/10/25 Created by Dennis Chen

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.start;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Hawk
 * 
 */
public class InventoryService {

	private static int nextId = 21;
			
	private List<Item> itemList = new LinkedList<Item>();

	private Random r = new Random(System.currentTimeMillis());
	
	public InventoryService() {
		itemList.add(new Item(1, "P001-A", "part 001, type A", nextPrice(), nextQuantity()));
		itemList.add(new Item(2, "P001-B", "part 001, type B", nextPrice(), nextQuantity()));
		itemList.add(new Item(3, "P001-C", "part 001, type C", nextPrice(), nextQuantity()));
		itemList.add(new Item(4, "P001-D", "part 001, type D", nextPrice(), nextQuantity()));
		itemList.add(new Item(5, "P001-E", "part 001, type E", nextPrice(), nextQuantity()));

		itemList.add(new Item(6, "P002-A", "part 002, type A", nextPrice(), nextQuantity()));
		itemList.add(new Item(7, "P002-B", "part 002, type B", nextPrice(), nextQuantity()));
		itemList.add(new Item(8, "P002-C", "part 002, type C", nextPrice(), nextQuantity()));
		itemList.add(new Item(9, "P002-D", "part 002, type D", nextPrice(), nextQuantity()));
		itemList.add(new Item(10, "P002-E", "part 002, type E", nextPrice(), nextQuantity()));

		itemList.add(new Item(11, "P003-A", "part 003, type A", nextPrice(), nextQuantity()));
		itemList.add(new Item(12, "P003-B", "part 003, type B", nextPrice(), nextQuantity()));
		itemList.add(new Item(13,"P003-C", "part 003, type C", nextPrice(), nextQuantity()));
		itemList.add(new Item(14, "P003-D", "part 003, type D", nextPrice(), nextQuantity()));
		itemList.add(new Item(15, "P003-E", "part 003, type E", nextPrice(), nextQuantity()));

		itemList.add(new Item(16, "P004-A", "part 004, type A", nextPrice(), nextQuantity()));
		itemList.add(new Item(17, "P004-B", "part 004, type B", nextPrice(), nextQuantity()));
		itemList.add(new Item(18, "P004-C", "part 004, type C", nextPrice(), nextQuantity()));
		itemList.add(new Item(19, "P004-D", "part 004, type D", nextPrice(), nextQuantity()));
		itemList.add(new Item(20, "P004-E", "part 004, type E", nextPrice(), nextQuantity()));
	}

	public List<Item> find(String fitler) {
		List<Item> items = new ArrayList<Item>();
		for (Item item : itemList) {
			if ((fitler == null || "*".equals(fitler))
					|| item.getName().indexOf(fitler) >= 0) {
				items.add(item);
			}
		}
		return items;
	}
	
	public void add(Item item){
		item.setId(nextId);
		itemList.add(item);
		nextId++;
	}


	private double nextPrice() {
		return r.nextDouble()*300;
	}
	
	private int nextQuantity() {
		return r.nextInt(10);
	}
}
