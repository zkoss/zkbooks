package org.zkoss.mvvm.shadow;

import org.zkoss.bind.annotation.*;
import org.zkoss.mvvm.advance.domain.Person;

import java.util.*;

public class DynamicTemplateVM {

	private Date today = Calendar.getInstance().getTime();
	private String value = "testing value";
	private boolean edit = false;
	private Person person = new Person(1, "Hawk", "Chen", 23);
	
	@Command @NotifyChange("edit")
	public void toggle(){
		edit = !edit;
	}
	
	public Date getToday() {
		return today;
	}
	public void setToday(Date today) {
		this.today = today;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isEdit() {
		return edit;
	}
	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}

