/* StockComposer.java

	Purpose:
		
	Description:
		
	History:
		Nov 19, 2010 2:44:37 PM, Created by henrichen

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/

package org.zkoss.zssessentials.cellaction;

import org.zkoss.poi.ss.usermodel.Sheet;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellEvent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;

/**
 * @author henrichen
 *
 */
public class StockComposer extends GenericForwardComposer {
	private StockUpdateService service;
	private Vlayout message;
	private Spreadsheet stock;
	private Sheet monitorSheet;
	private int left;
	private int top;
	private int right;
	private int bottom;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		service = new StockUpdateService(stock);
		monitorSheet = stock.getSelectedSheet();
		final Range priceRange = Ranges.range(monitorSheet, "price");
		left = priceRange.getColumn();
		top = priceRange.getRow();
		right = priceRange.getLastColumn();
		bottom = priceRange.getLastRow();
	}
	public void onCellChange$stock(CellEvent event) {
		final Sheet sheet = event.getSheet();
		if (!monitorSheet.equals(sheet)) {
			return; //not the monitorSheet, return
		}
		final int row = event.getRow();
		final int col = event.getColumn();
		if (left <= col && col <= right && top <= row && row <= bottom) { //in range
			final Range priceRng = Ranges.range(monitorSheet, row, col);
			final Range sellRng = priceRng.getOffset(0, 3);
			final Range buyRng = priceRng.getOffset(0, 2);
			final Range codeRng = priceRng.getOffset(0, -1);
			final double newPrice = ((Number)priceRng.getValue()).doubleValue();
			final double sellPrice = ((Number)sellRng.getValue()).doubleValue();
			final double buyPrice = ((Number)buyRng.getValue()).doubleValue();
			final String stockCode = (String) codeRng.getValue();
			if (newPrice <= buyPrice) {
				buy(stockCode, priceRng);
			} else if (newPrice >= sellPrice) {
				sell(stockCode, priceRng);
			}
		}
	}
	private void buy(String stockCode, Range priceRng) {
		//stockService.buy(stockCode, price);
		new Label("Buy "+stockCode+" at price: "+priceRng.getText()).setParent(message);
	}
	private void sell(String stockCode, Range priceRng) {
		new Label("Sell "+stockCode+" at price: "+priceRng.getText()).setParent(message);
	}
}
