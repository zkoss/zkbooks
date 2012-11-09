package org.zkoss.reference.developer.mvvm.collection;

import org.zkoss.reference.developer.mvvm.collection.model.MyTreeNode;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;

public class TreeVM {

	TreeModel<TreeNode<String>> itemTree;

	public TreeVM() {

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

	public TreeModel<TreeNode<String>> getItemTree() {
		return itemTree;
	}	
}
