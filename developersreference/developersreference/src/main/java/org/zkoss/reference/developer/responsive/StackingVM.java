package org.zkoss.reference.developer.responsive;

import org.zkoss.bind.annotation.*;

public class StackingVM extends ResponsiveGrid{
    private static String COLUMNS = "columns"; // all columns
    private static String STACKING = "stacking"; // stack columns as multi-lines text
    private String currentTemplate = COLUMNS;

    @MatchMedia("all and (min-width : 800px)")
    @NotifyChange({"currentTemplate"})
    public void set6Col(){
        currentTemplate = COLUMNS;
    }

    @MatchMedia("all and (max-width : 799px)")
    @NotifyChange({"currentTemplate"})
    public void set4Col(){
        currentTemplate = STACKING;
    }

    public String getCurrentTemplate() {
        return currentTemplate;
    }
}
