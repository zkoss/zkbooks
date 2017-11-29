package org.zkoss.mvvm.advance;

import java.util.LinkedList;
import java.util.List;

public class Dialog {
    private boolean open;
    private String title;
    private String message;
    private String type;
    private String iconClass;
    private List<DialogButton> buttons;

    /**
     * A dialog with one OK button.
     * @param message
     * @return
     */
    static public Dialog createInfoDialog(String message){
        Dialog dialog = new Dialog();
        dialog.setTitle("Info");
        dialog.setIconClass("z-icon-info");
        dialog.setMessage(message);
        DialogButton button = new DialogButton();
        button.setLabel("OK");
        button.setCommand("ok");
        List buttons = new LinkedList();
        buttons.add(button);
        dialog.setButtons(buttons);

        return dialog;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DialogButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<DialogButton> buttons) {
        this.buttons = buttons;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
}
