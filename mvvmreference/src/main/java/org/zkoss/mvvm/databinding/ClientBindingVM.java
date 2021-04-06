package org.zkoss.mvvm.databinding;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.util.*;

@ToServerCommand("hello")
public class ClientBindingVM {

    @Command
    public void hello(@BindingParam("data")String value){
        Notification.show("hello " + value);
    }

    @Command
    public void sayHello(){
        Clients.evalJavaScript("hello()");
    }
}
