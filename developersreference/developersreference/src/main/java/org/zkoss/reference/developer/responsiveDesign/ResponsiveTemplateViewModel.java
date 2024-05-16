package org.zkoss.reference.developer.responsiveDesign;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.MatchMedia;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;

public class ResponsiveTemplateViewModel {
	// Hold the state name
	private String viewTemplate;
	// Hold the List of data to be displayed
	private ListModelList<DataEntry> dataModel;
	
	@MatchMedia("all and (max-width: 399px)")
	@NotifyChange("viewTemplate")
	public void handleUnder400(@ContextParam(ContextType.TRIGGER_EVENT) ClientInfoEvent event) {
		viewTemplate="under400";
	}
	
	@MatchMedia("all and (min-width: 400px) and (max-width: 499px)")
	@NotifyChange("viewTemplate")
	public void handle400to500(@ContextParam(ContextType.TRIGGER_EVENT) ClientInfoEvent event) {
		viewTemplate="400to500";
	}
	
	@MatchMedia("all and (min-width: 500px) and (max-width: 599px)")
	@NotifyChange("viewTemplate")
	public void handle500to600(@ContextParam(ContextType.TRIGGER_EVENT) ClientInfoEvent event) {
		viewTemplate="500to600";
	}
	
	@MatchMedia("all and (min-width: 600px) and (max-width: 699px)")
	@NotifyChange("viewTemplate")
	public void handle600to700(@ContextParam(ContextType.TRIGGER_EVENT) ClientInfoEvent event) {
		viewTemplate="600to700";
	}
	
	@MatchMedia("all and (min-width: 700px) and (max-width: 799px)")
	@NotifyChange("viewTemplate")
	public void handle700to800(@ContextParam(ContextType.TRIGGER_EVENT) ClientInfoEvent event) {
		viewTemplate="700to800";
	}
	
	@MatchMedia("all and (min-width: 800px)")
	@NotifyChange("viewTemplate")
	public void handleOver800(@ContextParam(ContextType.TRIGGER_EVENT) ClientInfoEvent event) {
		viewTemplate="over800";
	}
	
	//Called by the View to perform any [action]
	@Command("doAction")
	public void doAction(@BindingParam("target")DataEntry target){
		Clients.log(String.format("Did action for user %s",target.getFirstName()));
	}
	
	//Provide an example of data and set the default template if no screen information is available.
	@Init
	public void init(){
		dataModel = new ListModelList<DataEntry>();
		dataModel.add(new DataEntry("MISA0123","Alice",30,"Engineer","MIS","MI1"));
		dataModel.add(new DataEntry("MKTB041","Bob",47,"Manager","Marketing","MK14"));
		dataModel.add(new DataEntry("MISC0331","Charly",21,"Intern","MIS","MI119"));
		dataModel.add(new DataEntry("SALD0356","Dan",39,"Team leader","Sales","SL32"));
		dataModel.add(new DataEntry("DIRE001","Erin",44,"CEO","Direction","D1"));
		viewTemplate = "over800";
	}
	
	public String getViewTemplate() {
		return viewTemplate;
	}

	public ListModelList<DataEntry> getDataModel() {
		return dataModel;
	}
	
}
