package org.zkoss.reference.developer.mvc.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zuti.zul.*;

import java.awt.*;

public class WireComponents extends SelectorComposer {
    @Wire("::shadow forEach")
    ForEach forEach;
    @Wire(":host if")
    If ifComponent;

    String[] fruits = {"apple", "banana", "orange"};

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        forEach.setItems(fruits);
        forEach.recreate();
        System.out.println(ifComponent);
    }
}
