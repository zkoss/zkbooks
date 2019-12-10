package org.zkoss.reference.developer.customization;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.util.DesktopInit;

/**
 * Ref: https://docs.microsoft.com/en-us/openspecs/ie_standards/ms-iedoco/380e2488-f5eb-4457-a07a-0cb1b6e4b4b5?redirectedfrom=MSDN
 */
public class IeDocumentMode implements DesktopInit {
    @Override
    public void init(Desktop desktop, Object request) throws Exception {
        Executions.getCurrent().addResponseHeader("X-UA-Compatible", "IE=edge");
    }
}
