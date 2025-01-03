package org.zkoss.reference.component.input;

import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.*;

import java.util.*;

/**
 * Show available items when an item contains the user input.
 */
public class ChosenboxFilteringComposer implements Composer<Chosenbox> {


    @Override
    public void doAfterCompose(Chosenbox chosenbox) throws Exception {
        ListModelList<Locale> fullModel = new ListModelList(Locale.getAvailableLocales());
        ListModel model = ListModels.toListSubModel(fullModel, new Comparator() {
            /**
             * @return 0: show the object, !0: don't show the object
             */
            @Override
            public int compare(Object typedValue, Object modelObject) {

                String TypedValueAsString = (String) typedValue;
                Locale modelObjectAsLocale = (Locale) modelObject;

                boolean containsTypedString = modelObjectAsLocale.toString().toLowerCase().contains(TypedValueAsString.toLowerCase());

                return containsTypedString?0:1;
            }
        }, 10);
        chosenbox.setModel(model);

    }

}
