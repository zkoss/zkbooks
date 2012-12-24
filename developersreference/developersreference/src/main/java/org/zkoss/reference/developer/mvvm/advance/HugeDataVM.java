package org.zkoss.reference.developer.mvvm.advance;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.reference.developer.mvvm.advance.domain.Person;
import org.zkoss.reference.developer.mvvm.advance.domain.PersonDao;


public class HugeDataVM {

	private PersonDao personDao = new PersonDao();
	private LivePersonListModel personListModel = new LivePersonListModel(personDao);
	private Person selectedPerson;
	
	@Init
	public void init(){
		personDao.generateData();
	}

	@Command @NotifyChange({"personListModel","selectedPerson"})
	public void delete(){
		if (selectedPerson != null){
			//remove selected from ListModel?
			personDao.delete(selectedPerson);
			selectedPerson = null;
		}
	}
	
	@Command @NotifyChange("personListModel")
	public void save(){
		personDao.add(selectedPerson);
	}
	
	@Command @NotifyChange("selectedPerson")
	public void reset(){
		selectedPerson = new Person();
	}
	
	
	
	public LivePersonListModel getPersonListModel() {
		return personListModel;
	}

	public void setPersonListModel(LivePersonListModel personList) {
		this.personListModel = personList;
	}

	public Person getSelectedPerson() {
		return selectedPerson;
	}

	public void setSelectedPerson(Person selectedPerson) {
		this.selectedPerson = selectedPerson;
	}
	
}