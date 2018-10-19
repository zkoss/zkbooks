package org.zkoss.reference.component.input;

import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.util.*;
import org.zkoss.zk.ui.ext.*;
import org.zkoss.zk.au.*;
import org.zkoss.zk.au.out.*;
import org.zkoss.zul.*;
import java.util.Comparator;

class BandboxExampleBeanComparator implements Comparator{
       public int compare(Object textInput, Object modelItem){
               return ((BandboxExampleBean)modelItem).getCode().startsWith((String)textInput)?0:1;
       }
}