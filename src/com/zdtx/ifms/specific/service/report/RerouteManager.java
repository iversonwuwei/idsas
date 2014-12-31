package com.zdtx.ifms.specific.service.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBarSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.service.BaseManager;
import com.zdtx.ifms.common.utils.ExportExcel;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.vo.query.DetailsVo;

@Service
@Transactional
public class RerouteManager {
	@Autowired
	public BaseDao baseDao;
	@Autowired
	public BaseManager baseMgr;
	
	@SuppressWarnings("unchecked")
	public InputStream getRepRouteExcel() {
		List<KeyAndValue> violationList = (List<KeyAndValue>)this.getCoeList(201000L);
		int violationCount = violationList.size();
		Page<Object[]> page_export = (Page<Object[]>) Utils.getSession().getAttribute("page");
		final String startDate = Utils.getSession().getAttribute("ge") + "";
		final String endDate = Utils.getSession().getAttribute("le") + "";
		List<Object[]> chartData = page_export.getResult();
		DetailsVo vo = (DetailsVo) Utils.getSession().getAttribute("dvo");
		final String dateType = vo.getBehCont();
		DecimalFormat format = new DecimalFormat("####.##");
		
		OPCPackage opcPackage = null;
		XSSFWorkbook workbook = null;
		InputStream fis = this.getClass().getResourceAsStream("/../../excel-temp/temp_reportRoute.xlsx");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			opcPackage = OPCPackage.open(fis);		//read temp stream can prevent the temp file from be modified
			workbook = new XSSFWorkbook(opcPackage);
			XSSFSheet sheet = workbook.getSheetAt(0);
			//title row
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5 + violationCount));
			//navigation row
			XSSFRow row = sheet.getRow(1);
			row.getCell(2).setCellValue(vo.getOrgName());	//set department
			if ("1".equals(dateType)) {
				row.getCell(4).setCellValue("Daily");	//set Stat Type
				row.getCell(7).setCellValue(vo.getDate1());	//set date
			}
			if ("2".equals(dateType)) {
				row.getCell(4).setCellValue("Weekly");	//set Stat Type
				row.getCell(7).setCellValue(vo.getDate2());	//set date
			}
			if ("3".equals(dateType)) {
				row.getCell(4).setCellValue("Monthly");	//set Stat Type
				row.getCell(7).setCellValue(vo.getDate3());	//set date
			}
			if ("4".equals(dateType)) {
				row.getCell(4).setCellValue("Yearly");	//set Stat Type
				row.getCell(7).setCellValue(vo.getDate4());	//set date
			}
			//grid's header row
			row = sheet.getRow(2);	
			XSSFCell cell = null;
			CellStyle cellStyle = ExportExcel.createXNormalFont(workbook);	//load header row's cell style
			cellStyle.setWrapText(true);	//set wrap
			for (int i = 0; i < violationCount; i++) {	//add violations columns
				cell = row.createCell(i + 6);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(violationList.get(i).getValue());
			}
			//grid's body row
			//first row
			row = sheet.getRow(3);	
			CellStyle redCellStyle = ExportExcel.createXRedNormalFont(workbook);		//load body row's red font cell style
			Object[] obj = chartData.get(0);	//first data
			row.getCell(1).setCellValue(obj[1] + "");
			row.getCell(2).setCellValue(startDate + " " + "00:00:00");
			row.getCell(3).setCellValue(endDate + " " + "23:59:59");
			row.getCell(4).setCellValue(Utils.isEmpty(obj[3]) ? 0 : Double.valueOf(obj[3] + ""));
			double totalScore = (obj[4] != null ? Double.parseDouble(obj[4] + "") : 0d);
			double score = 0d;
			if ("1".equals(dateType)) {
				score = 100 - totalScore;
			}
			if ("2".equals(dateType)) {
				score = 100 - totalScore/ 7;
			}
			if ("3".equals(dateType)) {
				score = 100 - totalScore/ 30;
			}
			if ("4".equals(dateType)) {
				score = 100 - totalScore/ 365;
			}
			if (score < 0) {
				score = 0d;
			}
			score = Double.valueOf(format.format(score));
			row.getCell(5).setCellValue(score);
			for (int i = 0; i < violationCount; i++) {	//add violations data
				row.createCell(i + 6).setCellValue(Utils.isEmpty(obj[i + 5]) ? 0 : Double.valueOf(obj[i + 5] + ""));
			}
			if(score < 50) {	//all row's cell  red font when score <= 50
				for (int j = 0, k = row.getLastCellNum(); j < k; j++) {
					row.getCell(j).setCellStyle(redCellStyle);
				}
			} else {
				for (int j = 0, k = row.getLastCellNum(); j < k; j++) {
					row.getCell(j).setCellStyle(cellStyle);
				}
			}
			//other row
			for (int i = 1; i < chartData.size(); i++) {	
				obj = chartData.get(i);	//current data
				row = sheet.createRow(i + 3);	//create new row
				row.createCell(0).setCellValue(i+1);	//No. column
				row.createCell(1).setCellValue(obj[1] + "");	//Department column
				row.createCell(2).setCellValue(startDate + " " + "00:00:00");	//Start Time column
				row.createCell(3).setCellValue(endDate + " " + "23:59:59");	//End Time column
				row.createCell(4).setCellValue(Utils.isEmpty(obj[3]) ? 0 : Double.valueOf(obj[3] + ""));	//Total column
				totalScore = (obj[4] != null ? Double.parseDouble(obj[4] + "") : 0d);
				score = 0d;
				if ("1".equals(dateType)) {
					score = 100 - totalScore;
				}
				if ("2".equals(dateType)) {
					score = 100 - totalScore/ 7;
				}
				if ("3".equals(dateType)) {
					score = 100 - totalScore/ 30;
				}
				if ("4".equals(dateType)) {
					score = 100 - totalScore/ 365;
				}
				if (score < 0) {
					score = 0d;
				}
				score = Double.valueOf(format.format(score));
				row.createCell(5).setCellValue(score);	//Score column
				for (int j = 0; j < violationCount; j++) {	//add violations data
					row.createCell(j + 6).setCellValue(Utils.isEmpty(obj[j + 5]) ? 0 : Double.valueOf(obj[j + 5] + ""));
				}
				if(score < 50) {	//all row's cell  red font when score <= 50
					for (int j = 0, k = row.getLastCellNum(); j < k; j++) {
						row.getCell(j).setCellStyle(redCellStyle);
					}
				} else {
					for (int j = 0, k = row.getLastCellNum(); j < k; j++) {
						row.getCell(j).setCellStyle(cellStyle);
					}
				}
			}
			//change chart's data area
			XSSFChart chart = sheet.createDrawingPatriarch().getCharts().get(0);	//get chart object
			CTBarSer ser = chart.getCTChart().getPlotArea().getBarChartList().get(0).getSerArray(0);
			ser.getCat().getNumRef().setF("'Report Route'!$B$4:$B$" + (chartData.size() + 3));
			ser.getVal().getNumRef().setF("'Report Route'!$F$4:$F$" + (chartData.size() + 3));
			workbook.write(baos);
			byte[] b = baos.toByteArray();
			return new ByteArrayInputStream(b);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				fis.close();
				baos.close();
				opcPackage.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<KeyAndValue> getCoeList(Long categoryID) {
		String sql = "SELECT DICT_ID AS KEY, DICTNAME AS VALUE FROM T_CORE_DICT WHERE ISDELETE = 'F' AND SORT_ID = "
				+ categoryID + " ORDER BY DICT_ID ASC";
		return baseDao.getKeyAndValueBySQL(sql);
	}
	public String setScore(String beh, String score) {
		DecimalFormat format = new DecimalFormat("####.##");
		if (score == null || "".equals(score)) {
			score = "0";
		}
		String temp = "";
		if (beh.equals("1")) {
			if (100 - Double.valueOf(score) > 0) {
				temp = format.format(100 - Double.valueOf(score));
			} else {
				temp = "0";
			}
		}
		if (beh.equals("2")) {
			if (100 - Double.valueOf(score) / 7 > 0) {
				temp = format.format(100 - Double.valueOf(score) / 7);
			} else {
				temp = "0";
			}
		}
		if (beh.equals("3")) {
			if (100 - Double.valueOf(score) / 30 > 0) {
				temp = format.format(100 - Double.valueOf(score) / 30);
			} else {
				temp = "0";
			}
		}
		if (beh.equals("4")) {
			if (100 - Double.valueOf(score) / 365 > 0) {
				temp = format.format(100 - Double.valueOf(score) / 365);
			} else {
				temp = "0";
			}
		}
		return temp;
	}
}
