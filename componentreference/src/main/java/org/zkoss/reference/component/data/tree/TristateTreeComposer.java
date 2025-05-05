package org.zkoss.reference.component.data.tree;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.*;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.DefaultTristateTreeModel;
import org.zkoss.zul.*;

import java.util.*;

public class TristateTreeComposer extends SelectorComposer<Component> {

    @Wire
    private Tree tree;
    @Wire
    private Label selection;

    private DefaultTristateTreeModel<NodeData> model;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initTreeModel();
        tree.setModel(model);
        tree.setItemRenderer(new TristateItemRenderer());

        tree.addEventListener(Events.ON_SELECT, event -> {
            updateSelectionLabel();
        });
        updateSelectionLabel();
    }

    private void updateSelectionLabel() {
        StringBuilder sBuilder = new StringBuilder();

        sBuilder.append("Selected: ");
        model.getSelection().stream()
                .map(node -> node.getData().getLabel())
                .forEach(label -> sBuilder.append(label).append(", "));

//        printPartialSelectedNode(sBuilder);
        selection.setValue(sBuilder.toString());
    }

    // ZK-5937
    private void printPartialSelectedNode(StringBuilder sBuilder) {
        sBuilder.append("\nPartial Selected: ");
        for (NodeData nodeData : model.getPartials()){
            sBuilder.append(nodeData.getLabel()).append(", ");
        }
    }

    private void initTreeModel() {
        NodeData root = new NodeData("Root");
        DefaultTreeNode<NodeData> rootNode = new DefaultTreeNode<>(root, new LinkedList<DefaultTreeNode<NodeData>>());
        model = new DefaultTristateTreeModel<>(rootNode);

        for (int i = 1; i <= 3; i++) {
            DefaultTreeNode<NodeData> parent = new DefaultTreeNode<>(new NodeData("Category " + i), new LinkedList<>());
            for (int j = 1; j <= 3; j++) {
                parent.add(new DefaultTreeNode<>(new NodeData("Item " + i + "." + j)));
            }
            rootNode.add(parent);
        }

        model.setMultiple(true);
        selectExpandNodes();
    }

    private void selectExpandNodes() {
        int[] childOfCategory1 = {0, 1};
        int[] category2 = {1};
        model.addSelectionPath(childOfCategory1);
        model.addSelectionPath(category2);

        model.addOpenPath(Arrays.copyOf(childOfCategory1, 1));
        model.addOpenPath(category2);
    }

    public class TristateItemRenderer implements TreeitemRenderer<DefaultTreeNode<NodeData>> {
        @Override
        public void render(Treeitem item, DefaultTreeNode<NodeData> node, int index) {
            NodeData data = node.getData();

            Treerow row = new Treerow();
            item.appendChild(row);

            Treecell cell = new Treecell();
            cell.setLabel(data.getLabel());
            row.appendChild(cell);
        }
    }

    public static class NodeData {
        private String label;

        public NodeData(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

    }
}