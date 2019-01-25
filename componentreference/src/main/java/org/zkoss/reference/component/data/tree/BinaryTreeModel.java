package org.zkoss.reference.component.data.tree;

import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.ext.TreeSelectableModel;

import java.util.ArrayList;

public class BinaryTreeModel<T> extends AbstractTreeModel<T> implements TreeSelectableModel {

	private static final long serialVersionUID = 7822729366554623684L;
	
	private ArrayList<T> _tree = null;

	/**
	 * Constructor
	 * 
	 * @param tree
	 *            the list is contained all data of nodes.
	 */
	public BinaryTreeModel(ArrayList<T> tree) {
		super(tree.get(0));
		_tree = tree;
	}

	// TreeModel //
	public T getChild(Object parent, int index) {
		int i = _tree.indexOf(parent) * 2 + 1 + index;
		if (i >= _tree.size())
			return null;
		else
			return _tree.get(i);
	}

	public int getChildCount(Object parent) {
		int count = 0;
		if (getChild(parent, 0) != null)
			count++;
		if (getChild(parent, 1) != null)
			count++;
		return count;
	}

	public boolean isLeaf(Object node) {
		return (getChildCount(node) == 0);
	}

	/**
	 * @since 5.0.6
	 * @see org.zkoss.zul.TreeModel#getIndexOfChild(Object,
	 *      Object)
	 */
	@Override
	public int getIndexOfChild(Object arg0, Object arg1) {
		return 0;
	}

}