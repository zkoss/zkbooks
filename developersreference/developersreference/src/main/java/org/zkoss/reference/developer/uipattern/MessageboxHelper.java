package org.zkoss.reference.developer.uipattern;

import org.zkoss.zk.ui.event.*;
import org.zkoss.zul.Messagebox;

public class MessageboxHelper {

    static void confirm(String title, String message, EventListener listener){
        Messagebox.show(message, title,
                Messagebox.YES + Messagebox.NO, Messagebox.QUESTION,
                listener);
    }

    static void confirm(String title, String message, Runnable yesCallback, Runnable noCallback){
        Messagebox.show(message, title,
                Messagebox.YES + Messagebox.NO, Messagebox.QUESTION,
                new EventListener<Event>() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        if (Messagebox.ON_YES.equals(event.getName())) {
                            if (yesCallback != null)
                                yesCallback.run();
                        }else{
                            if (noCallback != null)
                                noCallback.run();
                        }
                    }
                });
    }
}
