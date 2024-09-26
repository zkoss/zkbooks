package org.zkoss.reference.developer.uicomposing;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.Textbox;

public class Username extends HtmlMacroComponent {
    private String currentUser; //will be wired if currentUser is a Spring-managed bean, when compose() is called
    @Wire
    private Textbox mc_who; //will be wired when compose() is called

    public Username() {
        compose(); //for the template to be applied, and to wire members automatically
    }

    public String getWho() {
        return mc_who.getValue();
    }

    public void setWho(String who) {
        mc_who.setValue(who);
    }

    @Listen("onClick=#submit")
    public void submit() { //will be wired when compose() is called.
    }
}