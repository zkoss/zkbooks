package org.zkoss.reference.component.input;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.ListModelList;

public class BandboxSelectionPageVm {
	
	private BandboxExampleBean selectedItem;
	private ListModelList<BandboxExampleBean> model;
	
	public BandboxExampleBean getSelectedItem() {
		return selectedItem;
	}
	public void setSelectedItem(BandboxExampleBean selectedItem) {
		this.selectedItem = selectedItem;
	}
	public ListModelList<BandboxExampleBean> getModel() {
		return model;
	}
	@Init
	public void init(){
		model = new ListModelList<BandboxExampleBean>();
		model.add(new BandboxExampleBean("item1"));
		model.add(new BandboxExampleBean("item11"));
		model.add(new BandboxExampleBean("item111"));
		model.add(new BandboxExampleBean("item12"));
		model.add(new BandboxExampleBean("item121"));
		model.add(new BandboxExampleBean("item122"));
		model.add(new BandboxExampleBean("item13"));
		model.add(new BandboxExampleBean("item131"));
		model.add(new BandboxExampleBean("item1311"));
		model.add(new BandboxExampleBean("item132"));
		model.add(new BandboxExampleBean("item1321"));
	}
	

}
