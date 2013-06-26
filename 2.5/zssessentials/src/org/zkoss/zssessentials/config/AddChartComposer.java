/* AddChartComposer.java

	Purpose:
		
	Description:
		
	History:
		Oct 25, 2011 9:23:21 AM, Created by henri

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zssessentials.config;

import org.zkoss.poi.ss.usermodel.ClientAnchor;
import org.zkoss.poi.ss.usermodel.charts.ChartDataSource;
import org.zkoss.poi.ss.usermodel.charts.ChartGrouping;
import org.zkoss.poi.ss.usermodel.charts.ChartTextSource;
import org.zkoss.poi.ss.usermodel.charts.ChartType;
import org.zkoss.poi.ss.usermodel.charts.DataSources;
import org.zkoss.poi.ss.usermodel.charts.LegendPosition;
import org.zkoss.poi.ss.util.CellRangeAddress;
import org.zkoss.poi.xssf.usermodel.XSSFClientAnchor;
import org.zkoss.poi.xssf.usermodel.charts.XSSFPieChartData;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.ui.Spreadsheet;

/**
 * @author henri
 *
 */
public class AddChartComposer extends GenericForwardComposer {
	private Spreadsheet myss;
	
	public void onClick$add(MouseEvent evt) {
		//add a pie chart
		Worksheet sheet = myss.getSelectedSheet();
		Range rng = Ranges.range(sheet);
		
		ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 2, 1, 7, 11); //C2:H12
		XSSFPieChartData data = new XSSFPieChartData();
		
		ChartTextSource title = DataSources.fromString("Language Popularity");
		ChartDataSource<String> cats = DataSources.fromStringCellRange(sheet, CellRangeAddress.valueOf("A1:A3"));
		ChartDataSource<Number> vals = DataSources.fromNumericCellRange(sheet, CellRangeAddress.valueOf("B1:B3"));
		data.addSerie(title, cats, vals);
		rng.addChart(anchor, data, ChartType.Pie, ChartGrouping.STANDARD, LegendPosition.RIGHT);
	}
}
