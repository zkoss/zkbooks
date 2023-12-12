package org.zkoss.reference.component.data.tree;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.util.*;

public class TreeOperationComposer extends SelectorComposer {
    @Wire("tree")
    private Tree tree;
    @Wire
    private Textbox pathBox;
    @Wire
    private Intbox indexBox;
    @Wire
    private Textbox selectorBox;
    private DefaultTreeModel<DefaultTreeNode> model;
    private int maxNumber =100 ;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        List<DefaultTreeNode> childnodes = new LinkedList<DefaultTreeNode>();
        for (int i = 0; i < maxNumber; ++i) {
            DefaultTreeNode node = new DefaultTreeNode("" + i, createChildNodes(i + "-", 10));
            childnodes.add(node);
        }

        DefaultTreeNode root = new DefaultTreeNode("", childnodes);

        model = new DefaultTreeModel(root);
        tree.setModel(model);

    }

    List<DefaultTreeNode> createChildNodes(String data, int count) {
        List<DefaultTreeNode> childNodes = new ArrayList<DefaultTreeNode>(count);
        for (int i = 0; i < count; ++i) {
            DefaultTreeNode node = new DefaultTreeNode(data + i);
            childNodes.add(node);
        }
        return childNodes;
    }


    @Listen("onClick = #expandAll")
    public void expandTree() {
        //option 1
//        model.setOpenObjects(model.getRoot().getChildren());
        //option 2
        int[] path = {0};
        for (int i = 0 ; i< maxNumber ; i++) {
            path[0] = i;
            model.addOpenPath(path);
        }
    }

    @Listen("onClick = #expand")
    public void expandNode() {
        model.setOpenObjects(List.of(model.getChild(getTreePath())));
    }

    @Listen("onClick = #collapseAll")
    public void collapseTree() {
        model.setOpenObjects(Collections.emptyList());
    }

    @Listen(Events.ON_CLICK + "= #select")
    public void selectByPath() {
        int path[] = getTreePath();
        if (isMoreThan1Level(path)) { //need to open a node to select its child node
            model.setOpenObjects(List.of(model.getRoot().getChildAt(path[0])));
        }
        model.addToSelection(model.getChild(path));
    }

    private boolean isMoreThan1Level(int[] path) {
        return path.length > 1;
    }

    private int[] getTreePath() {
        return Arrays.stream(pathBox.getValue().split(",")).mapToInt(value -> Integer.parseInt(value)).toArray();
    }

    /* disable tree ROD to render all treeitems in a browser to make scrollIntoView work*/
    @Listen("onClick = #scroll")
    public void scrollIntoViewByComponent() {
        Clients.scrollIntoView(tree.getFellow(indexBox.getValue().toString()));
    }

    @Listen("onClick = #scrollSelector")
    public void scrollIntoViewBySelector() {
        Clients.scrollIntoView(selectorBox.getValue());
    }


}
