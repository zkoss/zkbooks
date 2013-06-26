/* CustomizedActionHandler.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Apr 2, 2012 12:30:11 PM , Created by sam
}}IS_NOTE

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.zssessentials.config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.usermodel.DataFormat;
import org.zkoss.poi.ss.usermodel.Hyperlink;
import org.zkoss.poi.ss.usermodel.PrintSetup;
import org.zkoss.poi.ss.usermodel.Sheet;
import org.zkoss.poi.ss.util.AreaReference;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.SelectionEvent;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Exporter;
import org.zkoss.zss.model.Exporters;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.model.impl.BookHelper;
import org.zkoss.zss.model.impl.Headings;
import org.zkoss.zss.ui.Rect;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.impl.Utils;
import org.zkoss.zss.ui.sys.ActionHandler;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * @author sam
 *
 */
public class CustomizedActionHandler extends ActionHandler {

	@Override
	public void doNewBook() {
		getSpreadsheet().setSrc("/WEB-INF/excel/config/blank.xlsx");
	}
	
	@Override
	public void doSaveBook() {
		Spreadsheet spreadsheet = getSpreadsheet();
		if (spreadsheet.getBook() != null) {
			final String filePath = Executions.getCurrent().getDesktop().getWebApp().getRealPath(spreadsheet.getSrc());
			
			Exporter exporter = Exporters.getExporter("excel");
			FileOutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(new File(filePath));
				exporter.export(spreadsheet.getBook(), outputStream);
					Messagebox.show("Saved");
			} catch (FileNotFoundException e) {
					Messagebox.show("Save excel failed");
			} finally {
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
					}
				}
			}
		}			
	}
	
	@Override
	public void doExportPDF(Rect selection) {
		Spreadsheet spreadsheet = getSpreadsheet();
		if (spreadsheet.getBook() != null && isValidSelection(selection)) {
			ExportPDFDialog dialog = new ExportPDFDialog(selection);
			dialog.setParent(spreadsheet.getParent());
			try {
				dialog.doModal();
			} catch (SuspendNotAllowedException e) {
				
			}
		}
	}
	
	@Override
	public void doPasteSpecial(Rect selection) {
		Spreadsheet spreadsheet = getSpreadsheet();
		if (getClipboard() != null && spreadsheet.getBook() != null && isValidSelection(selection)) {
			PasteSpecialDialog dialog = new PasteSpecialDialog(selection);
			dialog.setParent(spreadsheet.getParent());
			try {
				dialog.doModal();
			} catch (SuspendNotAllowedException e) {
				
			}
		}
	}
	
	@Override
	public void doCustomSort(Rect selection) {
		Spreadsheet spreadsheet = getSpreadsheet();
		if (spreadsheet.getBook() != null && isValidSelection(selection)) {
			CustomSortDialog dialog = new CustomSortDialog(selection);
			dialog.setParent(spreadsheet.getParent());
			try {
				dialog.doModal();
			} catch (SuspendNotAllowedException e) {
			};	
		}
	}
	
	@Override
	public void doHyperlink(Rect selection) {
		Spreadsheet spreadsheet = getSpreadsheet();
		if (spreadsheet.getBook() != null && isValidSelection(selection)) {
			InsertHyperlinkDialog dialog = new InsertHyperlinkDialog(selection);
			dialog.setParent(getSpreadsheet().getParent());
			try {
				dialog.doModal();
			} catch (SuspendNotAllowedException e) {
			}	
		}
	}
	

	@Override
	public void doColumnWidth(Rect selection) {
		Spreadsheet spreadsheet = getSpreadsheet();
		if (spreadsheet.getBook() != null && isValidSelection(selection)) {
			HeaderSizeDialog dialog = new HeaderSizeDialog("column", selection);
			dialog.setParent(getSpreadsheet().getParent());
			try {
				dialog.doModal();
			} catch (SuspendNotAllowedException e) {
			};	
		}
	}


	@Override
	public void doRowHeight(Rect selection) {
		Spreadsheet spreadsheet = getSpreadsheet();
		if (spreadsheet.getBook() != null && isValidSelection(selection)) {
			HeaderSizeDialog dialog = new HeaderSizeDialog("row", selection);
			dialog.setParent(getSpreadsheet().getParent());
			try {
				dialog.doModal();
			} catch (SuspendNotAllowedException e) {
			};	
		}
	}


	@Override
	public void doFormatCell(Rect selection) {
		Spreadsheet spreadsheet = getSpreadsheet();
		if (spreadsheet.getBook() != null && isValidSelection(selection)) {
			FormatDialog dialog = new FormatDialog(selection);
			dialog.setParent(getSpreadsheet().getParent());
			try {
				dialog.doModal();
			} catch (SuspendNotAllowedException e) {
			};	
		}
	}
	
	@Override
	public void doInsertFunction(Rect selection) {
		Spreadsheet spreadsheet = getSpreadsheet();
		if (spreadsheet.getBook() != null && isValidSelection(selection)) {
			InsertFunctionDialog dialog = new InsertFunctionDialog(selection);
			dialog.setParent(getSpreadsheet().getParent());
			try {
				dialog.doModal();
			} catch (SuspendNotAllowedException e) {
			};	
		}
	}
	
	public class Dialog extends Window {
		protected Rect selection;
		public Dialog(Rect selection) {
			this.selection = selection;
			setClosable(true);
			setBorder(true);
		}
	}
	
	public class InsertFunctionDialog extends Dialog {

		Listbox functions;
		
		Button okBtn;
		
		public InsertFunctionDialog(Rect selection) {
			super(selection);
			
			setTitle("Format");
			
			Executions.createComponents("/config/insertFunctionDialog.zul", this, null);
			Components.wireVariables(this, this);
			Components.addForwards(this, this);
		}
		
		public void onClick$okBtn() {
			Listitem seld = functions.getSelectedItem();
			if (seld != null) {
				String functionName = seld.getLabel();
				
				Ranges
				.range(getSpreadsheet().getSelectedSheet(), selection.getTop(), selection.getLeft(), selection.getBottom(), selection.getRight())
				.setEditText("=" + functionName + "()");
			}
			
			InsertFunctionDialog.this.detach();
		}
	}
	
	public class FormatDialog extends Dialog {

		Listbox category;
		Listitem formatGeneral;
		Listitem formatNumber;
		Listitem formatCurrency;
		Listitem formatAccounting;
		Listitem formatDate;
		Listitem formatTime;
		Listitem formatPercentage;
		Listitem formatFraction;
		Listitem formatScientific;
		Listitem formatText;
		Listitem formatSpecial;
		
		Listbox general;
		Listbox number;
		Listbox currency;
		Listbox accounting;
		Listbox date;
		Listbox time;
		Listbox percentage;
		Listbox fraction;
		Listbox scientific;
		Listbox text;
		Listbox special;
		HashMap<String, Listbox> listboxs = new HashMap<String, Listbox>();
		Listbox selected;
		
		Button okBtn;
		
		public FormatDialog(Rect selection) {
			super(selection);
			setTitle("Format");
			
			Executions.createComponents("/config/formatDialog.zul", this, null);
			Components.wireVariables(this, this);
			Components.addForwards(this, this);
			
			init();
		}
		
		void init() {
			listboxs.put("general", general);
			listboxs.put("number", number);
			listboxs.put("currency", currency);
			listboxs.put("accounting", accounting);
			listboxs.put("date", date);
			listboxs.put("time", time);
			listboxs.put("percentage", percentage);
			listboxs.put("fraction", fraction);
			listboxs.put("scientific", scientific);
			listboxs.put("text", text);
			listboxs.put("special", special);
		}
		
		public void onSelect$category () {
			String val = (String) category.getSelectedItem().getValue();
			for (String key : listboxs.keySet()) {
				Listbox cmp = listboxs.get(key);
				boolean found = key.equals(val) ? true : false;
				cmp.setVisible(found);
				if (found) {
					selected = cmp;
				}
			}
		}
		
		public void onClick$okBtn() {
			if (selected != null) {
				String format = (String) selected.getSelectedItem().getValue();
				
				Book book = _spreadsheet.getBook();
				Worksheet sheet = _spreadsheet.getSelectedSheet();
				DataFormat dataFormat = book.createDataFormat();
				short formatIndex = dataFormat.getFormat(format);
				
				for (int r = selection.getTop(); r <= selection.getBottom(); r++) {
					for (int c = selection.getLeft(); c <= selection.getRight(); c++) {
						Cell cell = BookHelper.getOrCreateCell(_spreadsheet.getSelectedSheet(), r, c);
						if (formatIndex != cell.getCellStyle().getDataFormat()) {
							CellStyle newStyle = book.createCellStyle();
							newStyle.cloneStyleFrom(cell.getCellStyle());
							newStyle.setDataFormat(formatIndex);
							Ranges.range(sheet, r, c).setStyle(newStyle);
						}
					}
				}
			}
			
			FormatDialog.this.detach();
		}
	}
	
	public class InsertHyperlinkDialog extends Dialog {
		
		Textbox display;
		Textbox address;
		
		Button okBtn;
		
		public InsertHyperlinkDialog(Rect selection) {
			super(selection);
			setTitle("Insert Hyperlink");
			
			Executions.createComponents("/config/insertHyperlinkDialog.zul", this, null);
			Components.wireVariables(this, this);
			Components.addForwards(this, this);
		}
		
		public void onClick$okBtn() {
			Range range = Ranges.range(_spreadsheet.getSelectedSheet(), selection.getTop(), selection.getLeft(), selection.getBottom(), selection.getRight());
			range.setHyperlink(Hyperlink.LINK_URL, address.getValue(), display.getValue());
			
			InsertHyperlinkDialog.this.detach();
		}
	}
	
	public class CustomSortDialog extends Dialog {

		Checkbox caseSensitive;
		Checkbox hasHeader;
		
		boolean sortByRow;//default is sort by column
		Combobox sort;
		Comboitem sortByColumns;
		Comboitem sortByRows;
		
		Combobox sortOrder;
		Comboitem sortAscending;
		Comboitem sortDescending;
		
		Combobox sortBy1;
		Combobox sortBy2;
		Combobox sortBy3;
		ListModel sortByModel;
		
		Button okBtn;
		ComboitemRenderer sortByRenderer = new ComboitemRenderer() {
			
			@Override
			public void render(Comboitem item, Object data, int i) throws Exception {
				Integer index = (Integer) data;
				
				if (sortByRow) {
					item.setLabel("Row " + _spreadsheet.getRowtitle(index));
				} else {
					item.setLabel("Column " + _spreadsheet.getColumntitle(index));
				}
				item.setValue(index);
			}

		};
		
		public CustomSortDialog(Rect selection) {
			super(selection);
			setTitle("Custom Sort");
			
			Executions.createComponents("/config/customSortDialog.zul", this, null);
			Components.wireVariables(this, this);
			Components.addForwards(this, this);
			
			init();
		}
		
		void init() {
			sortOrder.setSelectedItem(sortAscending);
			
			sortBy1.setItemRenderer(sortByRenderer);
			sortBy2.setItemRenderer(sortByRenderer);
			sortBy3.setItemRenderer(sortByRenderer);
			
			if (sortByRow) {
				sort.setSelectedItem(sortByRows);
				sortByModel = newRowModel();
			} else {
				sort.setSelectedItem(sortByColumns);
				sortByModel = newColumnModel();
			}
			
			sortBy1.setModel(sortByModel);
			sortBy2.setModel(sortByModel);
			sortBy3.setModel(sortByModel);
		}
		
		ListModel newColumnModel() {
			ArrayList<Integer> index = new ArrayList<Integer>();
			for (int i = selection.getLeft(); i <= selection.getRight(); i++) {
				index.add(i);
			}
			
			return new ListModelList(index);
		}
		
		ListModel newRowModel() {
			ArrayList<Integer> index = new ArrayList<Integer>();
			for (int i = selection.getTop(); i <= selection.getBottom(); i++) {
				index.add(i);
			}
			
			return new ListModelList(index);
		}
		
		public void onSelect$sort(SelectionEvent evt) {
			Component seld = evt.getTarget();
			if (seld == sortByColumns && sortByRow) {
				sortByRow = false;//sort by colum
				
				sortByModel = newColumnModel();
				
				sortBy1.setModel(sortByModel);
				sortBy2.setModel(sortByModel);
				sortBy3.setModel(sortByModel);
			} else if (seld == sortByRows && !sortByRow) {
				sortByRow = true;
				
				sortByModel = newRowModel();
				
				sortBy1.setModel(sortByModel);
				sortBy2.setModel(sortByModel);
				sortBy3.setModel(sortByModel);
			}
		}
		
		public void onClick$okBtn() {
			Range range = Ranges.range(_spreadsheet.getSelectedSheet(), selection.getTop(), selection.getLeft(), selection.getBottom(), selection.getRight());
			
			Range rng1 = null;
			Range rng2 = null;
			Range rng3 = null;
			
			if (sortBy1.getSelectedItem() != null) {
				Integer index = (Integer) sortBy1.getSelectedItem().getValue();
				if (sortByRow) {
					rng1 = Ranges.range(_spreadsheet.getSelectedSheet(), index, selection.getLeft(), index, selection.getRight()).getRows();
				} else {
					rng1 = Ranges.range(_spreadsheet.getSelectedSheet(), selection.getTop(), index, selection.getBottom(), index).getColumns();
				}
			}
			
			if (sortBy2.getSelectedItem() != null) {
				Integer index = (Integer) sortBy2.getSelectedItem().getValue();
				if (sortByRow) {
					rng2 = Ranges.range(_spreadsheet.getSelectedSheet(), index, selection.getLeft(), index, selection.getRight()).getRows();
				} else {
					rng2 = Ranges.range(_spreadsheet.getSelectedSheet(), selection.getTop(), index, selection.getBottom(), index).getColumns();
				}
			}
			
			if (sortBy3.getSelectedItem() != null) {
				Integer index = (Integer) sortBy3.getSelectedItem().getValue();
				if (sortByRow) {
					rng3 = Ranges.range(_spreadsheet.getSelectedSheet(), index, selection.getLeft(), index, selection.getRight()).getRows();
				} else {
					rng3 = Ranges.range(_spreadsheet.getSelectedSheet(), selection.getTop(), index, selection.getBottom(), index).getColumns();
				}
			}
			
			boolean descending = sortOrder.getSelectedItem() == sortDescending;
			range.sort(rng1, descending, rng2, 0, descending, rng3, descending, 
					hasHeader.isChecked() ? BookHelper.SORT_HEADER_YES : BookHelper.SORT_HEADER_NO, 0, 
							caseSensitive.isChecked(), sortByRow, 0, BookHelper.SORT_NORMAL_DEFAULT, BookHelper.SORT_NORMAL_DEFAULT, BookHelper.SORT_NORMAL_DEFAULT);
			
			CustomSortDialog.this.detach();
		}
	}
	
	
	
	public class ExportPDFDialog extends Dialog {
		
		Radiogroup range;
		Radio currSelection;
		Radio currSheet;
		Radio allSheet;
		
		Radiogroup orientation;
		Radio landscape;
		Radio portrait;
		
		Checkbox noHeader;
		
		Checkbox noGridlines;
		
		Button export;
		
		public ExportPDFDialog(final Rect selection) {
			super(selection);
			setTitle("Export PDF");
			
			Executions.createComponents("/config/exportPDFDialog.zul", this, null);
			Components.wireVariables(this, this);
			Components.addForwards(this, this);
			
			init();
		}
		
		void init() {
			range.setSelectedItem(currSheet);
			noHeader.setChecked(true);
			noGridlines.setChecked(_spreadsheet.getSelectedSheet().isPrintGridlines());
			orientation.setSelectedItem(_spreadsheet.getSelectedSheet().getPrintSetup().getLandscape() ? landscape : portrait);
			export.setAutodisable("self");
		}
		
		public void onClick$export() {
			applyPrintSetting();
			
			Exporter exporter = Exporters.getExporter("pdf");
			
			if (exporter instanceof Headings) {
				((Headings)exporter).enableHeadings(!noHeader.isChecked());
			}
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Radio seld = range.getSelectedItem();
			if (seld == allSheet) {
				exporter.export(_spreadsheet.getBook(), outputStream);
			} else if (seld == currSelection) {
				String ref = _spreadsheet.getColumntitle(selection.getLeft()) + _spreadsheet.getRowtitle(selection.getTop()) + ":" + 
					_spreadsheet.getColumntitle(selection.getRight()) + _spreadsheet.getRowtitle(selection.getBottom());
				exporter.exportSelection(_spreadsheet.getSelectedSheet(), new AreaReference(ref), outputStream);
			} else {
				exporter.export(_spreadsheet.getSelectedSheet(), outputStream);
			}
			
			AMedia amedia = new AMedia("generatedReport.pdf", "pdf", "application/pdf", outputStream.toByteArray());
			Filedownload.save(amedia);
			ExportPDFDialog.this.detach();
		}
		
		void applyPrintSetting() {
			_spreadsheet.getSelectedSheet().getPrintSetup().setLandscape(orientation.getSelectedItem() == landscape);
			_spreadsheet.getSelectedSheet().setPrintGridlines(!noGridlines.isChecked());
			boolean isLandscape = orientation.getSelectedItem() == landscape;
			final Book book = _spreadsheet.getBook(); 

			int numSheet = book.getNumberOfSheets();
			for (int i = 0; i < numSheet; i++) {
				Sheet sheet = _spreadsheet.getSheet(i);
				PrintSetup setup = sheet.getPrintSetup();
				setup.setLandscape(isLandscape);
			}
		}
	}
	
	public class PasteSpecialDialog extends Dialog {
		
		Radiogroup pasteSelector;
		
		Radio all;
		Radio allExcpetBorder;
		Radio colWidth;
		Radio comment;
		Radio formula;
		Radio formulaWithNum;
		Radio value;
		Radio valueWithNumFmt;
		Radio fmt;
		Radio validation;
		
		Checkbox skipBlanks;
		Checkbox transpose;
		
		Button okBtn;
		
		public PasteSpecialDialog(Rect selection) {
			super(selection);
			setTitle("Paste Special");
			
			Executions.createComponents("/config/pasteSpecialDialog.zul", this, null);
			Components.wireVariables(this, this);
			Components.addForwards(this, this);
			
			init();
		}
		
		void init() {
			pasteSelector.setSelectedItem(all);
			skipBlanks.setChecked(false);
			transpose.setChecked(false);
		}
		
		public void onClick$okBtn() {
			final ActionHandler actionHandler = _spreadsheet.getActionHandler();
			Clipboard clipboard = actionHandler.getClipboard();
			
			if (clipboard != null) {
				final Worksheet srcSheet = clipboard.sourceSheet;
				final Rect srcRect = clipboard.sourceRect;
				
				Range pasteFrom = Ranges.range(srcSheet, srcRect.getTop(), srcRect.getLeft(), srcRect.getBottom(), srcRect.getRight());
				Range pasteTo = Ranges.range(_spreadsheet.getSelectedSheet(), selection.getTop(), selection.getLeft(), selection.getBottom(), selection.getRight());
				pasteFrom.pasteSpecial(pasteTo, getPasteType(pasteSelector.getSelectedItem().getValue().toString()), Range.PASTEOP_NONE, skipBlanks.isChecked(), transpose.isChecked());
				
				if (clipboard.type == Clipboard.Type.CUT) {
					actionHandler.doClearAll(srcRect); //clear cell context and style
					actionHandler.clearClipboard();
					_spreadsheet.setHighlight(null); //hide highlight
				}
			}
			
			PasteSpecialDialog.this.detach();
		}
		
		private int getPasteType(String type) {
			if (type == null 
					|| "paste".equals(type)
					|| "all".equals(type) )
				return Range.PASTE_ALL;
			
			if ( "allExcpetBorder".equals(type) ) {
				return Range.PASTE_ALL_EXCEPT_BORDERS;
			} else if ( "columnWidth".equals(type) ) {
				return Range.PASTE_COLUMN_WIDTHS;
			} else if ( "comment".equals(type) ) {
				return Range.PASTE_COMMENTS;
			} else if ( "formula".equals(type) ) {
				return Range.PASTE_FORMULAS;
			} else if ( "formulaWithNumFmt".equals(type) ) {
				return Range.PASTE_FORMULAS_AND_NUMBER_FORMATS;
			} else if ( "value".equals(type) ) {
				return Range.PASTE_VALUES;
			} else if ( "valueWithNumFmt".equals(type) ) {
				return Range.PASTE_VALUES_AND_NUMBER_FORMATS;
			} else if ( "format".equals(type) ) {
				return Range.PASTE_FORMATS;
			} else if ( "validation".equals(type) ) {
				return Range.PASTE_VALIDATAION;
			}
			
			return Range.PASTE_ALL;
		}
	}
	
	public class HeaderSizeDialog extends Dialog {
		
		Label title;
		
		Intbox headerSize;
		
		Button okBtn;
		
		boolean isRowHeader;
		
		public HeaderSizeDialog(String header, Rect selection) {
			super(selection);
			setTitle("Header Size");
			
			Executions.createComponents("/config/headerSizeDialog.zul", this, null);
			Components.wireVariables(this, this);
			Components.addForwards(this, this);

			init(header);
		}
		
		void init(String header) {
			if ("row".equals(header)) {
				title.setValue("Row height:");
				isRowHeader = true;
			} else {
				title.setValue("Column width:");
			}
			
			
		}
		
		public void onClick$okBtn() {
			int points = Utils.pxToPoint(headerSize.getValue());
			
			Range range = Ranges.range(_spreadsheet.getSelectedSheet(), selection.getTop(), selection.getLeft(), selection.getBottom(), selection.getRight());
			if (isRowHeader) {
				range.getRows().setRowHeight(points);
			} else {
				range.getColumns().setColumnWidth(points);
			}
			
			HeaderSizeDialog.this.detach();
		}
	}
}
