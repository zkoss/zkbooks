package org.zkoss.reference.developer.customization;

import org.zkoss.zul.Textbox;

/**
 * Focus to select the whole text 
 */
public class SelectionTextbox extends Textbox {

    public SelectionTextbox() {
        super();
        this.setWidgetOverride("onFocus", "this.select()");
    }
}
