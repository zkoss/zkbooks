package org.zkoss.reference.component.essential;

import org.zkoss.bind.annotation.*;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.WebApp;

import java.io.*;

public class ImageVM {

    private AImage image;

    @Init
    public void init(@ContextParam(ContextType.APPLICATION) WebApp webapp) throws IOException {
        image = new AImage(new File(webapp.getRealPath("/multimedia/zklogo3.png")));
    }

    public AImage getImage() {
        return image;
    }
}
