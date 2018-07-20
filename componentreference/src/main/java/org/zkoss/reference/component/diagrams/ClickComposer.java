package org.zkoss.reference.component.diagrams;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

import java.util.Date;

import org.zkoss.zul.GanttModel.*;

public class ClickComposer extends SelectorComposer<Component> {

    @Wire
    private Chart gantt;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        //series, task (task description, start, end, complete percentage)
        GanttModel ganttmodel = new GanttModel();
        ganttmodel.addValue("Scheduled", new GanttTask("", date(2008, 4, 1), date(2008, 4, 1), 0.0));
        ganttmodel.addValue("Scheduled", new GanttTask("Write Proposal", date(2008, 4, 1), date(2008, 4, 5), 0.0));
        ganttmodel.addValue("Scheduled", new GanttTask("Obtain Approval", date(2008, 4, 9), date(2008, 4, 9), 0.0));
        ganttmodel.addValue("Scheduled", new GanttTask("Requirements Analysis", date(2008, 4, 10), date(2008, 5, 5), 0.0));
        ganttmodel.addValue("Scheduled", new GanttTask("Design Phase", date(2008, 5, 6), date(2008, 5, 30), 0.0));
        ganttmodel.addValue("Scheduled", new GanttTask("Design Signoff", date(2008, 6, 2), date(2008, 6, 2), 0.0));
        ganttmodel.addValue("Scheduled", new GanttTask("Alpha Implementation", date(2008, 6, 3), date(2008, 7, 31), 0.0));
        ganttmodel.addValue("Scheduled", new GanttTask("Design Review", date(2008, 8, 1), date(2008, 8, 8), 0.0));
        ganttmodel.addValue("Scheduled", new GanttTask("Revised Design Signoff", date(2008, 8, 10), date(2008, 8, 10), 0.0));
        ganttmodel.addValue("Scheduled", new GanttTask("Beta Implementation", date(2008, 8, 12), date(2008, 9, 12), 0.0));
        ganttmodel.addValue("Scheduled", new GanttTask("Testing", date(2008, 9, 13), date(2008, 10, 31), 0.0));
        ganttmodel.addValue("Scheduled", new GanttTask("Final Implementation", date(2008, 11, 1), date(2008, 11, 15), 0.0));
        ganttmodel.addValue("Scheduled", new GanttTask("Signoff", date(2008, 11, 28), date(2008, 11, 30), 0.0));

        ganttmodel.addValue("Actual", new GanttTask("Write Proposal", date(2008, 4, 1), date(2008, 4, 3), 0.0));
        ganttmodel.addValue("Actual", new GanttTask("Obtain Approval", date(2008, 4, 9), date(2008, 4, 9), 0.0));
        ganttmodel.addValue("Actual", new GanttTask("Requirements Analysis", date(2008, 4, 10), date(2008, 5, 15), 0.0));
        ganttmodel.addValue("Actual", new GanttTask("Design Phase", date(2008, 5, 15), date(2008, 6, 17), 0.0));
        ganttmodel.addValue("Actual", new GanttTask("Design Signoff", date(2008, 6, 30), date(2008, 6, 30), 0.0));
        ganttmodel.addValue("Actual", new GanttTask("Alpha Implementation", date(2008, 7, 1), date(2008, 9, 12), 0.0));
        ganttmodel.addValue("Actual", new GanttTask("Design Review", date(2008, 9, 12), date(2008, 9, 22), 0.0));
        ganttmodel.addValue("Actual", new GanttTask("Revised Design Signoff", date(2008, 9, 25), date(2008, 9, 27), 0.0));
        ganttmodel.addValue("Actual", new GanttTask("Beta Implementation", date(2008, 8, 12), date(2008, 9, 12), 0.0));
        ganttmodel.addValue("Actual", new GanttTask("Testing", date(2008, 10, 31), date(2008, 11, 17), 0.0));
        ganttmodel.addValue("Actual", new GanttTask("Final Implementation", date(2008, 11, 18), date(2008, 12, 5), 0.0));
        ganttmodel.addValue("Actual", new GanttTask("Signoff", date(2008, 12, 10), date(2008, 12, 11), 0.0));
        gantt.setModel(ganttmodel);
    }

    @Listen("onClick = #gantt")
    public  void drilldown(MouseEvent event) {
        final Area area = (Area)event.getAreaComponent();
        if (area != null)
            Messagebox.show(area.getAttribute("category")+": "+area.getTooltiptext());
    }

    protected Date date(int year, int month, int day) {
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(year, month - 1, day);
        final Date result = calendar.getTime();
        return result;
    }
}
