package org.zkoss.reference.component.input;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.util.*;
import org.zkoss.zk.ui.ext.*;
import org.zkoss.zk.au.*;
import org.zkoss.zk.au.out.*;
import org.zkoss.zul.*;

import java.util.Comparator;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

public class BandboxSelectionVm {

	private ListModelList subModel;
	private BandboxExampleBean selectedItem;
	ListModelList model;

	public ListModel getSubModel() {
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

	private void generateSubModel(String searchValue) {
		subModel = new ListModelList();
		Comparator comp = new BandboxExampleBeanComparator();
		for (Object object : model) {
			if(object instanceof BandboxExampleBean){
				BandboxExampleBean bean = (BandboxExampleBean) object;
				if(comp.compare(searchValue, bean)==0){
					subModel.add(bean);
				}
			}
		}
		BindUtils.postNotifyChange(null, null, this, "subModel");
	}

	@Init
	public void init() {
		model = new ListModelList();
		model.setMultiple(false);
		model.add(new BandboxExampleBean("item1"));
		model.add(new BandboxExampleBean("item11"));
		model.add(new BandboxExampleBean("item111"));
		model.add(new BandboxExampleBean("item12"));
		model.add(new BandboxExampleBean("item2"));
		model.add(new BandboxExampleBean("item20"));
		model.add(new BandboxExampleBean("item21"));
		model.add(new BandboxExampleBean("item22"));

		generateSubModel("");
	}

}
