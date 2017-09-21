package org.zkoss.reference.developer.uipattern;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.HistoryPopStateEvent;
import org.zkoss.zk.ui.util.Clients;

import java.util.Collections;
import java.util.Map;

public class PushStateVM {
    private int tabIndex = 0;

    @HistoryPopState
    @NotifyChange("tabIndex")
    public void handleHistoryPopState(@ContextParam(ContextType.TRIGGER_EVENT) HistoryPopStateEvent event) {
        Clients.log("[By @HistoryPopState]");
        Map state = (Map) event.getState();
        Clients.log("State: " + state);
        Clients.log("Url: " + event.getUrl());

        tabIndex = 0;
        if (state != null) {
            Integer page = (Integer) state.get("page");
            if (page != null) {
                tabIndex = page - 1;
            }
        }
    }

    @Command
    @NotifyChange("tabIndex")
    public void goPage(@BindingParam("title") String title,
                       @BindingParam("url") String url,
                       @BindingParam("page") int page) {
        tabIndex = page - 1;
        pushHistoryState(title, url, page);
    }

    @Command
    public void pushHistoryState(@BindingParam("title") String title,
                                 @BindingParam("url") String url,
                                 @BindingParam("page") int page) {
        Desktop desktop = Executions.getCurrent().getDesktop();
        desktop.pushHistoryState(Collections.singletonMap("page", page), title, url);
        System.out.println(page);
    }

    public int getTabIndex() {
        return tabIndex;
    }
}
