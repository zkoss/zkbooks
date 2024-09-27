package org.zkoss.reference.developer.serverpush;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

public class ServerPushComposer extends SelectorComposer {
    @Wire
    private Div result;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        getPage().getDesktop().enableServerPush(true);
    }

    @Listen("onClick = #start")
    public void start(){
        final Desktop desktop = getPage().getDesktop();
        Thread thread = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread()+ " run");
                fakeOperation(2);
                Executions.activate(desktop);
                fakeOperation(10);
                Label label = new Label("job complete " + System.currentTimeMillis());
                label.setStyle("display: block");
                result.appendChild(label);
                Executions.deactivate(desktop);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    /**
     * Simulating long-running task, keep a thread in RUNNING state.
     * Because Thread.sleep() puts the thread in the TIMED_WAITING
     */
    static public void fakeOperation(int seconds){
        long endTime = System.currentTimeMillis() + seconds*1000;
        while (System.currentTimeMillis() < endTime) {
            // Just a busy-wait. In real scenarios, this could be some meaningful computation.
        }
    }
}
