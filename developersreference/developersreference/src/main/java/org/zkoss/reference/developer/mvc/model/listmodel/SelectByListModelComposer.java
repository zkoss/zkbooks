package org.zkoss.reference.developer.mvc.model.listmodel;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;
import org.zkoss.zul.ext.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * select/deselect a listitem via ListModel API.
 * @author Hawk Chen
 */
public class SelectByListModelComposer extends SelectorComposer<Component> {

    @Wire("listbox")
    private Listbox listbox;
    @Wire("intbox")
    private Intbox intbox;

    @Wire
    private Label selectedItem;

    private ListModelList<Locale> listModel = new ListModelList<>(Locale.getAvailableLocales());

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        listModel.setMultiple(true);
        listbox.setModel(listModel);
    }

    @Listen("onClick = button")
    public void toggle(){
        Locale locale = listModel.getElementAt(intbox.getValue());
        if (listModel.getSelection().contains(locale)){
            listModel.removeFromSelection(locale);
        }else{
            listModel.addToSelection(locale);
        }
    }

    @Listen("onSelect = listbox")
    public void show(){

        selectedItem.setValue(String.join( ",",
                listModel.getSelection().stream().map(Locale::toString).collect(Collectors.toSet())));
    }
}
