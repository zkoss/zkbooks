package org.zkoss.reference.developer.serverpush;

import org.zkoss.lang.Threads;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * Run a long operation in a separate thread to avoid blocking users.
 * Update the result to UI with {@link Executions#schedule(Desktop, EventListener, Event)}
 */
public class AsyncTaskComposer extends SelectorComposer<Component> {

    @Wire
    private Vlayout status;
    private Desktop desktop;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        desktop = comp.getDesktop();
        desktop.enableServerPush(true);
    }

    @Listen("onClick = #start")
    public void start() throws ExecutionException, InterruptedException {
        // run in a separate thread
        CompletableFuture.runAsync(() -> {
            Threads.sleep(3000); //simulate a long task
            Executions.schedule(desktop,
                new EventListener<Event>() {
                    public void onEvent(Event event) {
                        //update UI
                        status.appendChild(new Label("done at " + LocalDateTime.now()));
                    }
                }, new Event("myEvent"));
        });
    }

}
