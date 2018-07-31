package org.zkoss.reference.client;

import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Clients;

public class ClientFireComposer extends SelectorComposer {

    @Listen("onMyEvent = button")
    public void myListener(Event event){
        Clients.showNotification("onMyEvent fired with " + ((JSONObject)event.getData()).get("myKey"));
    }
}
