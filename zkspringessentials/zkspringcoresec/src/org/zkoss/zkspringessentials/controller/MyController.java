/* MyController.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Apr 28, 2009 3:19:54 PM, Created by henrichen
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/

package org.zkoss.zkspringessentials.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.spring.context.annotation.AppliedTo;
import org.zkoss.spring.context.annotation.EventHandler;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;
import javax.annotation.Resource;

/**
 * @author henrichen
 *
 */
@Scope("idspace")
@Controller @AppliedTo("myWindow")
public class MyController {
	@Resource
	protected Window myWindow;
	
	@Resource
	protected Label myLabel;
	
	@EventHandler("btn.onClick")
	public void doShowWindow() {
		myLabel.setValue("["+myWindow+"]:"+myWindow.hashCode());
	}
	
}
