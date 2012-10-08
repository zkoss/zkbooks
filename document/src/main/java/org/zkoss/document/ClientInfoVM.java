package org.zkoss.document;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.ClientInfoEvent;

public class ClientInfoVM {

	private String information; 
	
	@Command
	@NotifyChange("information")
	public void showClientInfo(@ContextParam(ContextType.TRIGGER_EVENT) ClientInfoEvent event){
		information = "Orientation: "+event.getOrientation()
				+",Desktop width & height: "+event.getDesktopWidth()+","+event.getDesktopHeight()
				+",Screen width & height: "+event.getScreenWidth()+","+event.getScreenHeight();
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}
}
