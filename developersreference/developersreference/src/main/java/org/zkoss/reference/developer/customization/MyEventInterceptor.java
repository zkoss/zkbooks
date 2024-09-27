package org.zkoss.reference.developer.customization;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.EventInterceptor;

public class MyEventInterceptor implements EventInterceptor {

    @Override
    public Event beforeProcessEvent(Event event) {
        System.out.println("EventInterceptor beforeProcessEvent: " + event);
        return EventInterceptor.super.beforeProcessEvent(event);
    }
}
