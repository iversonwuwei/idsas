/*
 * @(#)ExportExcel.java  Mar 17, 2011
 * Copyright(c) 2011, Wiflg Goth. All rights reserved.
 */
package com.zdtx.ifms.common.utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * 抽象高阶定义 - 不同实现
 * */


/**
 *
 * @author Leon Chen
 * @since Mar 17, 2011
 */
public abstract class ExportExcel {

	/**
	 *	Convert workbook to inputStream
	 * @param HSSFWorkbook
	 * @return	InputStream
	 */
	private InputStream convertOutputStream(HSSFWorkbook workbook) {

		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			workbook.write(baos);
			baos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] b = baos.toByteArray();
		InputStream is = new ByteArrayInputStream(b);
		return is;
	}

	/**
	 * Draw data on workbook
	 * @param workbook
	 * @param total	total data
	 * @param data
	 * @return	HSSFWorkbook
	 * @throws IOException
	 */
	protected abstract HSSFWorkbook disposeData(HSSFWorkbook workbook, Object[] total, List<?> data) throws IOException;

	/**
	 *
	 * @param total
	 * @param data
	 * @param headers
	 * @return
	 */
	public final InputStream export(Object[] total, List<?> data, String sheetName, String... headers) {

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName + "-" + DateUtil.formatDate(new Date()));
		//生成列头
		if (null != headers) {
			HSSFRow row = sheet.createRow(1);
			HSSFCellStyle style = this.createStyle(wb);
			for (int i = 0; i < headers.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style);
				sheet.setColumnWidth(i, 7000);
				cell.setCellValue(headers[i].trim().replace("[cv]", "").replace("[ttli]", "").replace("[ttld]", ""));
			}
		}
		HSSFWorkbook workbook = null;
		try {
			workbook = this.disposeData(wb, total, data);
			if (null != workbook) {
				sheet = workbook.getSheetAt(0);
				if (null != sheet) {
					int n = sheet.getRow(1).getLastCellNum();
					for (int i = 0; i < n; i++) {
						sheet.autoSizeColumn(i);
						if((sheet.getColumnWidth(i) + 1000) >= 80*256) {	//防止内容过长超出excel最大宽度限制
							sheet.setColumnWidth(i, 80*256);
						} else {
							sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.convertOutputStream(workbook);
	}

	/**
	 * prepare the style 
	 * @param style	XSSFCellStyle
	 * @param isWrap	if wrap cell context
	 * @return	XSSFCellStyle
	 */
	private static XSSFCellStyle prepareStyle(XSSFCellStyle style, boolean isWrap) {
		style.setWrapText(isWrap);
		style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
		return style;
	}
	
	/**
	 * @param XSSFWorkbook
	 * @return XSSFCellStyle	黑色普通字体
	 */
	public static XSSFCellStyle createXNormalFont(XSSFWorkbook workbook){
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints(( short )11); //font size
		font.setFontName("Verdana");
		font.setColor(Font.COLOR_NORMAL);
		XSSFCellStyle style = workbook.createCellStyle();
		style = prepareStyle(style, true);
		style.setFont(font);
		return style;
	}

	/**
	 * @param XSSFWorkbook
	 * @return XSSFCellStyle	红色普通字体
	 */
	public static XSSFCellStyle createXRedNormalFont(XSSFWorkbook workbook){
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints(( short )11); //font size
		font.setFontName("Verdana");
		font.setColor(Font.COLOR_RED);
		XSSFCellStyle style = workbook.createCellStyle();
		style = prepareStyle(style, true);
        style.setFont(font);
        return style;
	}
	
	/**
	 * @param XSSFWorkbook
	 * @return XSSFCellStyle	黑色加粗字体
	 */
	public static XSSFCellStyle createXBolderFont(XSSFWorkbook workbook){
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints(( short )11); // 字体高度
		font.setFontName("Verdana"); // 字体
		font.setColor(Font.COLOR_NORMAL);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		XSSFCellStyle style = workbook.createCellStyle();
		style = prepareStyle(style, true);
		style.setFont(font);
		return style;
	}
	
	
	/**
	 * @param XSSFWorkbook
	 * @return XSSFCellStyle	红色加粗字体
	 */
	public static XSSFCellStyle createXRedBolderFont(XSSFWorkbook workbook){
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints(( short )11); // 字体高度
		font.setFontName("Verdana"); // 字体
		font.setColor(Font.COLOR_RED);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		XSSFCellStyle style = workbook.createCellStyle();
		style = prepareStyle(style, true);
		style.setFont(font);
        return style;
	}
	
	/**
	 * @param XSSFWorkbook
	 * @return	XSSFCellStyle	暗红色普通字体
	 */
	public static XSSFCellStyle createXDarkRedNormalFont(XSSFWorkbook workbook) {
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short)11); // 字体高度
		font.setFontName("Verdana"); // 字体
		font.setColor(HSSFColor.DARK_RED.index);
		XSSFCellStyle style = workbook.createCellStyle();
		style = prepareStyle(style, true);
		style.setFont(font);
		return style;
	}
	
	/**
	 * @param XSSFWorkbook
	 * @return	XSSFCellStyle	暗红色加粗字体
	 */
	public static XSSFCellStyle createXDarkRedBolderFont(XSSFWorkbook workbook) {
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short)11); // 字体高度
		font.setFontName("Verdana"); // 字体
		font.setColor(HSSFColor.DARK_RED.index);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		XSSFCellStyle style = workbook.createCellStyle();
		style = prepareStyle(style, true);
        style.setFont(font);
        return style;
	}
	
	/**
	 * @param wb
	 * @return 红色大字体
	 */
	public HSSFCellStyle createFontBigRed(HSSFWorkbook wb){
		HSSFFont cnFont = wb.createFont();
		cnFont.setFontHeightInPoints(( short ) 11 ); // 字体高度
		cnFont.setFontName( " 微软雅黑 " ); // 字体
		cnFont.setColor(HSSFColor.RED.index);
		cnFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		HSSFCellStyle style = wb.createCellStyle();
		style.setWrapText(true);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setFont(cnFont);
        return style;
	}
	
	/**
	* 根据颜色号获得样式
	*/
	public HSSFCellStyle getStyleByColorNo(HSSFWorkbook wb, short colorNo){
		HSSFFont cnFont = wb.createFont();
		cnFont.setFontHeightInPoints(( short ) 11 ); // 字体高度
		cnFont.setFontName("Verdana"); // 字体
		cnFont.setColor(colorNo);
		cnFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		HSSFCellStyle style = wb.createCellStyle();
		style.setWrapText(true);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setFont(cnFont);
        return style;
	}

	/**
	 *
	 * @param wb
	 * @param cell
	 * @param textString
	 * @return HSSFCell
	 */
	public HSSFCell createCell(final HSSFWorkbook wb,final HSSFCell cell,final String textString ){
		HSSFFont cnFont = wb.createFont();
		cnFont.setFontHeightInPoints(( short ) 11 ); // 字体高度
		cnFont.setFontName( "Verdana" ); // 字体
		cnFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
		HSSFCellStyle cnStyle = wb.createCellStyle();
		cnStyle.setFont(cnFont);
		cnStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中
		cell.setCellStyle(cnStyle);
		HSSFRichTextString richText = new HSSFRichTextString(textString);
		cell.setCellValue(richText);
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(cnFont);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //垂直居中
		cellStyle.setWrapText( true );
		cnStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //垂直居中
		cell.setCellStyle(cellStyle);
		return cell;
	}
	
	/**
	 * @param wb
	 * @return 黑色正常
	 */
	public HSSFCellStyle createStyle(HSSFWorkbook wb){
		HSSFFont cnFont = wb.createFont();
		cnFont.setFontHeightInPoints((short )11); // 字体高度
		cnFont.setFontName("Verdana"); // 字体
		HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setWrapText(true);     
        style.setFont(cnFont);
        return style;
	}
	
	/**
	 * @param wb
	 * @return 黑色居左自动换行正常
	 */
	public HSSFCellStyle blackLeftEnter(HSSFWorkbook wb){
		HSSFFont cnFont = wb.createFont();
		cnFont.setFontHeightInPoints((short )11); // 字体高度
		cnFont.setFontName("Verdana"); // 字体
		HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setFont(cnFont);
        style.setWrapText(true);
        return style;

	}
	/**
	 * @param wb
	 * @return 红色正常
	 */
	public HSSFCellStyle createFont_Thin(HSSFWorkbook wb){
		HSSFFont cnFont = wb.createFont();
		cnFont.setFontHeightInPoints((short)11); // 字体高度
		cnFont.setFontName("Verdana"); // 字体
		cnFont.setColor(HSSFColor.RED.index);
		HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setFont(cnFont);
        return style;

	}
	/**
	 * @param wb
	 * @return 暗红色粗体
	 */
	public HSSFCellStyle createFontRed(HSSFWorkbook wb){
		HSSFFont cnFont = wb.createFont();
		cnFont.setFontHeightInPoints((short)11); // 字体高度
		cnFont.setFontName("Verdana"); // 字体
		cnFont.setColor(HSSFColor.DARK_RED.index);
		cnFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setFont(cnFont);
        return style;
	}
	
	/**
	 * @param wb
	 * @return 黑色粗体
	 */
    public HSSFCellStyle createFont(HSSFWorkbook wb){
    	HSSFFont cnFont = wb.createFont();
		cnFont.setFontHeightInPoints(( short ) 11 ); // 字体高度
		cnFont.setFontName( "Verdana" ); // 字体
		cnFont.setColor(HSSFColor.RED.index);
		cnFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 加粗
		HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setFont(cnFont);
        return style;
	}
}