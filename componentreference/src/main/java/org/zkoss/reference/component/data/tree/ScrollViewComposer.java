package org.zkoss.reference.component.data.tree;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.util.*;

public class ScrollViewComposer extends SelectorComposer {
    @Wire("tree")
    private Tree tree;
    private DefaultTreeModel<DefaultTreeNode> model;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        List<DefaultTreeNode> childnodes = new LinkedList<DefaultTreeNode>();
        for (int i = 0; i < 100; ++i) {
            DefaultTreeNode node = new DefaultTreeNode("" + i, createChildNodes(i+"-", 10));
            childnodes.add(node);
        }

        DefaultTreeNode root = new DefaultTreeNode("", childnodes);

        model = new DefaultTreeModel(root);
        tree.setModel(model);

    }

    List<DefaultTreeNode> createChildNodes(String data, int count){
        List<DefaultTreeNode> childNodes = new ArrayList<DefaultTreeNode>(count);
        for (int i = 0; i < count; ++i) {
            DefaultTreeNode node = new DefaultTreeNode(data + i);
            childNodes.add(node);
        }
        return childNodes;
    }


    @Listen("onClick = #expand")
    public void expandTree() {
        model.setOpenObjects(model.getRoot().getChildren());
    }

    @Listen("onClick = #collapse")
    public void collapseTree() {
        model.setOpenObjects(Collections.emptyList());
    }
}
