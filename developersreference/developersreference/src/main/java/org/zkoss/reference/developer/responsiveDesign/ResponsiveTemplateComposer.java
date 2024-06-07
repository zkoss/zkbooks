package org.zkoss.reference.developer.responsiveDesign;

import java.util.Arrays;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.xel.VariableResolver;
import org.zkoss.xel.XelException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Templates;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Vlayout;
import org.zkoss.zuti.zul.ForEach;

public class ResponsiveTemplateComposer extends SelectorComposer<Component> {
	// Hold the state name
	private String viewTemplate = "";
	// Hold the List of data to be displayed
	private ListModelList<DataEntry> dataModel;
	
	@Wire
	private Grid myGrid;
	
	@Wire
	private Columns myColumns;
	
	@Wire
	private Panel myPanel;
	
	//wire shadow component "forEach", the shadow root directly under the main shadow host
	@Wire(":host::shadow#staticPanelForEach")
	private ForEach staticPanelForEach;
	
	//OPTIONAL: Choose between template renderer, and direct renderer to use templates from zul, or direct Java component creation from renderer
	private RowRenderer<DataEntry> myResponsiveDirectRenderer;
	private RowRenderer<DataEntry> myResponsiveTemplateRenderer;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp); //super afterCompose wires components and listeners automatically
		initModel();//create data
		staticPanelForEach.setItems(dataModel);//set forEach model (same as <forEach items="@init(dataModel)"> in zul MVVM)
		staticPanelForEach.recreate();//invalidate, but for shadow components
		//OPTIONAL: Choose between template renderer, and direct renderer to use templates from zul, or direct Java component creation from renderer
		myGrid.setRowRenderer(getMyResponsiveTemplateRenderer());
		myGrid.setModel(dataModel);
		drawGridView();
		comp.getPage().getFirstRoot().addEventListener(Events.ON_CLIENT_INFO, (ClientInfoEvent event) ->{
			int width = event.getDesktopWidth();
			String resolvedviewTemplate = getTemplateFromWidth(width);
			
			if(!viewTemplate.equals(resolvedviewTemplate)) { //avoid unnecessary redraws
				viewTemplate = resolvedviewTemplate;
				if(viewTemplate.equals("under400")) {
					myGrid.setVisible(false);
					myPanel.setVisible(true);
				}else {
					myPanel.setVisible(false);
					myGrid.setVisible(true);
					drawGridView();
				}
			}
		});
	}
	
	//OPTIONAL - ENABLED BY DEFAULT
	//This renderer creates the content of the rows using the templates provided from ZUL
	private RowRenderer<DataEntry> getMyResponsiveTemplateRenderer() {
		if (myResponsiveTemplateRenderer == null) {
			myResponsiveTemplateRenderer = new RowRenderer<DataEntry>() {
				@Override
				public void render(Row row, DataEntry data, int index) throws Exception {
					row.setAttribute("each", data); // add "each" attribute on row
					Templates.lookup(myGrid, viewTemplate).create(row, null, null, null); //creates component using templates, have access to row's ${each} attribute set above
				}
			};
		}
		return myResponsiveTemplateRenderer;
	}
	
	//OPTIONAL - DISABLED BY DEFAULT
	//other row renderer option: create rows by Java using renderer access to components
	private RowRenderer<DataEntry> getMyResponsiveDirectRenderer() {
		/* direct row rendering*/
		if (myResponsiveDirectRenderer == null) {
			myResponsiveDirectRenderer = new RowRenderer<DataEntry>() {
				@Override
				public void render(Row row, DataEntry data, int index) throws Exception {
					switch (viewTemplate) {
					case "400to500":
						addAllCells(row,Arrays.asList(data.getFirstName()));
						addAllDetails(row,Arrays.asList(Integer.toString(data.getAge()),data.getPosition(),data.getDepartment(),data.getUserId(),data.getDeskNumber()));
						break;
					case "500to600":
						addAllCells(row,Arrays.asList(data.getFirstName(),Integer.toString(data.getAge())));
						addAllDetails(row,Arrays.asList(data.getPosition(),data.getDepartment(),data.getUserId(),data.getDeskNumber()));
						break;
					case "600to700":
						addAllCells(row,Arrays.asList(data.getFirstName(),Integer.toString(data.getAge()),data.getPosition()));
						addAllDetails(row,Arrays.asList(data.getDepartment(),data.getUserId(),data.getDeskNumber()));
						break;
					case "700to800":
						addAllCells(row,Arrays.asList(data.getFirstName(),Integer.toString(data.getAge()),data.getPosition(),data.getDepartment()));
						addAllDetails(row,Arrays.asList(data.getUserId(),data.getDeskNumber()));
						break;
					case "over800":
						addAllCells(row,Arrays.asList(data.getFirstName(),Integer.toString(data.getAge()),data.getPosition(),data.getDepartment(),data.getUserId(),data.getDeskNumber()));
						break;
					default:
						addAllCells(row,Arrays.asList(data.getFirstName(),Integer.toString(data.getAge()),data.getPosition(),data.getDepartment(),data.getUserId(),data.getDeskNumber()));;
					}
				}
			};
		}
		return myResponsiveDirectRenderer;
	}
	
	//OPTIONAL: Used with direct renderer only, not used in default case
	//create the cells for a row, containing the item Data
	private void addAllCells(Row row, List<String> cellValueList) {
		cellValueList.forEach(item ->{
			Cell cell = new Cell();
			cell.appendChild(new Label(item));
			row.appendChild(cell);
		});
	}

	//OPTIONAL: Used with direct renderer only, not used in default case
	//create a master detail component, containing the item Data inside of a Vlayout
	private void addAllDetails(Row row,List<String> detailValueList) {
		Detail detail = new Detail();
		Vlayout vlayout = new Vlayout();
		detailValueList.forEach(item ->{
			vlayout.appendChild(new Label(item));
		});
		detail.appendChild(vlayout);
		row.appendChild(detail);
	}

	private void drawGridView() {
		Components.removeAllChildren(myColumns); //destroy columns content
		Templates.lookup(myColumns,"header"+viewTemplate).create(myColumns, null, null, null); //create the appropriate columns from templates
		myGrid.setModel((ListModelList)null); //set model to null, then back to regular model to purge all cached rows
		myGrid.setModel(dataModel);
	}
	
	//convert width intervals to template names
	private String getTemplateFromWidth(int width) {
		if(width < 400)
			return "under400";
		if(width < 500)
			return "400to500";
		if(width < 600)
			return "500to600";
		if(width < 700)
			return "600to700";
		if(width < 800)
			return "700to800";
		return "over800";
	}

	
	//Called by the View to perform any [action]
	@Command("doAction")
	public void doAction(@BindingParam("target")DataEntry target){
		Clients.log(String.format("Did action for user %s",target.getFirstName()));
	}
	
	//Provide an example of data and set the default template if no screen information is available.
	public void initModel(){
		dataModel = new ListModelList<DataEntry>();
		dataModel.add(new DataEntry("MISA0123","Alice",30,"Engineer","MIS","MI1"));
		dataModel.add(new DataEntry("MKTB041","Bob",47,"Manager","Marketing","MK14"));
		dataModel.add(new DataEntry("MISC0331","Charly",21,"Intern","MIS","MI119"));
		dataModel.add(new DataEntry("SALD0356","Dan",39,"Team leader","Sales","SL32"));
		dataModel.add(new DataEntry("DIRE001","Erin",44,"CEO","Direction","D1"));
		viewTemplate = "over800";
	}
	
	//single listener for forwarded events
	@Listen("onItemClick=#myHost")
	public void doPanelItemClick(ForwardEvent event) {
		DataEntry data =  (DataEntry)event.getData(); //retrieve parameter data from the event, same on all templates
		Clients.log("Clicked on item " + data.getFirstName());
	}
	
}
