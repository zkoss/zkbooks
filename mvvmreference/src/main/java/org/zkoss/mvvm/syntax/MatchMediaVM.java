package org.zkoss.mvvm.syntax;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.ClientInfoEvent;

public class MatchMediaVM {
    String windowWidth = "700px";
    String applyTemplateName = "min501";

    @MatchMedia("all and (max-width: 500px)")
    @NotifyChange("*")
    public void max500(@ContextParam(ContextType.TRIGGER_EVENT) ClientInfoEvent event) {
        windowWidth = "100%";
        applyTemplateName = "max500"; //change a template
        int desktopWidth = event.getDesktopWidth(); // get the desktop width by the event
        // do something according to desktopWidth
    }

    @MatchMedia("all and (min-width: 501px)")
    @NotifyChange("*")
    public void min501() {
        windowWidth = "700px";
        applyTemplateName = "min501"; //change a template
    }

    public String getWindowWidth() {
        return windowWidth;
    }

    public String getApplyTemplateName() {
        return applyTemplateName;
    }
}
