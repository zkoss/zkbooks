package org.zkoss.reference.developer.mvvm.databinding.form;
/* OrderVM.java

	Purpose:
		
	Description:
		
	History:
		2011/10/31 Created by Dennis Chen

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */


import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;


/**
 * @author dennis
 * 
 */
public class OrderVM3 extends OrderVM2{

	//message for confirming the deletion.
	String deleteMessage;
	
	public String getDeleteMessage(){
		return deleteMessage;
	}
	
	@Override
	@NotifyChange({"selected","orders","validationMessages","deleteMessage"})
	@Command
	public void deleteOrder(){
		super.deleteOrder();
		deleteMessage = null;
	}
	
	@NotifyChange("deleteMessage")
	@Command
	public void confirmDelete(){
		//set the message to show to user
		deleteMessage = "Do you want to delete "+selected.getId()+" ?";
	}
	
	@NotifyChange("deleteMessage")
	@Command
	public void cancelDelete(){
		//clear the message
		deleteMessage = null;
	}
	
}
