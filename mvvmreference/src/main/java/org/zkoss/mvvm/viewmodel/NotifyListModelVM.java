package org.zkoss.mvvm.viewmodel;

import org.zkoss.bind.annotation.*;
import org.zkoss.mvvm.collection.model.*;
import org.zkoss.zul.ListModelList;

/**
 * In most cases, you don't need to notify change for a {@link org.zkoss.zul.ListModel} with {@link NotifyChange}
 */
public class NotifyListModelVM {
    private ItemService itemService = new ItemService(50);
    private ListModelList<Item> model = new ListModelList(itemService.getAllItems());

    @Command
    public void updateContent() {
        for (int i = 0 ; i< model.getSize() ; i++){
            Item item = model.get(i);
            item.setName("Item"+ "-" + System.currentTimeMillis());
            model.notifyChange(item);
        }
    }

    /**
     * calling the methods of {@link ListModelList} notifies the change to the bound component automatically without {@link NotifyChange}
     */
    @Command
    public void add() {
        model.add(itemService.create());
    }

    public ListModelList getModel() {
        return model;
    }
}