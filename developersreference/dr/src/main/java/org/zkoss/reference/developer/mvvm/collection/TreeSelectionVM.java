package org.zkoss.reference.developer.mvvm.collection;

import java.util.Set;

import org.zkoss.bind.annotation.Command;
import org.zkoss.reference.developer.mvvm.collection.model.MyTreeNode;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.ext.Openable;

public class TreeSelectionVM {

	private TreeModel<TreeNode<String>> itemTree;
	private String pickedItem;
	private Set pickedSet;

	public TreeSelectionVM() {

		MyTreeNode root = new MyTreeNode("Root",
				new MyTreeNode[] {});

		String[] labs = new String[]{"A","B","C"};
		
		for (int i = 0; i < 3; i++) {
			MyTreeNode ni = new MyTreeNode(labs[i] + i,
					new MyTreeNode[] {});
			for (int j = 0; j < 3; j++) {
				MyTreeNode nj = new MyTreeNode(ni.getData()
						+ "-" + j, new MyTreeNode[] {});
				for (int k = 0; k < 2; k++) {
					MyTreeNode nk = new MyTreeNode(
							nj.getData() + "-" + k);
					nj.add(nk);
				}
				ni.add(nj);
			}
			root.add(ni);
		}

		itemTree = new DefaultTreeModel<String>(root);

	}

	@Command
	public void print(){
		System.out.println(((Openable)itemTree).getOpenObjects().toString());
	}
	
	@Command
	public void open(){
		TreeNode node = itemTree.getChild(itemTree.getRoot(), 0);
		((Openable)itemTree).addOpenObject(node);
	}
	public TreeModel<TreeNode<String>> getItemTree() {
		return itemTree;
	}


	public String getPickedItem() {
		return pickedItem;
	}

	public void setPickedItem(String pickedItem) {
		this.pickedItem = pickedItem;
	}

	public Set getPickedSet() {
		return pickedSet;
	}

	public void setPickedSet(Set pickedSet) {
		this.pickedSet = pickedSet;
	}	
}
