package org.zkoss.mvvm.databinding;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;

/**
 * Show an error message with an error box by calling Clients.wrongValue(). <br/>
 * Follows Decorator pattern to avoid hard-coded the way of showing an error message from the wrapped validator.
 */
public class ErrorboxValidator extends AbstractValidator {
    private Validator validator; //wrapped validator

    public ErrorboxValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void validate(ValidationContext validationContext) {
        validator.validate(validationContext);
        Component targetComponent = validationContext.getBindContext().getComponent();
        if (validationContext.isValid()) {
            Clients.clearWrongValue(targetComponent);
        } else {
            ValidationMessages vmsgs = ((BinderCtrl) validationContext.getBindContext().getBinder()).getValidationMessages();
            String[] msgs = vmsgs.getMessages(targetComponent);
            if (msgs != null) {
                for (String msg : msgs) {
                    Clients.wrongValue(targetComponent, msg);
                }
            }
        }
    }
}
