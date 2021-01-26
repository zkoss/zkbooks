package org.zkoss.reference.developer.eventHandling.eventQueue;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

public class Page2Composer extends SelectorComposer {
    private EventQueue eq;
    @Wire
    private Label lbl;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        eq = EventQueues.lookup("interactive", EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {
            public void onEvent(Event event) throws Exception {
                String value = (String)event.getData();
                lbl.setValue(value);
            }
        });
    }
}
