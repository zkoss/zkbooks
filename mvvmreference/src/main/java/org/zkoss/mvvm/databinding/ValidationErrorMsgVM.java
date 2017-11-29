package org.zkoss.mvvm.databinding;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zul.ListModelList;

import java.util.Calendar;

public class ValidationErrorMsgVM {
	private boolean flag;
	private String value;
	String[] userName = { "Tony", "Ryan", "Jumper", "Wing", "Sam" };
	private ListModelList model = new ListModelList(userName);
	private Integer selectedIndex;

	private Validator booleanValidator = new AbstractValidator() {
		@Override
		public void validate(ValidationContext validationContext) {
			if (!((Boolean)validationContext.getProperty().getValue())){
				addInvalidMessage(validationContext, "error");
			}
		}
	};

	private Validator intValidator = new AbstractValidator() {
		@Override
		public void validate(ValidationContext validationContext) {
			if ((Integer)validationContext.getProperty().getValue() == -1){
				addInvalidMessage(validationContext, "error");
			}
		}
	};

	public Validator getMyValidator(){
		return new ErrorboxValidator(booleanValidator);
	}

	public Validator getIntValidator(){
		return new ErrorboxValidator(intValidator);
	}

	@Command
	public void submit(){
		System.out.println(flag + ":" + Calendar.getInstance().getTime());
		System.out.println(value + ":" + Calendar.getInstance().getTime());
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ListModelList getModel() {
		return model;
	}

	public Integer getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(Integer selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
}
