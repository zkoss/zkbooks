/* CellStyleComposer.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Nov 17, 2010 8:57:21 AM , Created by Sam
}}IS_NOTE

Copyright (C) 2009 Potix Corporation. All Rights Reserved.
*/
package org.zkoss.zssessentials.config;

import org.zkoss.lang.Objects;
import org.zkoss.poi.ss.usermodel.BorderStyle;
import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.usermodel.Color;
import org.zkoss.poi.ss.usermodel.Font;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkex.zul.Colorbox;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.model.impl.BookHelper;
import org.zkoss.zss.ui.Rect;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellSelectionEvent;
import org.zkoss.zss.ui.impl.Utils;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Toolbarbutton;

/**
 * CellStyleComposer demonstrate how to set cell's style including set font style, cell's style
 * @author Sam
 *
 */
public class CellStyleComposer extends GenericForwardComposer{
	
	private Combobox dateFormat;
	private Combobox fontFamily;
	private Combobox fontSize;
	
	private boolean isBold;
	private Toolbarbutton boldBtn;
	
	private boolean isItalic;
	private Toolbarbutton italicBtn;
	
	private boolean isUnderline;
	private Toolbarbutton underlineBtn;
	
	private boolean isStrikethrough;
	private Toolbarbutton strikethroughBtn;
	
	private Combobox borderCombobox;
	
	private Colorbox fontColor;
	private Colorbox cellColor;
	
	private Spreadsheet spreadsheet;
	private Rect selection;
	
	/**
	 * Clone current style
	 * @param srcStyle
	 * @param book
	 * @return cellstyle
	 */
	private CellStyle cloneStyle(CellStyle srcStyle, Book book) {
		CellStyle newStyle =  book.createCellStyle();
		newStyle.cloneStyleFrom(srcStyle);
		return newStyle;
	}

	/**
	 * Save current cell selection
	 * @param event
	 */
	public void onCellSelection$spreadsheet(CellSelectionEvent event) {
		selection = spreadsheet.getSelection();
	}
	
	/**
	 * Retrieve cell selection
	 * @return
	 */
	public Rect getSelection() {
		return selection != null ? selection : spreadsheet.getSelection();
	}
	
	/**
	 * Sets font family of selection range
	 */
	public void onSelect$fontFamily() {
		String fontName = fontFamily.getText();
		setFontFamily(fontName);
	}

	/**
	 * Sets font family
	 * @param fontName
	 */
	public void setFontFamily(String fontName) {
		Rect rect = getSelection();
		Worksheet sheet = spreadsheet.getSelectedSheet();
		Book book = spreadsheet.getBook();
		for (int row = rect.getTop(); row <= rect.getBottom(); row++) {
			for (int col = rect.getLeft(); col <= rect.getRight(); col++) {
				Cell cell = Utils.getOrCreateCell(sheet, row, col);
				Font srcFont = book.getFontAt(cell.getCellStyle().getFontIndex());
					
				if (srcFont.getFontName() != fontName) {
					Font newFont = BookHelper.getOrCreateFont(book, srcFont.getBoldweight(), BookHelper.getFontColor(book, srcFont), srcFont.getFontHeight(), fontName, 
							srcFont.getItalic(), srcFont.getStrikeout(), srcFont.getTypeOffset(), srcFont.getUnderline());
						
					CellStyle newStyle = cloneStyle(cell.getCellStyle(), book);
					newStyle.setFont(newFont);
						
					Ranges.range(sheet, row, col).setStyle(newStyle);
				}
			}
		}
	}
	
	/**
	 * Returns font height of the font size
	 * @param fontSize
	 * @return
	 */
	private short getFontHeight(int fontSize) {
		return (short) (fontSize * 20);
	}
	
	/**
	 * Sets the cell format
	 */
	public void onSelect$dateFormat(SelectEvent event) {
		String format = (String) dateFormat.getSelectedItem().getValue();
		setCellFormat(format);
	}
	
	private void setCellFormat(String format) {
		Book book = spreadsheet.getBook();
		short dstFormat = book.createDataFormat().getFormat(format);
		
		Rect rect = getSelection();
		Worksheet sheet = spreadsheet.getSelectedSheet();
		for (int row = rect.getTop(); row <= rect.getBottom(); row++) {
			for (int col = rect.getLeft(); col <= rect.getRight(); col++) {
				Cell cell = Utils.getOrCreateCell(sheet, row, col);
				CellStyle srcCellStyle = cell.getCellStyle();
				short srcDataFormat = srcCellStyle.getDataFormat();
				
				if (srcDataFormat != dstFormat) {
					CellStyle newStyle = book.createCellStyle();
					newStyle.cloneStyleFrom(srcCellStyle);
					newStyle.setDataFormat(dstFormat);
					Ranges.range(sheet, row, col).setStyle(newStyle);
				}
			}
		}
	}
	
	/**
	 * Sets font size of selection range
	 */
	public void onSelect$fontSize() {
		short fontHeight = getFontHeight(Integer.parseInt(fontSize.getText()));
		setFontSize(fontHeight);
	}
	
	/**
	 * Sets font size
	 * @param fontHeight
	 */
	public void setFontSize(short fontHeight) {
		Rect rect = getSelection();
		Worksheet sheet = spreadsheet.getSelectedSheet();
		Book book = spreadsheet.getBook();
		for (int row = rect.getTop(); row <= rect.getBottom(); row++) {
			for (int col = rect.getLeft(); col <= rect.getRight(); col++) {
				Cell cell = Utils.getOrCreateCell(sheet, row, col);
			
				Font srcFont = book.getFontAt(cell.getCellStyle().getFontIndex());
				if (srcFont.getFontHeight() != fontHeight) {
					Font newFont = BookHelper.getOrCreateFont(book, srcFont.getBoldweight(), BookHelper.getFontColor(book, srcFont), fontHeight, srcFont.getFontName(), 
							srcFont.getItalic(), srcFont.getStrikeout(), srcFont.getTypeOffset(), srcFont.getUnderline());
					
					CellStyle newStyle = cloneStyle(cell.getCellStyle(), book);
					newStyle.setFont(newFont);
						
					Ranges.range(sheet, row, col).setStyle(newStyle);
				}
			}
		}
	}
	
	/**
	 * Sets font color of selection range
	 */
	public void onChange$fontColor() {
		String color = fontColor.getColor();
		setFontColor(color);
	}
	/**
	 * Sets font color
	 * @param color
	 */
	public void setFontColor(String color) {
		Rect rect = getSelection();
		Worksheet sheet = spreadsheet.getSelectedSheet();
		Book book = spreadsheet.getBook();
		for (int row = rect.getTop(); row <= rect.getBottom(); row++) {
			for (int col = rect.getLeft(); col <= rect.getRight(); col++) {
				Cell cell = Utils.getOrCreateCell(sheet, row, col);
				Font srcFont = book.getFontAt(cell.getCellStyle().getFontIndex());
				String srColor = BookHelper.getFontHTMLColor(book, srcFont);		
				srColor = srColor == null || BookHelper.AUTO_COLOR.equals(color) ? "#000000" : srColor;
				
				if (srColor != color) {
					Font newFont = BookHelper.getOrCreateFont(book, srcFont.getBoldweight(), BookHelper.HTMLToColor(book, color), srcFont.getFontHeight(), srcFont.getFontName(), 
							srcFont.getItalic(), srcFont.getStrikeout(), srcFont.getTypeOffset(), srcFont.getUnderline());

					CellStyle newStyle = cloneStyle(cell.getCellStyle(), book);
					newStyle.setFont(newFont);
					Ranges.range(sheet, row, col).setStyle(newStyle);
				}
			}
		}
	}
	
	/**
	 * Sets cell color
	 */
	public void onChange$cellColor() {
		String color = cellColor.getColor();
		setCellColor(color);
	}
	
	/**
	 * Sets cell color
	 * @param color
	 */
	public void setCellColor(String color) {
		Rect rect = getSelection();
		Worksheet sheet = spreadsheet.getSelectedSheet();
		Book book = spreadsheet.getBook();
		final Color newColor = BookHelper.HTMLToColor(book, color);
		
		for (int row = rect.getTop(); row <= rect.getBottom(); row++) {
			for (int col = rect.getLeft(); col <= rect.getRight(); col++) {
				Cell cell = Utils.getOrCreateCell(sheet, row, col);
				CellStyle cellStyle = cell.getCellStyle();
				final Color srcColor = cellStyle.getFillForegroundColorColor();
				
				if (!Objects.equals(newColor, srcColor)) {
					CellStyle newStyle = cloneStyle(cellStyle, book);
					newStyle.cloneStyleFrom(cellStyle);
					BookHelper.setFillForegroundColor(newStyle, newColor);
					
					Ranges.range(sheet, row, col).setStyle(newStyle);
				}
			}
		}
	}
	
	/**
	 * Sets font bold
	 */
	public void onClick$boldBtn() {
		isBold = !isBold;
		setFontBold(isBold);
	}
	/**
	 * Sets font bold
	 * @param isBold
	 */
	public void setFontBold(boolean isBold) {
		Rect rect = getSelection();
		Worksheet sheet = spreadsheet.getSelectedSheet();
		Book book = spreadsheet.getBook();
		for (int row = rect.getTop(); row <= rect.getBottom(); row++) {
			for (int col = rect.getLeft(); col <= rect.getRight(); col++) {
				Cell cell = Utils.getOrCreateCell(sheet, row, col);
				Font srcFont = book.getFontAt(
						cell.getCellStyle().getFontIndex());
					
				boolean srcBold = srcFont.getBoldweight() == Font.BOLDWEIGHT_BOLD; 
				if (srcBold != isBold) {
					Font newFont = BookHelper.getOrCreateFont(book, isBold ? Font.BOLDWEIGHT_BOLD : Font.BOLDWEIGHT_NORMAL, BookHelper.getFontColor(book, srcFont), srcFont.getFontHeight(), srcFont.getFontName(), 
							srcFont.getItalic(), srcFont.getStrikeout(), srcFont.getTypeOffset(), srcFont.getUnderline());
						
					CellStyle newStyle = cloneStyle(cell.getCellStyle(), book);
					newStyle.setFont(newFont);
						
					Ranges.range(sheet, row, col).setStyle(newStyle);
				}
			}
		}
	}
	/**
	 * Sets font italic
	 */
	public void onClick$italicBtn() {
		isItalic = !isItalic;
		setItalic(isItalic);
	}
	
	/**
	 * Sets font italic
	 * @param isItalic
	 */
	public void setItalic(boolean isItalic) {
		Rect rect = getSelection();
		Worksheet sheet = spreadsheet.getSelectedSheet();
		Book book = spreadsheet.getBook();
		for (int row = rect.getTop(); row <= rect.getBottom(); row++) {
			for (int col = rect.getLeft(); col <= rect.getRight(); col++) {
				Cell cell = Utils.getOrCreateCell(sheet, row, col);
				Font srcFont = book.getFontAt(
						cell.getCellStyle().getFontIndex());
					
				boolean srcItalic = srcFont.getItalic();
				if (srcItalic != isItalic) {
					Font newFont = BookHelper.getOrCreateFont(book, srcFont.getBoldweight(), BookHelper.getFontColor(book, srcFont), srcFont.getFontHeight(), srcFont.getFontName(), 
							isItalic, srcFont.getStrikeout(), srcFont.getTypeOffset(), srcFont.getUnderline());
					
					CellStyle newStyle = cloneStyle(cell.getCellStyle(), book);
					newStyle.setFont(newFont);
						
					Ranges.range(sheet, row, col).setStyle(newStyle);
				}
			}
		}
	}
	/**
	 * Sets font underline
	 */
	public void onClick$underlineBtn() {
		isUnderline = !isUnderline;
		setUnderline(isUnderline);
	}
	/**
	 * Sets font underline
	 * @param isUnderline
	 */
	public void setUnderline(boolean isUnderline){
		Rect rect = getSelection();
		Worksheet sheet = spreadsheet.getSelectedSheet();
		Book book = spreadsheet.getBook();
		
		for (int row = rect.getTop(); row <= rect.getBottom(); row++) {
			for (int col = rect.getLeft(); col <= rect.getRight(); col++) {
				Cell cell = Utils.getOrCreateCell(sheet, row, col);
				Font srcFont = book.getFontAt(
						cell.getCellStyle().getFontIndex());
					
				boolean srcUnderline = srcFont.getUnderline() == Font.U_SINGLE;
				if (srcUnderline != isUnderline) {
					Font newFont = BookHelper.getOrCreateFont(book, srcFont.getBoldweight(), BookHelper.getFontColor(book, srcFont), 
							srcFont.getFontHeight(), srcFont.getFontName(), 
							srcFont.getItalic(), srcFont.getStrikeout(), srcFont.getTypeOffset(), isUnderline ? Font.U_SINGLE : Font.U_NONE);
						
					CellStyle newStyle = cloneStyle(cell.getCellStyle(), book);
					newStyle.setFont(newFont);
						
					Ranges.range(sheet, row, col).setStyle(newStyle);
				}
			}
		}
	}
	/**
	 * Sets font strikethrough
	 */
	public void onClick$strikethroughBtn() {
		isStrikethrough = !isStrikethrough;
		setStrikethrough(isStrikethrough);
	}
	/**
	 * Sets font strikethrough
	 * @param isStrikethrough
	 */
	public void setStrikethrough(boolean isStrikethrough) {
		Rect rect = getSelection();
		Worksheet sheet = spreadsheet.getSelectedSheet();
		Book book = spreadsheet.getBook();
		
		for (int row = rect.getTop(); row <= rect.getBottom(); row++) {
			for (int col = rect.getLeft(); col <= rect.getRight(); col++) {
				Cell cell = Utils.getOrCreateCell(sheet, row, col);
				Font srcFont = book.getFontAt(
						cell.getCellStyle().getFontIndex());
					
				boolean srcStrikethrough = srcFont.getStrikeout();
				if (srcStrikethrough != isStrikethrough) {
					Font newFont = BookHelper.getOrCreateFont(book, srcFont.getBoldweight(), 
							BookHelper.getFontColor(book, srcFont), srcFont.getFontHeight(), srcFont.getFontName(), 
							srcFont.getItalic(), isStrikethrough, srcFont.getTypeOffset(), srcFont.getUnderline());
						
					CellStyle newStyle = cloneStyle(cell.getCellStyle(), book);
					newStyle.setFont(newFont);
						
					Ranges.range(sheet, row, col).setStyle(newStyle);
				}
			}
		}
	}
	
	/**
	 * Sets cell border
	 */
	public void onSelect$borderCombobox() {
		String border = borderCombobox.getText();
		setBorder(border);
	}
	
	/**
	 * Sets cell border
	 * @param border
	 */
	public void setBorder(String border) {
		//Border color
		String color = "#000000";
		//Border style
		BorderStyle style = "none".equals(border) ? BorderStyle.NONE : BorderStyle.MEDIUM;
		
		Rect rect = getSelection();
		Worksheet sheet = spreadsheet.getSelectedSheet();
		int lCol = rect.getLeft();
		int rCol = rect.getRight();
		int tRow = rect.getTop();
		int bRow = rect.getBottom();
		if ("bottom".equals(border)) {
			Ranges.range(sheet, tRow, lCol, bRow, rCol).
				setBorders(BookHelper.BORDER_EDGE_BOTTOM, style, color);
		} else if ("top".equals(border)) {
			Ranges.range(sheet, tRow, lCol, tRow, rCol).
				setBorders(BookHelper.BORDER_EDGE_TOP, style, color);
		} else if ("left".equals(border)) {
			Ranges.range(sheet, tRow, lCol, bRow, lCol).
				setBorders(BookHelper.BORDER_EDGE_LEFT, style, color);
		} else if ("right".equals(border)) {
			Ranges.range(sheet, tRow, rCol, bRow, rCol).
				setBorders(BookHelper.BORDER_EDGE_RIGHT, style, color);
		} else if ("none".equals(border)) {
			Ranges.range(sheet, tRow, lCol, bRow, rCol).
				setBorders(BookHelper.BORDER_FULL, style, color);
		} else if ("full".equals(border)) {
			Ranges.range(sheet, tRow, lCol, bRow, rCol).
				setBorders(BookHelper.BORDER_FULL, style, color);
		}
	}
	
	/**
	 * Sets cell alignment
	 * @param event
	 */
	public void onAlignClick(ForwardEvent event) {
		String alignStr = (String) event.getData();

		short align = CellStyle.ALIGN_GENERAL;
		boolean isVer = false;
		if (alignStr.equals("left")) {
			align = CellStyle.ALIGN_LEFT;
		} else if (alignStr.equals("center")) {
			align = CellStyle.ALIGN_CENTER;
		} else if (alignStr.equals("right")) {
			align = CellStyle.ALIGN_RIGHT;
		} else if (alignStr.equals("top")) {
			align = CellStyle.VERTICAL_TOP;
			isVer = true;
		} else if (alignStr.equals("middle")) {
			align = CellStyle.VERTICAL_CENTER;
			isVer = true;
		} else if (alignStr.equals("bottom")) {
			align = CellStyle.VERTICAL_BOTTOM;
			isVer = true;
		}

		if (!isVer)
			setAlignment(align);
		else
			setVerticalAlignment(align);
	}
	/**
	 * Sets cell alignment
	 * @param alignment
	 */
	public void setAlignment(short alignment) {
		Rect rect = getSelection();
		Worksheet sheet = spreadsheet.getSelectedSheet();
		Book book = spreadsheet.getBook();
		for (int row = rect.getTop(); row <= rect.getBottom(); row++) {
			for (int col = rect.getLeft(); col <= rect.getRight(); col++) {
				Cell cell = Utils.getOrCreateCell(sheet, row, col);
				short srcAlign = cell.getCellStyle().getAlignment();
				if (srcAlign != alignment) {
					CellStyle newStyle = cloneStyle(cell.getCellStyle(), book);
					newStyle.setAlignment(alignment);
					
					Ranges.range(sheet, row, col).setStyle(newStyle);
				}
			}
		}
	}
	
	/**
	 * Sets cell vertical alignment
	 * @param alignment
	 */
	public void setVerticalAlignment(short alignment) {
	    Rect rect = getSelection();
	    Worksheet sheet = spreadsheet.getSelectedSheet();
	    Book book = spreadsheet.getBook();
	    for (int row = rect.getTop(); row <= rect.getBottom(); row++) {
	        for (int col = rect.getLeft(); col <= rect.getRight(); col++) {
	            Cell cell = Utils.getOrCreateCell(sheet, row, col);
	            short srcAlign = cell.getCellStyle().getVerticalAlignment();
	            if (srcAlign != alignment) {
	                CellStyle newStyle = cloneStyle(cell.getCellStyle(), book);
	                newStyle.setVerticalAlignment(alignment);
	                Ranges.range(sheet, row, col).setStyle(newStyle);
	            }
	        }
	    }
	}
}