package org.zkoss.reference.developer.mvvm.advance;


import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.reference.developer.mvvm.advance.domain.Person;


public class DynamicFormBindingVM {

	private Person person;
	
	@Init
	public void init(){
		person = new Person();
		person.setFirstName("Barrack");
		person.setLastName("Obama");
		person.setAge(53);
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
