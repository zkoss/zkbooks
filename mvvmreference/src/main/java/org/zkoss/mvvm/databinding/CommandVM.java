package org.zkoss.mvvm.databinding;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.util.Notification;

public class CommandVM {

    //since 9.5.0 treat all public methods as command methods by default
    public void hello(){
        Notification.show("hello");
    }


    // command name is specified
    @Command("save")
    public void saveOrder() {
        Notification.show("save order");
    }

    // default command name is the method name
    @Command
    public void newOrder() {
        Notification.show("new order");
    }
}
