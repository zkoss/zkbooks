package org.zkoss.reference.developer.testing;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.impl.StaticIdGenerator;

/**
 * generate a predictable desktop ID.
 */
public class StaticIdGeneratorExt extends StaticIdGenerator {

    protected static final String ID_PREFIX = "zdt_";
    protected static final String DESKTOP_ID = "desktop-id";


    @Override
    public String nextDesktopId(Desktop desktop) {
        Integer desktopId;
        Session session = desktop.getSession();
        synchronized (session) {
            if ((desktopId = (Integer) session.getAttribute(DESKTOP_ID)) == null) {
                desktopId = 0;
            }
            desktopId++;
            session.setAttribute(DESKTOP_ID, desktopId);
        }
        return ID_PREFIX + desktopId;
    }
}
