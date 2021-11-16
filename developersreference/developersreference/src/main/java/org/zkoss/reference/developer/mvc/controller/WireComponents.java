package org.zkoss.reference.developer.mvc.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;
import org.zkoss.zuti.zul.*;


public class WireComponents extends SelectorComposer {
    @Wire("::shadow forEach")
    private ForEach forEach;
    @Wire(":host if")
    private If ifComponent;

    @Wire
    private Label message;

    @Wire("include #message") //because Include create an ID space, or #messageArea #message
    private Label message2;

    String[] fruits = {"apple", "banana", "orange"};

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        forEach.setItems(fruits);
        forEach.recreate();
        System.out.println(ifComponent);

        System.out.format("#message: %s\n", message);
        System.out.format("#include #message: %s\n", message2);
    }
}
