package org.zkoss.reference.developer.mvc.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.ui.select.annotation.Subscribe;

public class ReceiverComposer extends SelectorComposer<Component> {

    @Subscribe("queue1")
    public void receive(Event event) {
        // this method will be called when EventQueue "queue1" of Desktop scope is published
        Object data = event.getData();
        Component target = event.getTarget();
        Clients.showNotification("receive " + data + " from the target: " +target.getClass());
    }

}
