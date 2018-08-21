package org.zkoss.reference.developer.i18n;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppInit;

public class LocatorInit implements WebAppInit {
    @Override
    public void init(WebApp wapp) throws Exception {
        Labels.register(new MyLableLocator(wapp.getServletContext()));
    }
}
