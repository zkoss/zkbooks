package org.zkoss.reference.component.data.tree;

import org.zkoss.zul.DefaultTreeNode;

public class DirectoryTreeNode<T> extends DefaultTreeNode<T> {
	private static final long serialVersionUID = -8085873079938209759L;
	
	// Node Control the default open
	private boolean open = false;

	public DirectoryTreeNode(T data, DirectoryTreeNodeCollection<T> children, boolean open) {
		super(data, children);
		this.setOpen(open);
	}

	public DirectoryTreeNode(T data, DirectoryTreeNodeCollection<T> children) {
		super(data, children);
	}

	public DirectoryTreeNode(T data) {
		super(data);
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
}
