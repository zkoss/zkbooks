package org.zkoss.reference.component.input;

import org.zkoss.zk.ui.event.*;
import org.zkoss.zul.Combobox;

/**
 * a combobox that enforce user input to match an item of the model.
 * If a user input doesn't match any item of all Comboitems, it will focus back to the Combobox
 */
public class EnforceMatchCombobox extends Combobox {

    public EnforceMatchCombobox() {
        super();
        this.addEventListener(Events.ON_BLUR, new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (!getValue().isEmpty()
                && getSelectedItem() == null){
                    setFocus(true);
                }
            }
        });
    }
}
