package org.zkoss.reference.developer.customization;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.util.*;

public class DesktopRemoveListener implements DesktopCleanup, DesktopInit {
    @Override
    public void cleanup(Desktop desktop) throws Exception {
        System.out.printf("desktop %s was removed\n", desktop.getId());
    }

    @Override
    public void init(Desktop desktop, Object request) throws Exception {
        System.out.printf("desktop %s was created\n", desktop.getId());
    }
}
