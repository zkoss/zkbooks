package org.zkoss.reference.component.multimedia;

import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkmax.zul.Cropper;

public class CropperVM {

    private Media media;

    @Command @NotifyChange("media")
    public void crop(@BindingParam("image") Media media){
        this.media = media;
    }

    @Command
    public void changeSelection(@ContextParam(ContextType.TRIGGER_EVENT) Event event){
        ((Cropper)event.getTarget()).crop();
    }

    public Media getMedia() {
        return media;
    }
}
