package org.zkoss.simple;


import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class RegistrationComposer extends SelectorComposer<Window> {

	@Wire("button[label='Submit']")
	private Button submitButton;
	
	@Wire("textbox")
	private Textbox nameBox;
	@Wire("combobox")
	private Combobox bloodTypeBox;
	@Wire("datebox")
	private Datebox birthdayBox;
	
	
	@Wire("checkbox")
	Checkbox acceptTermCheckbox;
	
	@Listen("onCheck = checkbox")
	public void changeSubmitStatus(){
		if (acceptTermCheckbox.isChecked()){
			submitButton.setDisabled(false);
		}else{
			submitButton.setDisabled(true);
		}
	}
	
	@Listen("onClick = button[label='Reset']")
	public void reset(){
		nameBox.setValue("");
		bloodTypeBox.setValue("");
		birthdayBox.setValue(null);
	}
}
