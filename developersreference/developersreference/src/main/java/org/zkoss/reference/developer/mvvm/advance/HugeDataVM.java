package org.zkoss.reference.developer.mvvm.advance;

import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.reference.developer.mvvm.advance.domain.Person;
import org.zkoss.reference.developer.mvvm.advance.domain.PersonDao;


public class HugeDataVM {

	private PersonDao personDao = new PersonDao();
	private LivePersonListModel personListModel = new LivePersonListModel(personDao);
	private List<Person> personList;
	private Person selectedPerson;
	private int currentPage = 1;
	
	@Init
	public void init(){
		personDao.generateData();
		personList = personDao.findAll();
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
	public void add(){
		
			Person p = new Person();
			p.setFirstName("AA");
			p.setLastName("BB");
			p.setAge(10);
			personDao.add(p);
	}
	
	@Command
	public void save(){
		
	}
	
	
	@Command @NotifyChange({"personList","selectedPerson"})
	public void listDelete(){
		int index = personList.indexOf(selectedPerson);
		if (index != -1){
			personList.remove(index);
			selectedPerson = new Person();
		}
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

	public List<Person> getPersonList() {
		return personList;
	}

	public void setPersonList(List<Person> personList) {
		this.personList = personList;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
}