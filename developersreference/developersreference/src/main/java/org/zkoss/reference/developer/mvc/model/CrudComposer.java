package org.zkoss.reference.developer.mvc.model;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

import java.util.*;

public class CrudComposer extends SelectorComposer {

    @Wire
    private Listbox box;
    private ListModelList model = new ListModelList();

    private int nextId = 300;
    public CrudComposer() {
        for (int i = 0; i < nextId; i++) {
            model.add("item " + i);
        }
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        box.setModel(model);
    }

    @Listen(Events.ON_CLICK + " = #stack")
    public void stack(){
        model.add(0, "item " + nextId);
        nextId++;
    }
    @Listen(Events.ON_CLICK + " = #append")
    public void append(){
        model.add("item " + nextId);
        nextId++;
    }

    @Listen(Events.ON_CLICK + " = #del")
    public void remove(){
        Set selection = model.getSelection();
        if (selection.size() > 0){
            model.remove(selection.iterator().next());
        }
    }

    public ListModelList getModel() {
        return model;
    }
}
