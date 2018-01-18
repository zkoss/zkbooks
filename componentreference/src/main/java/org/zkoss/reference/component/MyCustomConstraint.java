package org.zkoss.reference.component;

import org.zkoss.zk.ui.*;
import org.zkoss.zul.*;

public class MyCustomConstraint implements Constraint, CustomConstraint{
    private String labelId;

    public MyCustomConstraint(String labelId){
        this.labelId = labelId;
    }

    //Constraint//
    public void validate(Component comp, Object value) {
        if (value == null || ((Integer)value).intValue() < 100)
            throw new WrongValueException(comp, "At least 100 must be specified");
    }
    //CustomConstraint//
    public void showCustomError(Component comp, WrongValueException ex) {
        ((Label)comp.getFellow(labelId)).setValue(ex != null ? ex.getMessage(): "");
    }
}
