package org.zkoss.mvvm.shadow;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;

public class ParameterParentVM {
    private String templateName = null;
    private String param = "unset";

    @Command @NotifyChange({"templateName", "param"})
    public void set(){
        param = "data";
        templateName = "parameterChild.zul";
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getParam() {
        return param;
    }
}
