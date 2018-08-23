package org.zkoss.reference.developer.uipattern;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.util.*;

import java.util.Map;

public class AllCustomerFinder implements Initiator, InitiatorExt {

    public void doInit(Page page, Map args) throws Exception {
        String name = (String)args.get("name");
        page.setAttribute(name != null ? name: "customers", CustomerManager.findAll());
        Executions.getCurrent().setAttribute("e", "in execution");
    }

    @Override
    public void doAfterCompose(Page page, Component[] comps) throws Exception {

    }

    public boolean doCatch(Throwable ex) throws Exception { //nothing to do
        return false;
    }
    public void doFinally() throws Exception { //nothing to do
    }
}
