package org.zkoss.reference.component.diagrams;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zkmax.zul.Fusionchart;
import org.zkoss.zul.*;

import java.util.*;

import static org.zkoss.zul.GanttModel.GanttTask;

public class GanttchartComposer extends SelectorComposer<Div> {
    @Wire
    private Fusionchart fusionchart;

    @Override
    public void doAfterCompose(Div comp) throws Exception {
        super.doAfterCompose(comp);

        GanttModel ganttmodel = new GanttModel();

        String scheduled = "Scheduled";
        String actual = "Actual";

        ganttmodel.addValue(scheduled, new GanttTask("Write Proposal", date(2008, 4, 1), date(2008, 4, 5), 0.0));
        ganttmodel.addValue(scheduled, new GanttTask("Requirements Analysis", date(2008, 4, 10), date(2008, 5, 5), 0.0));
        ganttmodel.addValue(scheduled, new GanttTask("Design Phase", date(2008, 5, 6), date(2008, 5, 30), 0.0));
        ganttmodel.addValue(scheduled, new GanttTask("Alpha Implementation", date(2008, 6, 3), date(2008, 7, 31), 0.0));

        ganttmodel.addValue(actual, new GanttTask("Write 測試", date(2008, 4, 1), date(2008, 4, 3), 0.0));
        ganttmodel.addValue(actual, new GanttTask("Requirements Analysis", date(2008, 4, 10), date(2008, 5, 15), 0.0));
        ganttmodel.addValue(actual, new GanttTask("Design Phase", date(2008, 5, 15), date(2008, 6, 17), 0.0));
        ganttmodel.addValue(actual, new GanttTask("Alpha Implementation", date(2008, 7, 1), date(2008, 9, 12), 0.0));

        fusionchart.setModel(ganttmodel);
    }

    @Listen("onClick = #fusionchart")
    public void show(Event event) {
        Map data = (Map) event.getData();
        Messagebox.show(data.toString());
    }

    private Date date(int year, int month, int day) {
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }
}
