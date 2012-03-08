package org.zkoss.zkspringessentials.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.zkoss.spring.context.annotation.EventHandler;
import org.zkoss.spring.util.GenericSpringComposer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

@org.springframework.stereotype.Component("greetingCtrl")
@Scope("desktop")
public class GreetingCtrl extends GenericSpringComposer {
 
    @Autowired
    private Textbox name;
     
    @Autowired
    private Button greetBtn;
     
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }
     
    @EventHandler("greetBtn.onClick")
    public void showGreeting(Event evt) throws WrongValueException, InterruptedException {
        Messagebox.show("Hello " + name.getValue() + "!");
    }
}