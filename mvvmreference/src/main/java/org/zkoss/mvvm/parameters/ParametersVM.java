package org.zkoss.mvvm.parameters;

import org.zkoss.bind.annotation.*;
import org.zkoss.zul.ListModelList;

import java.util.Arrays;

public class ParametersVM {
    private ListModelList items = new ListModelList();
    private String message;

    public ParametersVM() {
        String[] names = {"A", "B", "C", "D"};
        items.addAll(Arrays.asList(names));
    }

    @NotifyChange("message")
    public void delete(@BindingParam("item") String item){
        items.remove(item);
        message = "remove item: " + item;
    }

    @NotifyChange("message")
    public void showIndex(int index){
        message = "item index: " + index;
    }

    public ListModelList getItems() {
        return items;
    }

    public String getMessage() {
        return message;
    }
}
