package org.zkoss.reference.component.essential;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zkmax.zul.Dropupload;

import java.util.Arrays;

public class DropUploadComposer extends SelectorComposer {
    @Wire("dropupload")
    private Dropupload dropupload;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        dropupload.addEventListener(Events.ON_UPLOAD, (UploadEvent event) -> {
            Notification.show(Arrays.toString(event.getMedias()) + " uploaded");
        });
    }
}
