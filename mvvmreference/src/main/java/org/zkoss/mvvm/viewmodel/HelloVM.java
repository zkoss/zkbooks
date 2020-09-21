package org.zkoss.mvvm.viewmodel;

import org.zkoss.bind.annotation.NotifyChange;

public class HelloVM {
    private String greeting = "Hello!";
    private String name;

    @NotifyChange("name")
    public void hello(){
        name = "world";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGreeting() {
        return greeting;
    }
}
