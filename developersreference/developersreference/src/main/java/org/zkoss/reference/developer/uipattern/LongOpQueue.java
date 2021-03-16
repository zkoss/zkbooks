package org.zkoss.reference.developer.uipattern;

import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

public class LongOpQueue extends SelectorComposer {

    @Wire
    private Vlayout inf;
    private String result;

    @Listen(Events.ON_CLICK + "=button")
    public void start() {
        if (EventQueues.exists("longop")) {
            print("It is busy. Please wait");
            return; //busy
        }

        EventQueue eq = EventQueues.lookup("longop"); //create a queue

        //subscribe async listener to handle long operation
        eq.subscribe(new EventListener() {
            public void onEvent(Event evt) {
                if ("doLongOp".equals(evt.getName())) {
                    org.zkoss.lang.Threads.sleep(3000); //simulate a long operation
                    result = "success"; //store the result
                    eq.publish(new Event("endLongOp")); //notify it is done
                }
            }
        }, true); //asynchronous

        //subscribe a normal listener to show the resul to the browser
        eq.subscribe(new EventListener() {
            public void onEvent(Event evt) {
                if ("endLongOp".equals(evt.getName())) {
                    print(result); //show the result to the browser
                    EventQueues.remove("longop");
                }
            }
        }); //synchronous

        print("Wait for 3 seconds");
        eq.publish(new Event("doLongOp")); //kick off the long operation
    }

    void print(String msg) {
        new Label(msg).setParent(inf);
    }
}
