package org.zkoss.reference.developer.eventHandling.eventQueue;

import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.Button;

public class Page1Composer extends SelectorComposer {
    private EventQueue eq;
    @Wire
    Button btn;

    @Listen("onClick = button#btn")
    public void changeLabel() {
        eq = EventQueues.lookup("interactive", EventQueues.DESKTOP, true);
        eq.publish(new Event("onButtonClick", btn, "label is Changed!"));
    }
}
