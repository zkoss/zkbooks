package org.zkoss.reference.developer.mvc.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.Button;

public class SenderComposer extends SelectorComposer<Component> {

    @Wire
    private Button send;


    @Listen("onClick = #send")
    public void publish() {
        EventQueue<Event> eq = EventQueues.lookup("queue1", EventQueues.DESKTOP, true);
        eq.publish(new Event("onMyEvent", send, "send from " + this.getClass()));
    }

}
