package org.zkoss.reference.developer.mvc.view;

import org.zkoss.bind.annotation.*;
import org.zkoss.zul.*;

public class FruitProvider {
    public ListModelList fruits = new ListModelList();

    @Init
    public void init() {
        TabInfo tabinfo1 = new TabInfo();
        tabinfo1.setName("Apple");
        tabinfo1.setName("z-icon-user");

        TabInfo tabPlus = new TabInfo();
        tabPlus.setName("");
        tabPlus.setIcon("z-icon-plus");
        tabPlus.setCommand("add");
        fruits.add(tabinfo1);
        fruits.add(tabPlus);
    }


    @Command
    public void add(){
        TabInfo newTab = new TabInfo();
        newTab.setName("new " + fruits.size());

        fruits.add(fruits.size()-1, newTab); //keep the last tab
        fruits.addToSelection(newTab);
    }


    public ListModelList getFruits() {
        return fruits;
    }
}