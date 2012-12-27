package org.zkoss.reference.developer.mvvm.advance;

import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.reference.developer.mvvm.advance.domain.Person;
import org.zkoss.reference.developer.mvvm.advance.domain.PersonDao;


public class DynamicCollectionBindingVM {

	private PersonDao personDao = new PersonDao();
	private List<Person> personList;
	private Person selectedPerson = null; 
	private String printedResult;
	
	@Init
	public void init(){
		personDao.generateData(20);
		personList = personDao.findAll();
	}
	
	
	@NotifyChange("personList")
	@Command
	public void delete(@BindingParam("index") int index){
		personList.remove(index);
	}
	
	@NotifyChange("printedResult")
	@Command
	public void print(){
		StringBuilder result = new StringBuilder();
		for (Person p : personList){
			result.append(p.getFirstName()+" "+p.getLastName()+"\n");
		}
		printedResult = result.toString();
	}

	public List<Person> getPersonList() {
		return personList;
	}

	public void setPersonList(List<Person> personList) {
		this.personList = personList;
	}


	public Person getSelectedPerson() {
		return selectedPerson;
	}


	public void setSelectedPerson(Person selectedPerson) {
		this.selectedPerson = selectedPerson;
	}


	public String getPrintedResult() {
		return printedResult;
	}


	public void setPrintedResult(String printedResult) {
		this.printedResult = printedResult;
	}



}
