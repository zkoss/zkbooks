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

	public static final String ON_DO_OPERATION = "onDoOperation";

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp.addEventListener(ON_DO_OPERATION, event ->{
			Threads.sleep(3000);
			Clients.clearBusy();
			Notification.show("operation finished");
		});
	}
	
	@Listen("onClick = #trigger")
	public void triggerLongOperation() {
		Clients.showBusy("Doing loing task, please wait");
		Events.echoEvent(ON_DO_OPERATION, getSelf(), null);
	}
	
}
