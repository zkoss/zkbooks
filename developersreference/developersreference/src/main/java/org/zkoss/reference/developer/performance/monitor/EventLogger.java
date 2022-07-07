package org.zkoss.reference.developer.performance.monitor;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.EventInterceptor;

public class EventLogger implements EventInterceptor {

    @Override
    public void afterProcessEvent(Event event) {
        System.out.println(event);
    }
}
