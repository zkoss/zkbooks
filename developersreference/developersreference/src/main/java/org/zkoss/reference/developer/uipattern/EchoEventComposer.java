package org.zkoss.reference.developer.uipattern;

import org.zkoss.lang.Threads;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.Notification;

public class EchoEventComposer extends SelectorComposer<Component> {

	private Component comp;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		this.comp = comp;
		super.doAfterCompose(comp);
		comp.addEventListener("onLater", event ->{
			Threads.sleep(3000);
			Clients.clearBusy();
			Notification.show("operation finished");
		});
	}
	
	@Listen("onClick=#btn")
	public void doButtonClick() {
		Clients.showBusy("Doing loing task, please wait");
		Events.echoEvent("onLater", comp, null);
	}
	
}
