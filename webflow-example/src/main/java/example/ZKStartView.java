package example;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Panel;

import example.bean.FlowHelper;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author mschroen
 */
@Controller("ZKStartView")
@Scope("prototype")
public class ZKStartView extends SelectorComposer<Component> {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ZKStartView.class.getName());
    @WireVariable
    private FlowHelper impHelper;
    
    @Wire
    private Panel panel;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        if (impHelper != null) {
        	
        	System.out.println(">>>>>"+impHelper.getClass());
        	
            // now we modify the object
            impHelper.setMyint(12);
            impHelper.setMybool(true);
            impHelper.setMyList(new ArrayList());
            impHelper.getMyList().add("Sample Text1");
            impHelper.getMyList().add("Sample Text2");
            System.out.println("add 2 sample text to list");
        } else {
            logger.error("can't resolve impHelper");
        }
    }
    
    @Listen("onClick = #hide")
    public void hide(){
    	panel.setVisible(!panel.isVisible());
    }
}
