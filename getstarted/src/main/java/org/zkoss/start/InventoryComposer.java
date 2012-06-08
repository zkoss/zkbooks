/* SearchVM.java

	Purpose:
		
	Description:
		
	History:
		2011/10/25 Created by Dennis Chen

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.start;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
/**
 * 
 * @author Hawk
 */

@SuppressWarnings("serial")
public class InventoryComposer extends SelectorComposer<Component>{
	//the search result
	private ListModelList<Item> items = new ListModelList<Item>();

	private InventoryService inventoryService = new InventoryService();
	//the selected item
	private Item selected;
	//UI component
	@Wire("#filterBox")
	private Textbox filterBox;
	@Wire("button")
	private Button searchButton;
	@Wire("listbox")
	private Listbox itemListbox;
	@Wire("groupbox")
	private Groupbox detailBox;
	@Wire("caption")
	private Caption detailCaption;
	
	@Wire("#nameBox")
	private Textbox nameBox;
	@Wire("#descriptionLabel")
	private Textbox descriptionBox;
	@Wire("#costLabel")
	private Doublebox costBox;
	@Wire("#quantityLabel")
	private Intbox quantityBox;
	
	@Wire("hbox")
	private Hbox filterArea;
	
	

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		//get all items
		reload();
	}
	
	private void reload(){
		items.addAll(inventoryService.find("*"));
		itemListbox.setModel(items);
	}
	
	@Listen("onClick = button[label='Save']")
	public void save(){
		selected.setName(nameBox.getValue());
		selected.setDescription(descriptionBox.getValue());
		selected.setCost(costBox.getValue());
		selected.setQuantity(quantityBox.getValue());
		itemListbox.setModel(items);
	}
	
	//filter area related event handler
	
	@Listen("onClick = button[label='Search']")
	public void filter(){
		items = new ListModelList<Item>();
		items.addAll(inventoryService.find(filterBox.getValue()));
		itemListbox.setModel(items);
		detailBox.setVisible(false);
		//hide filter area
		filterArea.setVisible(false);
	}
	
	@Listen("onChange = #filterBox")
	public void changeButtonStatus(){
		searchButton.setDisabled(filterBox.getValue().length()==0);
	}
	
	@Listen("onSelect = listbox")
	public void selectItem(){
		selected = items.get(itemListbox.getSelectedIndex());
		//display item detail
		detailBox.setVisible(true);
		nameBox.setValue(selected.getName());
		descriptionBox.setValue(selected.getDescription());
		costBox.setValue(selected.getCost());
		quantityBox.setValue(selected.getQuantity());
		quantityBox.setSclass(selected.getQuantity()<3?"red":"");
	}
	
	@Listen("onClick = menuitem[label='Filter']")
	public void showFilterArea(){
		filterArea.setVisible(true);
	}
}
