package org.zkoss.reference.developer.performance;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

import java.util.Locale;

public class RodComposer extends SelectorComposer {
    @Wire
    private Listbox listbox1;
    @Wire
    private Label size;

    private ListModelList model = new ListModelList(Locale.getAvailableLocales());

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        listbox1.setModel(model);
    }

    @Listen("onDataLoading(-1)=#listbox1 ;onCreate = #listbox1")
    public void printItemSize(Event event){
        size.setValue(listbox1.getItems().size()+"");
    }

}
