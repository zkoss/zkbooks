package org.zkoss.mvvm.databinding;

import org.zkoss.bind.annotation.*;
import org.zkoss.zul.ListModelList;

public class ChildrenSimpleVM {
    private ListModelList<String> nodes;

    @Init
    public void init() {
        nodes = new ListModelList<>();
        nodes.add("Item A");
        nodes.add("Item B");
        nodes.add("Item C");
    }

    @Command
    public void cmd(){

    }

    public ListModelList<String> getNodes() {
        return nodes;
    }
}