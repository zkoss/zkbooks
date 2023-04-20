package org.zkoss.reference.developer.debugging;

import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zkmax.ui.select.annotation.Subscribe;
import org.zkoss.zul.Label;

public class AnotherDesktopComposer extends SelectorComposer {
    @Wire("label")
    private Label label;

    @Listen("onClick = label")
    public void send(){
        EventQueues.lookup("myqueue", "session", true).publish(new Event("myevent", null, label));
    }

    @Subscribe(value="myqueue",scope = "session")
    public void change(Event event){
        ((Label)event.getData()).setValue("changed at " + System.currentTimeMillis());
    }


}
