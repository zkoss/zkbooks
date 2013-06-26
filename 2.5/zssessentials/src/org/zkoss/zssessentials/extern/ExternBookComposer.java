/* ExternBookComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Feb 23, 2011 3:30:56 PM, Created by henrichen
}}IS_NOTE

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
*/


package org.zkoss.zssessentials.extern;

import java.io.InputStream;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.BookSeries;
import org.zkoss.zss.model.Importer;
import org.zkoss.zss.model.Importers;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zssex.model.impl.BookSeriesImpl;

/**
 * Composer for Extern Book example.
 * @author henrichen
 *
 */
public class ExternBookComposer extends GenericForwardComposer {
	private BookSeries bookSeries;
	private Spreadsheet src;
	private Spreadsheet dst;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		//prepare excel importer
		final Importer importer = Importers.getImporter("excel");
		
		//import source book
		final InputStream is1 = Sessions.getCurrent().getWebApp().getResourceAsStream("/WEB-INF/excel/extern/srcbook.xlsx"); 
		final Book srcbook = importer.imports(is1, "srcbook.xlsx");
		
		//import destination book
		final InputStream is2 = Sessions.getCurrent().getWebApp().getResourceAsStream("/WEB-INF/excel/extern/dstbook.xlsx"); 
		final Book dstbook = importer.imports(is2, "dstbook.xlsx");
		
		//add both books into a BookSeries
		final Book[] books = new Book[] {srcbook, dstbook}; 
		bookSeries = new BookSeriesImpl(books);
		
		//associate either books to their corresponding UI spreadsheet components
		src.setBook(srcbook);
		dst.setBook(dstbook);
	}
}
