package org.zkoss.reference.developer.uipattern;

import org.zkoss.lang.Threads;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zkmax.ui.util.*;
import org.zkoss.zul.Button;

import java.util.concurrent.CompletableFuture;

public class LoadingbarComposer extends SelectorComposer {

    private LoadingbarControl loadingbarCtrl;
    @Wire
    private Button start;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        loadingbarCtrl = Loadingbar.createLoadingbar();
        comp.getDesktop().enableServerPush(true);
    }

    @Listen("onClick = #start")
    public void startLongOperation() {
        final Desktop desktop = getSelf().getDesktop();
        loadingbarCtrl.start();
        start.setDisabled(true);

        //start a long operation in another thread
        CompletableFuture.runAsync(() -> {
            try {
                for (int n = 0; n < 10; n++) {
                    Threads.sleep(1000);
                    Executions.activate(desktop);
                    loadingbarCtrl.update((n + 1) * 10);
                    Executions.deactivate(desktop);
                }
                Executions.activate(desktop);
                start.setDisabled(false);
                Executions.deactivate(desktop);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
