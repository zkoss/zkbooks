package org.zkoss.reference.developer.mvc.model;

import org.zkoss.zul.AbstractTreeModel;

public class LoadOnDemandModel extends AbstractTreeModel<Object> {

    public LoadOnDemandModel() {
        super("Root");
    }


    public boolean isLeaf(Object node) {
        return getLevel((String) node) >= 4; //at most 4 levels
    }


    public Object getChild(Object parent, int index) {
        return parent + "." + index;
    }


    public int getChildCount(Object parent) {
        return isLeaf(parent) ? 0 : 5; //each node has 5 children
    }


    public int getIndexOfChild(Object parent, Object child) {
        String data = (String) child;
        int i = data.lastIndexOf('.');
        return Integer.parseInt(data.substring(i + 1));
    }


    private int getLevel(String data) {
        for (int i = -1, level = 0; ; ++level)
            if ((i = data.indexOf('.', i + 1)) < 0)
                return level;
    }
};
