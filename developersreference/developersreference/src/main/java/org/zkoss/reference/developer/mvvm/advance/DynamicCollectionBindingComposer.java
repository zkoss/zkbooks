package org.zkoss.reference.developer.mvvm.advance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.AnnotateBinder;
import org.zkoss.bind.Binder;
import org.zkoss.bind.DefaultBinder;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.reference.developer.mvvm.advance.domain.Person;
import org.zkoss.reference.developer.mvvm.advance.domain.PersonDao;
import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
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
	private List<Person> personList;
	private Person selectedPerson = null; 
	private String printedResult;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		personDao.generateData(20);
		personList = personDao.findAll();
		
		binder.init(div, this, null);
		
		div.setAttribute("vm", this);

		//add data binding
		binder.addPropertyLoadBindings(listbox, "model", "vm.personList", null, null, null, null, null);
		binder.addPropertyLoadBindings(listbox, "selectedItem", "vm.selectedPerson"
				, null, null, null, null, null);
		binder.addPropertySaveBindings(listbox, "selectedItem", "vm.selectedPerson"
				, null, null, null, null, null, null, null);
		
		binder.addCommandBinding(printButton, Events.ON_CLICK, "'print'", null);
		
		binder.addPropertyLoadBindings(selectedLabel, "value", "vm.selectedPerson.firstName", null, null, null, null, null);
		binder.addPropertyLoadBindings(resultLabel, "value", "vm.printedResult", null, null, null, null, null);
		
		listbox.setItemRenderer(new MyListboxRenderer());
//		listbox.setTemplate("model", new MyListboxTemplate());
		
		binder.loadComponent(div, true);
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
	
	
	
	
	class MyListboxRenderer implements ListitemRenderer{

		public void render(Listitem listitem, Object data, int index)
				throws Exception {

			//TODO explain why
			listitem.setAttribute("each", data);
			
			//first name
			Listcell fnCell = new Listcell();
			listitem.appendChild(fnCell);
			Textbox fnBox = new Textbox();
			fnBox.setInplace(true);
			fnCell.appendChild(fnBox);
			binder.addPropertyLoadBindings(fnBox, "value", "each.firstName", null, null, null, null, null);
			binder.addPropertySaveBindings(fnBox, "value", "each.firstName", null, null, null, null, null, null, null);

			//last name
			Listcell lnCell = new Listcell();
			listitem.appendChild(lnCell);
			Textbox lnBox = new Textbox();
			lnBox.setInplace(true);
			lnCell.appendChild(lnBox);
			binder.addPropertyLoadBindings(lnBox, "value", "each.lastName", null, null, null, null, null);
			binder.addPropertySaveBindings(lnBox, "value", "each.lastName", null, null, null, null, null, null, null);

			//age
			Listcell ageCell = new Listcell();
			listitem.appendChild(ageCell);
			Intbox ageBox = new Intbox();
			ageBox.setInplace(true);
			ageCell.appendChild(ageBox);
			binder.addPropertyLoadBindings(ageBox, "value", "each.age", null, null, null, null, null);
			binder.addPropertySaveBindings(ageBox, "value", "each.age", null, null, null, null, null, null, null);

			//delete button
			Listcell actionCell = new Listcell();
			listitem.appendChild(actionCell);
			Button button = new Button();
			button.setLabel("Delete");
			actionCell.appendChild(button);

			Map<String, Object> commandArgs = new HashMap<String, Object>();
			commandArgs.put("index", index);
			binder.addCommandBinding(button, Events.ON_CLICK, "'delete'", commandArgs);

		}
		
	}
	
	class MyListboxTemplate implements Template{
		
		public Component[] create(Component parent, Component insertBefore,
				VariableResolver resolver, Composer arg3) {
			Listitem listitem = new Listitem();
			
			//first name
			Listcell fnCell = new Listcell();
			listitem.appendChild(fnCell);
			Textbox fnBox = new Textbox();
			fnBox.setInplace(true);
			fnCell.appendChild(fnBox);
			binder.addPropertyLoadBindings(fnBox, "value", "each.firstName", null, null, null, null, null);
			binder.addPropertySaveBindings(fnBox, "value", "each.firstName", null, null, null, null, null, null, null);

			//last name
			Listcell lnCell = new Listcell();
			listitem.appendChild(lnCell);
			Textbox lnBox = new Textbox();
			lnBox.setInplace(true);
			lnCell.appendChild(lnBox);
			binder.addPropertyLoadBindings(lnBox, "value", "each.lastName", null, null, null, null, null);
			binder.addPropertySaveBindings(lnBox, "value", "each.lastName", null, null, null, null, null, null, null);
			
			//age
			Listcell ageCell = new Listcell();
			listitem.appendChild(ageCell);
			Intbox ageBox = new Intbox();
			ageBox.setInplace(true);
			ageCell.appendChild(ageBox);
			binder.addPropertyLoadBindings(ageBox, "value", "each.age", null, null, null, null, null);
			binder.addPropertySaveBindings(ageBox, "value", "each.age", null, null, null, null, null, null, null);
			
			//delete button
			Listcell actionCell = new Listcell();
			listitem.appendChild(actionCell);
			Button button = new Button();
			button.setLabel("Delete");
			actionCell.appendChild(button);
			
			Map<String, Object> commandArgs = new HashMap<String, Object>();
			
			//I can't make a "delete" button for each row because the resolver is null
			//it should have a inner resolver of BindListitemRenderer 
//			commandArgs.put("index", resolver.resolveVariable("eachStatus.index"));
			binder.addCommandBinding(button, Events.ON_CLICK, "'delete'", commandArgs);
			
			//append to the parent
			if (insertBefore ==null){
				parent.appendChild(listitem);
			}else{
				parent.insertBefore(listitem, insertBefore);
			}
			
			Component[] components = new Component[1];
			components [0] = listitem;
			
			return components;			
		}
		
		public Map<String, Object> getParameters() {
			return new HashMap<String, Object>();
		}
		
	}
}
