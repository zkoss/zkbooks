package org.zkoss.reference.developer.uicomposing;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.A;


public class MegaMenuComposer extends SelectorComposer<Component> {

    @Listen("onClick = a")
    public void clickLink(MouseEvent e){
        Notification.show("click " + ((A)e.getTarget()).getLabel());
    }
}
