package org.zkoss.reference.developer.mvc.view;

import org.zkoss.bind.annotation.*;
import org.zkoss.zul.*;

public class TabOperationVM {
    public ListModelList tabList = new ListModelList();

    @Init
    public void init() {
        TabInfo tabinfo1 = new TabInfo();
        tabinfo1.setName("Apple");

        TabInfo tabPlus = new TabInfo();
        tabPlus.setIcon("z-icon-plus");
        tabPlus.setCommand("add");
        tabList.add(tabinfo1);
        tabList.add(tabPlus);
    }


    @Command
    public void add(){
        TabInfo newTab = new TabInfo();
        newTab.setName("new " + tabList.size());

        tabList.add(tabList.size()-1, newTab); //keep the last tab
        tabList.addToSelection(newTab);
    }

    @Command
    public void del(@BindingParam("target")TabInfo tabToDelete){
        tabList.remove(tabToDelete);
    }


    public ListModelList getTabList() {
        return tabList;
    }
}