package org.zkoss.mvvm.client;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.ListModelList;

import java.util.*;

public class ClearItemsVM {

    private ListModelList<String> model = new ListModelList<>();
    private List dataList = new LinkedList();
    public ClearItemsVM() {
        for (int i = 0; i < 20000; i++) {
            dataList.add("item"+ i);
        }
    }

    private Component root;
    @AfterCompose
    public void findRoot(@ContextParam(ContextType.VIEW) Component component){
        this.root = component;
    }

    @Command
    public void fill() {
        model.addAll(dataList);
    }
    @Command
    public void clear() {
        model.clear();
    }

    public ListModelList<String> getModel() {
        return model;
    }

    public void setModel(ListModelList<String> model) {
        this.model = model;
    }

}
