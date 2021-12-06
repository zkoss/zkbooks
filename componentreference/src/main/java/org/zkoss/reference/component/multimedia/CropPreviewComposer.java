package org.zkoss.reference.component.multimedia;

import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zkmax.zul.Cropper;
import org.zkoss.zul.Image;

public class CropPreviewComposer extends SelectorComposer<Component> {

    @Wire
    private Image img;
    @Wire("cropper")
    private Cropper cropper;

    @Listen("onCrop = cropper")
    public void showImage(UploadEvent e){
        img.setContent((AImage)e.getMedia());
    }

    @Listen("onChange = cropper")
    public void changeSelection(Event e){
        cropper.crop();
    }
}
