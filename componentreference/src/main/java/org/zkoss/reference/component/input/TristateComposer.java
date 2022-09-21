package org.zkoss.reference.component.input;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.*;
import org.zkoss.zul.Checkbox;

import java.util.*;


/**
 * A composer that changes the tri-state checkbox' state by monitoring the checked state of a group of checkboxes (sub-checkboxes). If not all sub-checkboxes are checked, the tri-state checkbox shows indeterminate state.
 * If you check the tri-state checkbox, all sub-checkboxes are checked. If you uncheck the tri-state checkbox, all sub-checkboxes are unchecked.
 * <h2>Usage:</h2>
 * 1. Apply this composer on a tri-state checkbox
 * 2. Set a custom attribute <code>subcheckbox</code> with a selector to select all sub-checkboxes.
 */
public class TristateComposer extends SelectorComposer {
    private Checkbox tristateCheckbox;
    private SubCheckboxListener subCheckboxListener = new SubCheckboxListener();
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        ensureApplyOnTriStateCheckbox(comp);
        //sub-checkboxes are usually after tristate checkbox on a page, need to add listeners later
        tristateCheckbox.addEventListener(Events.ON_CREATE, event -> {
            subCheckboxListener.addOnCheckListener();
        });

        tristateCheckbox.addEventListener(Events.ON_CHECK, (CheckEvent event) -> {
           subCheckboxListener.setCheckedForAll(event.isChecked());
        });
    }

    private void ensureApplyOnTriStateCheckbox(Component comp) {
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
            if (isAllChecked()) {
                tristateCheckbox.setChecked(true);
            } else if (isAllUnchecked()) {
                tristateCheckbox.setChecked(false);
            } else {
                tristateCheckbox.setIndeterminate(true);
            }
        }

        private boolean isAllChecked() {
            for (Checkbox box : checkboxList) {
                if (!box.isChecked()) {
                    return false;
                }
            }
            return true;
        }

        private boolean isAllUnchecked() {
            for (Checkbox box : checkboxList) {
                if (box.isChecked()) {
                    return false;
                }
            }
            return true;
        }

        public void setCheckedForAll(boolean checked) {
            for (Checkbox box : checkboxList) {
                box.setChecked(checked);
            }
        }
    }

}
