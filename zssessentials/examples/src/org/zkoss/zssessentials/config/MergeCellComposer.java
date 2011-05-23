/* MergeCellComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		May 23, 2011 9:25:33 AM , Created by sam
}}IS_NOTE

Copyright (C) 2011 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.zssessentials.config;

import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellSelectionEvent;
import org.zkoss.zul.Button;

/**
 * @author sam
 *
 */
public class MergeCellComposer extends GenericForwardComposer {
	Spreadsheet spreadsheet;
	int topRow;
	int bottomRow;
	int leftCol;
	int rightCol;
	
	Button mergeCells;
	Button splitMergedCells;
	
	/**
	 * Save current spreadsheet selection range
	 */
	public void onCellSelection$spreadsheet(CellSelectionEvent event) {
		topRow = event.getTop();
		bottomRow = event.getBottom();
		leftCol = event.getLeft();
		rightCol = event.getRight();
	}
	
	/**
	 * Merge cells
	 */
	public void onClick$mergeCells() {
		Ranges.range(spreadsheet.getSelectedSheet(), 
				topRow, 
				leftCol, 
				bottomRow, 
				rightCol).merge(false);
	}
	
	/**
	 * Split merged cells
	 */
	public void onClick$splitMergedCells() {
		Ranges.range(spreadsheet.getSelectedSheet(), 
				topRow, 
				leftCol, 
				bottomRow, 
				rightCol).unMerge();
	}
}
