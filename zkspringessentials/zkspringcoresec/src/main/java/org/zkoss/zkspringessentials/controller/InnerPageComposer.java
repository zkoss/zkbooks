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

@Component("innerPageComposer")
@Scope("idspace")
public class InnerPageComposer extends GenericSpringComposer {

	@Autowired 
	private Button showBtn;
	
	@Autowired
	private Label someLbl;
	
	public void doAfterCompose(org.zkoss.zk.ui.Component comp) throws Exception {
		System.out.println("Calling doAfterCompose");
		super.doAfterCompose(comp);
	}
	@EventHandler("showBtn.onClick")
	public void runDemo(Event event) throws InterruptedException {
		Messagebox.show("Hello");
		someLbl.setValue("Showing Message:");
	}
}
