package org.zkoss.mvvm.parameters;

import org.zkoss.bind.annotation.*;
import org.zkoss.mvvm.collection.model.Item;
import org.zkoss.zul.ListModelList;

public class BeanCreationVM {
    private ListModelList<Item> itemList = new ListModelList();

    @Command
//    public void create(@BindingParam("id")int id, @BindingParam("name")String name){
    public void create(@BindingParams Item item){
        itemList.add(item);
    }

    public ListModelList<Item> getItemList() {
        return itemList;
    }
}
