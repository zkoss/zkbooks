package org.zkoss.reference.component.diagrams;

import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.zkoss.zkex.zul.impl.JFreeChartEngine;
import org.zkoss.zul.Chart;

/**
 * change a gantt plot series color
 */
public class MyJfreeChartEngine extends JFreeChartEngine {

    @Override
    protected boolean prepareJFreeChart(JFreeChart jfchart, Chart chart) {
        CategoryPlot categoryPlot = jfchart.getCategoryPlot();
        if (categoryPlot != null){ //gantt chart uses CategoryPlot
            categoryPlot.getRenderer().setSeriesPaint(0, ChartColor.YELLOW);
            categoryPlot.getRenderer().setSeriesPaint(0, ChartColor.GREEN);
        }
        return super.prepareJFreeChart(jfchart, chart);
    }
}
