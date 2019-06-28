package org.zkoss.reference.developer.serverpush;

import org.zkoss.lang.Threads;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.Label;

import java.time.LocalDateTime;
import java.util.concurrent.*;

public class AsyncTaskComposer extends SelectorComposer<Component> {

    @Wire
    private Label status;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        getPage().getDesktop().enableServerPush(true);
    }

    @Listen("onClick = #start")
    public void start() throws ExecutionException, InterruptedException {
        final Desktop desktop = getPage().getDesktop();
        
        // run in a separate thread
        CompletableFuture.runAsync(() -> {
            Threads.sleep(3000); //simulate a long task
            Executions.schedule(desktop,
                    new EventListener<Event>() {
                        public void onEvent(Event event) {
                            //update UI
                            status.setValue("done at " + LocalDateTime.now());
                        }
                    }, new Event("myEvent"));
        }, Executors.newCachedThreadPool());
    }

}
