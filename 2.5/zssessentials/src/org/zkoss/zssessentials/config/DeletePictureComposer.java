/* AddChartComposer.java

	Purpose:
		
	Description:
		
	History:
		Oct 25, 2011 9:23:21 AM, Created by henri

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zssessentials.config;

import org.zkoss.poi.ss.usermodel.Picture;
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
public class DeletePictureComposer<Component> extends GenericForwardComposer {
	private Spreadsheet myss;
	
	public void onClick$delete(MouseEvent evt) throws Exception {
		//delete all picture
		Worksheet sheet = myss.getSelectedSheet();
		Range rng = Ranges.range(sheet);
		for(Picture pic : sheet.getPictures()) {
			rng.deletePicture(pic);
		}
	}
}
