package org.zkoss.reference.component.input;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.*;
import org.zkoss.zul.Checkbox;

import java.util.*;


/**
 * A composer that changes the tristate checkbox' state by monitoring the checked state of a group of checkboxes (sub-checkboxes). If not all sub-checkboxes are checked, the tristate checkbox shows indeterminate state.
 * Usage:
 * 1. Apply this composer on a tristate checkbox
 * 2. Set a custom attribute "subcheckbox" with a selector to select all sub-checkboxes.
 */
public class TristateComposer extends SelectorComposer {
    private Checkbox tristateCheckbox;
    private SubCheckboxListener subCheckboxListener = new SubCheckboxListener();
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        validateAppliedComponent(comp);
        //sub-checkboxes are usually after tristate checkbox on a page
        tristateCheckbox.addEventListener("onCreate", event -> {
            subCheckboxListener.addOnCheckListener();
        });
        //TODO check/uncheck the tristate checkbox
    }

    private void validateAppliedComponent(Component comp) {
        if (!(comp instanceof Checkbox)) {
            throw new RuntimeException("Should apply " + this.getClass() + " on a Checkbox");
        }
        tristateCheckbox = (Checkbox) comp;
        if (!tristateCheckbox.getMold().equals("tristate")) {
            throw new RuntimeException("Should apply " + this.getClass() + " on a tristate Checkbox");
        }
    }


    class SubCheckboxListener implements EventListener {

        private List<Checkbox> checkboxList = new LinkedList<>();

        public void addOnCheckListener() {
            String checkboxSelector = tristateCheckbox.getAttribute("subcheckbox").toString();
            List<Component> componentList = Selectors.find(getPage(), checkboxSelector);
            if (componentList.size() == 0) {
                throw new RuntimeException("cannot find any sub-checkbox with the selector: " + checkboxSelector);
            }
            for (Component box : componentList){
                if (!(box instanceof Checkbox)){
                    throw new RuntimeException("selector " + checkboxSelector + " should find checkboxes" );
                }
                Checkbox cb = (Checkbox) box;
                cb.addEventListener(Events.ON_CHECK, this);
                checkboxList.add(cb);
            }
        }

        @Override
        public void onEvent(Event event) throws Exception {
            tristateCheckbox.setIndeterminate(false);
            if (allCondimentsChecked()) {
                tristateCheckbox.setChecked(true);
            } else if (allCondimentsUnchecked()) {
                tristateCheckbox.setChecked(false);
            } else {
                tristateCheckbox.setIndeterminate(true);
            }

        }

        private boolean allCondimentsChecked() {
            for (Checkbox box : checkboxList) {
                if (box.isChecked()) {
                    continue;
                } else {
                    return false;
                }
            }
            return true;
        }

        private boolean allCondimentsUnchecked() {
            for (Checkbox box : checkboxList) {
                if (box.isChecked()) {
                    return false;
                } else {
                    continue;
                }
            }
            return true;
        }
    }

}
