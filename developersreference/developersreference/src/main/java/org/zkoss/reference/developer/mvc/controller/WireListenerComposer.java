package org.zkoss.reference.developer.mvc.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Button;


public class WireListenerComposer extends SelectorComposer<Component> {
    @Wire
    private Button btn0;

    @Listen("onClick = #btn0")
    public void submit(MouseEvent event) {
        // called when onClick is received on the component of id btn0.
        Notification.show(btn0 + event.toString());
    }
    @Listen("onSelect = #listbox0")
    public void select(SelectEvent event) {
        // called when onSelect is received on the component of id listbox0.
        Notification.show(event.toString());
    }
}
