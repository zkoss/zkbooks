package org.zkoss.reference.component.input;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.util.*;

public class MyAutoCompleteComposer extends SelectorComposer<Component> {
    @Wire("combobox")
    private Combobox combobox;
    private List<Locale> items = Arrays.asList(Locale.getAvailableLocales());

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        combobox.setModel(ListModels.toListSubModel(new ListModelList(getAllItems())));
    }

    List getAllItems() {
        return items;
    }
}
