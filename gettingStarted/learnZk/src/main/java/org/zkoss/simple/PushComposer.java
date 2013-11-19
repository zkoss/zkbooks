package org.zkoss.simple;


import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

@SuppressWarnings("serial")
public class PushComposer extends SelectorComposer<Window> {


	@WireVariable
	private Desktop desktop;

	@Wire("#acceptTermBox")
	private Checkbox acceptTermCheckbox;
	@Wire("#percentage")
	private Label percentage;
	@Wire("#progressBar")
	private Progressmeter progressBar;
	@Wire("#progressRow")
	private Row progressRow;
	@Wire("#submitButton")
	private Button submitButton;

	@Listen("onCheck = #acceptTermBox")
	public void changeSubmitStatus(){
		if (acceptTermCheckbox.isChecked()){
			submitButton.setDisabled(false);
			submitButton.setImage("/images/submit.png");
		}else{
			submitButton.setDisabled(true);
			submitButton.setImage("");
		}
	}

	@Listen("onClick = #submitButton")
	public void doLong(){
		if (EventQueues.exists("longop")) {
			return; //busy
		}
		final EventQueue<Event> queue = EventQueues.lookup("longop"); //create a queue
		
		//subscribe async listener to handle long operation
		queue.subscribe(new EventListener<Event>() {
			public void onEvent(Event evt) {
				if ("doLongOp".equals(evt.getName())) {
					for (int i=0 ; i<=10 ; i=i+2){
						reportProgress(i*10);
						org.zkoss.lang.Threads.sleep(1000);
					}
				}
			}
			
			private void reportProgress(int percentage){
				try{
					Executions.activate(desktop);
					queue.publish(new Event("update",null, percentage));
				}catch (Exception e) {
					e.printStackTrace();
				}finally{
					Executions.deactivate(desktop);
				}
			}
		}, true); //asynchronous  

		queue.subscribe(new EventListener<Event>() {
			public void onEvent(Event evt) {
				if ("update".equals(evt.getName())){
					Integer porgressValue = (Integer)evt.getData();
					progressBar.setValue(porgressValue);
					percentage.setValue(porgressValue.toString());
					if (porgressValue==100){
						submitButton.setDisabled(false);
						progressRow.setVisible(false);
					}
				}
			}
		}); //synchronous
		queue.publish(new Event("doLongOp")); //kick off the long operation
		progressRow.setVisible(true);
	}
}
