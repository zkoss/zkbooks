/* StockUpdateThread.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Oct 8, 2007 9:58:59 AM     2007, Created by Dennis.Chen
}}IS_NOTE

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under GPL Version 2.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zssessentials.cellaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.model.Worksheet;

/**
 * @author Dennis.Chen
 *
 */
public class StockUpdateService {
	private Worksheet dataSheet;
	private List<StockInfo> stockModel;
	private Random random = new Random(System.currentTimeMillis());
	
	private Thread udpateThread;
	public StockUpdateService(Book book){
		dataSheet = book.getWorksheet("dataSheet");
		
		stockModel = new ArrayList<StockInfo>();
		
		//initialize dataSheet
		addInfo(new StockInfo("CSCO",32.65)); 
		addInfo(new StockInfo("EWJ",14.65));
		addInfo(new StockInfo("INTC",25.54));
		addInfo(new StockInfo("IWM",48.07));
		addInfo(new StockInfo("JPM",47.58));
		addInfo(new StockInfo("MSFT",29.84));
		addInfo(new StockInfo("SPY",155.85));
		
		udpateThread = new Thread(new UpdateRunnable());
		udpateThread.start();
	}
	
	public void addStock(String label){
		synchronized(stockModel){
			StockInfo info = new StockInfo(label,random.nextInt(10000)/100);
			addInfo(info);
		}
	}
	
	private void addInfo(StockInfo info) {
		stockModel.add(info);
		final int row = stockModel.size()-1;
		Ranges.range(dataSheet, row, 0).setEditText(info.getLabel());
		Ranges.range(dataSheet, row, 1).setValue(info.getCurrentPrice());
		Ranges.range(dataSheet, row, 2).setValue(info.getRatio());
	}
	
	public void clearAll(){
		synchronized(stockModel){
			stockModel.clear();
		}
	}
	
	public void clearRamdom(){
		synchronized(stockModel){
			int index = random.nextInt(stockModel.size());
			stockModel.remove(index);
		}
	}
	
	private void setInfo(int row, StockInfo info) {
		stockModel.set(row, info);
		Ranges.range(dataSheet, row, 0).setEditText(info.getLabel());
		Ranges.range(dataSheet, row, 1).setValue(info.getCurrentPrice());
		Ranges.range(dataSheet, row, 2).setValue(info.getRatio());
	}
	
	class UpdateRunnable implements Runnable{
		
		boolean running = false;
		int maxRatio = 10;
		public void run() {
			running = true;
			while(running){
				int size = stockModel.size();
				if(size>0){
					int updateCount = random.nextInt(size>3?3:size);
					if(updateCount==0) updateCount=1;
					for(int i=0;i<updateCount;i++){
						int index = random.nextInt(size);
						
						StockInfo si = getCurrentStockQuotes(index);//get current stock quotes			
						synchronized(stockModel){
							setInfo(index,si);//and update it
						}
					}
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					running = false;
				}
			}
		}
		/**
		 * @param si
		 * @return
		 */
		private StockInfo getCurrentStockQuotes(int index) {
			StockInfo si = (StockInfo)stockModel.get(index);
			double ratio = ((double)(random.nextInt(maxRatio*2) - maxRatio))/100;
			double price = si.getCurrentPrice();
			si.setPrviousPrice(price);
			si.setRatio(ratio);
			si.setCurrentPrice(price+ price*ratio);
			
			return si;
		}
	}
}
