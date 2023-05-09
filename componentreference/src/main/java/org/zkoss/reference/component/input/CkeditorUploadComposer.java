package org.zkoss.reference.component.input;

import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;

public class CkeditorUploadComposer extends SelectorComposer {

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        ((CKeditor)comp).setFileWriter(new MyCkezFileWriter("/tmp"));
    }
}

