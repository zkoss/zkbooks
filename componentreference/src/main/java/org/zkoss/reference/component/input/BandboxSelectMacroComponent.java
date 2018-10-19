package org.zkoss.reference.component.input;

import java.util.Comparator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.annotation.ComponentAnnotation;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

@ComponentAnnotation("selectedItem:@ZKBIND(ACCESS=both, SAVE_EVENT=onSelect)")
public class BandboxSelectMacroComponent extends HtmlMacroComponent {

	@Wire
	private Bandbox bb;
	@Wire
	private Textbox tb;
	@Wire
	Listbox lb;

	private ListModelList<BandboxExampleBean> model;
	private ListModelList<BandboxExampleBean> submodel;

	@WireVariable
	private BandboxExampleBean selectedItem;

	public BandboxSelectMacroComponent() {
		compose();
		final Component self = this;
		bb.addEventListener("onOK", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				bb.open();
			}
		});
		lb.addEventListener("onSelect", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				selectedItem = lb.getSelectedItem().getValue();
				Events.postEvent("onSelect", self, selectedItem);
				updateBandboxAfterSelect();
			}
		});
		tb.addEventListener("onOK", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				selectedItem = submodel.get(0);
				Events.postEvent("onSelect", self, selectedItem);
				updateBandboxAfterSelect();
			}
		});
		tb.addEventListener("onChange", new EventListener<InputEvent>() {
			@Override
			public void onEvent(InputEvent event) throws Exception {
				submodel = getSubModelFromString(event.getValue());
				lb.setModel(submodel);
			}
		});
	}
	private ListModelList<BandboxExampleBean> getSubModelFromString(String value) {
		ListModelList<BandboxExampleBean> tempModel = new ListModelList<BandboxExampleBean>();
		Comparator comp = new BandboxExampleBeanComparator();
		for (Object object : model) {
			BandboxExampleBean bean = (BandboxExampleBean) object;
			if (comp.compare(value, bean) == 0) {
				tempModel.add(bean);
			}
		}
		return tempModel;
	}
	
	private void updateBandboxAfterSelect() {
		bb.setValue(selectedItem.getCode());
		bb.close();
		tb.setValue("");
		submodel.clear();
	}

	public void setModel(ListModelList model){
		this.model = model;
	}
	
	public void setSelectedItem(BandboxExampleBean selectedItem) {
		this.selectedItem = selectedItem;
	}

	public BandboxExampleBean getSelectedItem() {
		return selectedItem;
	}

}
