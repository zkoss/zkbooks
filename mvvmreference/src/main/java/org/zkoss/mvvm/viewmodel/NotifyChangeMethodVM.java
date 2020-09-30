package org.zkoss.mvvm.viewmodel;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;

public class NotifyChangeMethodVM {
    private String data;
    private String value1;
    private String value2;

    @Command
    public void update() {
        if (data.equals("A")) {
            //other codes...
            value1 = System.currentTimeMillis() +hashCode() + "";
            BindUtils.postNotifyChange(this, "value1");
        } else {
            //other codes...
            value2 = System.currentTimeMillis() +hashCode() + "";
            BindUtils.postNotifyChange(this, "value2");
        }
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }
}
