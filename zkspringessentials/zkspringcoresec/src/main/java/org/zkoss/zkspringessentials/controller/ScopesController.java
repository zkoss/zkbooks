/**
 * 
 */
package org.zkoss.zkspringessentials.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkspringessentials.beans.SimpleMessageBean;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

/**
 * @author ashish
 *
 */
@org.springframework.stereotype.Component("scopesCtrl1")
@Scope("desktop")
public class ScopesController extends GenericForwardComposer {

	@Autowired
	private SimpleMessageBean msgBean;
	
	private Textbox name;
	private Button setAppNameBtn;
	private Button showAppNameBtn;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
	
	public void onClick$setAppNameBtn(Event evt) {
		msgBean.setMsg(name.getValue());
	}
	
	public void onClick$showAppNameBtn(Event evt) throws InterruptedException {
		Messagebox.show(msgBean.getMsg());
	}
}
