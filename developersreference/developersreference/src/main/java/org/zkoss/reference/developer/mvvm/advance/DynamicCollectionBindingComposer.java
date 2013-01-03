package org.zkoss.reference.developer.mvvm.advance;

import org.zkoss.bind.Binder;
import org.zkoss.bind.DefaultBinder;
import org.zkoss.reference.developer.mvvm.advance.domain.Person;
import org.zkoss.reference.developer.mvvm.advance.domain.PersonDao;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

public class DynamicCollectionBindingComposer extends SelectorComposer {

	private Binder binder = new DefaultBinder();
	
	@Wire("div")
	private Div div;
	
	@Wire("listbox")
	private Listbox listbox;
	@Wire("button[label='Print']")
	private Button printButton;
	@Wire("#selected")
	private Label selectedLabel;
	@Wire("#result")
	private Label resultLabel;

	private PersonDao personDao = new PersonDao();
	private ListModelList<Person> personList;
	private Person selectedPerson = null; 
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		personDao.generateData(30);
		personList = new ListModelList(personDao.findAll());
		
		binder.init(div, this, null);
		
		div.setAttribute("selectedPerson", selectedPerson);

		//add data binding
		binder.addPropertyLoadBindings(listbox, "selectedItem", "selectedPerson"
				, null, null, null, null, null);
		binder.addPropertySaveBindings(listbox, "selectedItem", "selectedPerson"
				, null, null, null, null, null, null, null);
		binder.addPropertyLoadBindings(selectedLabel, "value", "selectedPerson.firstName", null, null, null, null, null);
		
		listbox.setModel(personList);
		listbox.setItemRenderer(new MyListboxRenderer());
		
		binder.loadComponent(div, false);//load beans' data to initialize components
	}
	
	
	@Listen("onClick=button[label='Print']")
	public void print(){
		StringBuilder result = new StringBuilder();
		for (Person p : personList){
			result.append(p.getFirstName()+" "+p.getLastName()+"\n");
		}
		resultLabel.setValue(result.toString());
	}

	public ListModelList<Person> getPersonList() {
		return personList;
	}

	public Person getSelectedPerson() {
		return selectedPerson;
	}

	public void setSelectedPerson(Person selectedPerson) {
		this.selectedPerson = selectedPerson;
	}

	class MyListboxRenderer implements ListitemRenderer{

		public void render(Listitem listitem, Object data, int index)
				throws Exception {

			//store the data bean as an attribute which can be accessed by EL expression
			listitem.setAttribute("bean", data);
			
			//first name
			Listcell fnCell = new Listcell();
			listitem.appendChild(fnCell);
			Textbox fnBox = new Textbox();
			fnBox.setInplace(true);
			fnCell.appendChild(fnBox);
			binder.addPropertyLoadBindings(fnBox, "value", "bean.firstName"
					, null, null, null, null, null);
			binder.addPropertySaveBindings(fnBox, "value", "bean.firstName"
					, null, null, null, null, null, null, null);

			//last name
			Listcell lnCell = new Listcell();
			listitem.appendChild(lnCell);
			Textbox lnBox = new Textbox();
			lnBox.setInplace(true);
			lnCell.appendChild(lnBox);
			binder.addPropertyLoadBindings(lnBox, "value", "bean.lastName", null, null, null, null, null);
			binder.addPropertySaveBindings(lnBox, "value", "bean.lastName", null, null, null, null, null, null, null);

			//age
			Listcell ageCell = new Listcell();
			listitem.appendChild(ageCell);
			Intbox ageBox = new Intbox();
			ageBox.setInplace(true);
			ageCell.appendChild(ageBox);
			binder.addPropertyLoadBindings(ageBox, "value", "bean.age", null, null, null, null, null);
			binder.addPropertySaveBindings(ageBox, "value", "bean.age", null, null, null, null, null, null, null);

			//delete button
			Listcell actionCell = new Listcell();
			listitem.appendChild(actionCell);
			Button button = new Button();
			button.setLabel("Delete");
			actionCell.appendChild(button);
			
			button.addEventListener("onClick", new EventListener<MouseEvent>() {
				public void onEvent(MouseEvent event) throws Exception {
					Person p = (Person)event.getTarget().getAttribute("bean", true);
					personList.remove(p);
				}
			});
		}
		
	}
}
