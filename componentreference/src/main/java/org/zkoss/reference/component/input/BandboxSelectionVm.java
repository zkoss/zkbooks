package org.zkoss.reference.component.input;

import java.util.Comparator;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class BandboxSelectionVm {

	private ListModelList<BandboxExampleBean> subModel;
	private BandboxExampleBean selectedItem;
	private ListModelList<BandboxExampleBean> model;
	private boolean open;
	
	public ListModel<BandboxExampleBean> getSubModel() {
		return this.subModel;
	}

	public BandboxExampleBean getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(BandboxExampleBean selectedItem) {
		this.selectedItem = selectedItem;
	}

	public void setSearchValue(String searchValue) {
		this.generateSubModel(searchValue);
	}

	public boolean isOpen() {
		return this.open;
	}

	public void setOpen(boolean open){
		this.open = open;
	}
	
	@Init
	public void init(@BindingParam("model") ListModelList model) {
		this.model = model;
	}

	
	private void generateSubModel(String searchValue) {
		subModel = new ListModelList<BandboxExampleBean>();
		Comparator comp = new BandboxExampleBeanComparator();
		for (Object object : model) {
			BandboxExampleBean bean = (BandboxExampleBean) object;
			if (comp.compare(searchValue, bean) == 0) {
				subModel.add(bean);
			}
		}
		BindUtils.postNotifyChange(null, null, this, "subModel");
	}

	@NotifyChange("selectedItem")
	@Command
	public void selectFromText(@BindingParam("input") String input) {
		// set first available bean as selected;
		if (subModel.size() != 0) {
			this.selectedItem = subModel.get(0);
		} else {
			this.selectedItem = null;
		}
		this.open = false;
		BindUtils.postNotifyChange(null, null, this, "open");
	}
	@NotifyChange("selectedItem")
	@Command
	public void selectFromListbox() {
		this.open = false;
		BindUtils.postNotifyChange(null, null, this, "open");
		Clients.log(selectedItem.getCode());
	}
}
