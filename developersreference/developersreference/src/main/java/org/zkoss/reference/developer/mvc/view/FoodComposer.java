package org.zkoss.reference.developer.mvc.view;

import org.zkoss.reference.developer.mvc.model.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zul.*;

import java.util.LinkedList;

public class FoodComposer extends SelectorComposer<Component> {

    static public LinkedList<Food> getFoodList() {
        return Food.foodList;
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        Listbox listbox = (Listbox)comp;
        FoodGroupsModel model = new FoodGroupsModel(Food.foodList);
        listbox.setModel(model);
    }
}
