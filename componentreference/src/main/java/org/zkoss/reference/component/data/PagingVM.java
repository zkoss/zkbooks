package org.zkoss.reference.component.data;

import org.zkoss.bind.annotation.*;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ext.Pageable;

import java.util.*;

public class PagingVM {

    private ListModelList model = new ListModelList(Locale.getAvailableLocales());

    @Init
    public void init(){
        ((Pageable)model).setPageSize(15);
    }

    @Command
    public void next(){
        int activePage = ((Pageable)model).getActivePage();
        ((Pageable)model).setActivePage(activePage + 1);
    }

    @Command
    public void previous(){
        int activePage = ((Pageable)model).getActivePage();
        if (--activePage < 0 ){
            activePage = 0;
        }
        ((Pageable)model).setActivePage(activePage);
    }

    public List getModel() {
        return model;
    }
}
