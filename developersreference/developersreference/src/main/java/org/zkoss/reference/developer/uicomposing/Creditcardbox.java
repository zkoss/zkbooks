package org.zkoss.reference.developer.uicomposing;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.Textbox;

import java.util.List;

public class Creditcardbox extends HtmlMacroComponent {
    public static final String SCLASS = "z-creditcardbox";

    public Creditcardbox() {
        this.setSclass(SCLASS);
    }

    @Wire("textbox")
    protected List<Textbox> textboxList;
    /**
     * @return credit card number
     */
    public String getNumber(){
        return textboxList.stream().collect(StringBuilder::new, (sb, t) -> sb.append(t.getValue()), StringBuilder::append).toString();
    }

    @Listen("onChange = textbox")
    public void fireOnChange(Event event) {
        Events.postEvent("onChange", this, null);
    }
}
