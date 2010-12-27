/* StockComposer.java

	Purpose:
		
	Description:
		
	History:
		Nov 19, 2010 2:44:37 PM, Created by henrichen

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/

package org.zkoss.zssessentials.cellaction;

import java.io.InputStream;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Importer;
import org.zkoss.zss.model.Importers;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellSelectionEvent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;

/**
 * @author henrichen
 *
 */
public class StockComposer extends GenericForwardComposer {
	private static Book book = null;
	private static StockUpdateService service;
	private Vlayout message;
	private Spreadsheet stock;
	private Worksheet monitorSheet;
	private int left;
	private int top;
	private int right;
	private int bottom;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		synchronized (StockComposer.class) {
			if (book == null) {
				final Importer importer = Importers.getImporter("excel");
				final InputStream is = Sessions.getCurrent().getWebApp().getResourceAsStream("/WEB-INF/excel/cellaction/stock.xls");
				book = importer.imports(is, "stock.xls");
				book.setShareScope(EventQueues.APPLICATION);
				service = new StockUpdateService(book);
			}
		}
		stock.setBook(book);
		monitorSheet = stock.getSelectedSheet();
		final Range priceRange = Ranges.range(monitorSheet, "price");
		left = priceRange.getColumn();
		top = priceRange.getRow();
		right = priceRange.getLastColumn();
		bottom = priceRange.getLastRow();
	}
	public void onCellChange$stock(CellSelectionEvent event) {
		final Worksheet sheet = event.getSheet();
		if (!monitorSheet.equals(sheet)) {
			return; //not the monitorSheet, return
		}
		final int eleft = event.getLeft();
		final int etop = event.getTop();
		final int eright = event.getRight();
		final int ebottom = event.getBottom();
		if (left > eright || eleft > right || top > ebottom || etop > bottom) {
			return; //no intersection, return
		}
		for (int row = etop; row <= ebottom; ++row) {
			for (int col = eleft; col <= eright; ++col) {
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
		}
	}
	private int mcount = 0;
	private void buy(String stockCode, Range priceRng) {
		//stockService.buy(stockCode, price);
		new Label("Buy "+stockCode+" at price: "+priceRng.getText()).setParent(message);
		keepMessageShort(10);
	}
	private void sell(String stockCode, Range priceRng) {
		new Label("Sell "+stockCode+" at price: "+priceRng.getText()).setParent(message);
		keepMessageShort(10);
	}
	private void keepMessageShort(int count) {
		if (++mcount > count) {
			((Component)message.getChildren().get(0)).detach();
			mcount=10;
		}
	}
}
