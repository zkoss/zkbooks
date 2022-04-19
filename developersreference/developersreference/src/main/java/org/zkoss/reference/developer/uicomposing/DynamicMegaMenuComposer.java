package org.zkoss.reference.developer.uicomposing;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;

import java.util.*;

public class DynamicMegaMenuComposer extends SelectorComposer<Component> {
    @Override
    public void doBeforeComposeChildren(Component comp) throws Exception {
        super.doBeforeComposeChildren(comp);
        buildMenuModel(comp);
    }

    private void buildMenuModel(Component comp) {
        comp.setAttribute("menuTitle", "Extra Functions");

        List menuList = new LinkedList<>();
        Menu legal = new Menu("Legal");
        String[] items = {"Legal 1", "Legal 2", "Legal 3"};
        legal.setItems(Arrays.asList(items));
        menuList.add(legal);
        Menu finance = new Menu("Finance");
        String[] items2 = {"Finance 1", "Finance 2", "Finance 3"};
        finance.setItems(Arrays.asList(items2));
        menuList.add(finance);
        Menu management = new Menu("Management");
        String[] items3 = {"Management 1", "Management 2", "Management 3"};
        management.setItems(Arrays.asList(items3));
        menuList.add(management);
        comp.setAttribute("menuList", menuList);
    }

}
