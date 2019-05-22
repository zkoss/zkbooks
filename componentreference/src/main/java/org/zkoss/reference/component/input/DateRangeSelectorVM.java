package org.zkoss.reference.component.input;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

public class DateRangeSelectorVM extends SelectorComposer<Component> {
    private Calendar calendar = Calendar.getInstance();
    private Date startDate = calendar.getTime();
    private Date endDate = calendar.getTime();
    private String endDateConstraint = "no past";
    private String dateRange;
    final private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    @Init
    public void init(){

    }

    @Command @NotifyChange({"endDate", "endDateConstraint", "dateRange"})
    public void updateConstraint(){
        endDate = startDate;
        endDateConstraint = "after " + format.format(startDate);
        updateDateRange();
    }

    @Command @NotifyChange("dateRange")
    public void updateDateRange(){
        dateRange = format.format(startDate) + " - " + format.format(endDate);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEndDateConstraint() {
        return endDateConstraint;
    }

    public void setEndDateConstraint(String endDateConstraint) {
        this.endDateConstraint = endDateConstraint;
    }

    public String getDateRange() {
        return dateRange;
    }
}
