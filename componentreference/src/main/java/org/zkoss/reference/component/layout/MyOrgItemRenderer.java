package org.zkoss.reference.component.layout;

import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.*;
import org.zkoss.zul.Button;

import java.util.Objects;

public class MyOrgItemRenderer implements OrgitemRenderer {
    public void render(Orgitem orgitem, Object data, int index) throws Exception {
        final Button button = new Button(Objects.toString(data));
        button.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
            public void onEvent(Event event) {
                Clients.showNotification(button.getLabel());
            }
        });

        Orgnode orgnode = new Orgnode();
        button.setParent(orgnode);
        orgnode.setParent(orgitem);
    }
}