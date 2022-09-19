package org.zkoss.reference.developer.performance;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

import java.util.*;

public class TreeComposer extends SelectorComposer {
    @Wire("tree")
    private Tree tree;
    private DefaultTreeModel<DefaultTreeNode> model;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        List<DefaultTreeNode> childnodes = new LinkedList<DefaultTreeNode>();
        for (int i = 0; i < 100; ++i) {
            DefaultTreeNode node = new DefaultTreeNode("" + i);
            childnodes.add(node);
        }
        childnodes.add(new DefaultTreeNode<>("this a a long node"));

        DefaultTreeNode root = new DefaultTreeNode("", childnodes);

        model = new DefaultTreeModel(root);
        tree.setModel(model);

    }



}
