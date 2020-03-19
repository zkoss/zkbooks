package org.zkoss.reference.component.multimedia;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Pdfviewer;

import java.io.File;
import java.io.FileNotFoundException;

public class PdfviewerController extends SelectorComposer {

    @Wire("pdfviewer")
    Pdfviewer pdfviewer;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        AMedia pdf = loadPdf();
        pdfviewer.setContent(pdf);
    }

    private AMedia loadPdf() throws FileNotFoundException {
        return new AMedia(new File(WebApps.getCurrent().getRealPath("/multimedia/Bill.pdf")), null, null);
    }
}
