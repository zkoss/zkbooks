/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import java.util.Calendar;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import example.bean.FlowHelper;
import example.bean.MyConversation;

/**
 *
 * @author mschroen
 */
@Service("ZK2ndStep")
@Scope("prototype")
public class ZK2ndStep extends SelectorComposer<Component> {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ZK2ndStep.class.getName());
    @WireVariable
    private FlowHelper impHelper;
    @WireVariable
    private String value1;
   
    
    @Wire
    private Listbox myList;
    @Wire
    private Textbox valuebox;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        if (impHelper != null) {
            logger.info("impHelper.getMyint() = " + impHelper.getMyint());
            logger.info("impHelper.getMyList().size() = " + impHelper.getMyList().size());
            logger.info("impHelper.isMybool() = " + impHelper.isMybool());
        } else {
            logger.error("can't resolve impHelper");
        }
    }
    
    @Listen("onClick=#add")
    public void add() {
        if (impHelper != null) {
            // now we modify the list
            impHelper.getMyList().add("Text 3");
//            myList.setModel(new ListModelList(impHelper.getMyList()));
            logger.info("Add, impHelper.getMyList().size() = " + impHelper.getMyList().size());
        }
    }

    @Listen("onClick=#change")
    public void change(){
    	
    	valuebox.setValue(Calendar.getInstance().getTime().toString());
    	value1 = "changed";
    }
    @Listen("onClick=#btnNext")
    public void btnNext() {

    }
}
