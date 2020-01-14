package org.zkoss.reference.component.base;

import org.zkoss.zk.ui.*;
import org.zkoss.zul.Constraint;

public class EvenNumberConstraint implements Constraint {
    @Override
    public void validate(Component comp, Object value) throws WrongValueException {

        if (value != null && new Integer(value.toString()).intValue() % 2 == 1)
            throw new WrongValueException(comp, "Only even numbers are allowed, not "+value);
    }
}

