package org.zkoss.reference.developer.mvvm.advance;

import org.zkoss.bind.AnnotateBinder;
import org.zkoss.bind.Binder;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.impl.ValidationMessagesImpl;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.reference.developer.mvvm.advance.domain.Person;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class DynamicFormBindingComposer extends SelectorComposer {

	private Binder binder = new AnnotateBinder();
	@Wire("div")
	private Div div;
	@Wire("grid")
	private Grid grid;
	
	@Wire("#fn") 
	private Textbox firstNameBox;
	@Wire("#fnError")
	private Label fNameErrorLabel;
	@Wire("#ln") 
	private Textbox lastNameBox;
	@Wire("#lnError")
	private Label lNameErrorLabel;
	@Wire("intbox")
	private Intbox ageBox;
	
	@Wire("#fnLabel")
	private Label firstNameLabel;
	@Wire("#lnLabel")
	private Label lastNameLabel;
	@Wire("#ageLabel")
	private Label ageLabel;
	
	@Wire("button[label='Submit']")
	private Button button;
	
	private Person person;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		person = new Person();
		person.setFirstName("Barrack");
		person.setLastName("Obama");
		person.setAge(53);
		
		binder.init(div, this,null);
		//set ViewModel
		div.setAttribute("vm", this);
		//set validation message holder
		div.setAttribute("vmsgs", new ValidationMessagesImpl());
		
		String[] command = {"submit"};
		binder.addFormLoadBindings(grid, "fx", "vm.person", null, null, null);
		binder.addFormSaveBindings(grid, "fx", "vm.person", command, null, null, null, null);
		
		binder.addPropertyLoadBindings(firstNameBox, "value", "fx.firstName", null, null, null, null, null);
		binder.addPropertySaveBindings(firstNameBox, "value", "fx.firstName", null, null, null, null, null,"vm.emptyValidator",null);
		binder.addPropertyLoadBindings(fNameErrorLabel, "value", "vmsgs[fn]", null, null, null, null, null);
		binder.addPropertyLoadBindings(lastNameBox, "value", "fx.lastName", null, null, null, null, null);
		binder.addPropertySaveBindings(lastNameBox, "value", "fx.lastName", null, null, null, null, null,"vm.emptyValidator",null);
		binder.addPropertyLoadBindings(lNameErrorLabel, "value", "vmsgs[ln]", null, null, null, null, null);
		binder.addPropertyLoadBindings(ageBox, "value", "fx.age", null, null, null, null, null);
		binder.addPropertySaveBindings(ageBox, "value", "fx.age", null, null, null, null, null,null,null);
		
		binder.addPropertyLoadBindings(firstNameLabel, "value", "vm.person.firstName", null, command, null, null, null);
		binder.addPropertyLoadBindings(lastNameLabel, "value", "vm.person.lastName", null, command, null, null, null);
		binder.addPropertyLoadBindings(ageLabel, "value", "vm.person.age", null, command, null, null, null);
		
		binder.addCommandBinding(button, Events.ON_CLICK, "'submit'", null);
		
		binder.loadComponent(grid, true);
	}

	@Command
	public void submit(){
		
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public Validator getEmptyValidator(){
		return new AbstractValidator(){
			public void validate(ValidationContext ctx) {
				Object value = ctx.getProperty().getValue();
				if(value==null || value.toString().length()==0){
					addInvalidMessage(ctx, "must not be empty");
				}
			}
		};
	}
}
