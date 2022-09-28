package org.zkoss.reference.developer.mvc.model;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zul.*;

public class FoodComposer extends SelectorComposer<Component> {

	@Override
    public void doAfterCompose(Component comp) throws Exception {
        Grid grid = (Grid)comp;
        GroupsModelArray model = new GroupsModelArray(Food.foods, new ArrayComparator(0, true));
		grid.setModel(model);
             //Initially, we group data on 1st column in ascending order
        grid.setRowRenderer(new FoodGroupRenderer());
    }
}
