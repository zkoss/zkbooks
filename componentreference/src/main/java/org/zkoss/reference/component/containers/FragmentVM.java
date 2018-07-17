package org.zkoss.reference.component.containers;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.util.Clients;

public class FragmentVM {
    private String prop1;
    private String prop2;

    @Command
    public void submit(){
        Clients.showNotification("user input: " + prop1 + ", " + prop2);
    }

    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }

    public String getProp2() {
        return prop2;
    }

    public void setProp2(String prop2) {
        this.prop2 = prop2;
    }
}
