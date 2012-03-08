package org.zkoss.zkspringessentials.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zkoss.spring.context.annotation.EventHandler;
import org.zkoss.spring.util.GenericSpringComposer;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

@Component("windowComposer")
@Scope("desktop")
//public class WindowComposer implements Composer { //extends GenericSpringComposer {
public class WindowComposer extends GenericSpringComposer {

	@Autowired 
	private Button myBtn;
	
	@Autowired
	private Label myLbl;
	
	@Autowired
	private Label lastRanLbl;
	
	private String msg;

	public String getMsg() {
		return msg;// + myBtn.getId();
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public void doAfterCompose(org.zkoss.zk.ui.Component comp) throws Exception {
		super.doAfterCompose(comp);
		System.out.println("Calling doAfterCompose");
		myLbl.setValue("Running:");
	}
	@EventHandler("myBtn.onClick")
	public void runDemo(Event evt) throws InterruptedException {
		Messagebox.show("Running Demo");
		lastRanLbl.setValue("Demo visited last time at:" + System.currentTimeMillis());
	}
}
