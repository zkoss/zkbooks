package org.zkoss.reference.developer.mvvm.collection.model;

import org.zkoss.zul.DefaultTreeNode;

public class MyTreeNode extends DefaultTreeNode<String> {

	private static final long serialVersionUID = 1L;
	MyTreeNodeData data = new MyTreeNodeData();

	public MyTreeNode(String data, MyTreeNode[] children) {
		super(data,children);
	}
	public MyTreeNode(String data) {
		super(data);
	}

	public boolean isOpen() {
		return data.open;
	}

	public void setOpen(boolean open) {
		this.data.open = open;
	}
	
//	public boolean isStartA(){
//		return getData().startsWith("A");
//	}

}