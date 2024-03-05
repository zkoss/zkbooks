package org.zkoss.mvvm.client;

import org.zkoss.bind.annotation.*;
import org.zkoss.mvvm.data.ItemService;
import org.zkoss.zul.ListModelList;

public class SearchboxVM {
    private ListModelList model;

    public SearchboxVM() {
        ItemService.getInstance().produceItems(5000);
        this.model = new ListModelList(ItemService.getInstance().getItems());
        model.setMultiple(true);
    }


    public ListModelList getModel() {
        return model;
    }

}
