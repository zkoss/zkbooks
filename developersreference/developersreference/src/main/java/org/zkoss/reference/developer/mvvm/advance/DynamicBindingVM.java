package org.zkoss.reference.developer.mvvm.advance;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.reference.developer.mvvm.advance.domain.Person;


public class DynamicBindingVM {

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
	

}
