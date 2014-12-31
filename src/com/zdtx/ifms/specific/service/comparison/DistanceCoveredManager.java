/**
 * @File: DistanceCoveredManager.java
 * @path: idsas - com.zdtx.ifms.specific.service.comparison
 */
package com.zdtx.ifms.specific.service.comparison;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBarSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.vo.comparison.ChartVO;

/**
 * @ClassName: DistanceCoveredManager
 * @Description: comparison-Distance Covered-manager
 * @author: Leon Liu
 * @date: 2013-9-24 下午1:32:06
 * @version V1.0
 */
@Service
@Transactional
public class DistanceCoveredManager {

	@Autowired
	private BaseDao dao;
	
	/**
	 * 根据条件获取图表数据
	 * @param vo 条件集合
	 * @return List<Chart>
	 */
	public List<KeyAndValue> getBatch(ChartVO vo) {
		String[] dateRange = DateUtil.getThreeWeeksBeginAndEnd(vo.getBeginDate());
		String sql = "SELECT SUM(RUNKM) AS KEY, WEEKLY AS VALUE" +
								" FROM T_BEH_NOT_DAY_SUM" +
								" WHERE DEPT_ID IS NOT NULL" +
								" AND RIQI >= '" + dateRange[0] + 
								"' AND RIQI <= '" + dateRange[1] + "'";
		if(!Utils.isEmpty(vo.getDepartmentID()) && vo.getDepartmentID() != -1L) {		//按部门查询，没选则为全部部门
			sql += " AND DEPT_ID = " + vo.getDepartmentID();
		}
		sql += " GROUP BY WEEKLY ORDER BY WEEKLY ASC";
		return dao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 *  导出excel 操作
	 * @param vo 条件集合
	 * @return excel stream
	 */
	public InputStream getExcel(ChartVO vo) {
		List<KeyAndValue> dataList = this.getBatch(vo);	//get chart data
		OPCPackage opcPackage = null;
		XSSFWorkbook workbook = null;
		InputStream fis = this.getClass().getResourceAsStream("/../../excel-temp/temp_distanceCovered.xlsx");
		try {
			opcPackage = OPCPackage.open(fis);		//read temp stream can prevent the temp file from be modified
			workbook = new XSSFWorkbook(opcPackage);
			XSSFSheet sheet = workbook.getSheetAt(0);	//read first sheet
			for (int i = 0, j = dataList.size(); i < j; i++) {
				sheet.getRow(i + 1).getCell(0).setCellValue(dataList.get(i).getValue());
				sheet.getRow(i + 1).getCell(1).setCellValue(Double.valueOf(dataList.get(i).getKey()));
			}
			XSSFChart chart = sheet.createDrawingPatriarch().getCharts().get(0);	//get chart object
			//change chart's data area
			CTBarSer ser = chart.getCTChart().getPlotArea().getBarChartList().get(0).getSerArray(0);
			ser.getCat().getNumRef().setF("'Distance Covered'!$A$2:$A$" + (dataList.size() + 1));
			ser.getVal().getNumRef().setF("'Distance Covered'!$B$2:$B$" + (dataList.size() + 1));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
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
				opcPackage.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}