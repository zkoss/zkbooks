package org.zkoss.mvvm.collection;

import org.zkoss.bind.*;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;

import java.util.*;

public class CheckboxGroupVM {
    private List<String> choices = Arrays.asList("Opt1", "Opt2", "Opt3", "Opt4");
    private Set<String> selection = new HashSet<>();

    public List<String> getChoices() {
        return choices;
    }

    public Set<String> getSelection() {
        return selection;
    }

    public void setSelection(Set<String> selection) {
        this.selection = selection;
    }

    public Converter getSelectionConverter() {
        return new Converter<Boolean, Set<String>, Checkbox>() {
            @Override
            public Boolean coerceToUi(Set<String> beanProp, Checkbox component, BindContext ctx) {
                Set<String> selection = (Set<String>) ctx.getConverterArg("selection");
                String item = (String)ctx.getConverterArg("item");
                return selection.contains(item);
            }

            @Override
            public Set<String> coerceToBean(Boolean compAttr, Checkbox component, BindContext ctx) {
                Set<String> selection = (Set<String>) ctx.getConverterArg("selection");
                String item = (String)ctx.getConverterArg("item");
                if(compAttr) {
                    selection.add(item);
                } else {
                    selection.remove(item);
                }
                return selection;
            }
        };
    }

    public Validator getSelectionValidator() {
        return new AbstractValidator() {

            @Override
            public void validate(ValidationContext ctx) {
                Clients.log("current selection to validate: " + ctx.getProperty().getValue());
            }
        };
    }
}
