package org.zkoss.reference.developer.uipattern;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;
import org.zkoss.zul.impl.LabelImageElement;

import java.time.LocalDate;
import java.util.Date;

public class InputFeedbackComposer extends SelectorComposer {
    @Wire(".errorMessage")
    private Label errorMessage;

    @Listen("onClick = #create")
    public void validateUserName() {
        // query a database
        // assume validation failed and need to show an error message
        errorMessage.setVisible(true);
        errorMessage.setValue("The user name you entered already existed, please choose another name");
    }

    @Listen(Events.ON_BLUR + "= .form-field")
    public void validate(Event event) {
        LabelImageElement statusIcon = (LabelImageElement) event.getTarget().getNextSibling();
        if (event.getTarget() instanceof Textbox) {
            String value = ((Textbox) event.getTarget()).getValue();
            if (value == null || value.isEmpty()) {
                showError(statusIcon);
            } else {
                showPass(statusIcon);
            }

        } else if (event.getTarget() instanceof Datebox) {
            LocalDate value = ((Datebox) event.getTarget()).getValueInLocalDate();
            if (value == null || isLess18YearsOld(value)) {
                showError(statusIcon);
            } else {
                showPass(statusIcon);
            }
        }
    }

    private void showError(LabelImageElement component) {
        component.setIconSclass("z-icon-times-circle");
    }

    private void showPass(LabelImageElement component) {
        component.setIconSclass("z-icon-check-circle");
    }

    private boolean isLess18YearsOld(LocalDate value) {
        LocalDate today = LocalDate.now();

        return value == null || (today.getYear() - value.getYear()) < 18;
    }

}

