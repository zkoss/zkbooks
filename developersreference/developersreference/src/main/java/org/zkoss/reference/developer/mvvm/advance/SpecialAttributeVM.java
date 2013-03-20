package org.zkoss.reference.developer.mvvm.advance;

import java.util.List;

import org.zkoss.bind.annotation.Init;
import org.zkoss.reference.developer.mvvm.advance.domain.Person;
import org.zkoss.reference.developer.mvvm.advance.domain.PersonDao;


public class SpecialAttributeVM {

	private Person currentUser = new Person();
	private List<Person> personList;
	private PersonDao personDao = new PersonDao();
	
	@Init
	public void init(){
		personDao.generateData(10);
		personList = personDao.findAll();
	}

	public Person getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Person currentUser) {
		this.currentUser = currentUser;
	}

	public List<Person> getPersonList() {
		return personList;
	}

	public void setPersonList(List<Person> personList) {
		this.personList = personList;
	}
}

