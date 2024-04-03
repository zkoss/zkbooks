package org.zkoss.reference.developer.responsive;

import org.zkoss.zul.ListModelList;

public class ResponsiveGrid {

	protected ListModelList<Employee> myModel = new ListModelList<Employee>();
	private String currentTemplate = "8cols";
	
	public ResponsiveGrid(){
		myModel.add(new Employee("Airi", "Satou", "Accountant", "Tokyo", 33, "2008/11/28", 162700, 5407));
		myModel.add(new Employee("Angelica", "Ramos", "Chief Executive Officer (CEO)", "London", 47, "2009/10/09", 1200000, 5797));
		myModel.add(new Employee("Asthon", "Cox", "Junior Technical Author", "San Francisco", 66, "2009/01/12", 86000, 1562));
		myModel.add(new Employee("Bradley", "Greer", "Software Engineer", "London", 41, "2012/10/13", 132000, 2558));
	}
	

	public ListModelList<Employee> getMyModel() {
		return myModel;
	}

	public void setMyModel(ListModelList<Employee> myModel) {
		this.myModel = myModel;
	}

}
