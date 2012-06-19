package org.zkoss.simple;


import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

@SuppressWarnings("serial")
public class RegistrationComposer extends SelectorComposer<Window> {

	@Wire("#submitButton")
	private Button submitButton;
	@Wire("#nameBox")
	private Textbox nameBox;
	@Wire("#genderRadio")
	private Radiogroup genderRadio;
	@Wire("#birthdayBox")
	private Datebox birthdayBox;
	@Wire("#acceptTermBox")
	private Checkbox acceptTermCheckbox;
	@Wire("#nameRow")
	private Row nameRow;
	@Wire("#helpPopup")
	private Popup helpPopup;
	
	@Listen("onCheck = #acceptTermBox")
	public void changeSubmitStatus(){
		if (acceptTermCheckbox.isChecked()){
			submitButton.setDisabled(false);
			submitButton.setImage("/images/submit.png");
		}else{
			submitButton.setDisabled(true);
			submitButton.setImage("");
		}
	}
	
	@Listen("onClick = #resetButton")
	public void reset(){
		//set raw value to avoid triggering constraint error message
		nameBox.setRawValue("");
		genderRadio.setSelectedIndex(0);
		birthdayBox.setRawValue(null);
		acceptTermCheckbox.setChecked(false);
		submitButton.setDisabled(true);
	}
	
	@Listen("onClick = #submitButton")
	public void submit(){
		if (!validateInput()){
			return;
		}
		
		Messagebox.show("Congratulation! "+nameBox.getValue()+". Your registration is success.");
		reset();
	}
	
	private boolean validateInput(){
		if (nameBox.getValue().length()==0){
			return false;
		}
		
		if (birthdayBox.getValue()==null){
			return false;
		}
	
		if (!acceptTermCheckbox.isChecked()){
			return false;
		}
		return true;
	}
	
	@Listen("onOK = #formGrid")
	public void onOK(){
		submit();
	}
	
	@Listen("onCtrlKey = #formGrid")
	public void toggleHelpRow(){
		helpPopup.open(nameRow, "end_before");
	}
	
}
