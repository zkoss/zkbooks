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
	
	@Init
	public void init(){
		personDao.generateData();
		personList = personDao.findAll();
	}
	
	
	@NotifyChange("personList")
	@Command
	public void delete(@BindingParam("index") Integer index){
		personList.remove(index);
	}

	public List<Person> getPersonList() {
		return personList;
	}

	public void setPersonList(List<Person> personList) {
		this.personList = personList;
	}



}
