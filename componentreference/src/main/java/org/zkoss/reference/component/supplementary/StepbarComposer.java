package org.zkoss.reference.component.supplementary;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zkmax.zul.*;

/**
 * {@link Stepbar} supports model-driven display.
 * Show Stepbar usage with a {@link org.zkoss.zul.ListModel}
 */
public class StepbarComposer extends SelectorComposer {

    @Wire("stepbar")
    private Stepbar stepbar;
    private StepModel<MyStep> model;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initializeStepModel();
        stepbar.setModel(model);
        model.setActiveIndex(0);
    }

    private void initializeStepModel(){
        model = new DefaultStepModel<>();
        model.add(new MyStep("Step 1"));
        model.add(new MyStep("Step 2"));
        model.add(new MyStep("Step 3"));
    }

    @Listen("onClick=#next")
    public void next() {
        if (model.getActiveIndex() == (model.size()-1)){
            stepbar.getActiveStep().setComplete(true);
        }else {
            model.next();
        }
    }

    @Listen("onClick=#back")
    public void back() {
        model.back();
    }

}
