package org.zkoss.reference.component.base;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Constraint;

public class EvenNumberConstraint implements Constraint {
    @Override
    public void validate(Component comp, Object value) throws WrongValueException {
        //https://www.zkoss.org/wiki/ZK_Developer's_Reference/UI_Patterns/Useful_Java_Utilities
        if (new Integer(value.toString()).intValue() == 2) { //a warning
            Notification.show("warning: should >2", Notification.TYPE_WARNING, comp, "end_after", 0, true);
            return;
        }
        if (value != null && new Integer(value.toString()).intValue() % 2 == 1) //an error
            throw new WrongValueException(comp, "Only even numbers are allowed, not "+value);
    }
}

