package org.zkoss.reference.component.base;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.Textbox;

public class ShowErrorComposer extends SelectorComposer<Component> {
    @Wire
    private Textbox username;
    @Wire
    private Textbox password;

    @Listen("onClick = #login")
    public void doLogin() {
        username.clearErrorMessage(); //important to clear the previous error, if any
        if (examine(username, password)) {
            //success
        } else {
            throw new WrongValueException(username, "Not a valid username or password. Please try zk/zk.");
        }
    }

    private boolean examine(Textbox username, Textbox password) {
        return (username.getValue().equals("zk") && password.getValue().equals("zk"));
    }

}
