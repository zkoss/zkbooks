/**
 * 
 */
package org.zkoss.zkspringessentials.beans;

import org.springframework.context.annotation.Scope;

/**
 * @author ashish
 *
 */
@org.springframework.stereotype.Component("msgBean")
@Scope("desktop")
public class SimpleMessageBean {

	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
