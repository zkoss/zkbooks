package org.zkoss.reference.developer.serverpush;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zul.ListModelList;

public class ServerPushVM {

    private Desktop desktop;
    private ListModelList<String> data = new ListModelList<>();

    @Init
    public void init(@ContextParam(ContextType.DESKTOP)Desktop desktop){
        this.desktop = desktop;
        desktop.enableServerPush(true);
    }

    @Command
    public void start(){
        Thread thread = new WorkingThread(desktop);
        ((WorkingThread) thread).setData(data);
        thread.start();
    }

    public ListModelList<String> getData() {
        return data;
    }
}
