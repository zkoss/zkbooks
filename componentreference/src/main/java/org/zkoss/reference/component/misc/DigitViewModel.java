package org.zkoss.reference.component.misc;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.NotifyChange;

public class DigitViewModel {

    private String value = "0000";
    private String[] digits; //CSS name
    static private String DIGIT_CSS[] = {"zero","one", "two", "three", "four","five","six","seven", "eight", "nine"};
    private boolean edit = false;

    @Command @NotifyChange({"edit", "value"})
    public void toggle(){
        edit = !edit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @DependsOn("value")
    public String[] getDigits() {
        digits = new String[value.length()];
        for (int i = 0; i < value.length() ; i++){
            digits[i] = "digit "+DIGIT_CSS[Integer.parseInt(String.valueOf(value.charAt(i)))];
        }
        return digits;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
}
