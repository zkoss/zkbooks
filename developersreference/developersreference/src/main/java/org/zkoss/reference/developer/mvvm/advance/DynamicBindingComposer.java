package org.zkoss.reference.developer.mvvm.advance;

import org.w3c.dom.events.EventTarget;
import org.zkoss.bind.Binder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class DynamicBindingComposer extends SelectorComposer {

	private Binder binder;
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
	
	@Wire("button[label='Submit']")
	private Button button;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		binder = (Binder)grid.getAttribute("binder");
		
		binder.addPropertyLoadBindings(firstNameBox, "value", "vm.person.firstName", null, null, null, null, null);
		binder.addPropertySaveBindings(firstNameBox, "value", "vm.person.firstName", null, null, null, null, null,null,null);
		binder.addPropertyLoadBindings(lastNameBox, "value", "vm.person.lastName", null, null, null, null, null);
		binder.addPropertySaveBindings(lastNameBox, "value", "vm.person.lastName", null, null, null, null, null,null,null);
		binder.addPropertyLoadBindings(ageBox, "value", "vm.person.age", null, null, null, null, null);
		binder.addPropertySaveBindings(ageBox, "value", "vm.person.age", null, null, null, null, null,null,null);
		
		String[] command = {"submit"};
		binder.addPropertyLoadBindings(firstNameLabel, "value", "vm.person.firstName", null, command, null, null, null);
		binder.addPropertyLoadBindings(lastNameLabel, "value", "vm.person.lastName", null, command, null, null, null);
		binder.addPropertyLoadBindings(ageLabel, "value", "vm.person.age", null, command, null, null, null);
		
		binder.addCommandBinding(button, Events.ON_CLICK, "'submit'", null);
		
		binder.loadComponent(grid, true);
	}
	
}
