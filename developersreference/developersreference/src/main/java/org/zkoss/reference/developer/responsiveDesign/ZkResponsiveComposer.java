package org.zkoss.reference.developer.responsiveDesign;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Radiogroup;

public class ZkResponsiveComposer extends SelectorComposer<Component> {

	//internal state flag
	private boolean bigLayout;
	//selection model used by both states
	private ListModelList<String> selectionModel;
	
	@Wire
	private Div content;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		//building the selection model
		selectionModel = new ListModelList<String>();
		selectionModel.add("choice 1");
		selectionModel.add("choice 2");
		selectionModel.add("choice 3");
		
		//Initialize page with biglayout.
		//This happen during the initial page request, before clientinfo is available
		//A clientinfo event will fire immediately after client initialization.
		handleBigLayout();
		
		//Registering the ClientInfo listener on the root component
		comp.addEventListener(Events.ON_CLIENT_INFO, new EventListener<ClientInfoEvent>() {
			public void onEvent(ClientInfoEvent event) throws Exception {
				if(event.getDesktopWidth()>=1200){
					handleBigLayout();
				}else if(event.getDesktopWidth()<1200){
					handleSmallLayout();
				}
			}
		});
	}
	
	private void handleBigLayout(){
		//check the bigLayout flag to avoid re-rendering if the window is resized while already in big layout
		if(bigLayout)
			return;
		bigLayout = true;
		
		//clear current content
		if(content.getFirstChild()!=null)
			content.getFirstChild().detach();
		
		Listbox lb = new Listbox();
		lb.setModel(selectionModel);
		lb.setMold("select");

		content.appendChild(lb);
	}

	private void handleSmallLayout(){
		//check the bigLayout flag to avoid re-rendering if the window is resized while already in small layout
		if(!bigLayout)
			return;
		bigLayout = false;
		
		//clear current content
		if(content.getFirstChild()!=null)
			content.getFirstChild().detach();
		
		Radiogroup rg = new Radiogroup();
		rg.setModel(selectionModel);
		
		content.appendChild(rg);
	}

}
