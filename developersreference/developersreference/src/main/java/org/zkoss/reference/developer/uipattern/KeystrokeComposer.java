package org.zkoss.reference.developer.uipattern;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.Textbox;

public class KeystrokeComposer extends SelectorComposer<Component> {

    @Wire
    private Textbox username;
    @Wire
    private Textbox password;

    @Listen("onOK = #form")
    public void onOK() {
        //handle login
        System.out.println("ok");
    }

    @Listen("onCancel = #form")
    public void onCancel() {
        username.setValue("");
        password.setValue("");
    }
}