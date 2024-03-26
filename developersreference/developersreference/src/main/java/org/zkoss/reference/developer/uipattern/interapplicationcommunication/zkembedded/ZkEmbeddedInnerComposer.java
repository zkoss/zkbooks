package org.zkoss.reference.developer.uipattern.interapplicationcommunication.zkembedded;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;

public class ZkEmbeddedInnerComposer extends SelectorComposer<Component> {

	@Wire
	private Textbox inputTextbox;
	
	@Listen(Events.ON_CLICK+"=#sendToOuterButton")
	public void sendToOuterPage() {
		Clients.evalJavaScript("window.sendMessageToParent('"+inputTextbox.getValue()+"')");
	}
	
}
