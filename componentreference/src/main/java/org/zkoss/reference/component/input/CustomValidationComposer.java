package org.zkoss.reference.component.input;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.Textbox;

public class CustomValidationComposer extends SelectorComposer {
    @Wire
    private Textbox username;
    @Wire
    private Textbox password;

    @Listen("onClick = #login")
    public void doLogin() {
        username.clearErrorMessage(); //important to clear the previous error, if any
        if (validate(username, password)) {
            //success
        } else {
            throw new WrongValueException(username, "Not a valid username or password. Please retry.");
        }
    }

    private boolean validate(Textbox username, Textbox password) {
        return false;
    }
}
