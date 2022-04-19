package org.zkoss.mvvm.databinding;

import org.zkoss.bind.annotation.*;

@ToClientCommand("doCountChange")
public class ToClientVM {
    private int count = 0;

    @Command
    public void doCountChange() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
