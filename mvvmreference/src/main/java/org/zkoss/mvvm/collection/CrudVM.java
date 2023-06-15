package org.zkoss.mvvm.collection;

import org.zkoss.zul.ListModelList;

public class CrudVM {
    private ListModelList<String> model;

    public CrudVM() {
        this.model = new ListModelList<>();
        for (int i = 0; i < 4; i++) {
            model.add("item " + i);
        }
    }

    public void add(){
        model.add("item " + System.currentTimeMillis());
    }

    public void delete(){
        if (!model.getSelection().isEmpty()){
            model.removeAll(model.getSelection());
        }
    }

    public ListModelList<String> getModel() {
        return model;
    }
}
