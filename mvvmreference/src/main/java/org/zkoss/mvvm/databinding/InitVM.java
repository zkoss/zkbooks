package org.zkoss.mvvm.databinding;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.util.DesktopCleanup;

public class InitVM {

    private String value = "my-test-value";
    private int price = 14;

    @Init
    public void init(@BindingParam("arg1") String arg1, @ContextParam(ContextType.DESKTOP)Desktop desktop) {
        System.out.println("init: arg1: " + arg1);
        desktop.addListener(new DesktopCleanup() {
            @Override
            public void cleanup(Desktop desktop) throws Exception {
                System.out.println(desktop.getId() + " is destroyed");
            }
        });
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
