package org.zkoss.reference.developer.mvc;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.ui.select.annotation.Subscribe;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Separator;

public class SubscribeComposer extends SelectorComposer<Component>{
	
	@Wire
	Div msg;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		/*
		EventQueue<Event> eq = EventQueues.lookup("queue1", EventQueues.DESKTOP, true);
		EventListener<Event> listener = new EventListener<Event>() {
			
			public void onEvent(Event event) throws Exception {
				String id = event.getTarget().getDesktop().getId();
				msg.appendChild(new Label(id));
				msg.appendChild(new Separator());
				System.out.println(id);
			}
		};
		eq.subscribe(listener);
		 */
	}

	/**
	 * @Subscribe can automatically un-subscribe a listener, when component of composer was detached. 
	 * @param event
	 */
	@Subscribe(value = "queue1", scope = EventQueues.DESKTOP)
	public void listener(Event event) {
	    String id = event.getTarget().getDesktop().getId();
		msg.appendChild(new Label(id));
		msg.appendChild(new Separator());
		System.out.println(id);
	    
	}
	
	@Listen("onClick = #send")
	public void publish(MouseEvent e) {
		EventQueue<Event> eq = EventQueues.lookup("queue1", EventQueues.DESKTOP, true);
		eq.publish(new Event("onMyEvent", e.getTarget(),null));
	}
}
