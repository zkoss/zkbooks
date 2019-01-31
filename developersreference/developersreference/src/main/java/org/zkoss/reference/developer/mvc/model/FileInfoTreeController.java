package org.zkoss.reference.developer.mvc.model;

import org.zkoss.zul.Tree;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

public class FileInfoTreeController extends SelectorComposer {
    @Wire
    private Tree tree;

    @Override
    public void doAfterCompose(Component component) throws Exception {
        super.doAfterCompose(component);
        TreeModel model = new DefaultTreeModel(
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
        tree.setItemRenderer(new FileInfoRenderer());
    }
}
