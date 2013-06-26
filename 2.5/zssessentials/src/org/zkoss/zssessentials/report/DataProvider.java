/* DataProvider.java

	Purpose:
		
	Description:
		
	History:
		Nov 18, 2010 6:43:09 PM, Created by henrichen

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/

package org.zkoss.zssessentials.report;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import au.com.bytecode.opencsv.CSVReader;

/**
 * @author henrichen
 *
 */
public class DataProvider {
	public static List<ComputerBean> query() throws IOException {
		File csv = new File(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/WEB-INF/excel/report/data.csv"));
		CSVReader reader = new CSVReader(new FileReader(csv));
		List<ComputerBean> data = new ArrayList<ComputerBean>();
		String[] nextLine;
	    while ((nextLine = reader.readNext()) != null) {
	    	ComputerBean computerBean = new ComputerBean();
	    	computerBean.setId(nextLine[0]);
	    	computerBean.setProduct(nextLine[1]);
	    	computerBean.setBrand(nextLine[2]);
	    	computerBean.setModel(nextLine[3]);
	    	computerBean.setSerialNumber(nextLine[4]);
	    	computerBean.setDate(nextLine[5]);
	    	computerBean.setWarrantyTime(nextLine[6]);
	    	computerBean.setCost(Double.valueOf(nextLine[7]).doubleValue());
	    	computerBean.setOs(nextLine[8]);
	    	computerBean.setSalvage(Double.valueOf(nextLine[9]).doubleValue());
	        data.add(computerBean);
	    }
	    return data;
	}
}
