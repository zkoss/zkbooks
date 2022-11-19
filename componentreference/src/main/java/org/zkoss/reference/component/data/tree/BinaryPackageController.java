package org.zkoss.reference.component.data.tree;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.util.*;

public class BinaryPackageController extends SelectorComposer<Component> {

	@Wire
	private Tree tree;
	private DefaultTreeModel<PackageData> treeModel;

	int[] path = {1,1}; //target node to scroll into view
	private static final long serialVersionUID = 43014628867656917L;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		treeModel = new DefaultTreeModel<PackageData>(PackageDataUtil.getRoot());
		tree.setModel(treeModel);
	}

	/**
	 * To scroll a tree item into view, the item's each ancestor should be open.
	 * select the item also make it scroll into the view.
	 */
	@Listen("onClick = #selectModel")
	public void selectModel(){
		treeModel.setOpenObjects(List.of(treeModel.getRoot().getChildren().get(path[0])));
		treeModel.addToSelection(treeModel.getChild(path));
	}

	/**
	 * To scroll a tree item into view, the item's each ancestor should be open.
	 * select the item also make it scroll into the view.
	 */
	@Listen("onClick = #selectTree")
	public void select(){
		treeModel.setOpenObjects(treeModel.getRoot().getChildren());
		Treeitem[] items = tree.getItems().toArray(new Treeitem[0]);
		tree.setSelectedItem(items[items.length - 1]);
	}

	/**
	 * To scroll a tree item into view, the item's each ancestor should be open first.
	 */
	@Listen("onClick = #scroll")
	public void scrollIntoView() {
		treeModel.setOpenObjects(treeModel.getRoot().getChildren());
		Treeitem[] items = tree.getItems().toArray(new Treeitem[0]);
		Clients.scrollIntoView(items[items.length - 1]); //last item is /WEB-INF
	}
}
