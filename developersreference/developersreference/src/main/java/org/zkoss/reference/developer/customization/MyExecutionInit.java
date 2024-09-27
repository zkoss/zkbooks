package org.zkoss.reference.developer.customization;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.util.ExecutionInit;

public class MyExecutionInit implements ExecutionInit {
    @Override
    public void init(Execution exec, Execution parent) throws Exception {
        System.out.println("ExecutionInit: " + exec);
    }
}
