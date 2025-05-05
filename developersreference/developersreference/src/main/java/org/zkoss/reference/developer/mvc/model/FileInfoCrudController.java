package org.zkoss.reference.developer.mvc.model;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.*;

import java.util.*;

/**
 * an example to demonstrate CRUD operation of a tree model (DefaultTreeModel, DefaultTreeNode)
 */
public class FileInfoCrudController extends SelectorComposer {
    @Wire
    private Tree tree;
    @Wire
    private Textbox pathBox;
    @Wire
    private Textbox descriptionBox;
    @Wire
    private Intbox index;

    private DefaultTreeModel<FileInfo> model = new DefaultTreeModel<FileInfo>(
            new DefaultTreeNode<FileInfo>(new FileInfo("root", "root folder"), new LinkedList()), true);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        model = new DefaultTreeModel(
                new DefaultTreeNode(null,
                        new DefaultTreeNode[]{
                                new DefaultTreeNode(new FileInfo("/doc", "Release and License Notes")),
                                new DefaultTreeNode(new FileInfo("/dist", "Distribution"),
                                        new DefaultTreeNode[]{
                                                new DefaultTreeNode(new FileInfo("/lib", "ZK Libraries"),
                                                        new DefaultTreeNode[]{
                                                                new DefaultTreeNode(new FileInfo("zcommon.jar", "ZK Common Library")),
                                                                new DefaultTreeNode(new FileInfo("zk.jar", "ZK Core Library"))
                                                        }),
                                                new DefaultTreeNode(new FileInfo("/src", "Source Code")),
                                                new DefaultTreeNode(new FileInfo("/xsd", "XSD Files"))
                                        })
                        }
                ));
        tree.setModel(model);
    }

    //register onClick event for creating new node into tree model
    @Listen(Events.ON_CLICK + "= #create")
    public void add() {
        String path = pathBox.getValue();
        String description = descriptionBox.getValue();
        if ("".equals(path)) {
            Notification.show("no new content to add");
            return;
        }

        // if no treeitem is selected, append child to root
        DefaultTreeNode selectedTreeNode = model.getSelection().isEmpty() ?
                (DefaultTreeNode) (tree.getModel()).getRoot() : (DefaultTreeNode) model.getSelection().iterator().next();
        Integer i = index.getValue();
        DefaultTreeNode newNode = new DefaultTreeNode(new FileInfo(path, description));
        if (i == null) // if no index specified, append to last.
            selectedTreeNode.add(newNode);
        else // if index specified, insert before the index number.
            selectedTreeNode.insert(newNode, i);

        resetInput();
    }

    private void resetInput() {
        pathBox.setValue("");
        descriptionBox.setValue("");
    }

    @Listen(Events.ON_CLICK + "= #update")
    public void update() {
        if (model.getSelection().isEmpty()) {
            Notification.show("select one item to update");
            return;
        }
        DefaultTreeNode selectedTreeNode = (DefaultTreeNode) model.getSelection().iterator().next();
        //get current FileInfo from selected tree node
        FileInfo fileInfo = (FileInfo) selectedTreeNode.getData();
        //set new value of current FileInfo
        fileInfo.setPath(pathBox.getValue());
        fileInfo.setDescription(descriptionBox.getValue());
        //set current FileInfo in the selected tree node
        selectedTreeNode.setData(fileInfo);
    }

    @Listen(Events.ON_CLICK + "=#delete")
    public void delete() {
        if (model.getSelection().isEmpty()) {
            Notification.show("select one item to delete");
            return;
        }
        ((DefaultTreeNode) model.getSelection().iterator().next()).removeFromParent();
    }

    @Listen(Events.ON_CLICK + "=#clear")
    public void clear() {
        model.clearSelection();
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
