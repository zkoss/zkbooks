package org.zkoss.mvvm.advance;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;

import java.util.Locale;


public class ScrollFocusVM {

    private String selectorString;

    static private Locale[] locales = Locale.getAvailableLocales();
    private ListModelList dataModel = new ListModelList(locales);

    public void focusTextbox(){
        Clients.focus("textbox");
    }

    public void focusListitem(){
        Clients.focus("listitem:nth-child(50)");
    }

    @NotifyChange("selectorString")
    public void setSelector(@BindingParam("selector") String selector){
        this.selectorString = selector;
    }

    public void focus(){
        Clients.focus(selectorString);
    }

    public void scroll(){
        Clients.scrollIntoView(selectorString);
    }

    public String getSelectorString() {
        return selectorString;
    }

    public void setSelectorString(String selectorString) {
        this.selectorString = selectorString;
    }

    public ListModelList getDataModel() {
        return dataModel;
    }
}
