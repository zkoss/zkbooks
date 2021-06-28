package org.zkoss.reference.developer.uipattern;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

public class ExpandableInputComposer extends SelectorComposer<Component> {

    @Wire("textbox")
    private Textbox textbox;
    @Wire(".toolbar")
    private Div toolbar;

    @Listen(Events.ON_FOCUS + "= textbox")
    public void expand(){
        textbox.setRows(5);
        toolbar.setVisible(true);
    }

    @Listen(Events.ON_BLUR + "= textbox")
    public void collapse(){
        textbox.setRows(1);
        toolbar.setVisible(false);
    }
}
