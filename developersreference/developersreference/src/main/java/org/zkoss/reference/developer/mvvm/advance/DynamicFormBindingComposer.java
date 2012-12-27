package org.zkoss.reference.developer.mvvm.advance;

import org.zkoss.bind.Binder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class DynamicFormBindingComposer extends SelectorComposer {

	private Binder binder;
	@Wire("div")
	private Div div;
	@Wire("grid")
	private Grid grid;
	
	@Wire("#fn") 
	private Textbox firstNameBox;
	@Wire("#fnError")
	private Label fNameErrorLabel;
	@Wire("#ln") 
	private Textbox lastNameBox;
	@Wire("#lnError")
	private Label lNameErrorLabel;
	@Wire("intbox")
	private Intbox ageBox;
	
	@Wire("#fnLabel")
	private Label firstNameLabel;
	@Wire("#lnLabel")
	private Label lastNameLabel;
	@Wire("#ageLabel")
	private Label ageLabel;
	
	@Wire("button[label='Submit']")
	private Button button;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		binder = (Binder)div.getAttribute("binder");
		
		String[] command = {"submit"};
		binder.addFormLoadBindings(grid, "fx", "vm.person", null, null, null);
		binder.addFormSaveBindings(grid, "fx", "vm.person", command, null, null, null, null);
		
		binder.addPropertyLoadBindings(firstNameBox, "value", "fx.firstName", null, null, null, null, null);
		binder.addPropertySaveBindings(firstNameBox, "value", "fx.firstName", null, null, null, null, null,"vm.emptyValidator",null);
		binder.addPropertyLoadBindings(fNameErrorLabel, "value", "vmsgs[fn]", null, null, null, null, null);
		binder.addPropertyLoadBindings(lastNameBox, "value", "fx.lastName", null, null, null, null, null);
		binder.addPropertySaveBindings(lastNameBox, "value", "fx.lastName", null, null, null, null, null,"vm.emptyValidator",null);
		binder.addPropertyLoadBindings(lNameErrorLabel, "value", "vmsgs[ln]", null, null, null, null, null);
		binder.addPropertyLoadBindings(ageBox, "value", "fx.age", null, null, null, null, null);
		binder.addPropertySaveBindings(ageBox, "value", "fx.age", null, null, null, null, null,null,null);
		
		binder.addPropertyLoadBindings(firstNameLabel, "value", "vm.person.firstName", null, command, null, null, null);
		binder.addPropertyLoadBindings(lastNameLabel, "value", "vm.person.lastName", null, command, null, null, null);
		binder.addPropertyLoadBindings(ageLabel, "value", "vm.person.age", null, command, null, null, null);
		
		binder.addCommandBinding(button, Events.ON_CLICK, "'submit'", null);
		
		binder.loadComponent(grid, true);
	}
	
}
