package org.zkoss.reference.component.data.tree;

import java.util.AbstractList;

public class BigList extends AbstractList<Integer> {

	private int size;

	public BigList(int sz) {
		if (sz < 0)
			throw new IllegalArgumentException("Negative not allowed: " + sz);
		size = sz;
	}

	public int size() {
		return size;
	}

	public Integer get(int j) {
		return Integer.valueOf(j);
	}

}