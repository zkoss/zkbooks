package org.zkoss.reference.developer.cdi.composer;


import javax.inject.Named;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * @author Hawk
 *
 */
@Named
public class CdiComposer extends SelectorComposer<Window> {

	private String value = "value in composer";
	
	@Wire("#composerValue")
	private Label valueLabel;
	
	@Listen("onClick = #set")
	public void loadTextboxValue(){
		value = "changed";
		valueLabel.setValue(value);
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
