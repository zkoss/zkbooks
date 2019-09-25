package org.zkoss.reference.client;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.util.Clients;

import java.util.Map;

public class ClickReporter extends AbstractComponent {
    static {
        addClientEvent(ClickReporter.class, "onFly", CE_IMPORTANT); //assume it is an important event
    }

    public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
        String cmd = request.getCommand();
        if (cmd.equals("onFly")) {
            Map data = request.getData();
            int x = ((Integer) data.get("x")).intValue();
            int y = ((Integer) data.get("y")).intValue();
            //do whatever you want
            Clients.showNotification(String.format("x:%s, y:%s", x, y));
        } else {
            super.service(request, everError);
        }
    }
}
