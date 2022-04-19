package org.zkoss.reference.developer.uicomposing;

import java.util.List;

public class Menu {
    private String name;
    private List<String> items;

    public Menu(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
