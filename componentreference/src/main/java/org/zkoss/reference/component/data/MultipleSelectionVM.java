package org.zkoss.reference.component.data;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.ListModelList;

import java.util.*;

public class MultipleSelectionVM {
    private ListModelList model;
    private List<String> selectedItems;

    @Init
    public void init() {
        List Items = new ArrayList();
        for (int i = 0; i < 100; i++) {
            Items.add("data " + i);
        }
        model = new ListModelList(Items);
        model.setMultiple(true);
    }

    public ListModelList getModel() {
        return model;
    }

    public List<String> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<String> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
