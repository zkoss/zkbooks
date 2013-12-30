package org.zkoss.zss.essential.advanced.customization;

import java.io.File;
import java.io.IOException;

import org.zkoss.zk.ui.WebApps;
import org.zkoss.zss.api.Importer;
import org.zkoss.zss.api.Importers;
import org.zkoss.zss.api.model.Book;
import org.zkoss.zss.api.model.Sheet;
import org.zkoss.zss.ui.UserActionContext;
import org.zkoss.zss.ui.UserActionHandler;

/**
 * Load a blank Excel file to spreadsheet.
 * @author Hawk
 *
 */
public class NewBookHandler implements UserActionHandler {

	@Override
	public boolean isEnabled(Book book, Sheet sheet) {
		return true;
	}

	@Override
	public boolean process(UserActionContext context) {
		Importer importer = Importers.getImporter();
		
		try {
			Book loadedBook = importer.imports(new File(WebApps.getCurrent()
							.getRealPath("/WEB-INF/books/blank.xlsx")), "blank.xlsx");
			context.getSpreadsheet().setBook(loadedBook);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
