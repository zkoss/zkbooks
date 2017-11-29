package org.zkoss.mvvm.advance;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

public class DialogVM {
    private Dialog dialog;

    @Init
    public void init(){
        dialog = Dialog.createInfoDialog("hello");
    }

    @Command @NotifyChange("dialog")
    public void open(){
        dialog.setOpen(true);
    }

    @Command @NotifyChange("dialog")
    public void ok(){
        dialog.setOpen(false);
    }

    public Dialog getDialog() {
        return dialog;
    }
}
