package org.zkoss.reference.component.input;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.ItemRenderer;

public class TooltipRenderer implements ItemRenderer {
    @Override
    public String render(Component owner, Object data, int index) throws Exception {
        return String.format("<span title=\"%s\" style=\"width: 100%%;display: inline-block;\">%s</span>", data, data);
    }
}
