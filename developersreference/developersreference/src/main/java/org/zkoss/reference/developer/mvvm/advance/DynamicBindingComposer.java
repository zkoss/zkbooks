package org.zkoss.reference.developer.mvvm.advance;

import org.zkoss.bind.Binder;
import org.zkoss.bind.DefaultBinder;
import org.zkoss.bind.impl.BinderImpl;
import org.zkoss.bind.tracker.impl.TrackerImpl;
import org.zkoss.reference.developer.mvvm.advance.domain.Person;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * This usage fit the requirement that users want component's data are automatically saved to beans.
 * It eliminates getting data by a component's getter methods.
 */
public class DynamicBindingComposer extends SelectorComposer {

	private Binder binder = new DefaultBinder();
	
	@Wire("window")
	private Window window;
	@Wire("grid")
	private Grid grid;
	
	@Wire("#fn") 
	private Textbox firstNameBox;
	@Wire("#ln") 
	private Textbox lastNameBox;
	@Wire("intbox")
	private Intbox ageBox;
	
	@Wire("#introduction")
	private Label introductionLabel;
	
	private Person person;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		//initialized
		person = new Person();
		person.setFirstName("Barrack");
		person.setLastName("Obama");
		person.setAge(53);
		
		binder.init(grid,this, null);
		
		comp.setAttribute("person", person);
		
		binder.addPropertySaveBindings(firstNameBox, "value", "person.firstName", null, null, null, null, null,null,null);
		binder.addPropertyLoadBindings(firstNameBox, "value", "person.firstName", null, null, null, null, null);
		binder.addPropertySaveBindings(lastNameBox, "value", "person.lastName", null, null, null, null, null,null,null);
		binder.addPropertyLoadBindings(lastNameBox, "value", "person.lastName", null, null, null, null, null);
		binder.addPropertySaveBindings(ageBox, "value", "person.age", null, null, null, null, null,null,null);
		binder.addPropertyLoadBindings(ageBox, "value", "person.age", null, null, null, null, null);
		
		binder.loadComponent(grid, false); //load beans' data to initialize components
	}
	
	@Listen("onClick = button[label='Submit']")
	public void submit(){
		introductionLabel.setValue("I am "+person.getFirstName()+" "
			+person.getLastName()+". I am "+person.getAge()+" years old.");
	}

	@Listen("onClick = button[label='Reset']")
	public void reset(){
		person = new Person();
		window.setAttribute("person", person);
		binder.loadComponent(grid, false);
	}
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	@Listen("onClick = button[label='Dump']")
	public void dump(){
		((TrackerImpl)((BinderImpl)binder).getTracker()).dump();
	}	
	
}
