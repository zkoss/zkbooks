package org.zkoss.mvvm.advance;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

public class SearchAutowireVM {

    //UI component
    @Wire("#msgPopup")
    private Popup popup;
    @Wire
    private Label msg;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        popup.open(view);
        msg.setValue("wired");
    }
}
