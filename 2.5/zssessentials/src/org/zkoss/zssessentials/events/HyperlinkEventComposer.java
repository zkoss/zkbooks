package org.zkoss.zssessentials.events;

import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.HyperlinkEvent;
import org.zkoss.zul.Label;

public class HyperlinkEventComposer extends GenericForwardComposer {

	Spreadsheet ss;
	Label keysLbl;
	
	public void onHyperlink$ss(HyperlinkEvent event) {
		String href = event.getHref();
		int key = event.getKeys();

		switch (key) {
		case HyperlinkEvent.ALT_KEY | HyperlinkEvent.LEFT_CLICK:
			keysLbl.setValue("ALT+LEFT CLICK");
			break;
		case HyperlinkEvent.CTRL_KEY | HyperlinkEvent.LEFT_CLICK:
			keysLbl.setValue("CTRL+LEFT CLICK");
			break;
		case HyperlinkEvent.SHIFT_KEY | HyperlinkEvent.LEFT_CLICK:
			keysLbl.setValue("SHIFT_KEY+LEFT CLICK");
			break;
		case HyperlinkEvent.LEFT_CLICK:
			keysLbl.setValue("LEFT CLICK");
			break;
		default:
			keysLbl.setValue("NONE");
			break;
		}
	}
}
