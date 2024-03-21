package org.zkoss.reference.developer.uicomposing;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zul.*;

public class TestRichlet extends GenericRichlet {
	//Richlet//
	public void service(Page page) {
		page.setTitle("Richlet Test");

		final Window w = new Window("Richlet Test", "normal", false);
		new Label("Hello World!").setParent(w);
		final Label label = new Label();
		label.setParent(w);

		final Button button = new Button("Change");
		button.addEventListener(Events.ON_CLICK,
				new EventListener() {
			int count;
			public void onEvent(Event evt) {
				label.setValue("" + ++count);
			}
		});
		button.setParent(w);

		w.setPage(page);
	}

	@Override
	public void init(RichletConfig config) {
		super.init(config);
		//initialize resources
	}

	@Override
	public void destroy() {
		super.destroy();
		//destroy resources
	}
}
