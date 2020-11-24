package org.zkoss.reference.component.multimedia;

import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;

public class CropperVM {

    @Command
    public void crop(@BindingParam("image") Media media){
        System.out.println(media.getFormat());
    }
}
