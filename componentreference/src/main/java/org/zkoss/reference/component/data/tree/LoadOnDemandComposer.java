package org.zkoss.reference.component.data.tree;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.util.ArrayList;

public class LoadOnDemandComposer extends SelectorComposer {
    @Wire
    private Tree tree;

    private TreeModel treeModel = new org.zkoss.reference.component.data.tree.BinaryTreeModel(
            new ArrayList(
                    new org.zkoss.reference.component.data.tree.BigList(1000)));

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        tree.setModel(treeModel);
    }
}
