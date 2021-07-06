package org.zkoss.reference.developer.uipattern;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Listitem;

public class DragDropComposer extends SelectorComposer<Component> {
    @Listen("onDrop = listitem")
    public void move(DropEvent event){
        Listitem listitem = (Listitem)event.getTarget();
        listitem.getParent().insertBefore(event.getDragged(), listitem);
    }
}
