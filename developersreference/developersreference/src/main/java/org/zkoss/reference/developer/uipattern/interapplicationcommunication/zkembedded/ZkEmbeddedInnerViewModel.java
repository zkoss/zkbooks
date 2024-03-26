package org.zkoss.reference.developer.uipattern.interapplicationcommunication.zkembedded;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.util.Clients;

public class ZkEmbeddedInnerViewModel {
	
	private String outerValue;
	private String textValue;
	
    @Init
    public void init(@QueryParam("param1") String parm1) {
    	this.outerValue = parm1;
    	textValue = "some text";
    }
	
    @Command
    public void sendDataToOuterPage() {
    	Clients.evalJavaScript("window.sendMessageToParent('"+textValue+"')");
    }
    
	public String getOuterValue() {
		return outerValue;
	}

	public void setOuterValue(String outerValue) {
		this.outerValue = outerValue;
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
	
}
