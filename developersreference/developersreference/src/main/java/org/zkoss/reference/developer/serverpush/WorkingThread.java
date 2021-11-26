package org.zkoss.reference.developer.serverpush;

import org.zkoss.bind.BindUtils;
import org.zkoss.lang.Threads;
import org.zkoss.zk.ui.*;
import org.zkoss.zul.ListModelList;

import java.util.Calendar;

/**
 * a thread that prepare (query, calculate) application data for updating UI
 */
public class WorkingThread extends Thread{

    private Desktop desktop;
    private ListModelList data;

    public WorkingThread(Desktop desktop){
        this.desktop = desktop;
    }

    public void setData(ListModelList data){
        this.data = data;
    }

    public void run() {
        try {
            while (anyDataToShow()) {
                //Step 1. Prepare the data that will be updated to UI
                collectData(); //prepare the data to set to components

                //Step 2. Activate to grant the access of the given desktop
                Executions.activate(desktop);
                try {
                    //Step 3. Update UI
                    updateUI(); //implement the logic to change UI
                } finally {
                    //Step 4. Deactivate to return the control of UI back
                    Executions.deactivate(desktop);
                }
            }
        } catch (InterruptedException ex) {
            //Interrupted. You might want to handle it
            ex.printStackTrace();
        }
    }

    protected void updateUI() {
        data.clear();
        data.add("now " + System.currentTimeMillis());
        // need to notify change if change a ViewModel's property with BindUtils.postNotifyChange();
    }

    protected void collectData() {
        Threads.sleep(1000);
    }

    protected boolean anyDataToShow() {
        return true;
    }

}
