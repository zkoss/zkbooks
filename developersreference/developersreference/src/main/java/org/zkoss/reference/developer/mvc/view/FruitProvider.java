package org.zkoss.reference.developer.mvc.view;

import org.zkoss.zul.ListModelArray;

public class FruitProvider extends org.zkoss.zk.ui.select.SelectorComposer {
    public ListModelArray fruits = new ListModelArray(
            new String[][] {
                    {"Apple", "10kg"},
                    {"Orange", "20kg"},
                    {"Mango", "12kg"}
            });

    public ListModelArray getFruits() {
        return fruits;
    }
}