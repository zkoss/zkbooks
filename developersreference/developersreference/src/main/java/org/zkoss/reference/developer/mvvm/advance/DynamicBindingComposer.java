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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.MessageboxDlg;

/**
 * This usage fit the requirement that users want component's data are automatically saved to beans.
 * It eliminates getting data by a component's getter methods.
 */
public class DynamicBindingComposer extends SelectorComposer {

	private Binder binder = new DefaultBinder();
	
	@Wire("grid")
	private Grid grid;
	
	@Wire("#fn") 
	private Textbox firstNameBox;
	@Wire("#ln") 
	private Textbox lastNameBox;
	@Wire("intbox")
	private Intbox ageBox;
	
	@Wire("#fnLabel")
	private Label firstNameLabel;
	@Wire("#lnLabel")
	private Label lastNameLabel;
	@Wire("#ageLabel")
	private Label ageLabel;
	
	private Person person;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		//initialized
		person = new Person();
		person.setFirstName("Barrack");
		person.setLastName("Obama");
		person.setAge(53);
		
		binder.init(comp,this, null);
		
		grid.setAttribute("person", person);
		
		binder.addPropertySaveBindings(firstNameBox, "value", "person.firstName"
				, null, null, null, null, null,null,null);
		binder.addPropertyLoadBindings(firstNameBox, "value", "person.firstName"
				, null, null, null, null, null);
		binder.addPropertySaveBindings(lastNameBox, "value", "person.lastName", null, null, null, null, null,null,null);
		binder.addPropertyLoadBindings(lastNameBox, "value", "person.lastName", null, null, null, null, null);
		binder.addPropertySaveBindings(ageBox, "value", "person.age", null, null, null, null, null,null,null);
		binder.addPropertyLoadBindings(ageBox, "value", "person.age", null, null, null, null, null);
		
		binder.addPropertyLoadBindings(firstNameLabel, "value", "person.firstName", null, null, null, null, null);
		binder.addPropertyLoadBindings(lastNameLabel, "value", "person.lastName", null, null, null, null, null);
		binder.addPropertyLoadBindings(ageLabel, "value", "person.age", null, null, null, null, null);
		
		binder.loadComponent(grid, false); //load beans' data to initialize components
	}
	
	@Listen("onClick = button[label='Submit']")
	public void submit(){
		Messagebox.show("I am "+person.getFirstName()+" "
			+person.getLastName()+", "+person.getAge()+" years old.");
	}

	@Listen("onClick = button[label='Reset']")
	public void reset(){
		person = new Person();
		grid.setAttribute("person", person);
		binder.loadComponent(grid, false);
	}
	
	public void resetAlternative(){
		person.setFirstName("");
		person.setLastName("");
		person.setAge(0);
		binder.notifyChange(person, "*");
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
