package org.zkoss.reference.component.multimedia;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.StateChangeEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Video;

public class VideoVM {

    @Command
    public void stateChange(@ContextParam(ContextType.TRIGGER_EVENT) StateChangeEvent event) {
        if (event.getState() == Video.PLAY) {
            Clients.showNotification("play");
        }else if (event.getState() == Video.PAUSE){
            Clients.showNotification("pause");
        }
    }
}
