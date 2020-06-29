package org.zkoss.mvvm.collection;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zul.*;

import java.util.*;

public class DragDropVM {

    private ListModelList<Locale> availableLocales;
    private ListModelList<Locale> selectedLocales = new ListModelList<>();

    public DragDropVM(){
        availableLocales = new ListModelList<>(Arrays.asList(Locale.getAvailableLocales()).subList(5, 15));

    }

    @Command
    public void select(@ContextParam(ContextType.TRIGGER_EVENT)DropEvent event){
        Locale locale = ((Listitem)event.getDragged()).getValue();
        availableLocales.remove(locale);
        selectedLocales.add(locale);
    }

    @Command
    public void deselect(@ContextParam(ContextType.TRIGGER_EVENT)DropEvent event){
        Locale locale = ((Listitem)event.getDragged()).getValue();
        availableLocales.add(locale);
        selectedLocales.remove(locale);
    }

    public ListModelList<Locale> getAvailableLocales() {
        return availableLocales;
    }

    public ListModelList<Locale> getSelectedLocales() {
        return selectedLocales;
    }
}
