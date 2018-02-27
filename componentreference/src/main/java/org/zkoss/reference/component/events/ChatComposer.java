package org.zkoss.reference.component.events;

import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.ui.select.annotation.Subscribe;
import org.zkoss.zul.*;

import java.util.*;

public class ChatComposer extends SelectorComposer<Window> {

	private static final long serialVersionUID = 4732207200308407419L;
	
	@Wire
	private Window win;
	@Wire
	private Textbox user;
	@Wire
	private Textbox message;
	@Wire
	private Vlayout info;
	
	@Listen("onChange = #message; onOK = #message")
	public void post(Event event) {
		EventQueue<Event> que = EventQueues.lookup("chat", EventQueues.APPLICATION, true);
		String text = message.getValue();
		String username = user.getValue();
		if ("".equals(username))
			username = "Unknow user";
		Map<String, String> data = new HashMap<String, String>();
		data.put("username", username + ": ");
		if (text.length() > 0) {
			message.setValue("");
			data.put("text", text);
			que.publish(new Event("onChat", null, data));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Subscribe(value = "chat", scope = EventQueues.APPLICATION)
	public void chat(Event event) {
		Map<String, String> data = (Map<String, String>) event.getData();
		new Label(data.get("username") + data.get("text")).setParent(info);
	}
	
	@Listen("onVisibilityChange = #win")
	public void changeVisibility(Event event) {
		VisibilityChangeEvent evt = null;
		if (event instanceof VisibilityChangeEvent) {
			evt = (VisibilityChangeEvent) event;
			
			String username = user.getValue();
			if ("".equals(username))
				username = "Unknow user";
			Map<String, String> data = new HashMap<String, String>();
			data.put("username", username);
			data.put("status", evt.isHidden() ? " Leaved" : " Come back");
			
			EventQueue<Event> que = EventQueues.lookup("visibilityChange", EventQueues.APPLICATION, true);
			que.publish(new Event("onShowNotification", null, data));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Subscribe(value = "visibilityChange", scope = EventQueues.APPLICATION)
	public void showNotification(Event event) {
		Map<String, String> data = (Map<String, String>) event.getData();
		String username = data.get("username");
		if (!username.equals(user.getValue()))
			Clients.showNotification(data.get("username") + data.get("status"), "info", null, null, 1500);
	}
}
