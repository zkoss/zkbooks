/**
 * 
 */
package org.zkoss.zkspringessentials.controller;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Window;

public class DynamicSecureController extends GenericForwardComposer {

	
	Window win;
	
	public void onClick$secure(Event evt) {
		Executions.createComponents("secure/index.zul", win, arg);
	}
	
	public void onClick$exSecure(Event evt) {
		Executions.createComponents("secure/extreme/index.zul", win, arg);
	}
	
}
