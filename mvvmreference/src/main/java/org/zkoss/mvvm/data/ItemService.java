package org.zkoss.mvvm.data;

import java.util.*;

public class ItemService {
	private static ItemService instance = new ItemService();
	private static List<String> list = Collections.nCopies(3000, "item");

	private ItemService() {
	}

	public static ItemService getInstance() {
		return instance;
	}

	public List<String> getItems() {
		return list;
	}

	public void produceItems(int size) {
		list = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			list.add("item " + i);
		}
	}
}
