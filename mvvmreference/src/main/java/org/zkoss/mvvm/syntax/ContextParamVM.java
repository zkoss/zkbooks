package org.zkoss.mvvm.syntax;

import org.zkoss.bind.*;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.*;

public class ContextParamVM {
    @Init
    public void init(
        @ContextParam(ContextType.EXECUTION) Execution execution,
        @ContextParam(ContextType.COMPONENT) Component component,
        @ContextParam(ContextType.VIEW) Component view,
        @ContextParam(ContextType.SPACE_OWNER) IdSpace spaceOwner,
        @ContextParam(ContextType.PAGE) Page page,
        @ContextParam(ContextType.DESKTOP) Desktop desktop,
        @ContextParam(ContextType.SESSION) Session session,
        @ContextParam(ContextType.APPLICATION) WebApp application,
        @ContextParam(ContextType.BIND_CONTEXT) BindContext bindContext,
        @ContextParam(ContextType.BINDER) Binder binder) {

        System.out.println(execution);
        System.out.println(component);
        System.out.println(view);
        System.out.println(spaceOwner);
        System.out.println(page);
        System.out.println(desktop);
        System.out.println(session);
        System.out.println(application);
        System.out.println(bindContext);
        System.out.println(binder);
    }

    @Command
    public void cmd(@ContextParam(ContextType.COMPONENT) Component component,
        @ContextParam(ContextType.VIEW) Component view) {
        System.out.println(component);
        System.out.println(view);
    }
}
