package org.zkoss.reference.developer.uipattern;

import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

public class MessageboxComposer extends SelectorComposer {

    @Listen("onClick = #info")
    public void info() {
        Messagebox.show("The file has been removed successfully.");
    }

    @Listen("onClick = #error")
    public void error() {
        Messagebox.show("Unable to delete the file", null, 0, Messagebox.ERROR);
    }

    @Listen("onClick = #confirm")
    public void confirm() {
        Messagebox.show("Are you sure you want to remove the file?", null,
                Messagebox.YES + Messagebox.NO, Messagebox.QUESTION,
            new EventListener<Event>() {
                public void onEvent(Event event) {
                    if (Messagebox.ON_YES.equals(event.getName())) {
                        //delete the file
                        Clients.showNotification("deleted");
                    }
                }
            });

        /* another way
        MessageboxHelper.confirm("title", "message", event -> {
            if (Messagebox.ON_YES.equals(event.getName())) {
                //delete the file
                Clients.showNotification("deleted");
            }
        });

        MessageboxHelper.confirm("title", "message", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
            if (Messagebox.ON_YES.equals(event.getName())) {
                    //delete the file
                    Clients.showNotification("deleted");
                }
            }
        });

        MessageboxHelper.confirm("title", "message", () -> {
                Clients.showNotification("deleted");
            }
        , null);
        */
    }

}
