package org.zkoss.reference.developer.mvc.model;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.util.LinkedList;

public class RodController extends SelectorComposer {
    @Wire
    private Tree tree;
    boolean initialized = false;

    @Override
    public void doAfterCompose(Component component) throws Exception {
        super.doAfterCompose(component);
        DefaultTreeNode<String> root = new DefaultTreeNode<String>("root", new LinkedList<>());
        TreeModel model = new DefaultTreeModel(root);
        tree.setModel(model);

        appendChild(root);
        initialized = true;
    }

    private void appendChild(DefaultTreeNode<String> root) {
        int numChild = 60;
        for (int i = 0; i < numChild; i++ ){
            root.add(new DefaultTreeNode<String>(root.getData() + "." + i));
        }
    }
}
