package org.zkoss.reference.developer.mvc.model;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.ext.*;

import java.util.*;

/**
 * @author Hawk Chen
 */
public class SelectionControlComposer extends SelectorComposer<Component> {

    private static final long serialVersionUID = -7481683315099616293L;

    @Wire("listbox")
    private Listbox listbox;
    private ListModelList<Locale> listModel = new ListModelList<>(Locale.getAvailableLocales());

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        listModel.setMultiple(true);
        listModel.setSelectionControl(new PageSelection(listModel));
        listbox.setModel(listModel);
        /*
        listModel.setSelectionControl(new AbstractListModel.DefaultSelectionControl(listModel) {
            public boolean isSelectable(Object e) {
                int i = listModel.indexOf(e);
                return i % 2 == 0;
            }
        });
        */
    }

    /**
     * Only select all items in the current active page.
     */
    class PageSelection implements SelectionControl {
        protected AbstractListModel model;

        public PageSelection(AbstractListModel model) {
            this.model = model;
        }

        @Override
        public boolean isSelectable(Object o) {
            return true;
        }

        /**
         * select the items of the current page
         *
         * @param selectAll all items are selected or not
         */
        @Override
        public void setSelectAll(boolean selectAll) {
            if (selectAll) {
                int currentPageIndex = listbox.getActivePage();
                List selectedList = new LinkedList();
                int pageSize = listbox.getPageSize();
                for (int i = currentPageIndex * pageSize; i < currentPageIndex * pageSize + pageSize; i++) {
                    Object o = model.getElementAt(i);
                    if (isSelectable(o)) // check whether it can be selectable or not
                        selectedList.add(o);
                }

                ((Selectable) model).setSelection(selectedList);
            } else {
                this.model.clearSelection();
            }
        }

        @Override
        public boolean isSelectAll() {
            return model.getSelection().size() == listbox.getPageSize();
        }
    }
}
