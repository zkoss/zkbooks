package org.zkoss.reference.component.input;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.util.*;
import org.zkoss.zk.ui.ext.*;
import org.zkoss.zk.au.*;
import org.zkoss.zk.au.out.*;
import org.zkoss.zul.*;

public class BandboxExampleBean{
    private String code;
    public BandboxExampleBean(String code){
            super();
            this.code = code;
    }
    public String getCode(){
            return this.code;
    }
}
