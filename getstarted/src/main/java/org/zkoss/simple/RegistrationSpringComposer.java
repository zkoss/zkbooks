package org.zkoss.simple;




import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

@SuppressWarnings("serial")
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class RegistrationSpringComposer extends SelectorComposer<Component> {

	@Wire
	private Button submitButton;
	@Wire
	private Textbox nameBox;
	@Wire
	private Radiogroup genderRadio;
	@Wire
	private Datebox birthdayBox;
	@Wire
	private Checkbox acceptTermBox;
	
	@WireVariable
	private RegistrationDao registrationDao;
	
	private static Logger logger = Logger.getLogger(RegistrationSpringComposer.class.getName());
	private LegacyRegister legacyRegister = new LegacyRegister();
	private User newUser = new User();
	
	@Listen("onCheck = #acceptTermBox")
	public void changeSubmitStatus(){
		if (acceptTermBox.isChecked()){
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
		acceptTermBox.setChecked(false);
		submitButton.setDisabled(true);
	}
	
	// sample method for 3rd party integration
	@Listen("onClick = #submitButton")
	public void register(){
		
		
		newUser.setName(nameBox.getValue());
		if (genderRadio.getSelectedIndex()==0){
			newUser.setMale(true);
		}else{
			newUser.setMale(false);
		}
		newUser.setBirthday(birthdayBox.getValue());

		
		legacyRegister.add(newUser);
		logger.debug("a user was added.");
		
		Messagebox.show("Congratulation! "+nameBox.getValue()+". Your registration is success.");
		reset();
	}
	
	
	@Listen("onClick = #submitButton")
	public void submit(){
		if (!validateInput()){
			logger.debug("input validation failed");
			return;
		}
		
		User newUser = new User();
		newUser.setName(nameBox.getValue());
		if (genderRadio.getSelectedIndex()==0){
			newUser.setMale(true);
		}else{
			newUser.setMale(false);
		}
		newUser.setBirthday(birthdayBox.getValue());
		registrationDao.add(newUser);
		
		Messagebox.show("Congratulation! "+nameBox.getValue()+". Your registration is success.");
		reset();
	}
	
	private boolean validate(User user){
		return true;
	}
	private boolean validateInput(){
		if (nameBox.getValue().length()==0){
			return false;
		}
		
		if (birthdayBox.getValue()==null){
			return false;
		}
	
		return true;
	}
}
