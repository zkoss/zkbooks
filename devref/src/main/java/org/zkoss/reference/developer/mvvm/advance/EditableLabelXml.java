package org.zkoss.reference.developer.mvvm.advance;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

/**
 * Data binding definition is defined in XML. 
 * @author Hawk
 *
 */
public class EditableLabelXml extends HtmlMacroComponent{

	private static final long serialVersionUID = 1L;

	@Wire
	Textbox textbox;
	
	@Wire
	Label label;
	
	public EditableLabelXml(){
		setMacroURI("/WEB-INF/component/editablelabel.zul");
	}
	
	public String getValue(){
		return textbox.getValue();
	}
	
	public void setValue(String value){
		textbox.setValue(value);
		label.setValue(value);
	}

	@Listen("onDoubleClick=#label")
	public void doEditing(){
		textbox.setVisible(true);
		label.setVisible(false);
		textbox.focus();
	}
	
	@Listen("onBlur=#textbox")
	public void doEdited(){
		label.setValue(textbox.getValue());
		textbox.setVisible(false);
		label.setVisible(true);
		Events.postEvent("onEdited", this, null);
	}
	

}
