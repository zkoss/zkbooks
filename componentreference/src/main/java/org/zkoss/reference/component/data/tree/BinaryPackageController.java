package org.zkoss.reference.component.data.tree;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

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

	@Listen("onClick = #show")
	public void scrollIntoView(){
		treeModel.setOpenObjects(treeModel.getRoot().getChildren());
		treeModel.addToSelection(treeModel.getChild(path));
	}
}
