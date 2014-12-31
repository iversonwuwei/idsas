package com.zdtx.ifms.specific.service.query;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.ExportExcel;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.query.RunDetails;
import com.zdtx.ifms.specific.vo.query.RunDetailsVo;

/**
 * Run Details
 * 
 * @author Lonn
 * @since 2013-05-03
 */
@Service
@Transactional
public class RunDetailsManger {
	@Autowired
	public BaseDao baseDao;
	private List<Object[]> totals;

	public Page<RunDetails> getBetch(Page<RunDetails> page, RunDetailsVo rdvo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RunDetails.class);
		criteria.add(Restrictions.in("org_id", (Long[])Struts2Util.getSession().getAttribute("userFleet")));	//add authority
		List<Order> orderList = new ArrayList<Order>();
		if (null != rdvo.getOrg_id() && -1L != rdvo.getOrg_id()) {
			criteria.add(Restrictions.eq("org_id", rdvo.getOrg_id()));
		}
		if (null != rdvo.getVeh_id() && -1L != rdvo.getVeh_id()) {
			criteria.add(Restrictions.eq("veh_id", rdvo.getVeh_id()));
		}
		if (null != rdvo.getDriver_id() && -1L != rdvo.getDriver_id()) {
			criteria.add(Restrictions.eq("driver_id", rdvo.getDriver_id()));
		}
		if (null != rdvo.getRiqi() && !"".equals(rdvo.getRiqi())) {
			criteria.add(Restrictions.eq("riqi", rdvo.getRiqi()));
		}
		Page<RunDetails> pageResult = baseDao.getBatch(page, criteria.getExecutableCriteria(baseDao.getSession()), orderList);
		setTotals(rdvo);
		if (1 == pageResult.getCurrentPage()) {
			Utils.getSession().setAttribute("criteria_export", criteria);
			Utils.getSession().setAttribute("page_export", page);
			Utils.getSession().setAttribute("orderList_export", orderList);
		}
		orderList.add(Order.asc("org_id"));
		return pageResult;
	}

	@SuppressWarnings("unchecked")
	public void setTotals(RunDetailsVo rdvo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(RunDetails.class);
		criteria.add(Restrictions.in("org_id", (Long[])Struts2Util.getSession().getAttribute("userFleet")));	//add authority
		if (null != rdvo.getVeh_id() && -1L != rdvo.getVeh_id()) {
			criteria.add(Restrictions.eq("veh_id", rdvo.getVeh_id()));
		}
		if (null != rdvo.getOrg_id() && -1L != rdvo.getOrg_id()) {
			criteria.add(Restrictions.eq("org_id", rdvo.getOrg_id()));
		}
		if (null != rdvo.getRiqi() && !"".equals(rdvo.getRiqi())) {
			criteria.add(Restrictions.eq("riqi", rdvo.getRiqi()));
		}
		if (null != rdvo.getDriver_id() && -1L != rdvo.getDriver_id()) {
			criteria.add(Restrictions.eq("driver_id", rdvo.getDriver_id()));
		}
		criteria.setProjection(Projections.projectionList().add(Projections.sum("period_run")).add(Projections.sum("period_drive")).add(Projections.sum("period_idle"))
				.add(Projections.sum("period_air")).add(Projections.sum("period_hot")).add(Projections.sum("km_run")));
		this.totals = criteria.getExecutableCriteria(baseDao.getSession()).list();
	}
 
	@SuppressWarnings("unchecked")
	public InputStream getCNGALL(RunDetailsVo rdVo) {
		List<RunDetails> data = new ArrayList<RunDetails>();
		Page<RunDetails> page_export = (Page<RunDetails>) Utils.getSession().getAttribute("page_export");
		DetachedCriteria criteria_export = (DetachedCriteria) Utils.getSession().getAttribute("criteria_export");
		List<Order> orderList_export = (List<Order>) Utils.getSession().getAttribute("orderList_export");
		page_export.setPageSize(page_export.getTotalCount());
		Page<RunDetails> pageResult = baseDao.getBatch(page_export, criteria_export.getExecutableCriteria(baseDao.getSession()), orderList_export);
		if (null != pageResult) {
			data = pageResult.getResult();
		}
		ExportExcel ee = new ExportExcel() {
			@Override
			protected HSSFWorkbook disposeData(HSSFWorkbook wb, Object[] obj, List<?> data) throws IOException {
				HSSFSheet sheet = wb.getSheetAt(0);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 19));
				HSSFRow rowss = sheet.createRow(0);
				rowss.setHeightInPoints(20);
				HSSFCell hssfCell = rowss.createCell(0);
				hssfCell = this.createCell(wb, hssfCell, "Vehicle Run List");
				HSSFCellStyle style = this.createStyle(wb);
				List<RunDetails> list = (List<RunDetails>) data;
				for (int i = 0; i < list.size(); i++) {
					RunDetails r = list.get(i);
					HSSFRow row = sheet.createRow(i + 2);
					HSSFCell cell = row.createCell(0);
					cell.setCellStyle(style);
					cell.setCellValue(i + 1);
					cell = row.createCell(1);
					cell.setCellStyle(style);
					cell.setCellValue(list.get(i).getOrg_name());
					cell = row.createCell(2);// vehicle
					cell.setCellStyle(style);
					cell.setCellValue(list.get(i).getVehcode());
					cell = row.createCell(3);
					cell.setCellStyle(style);
					cell.setCellValue(list.get(i).getDriver_name());
					cell = row.createCell(4);// date
					cell.setCellStyle(style);
					cell.setCellValue(list.get(i).getRiqi());
					cell = row.createCell(5);// time start
					cell.setCellStyle(style);
					cell.setCellValue(rev(r.getTime_start()));
					cell = row.createCell(6);// time end
					cell.setCellStyle(style);
					cell.setCellValue(rev(r.getTime_end()));
					cell = row.createCell(7);// Period_run
					cell.setCellStyle(style);
					cell.setCellValue(DateUtil.formatHmsDate(r.getPeriod_run()));
					cell = row.createCell(8);// Period_drive
					cell.setCellStyle(style);
					cell.setCellValue(DateUtil.formatHmsDate(r.getPeriod_drive()));
					cell = row.createCell(9);// Period_idle
					cell.setCellStyle(style);
					cell.setCellValue(rev(r.getPeriod_idle()));
					cell = row.createCell(10);// Period_air
					cell.setCellStyle(style);
					cell.setCellValue(rev(r.getPeriod_air()));
					cell = row.createCell(11);// Period_hot
					cell.setCellStyle(style);
					cell.setCellValue(rev(r.getPeriod_hot()));
					cell = row.createCell(12);// Km_run
					cell.setCellStyle(style);
					cell.setCellValue(null == r.getKm_run() ? "" : r.getKm_run().toString());
					cell = row.createCell(13);// Km_begin
					cell.setCellStyle(style);
					cell.setCellValue(null == r.getKm_begin() ? "" : r.getKm_begin().toString());
					cell = row.createCell(14);// getKm_end
					cell.setCellStyle(style);
					cell.setCellValue(null == r.getKm_end() ? "" : r.getKm_end().toString());
					cell = row.createCell(15);// Spead_avg
					cell.setCellStyle(style);
					cell.setCellValue(null == r.getSpead_avg() ? "" : r.getSpead_avg().toString());
					cell = row.createCell(16);// Fuel
					cell.setCellStyle(style);
					cell.setCellValue(null == r.getFuel() ? "" : r.getFuel().toString());
					cell = row.createCell(17);// Fuel_avg
					cell.setCellStyle(style);
					cell.setCellValue(null == r.getFuel_avg() ? "" : r.getFuel_avg().toString());
					List<Object[]> cunt = (List<Object[]>) Utils.getSession().getAttribute("_count");

					if (null != cunt && 0 != cunt.size()) {
						HSSFRow count_row = sheet.createRow(list.size() + 2);
						for (int j = 0; j < 7; j++) {
							HSSFCell c = count_row.createCell(j);
							c.setCellValue((0 == j ? "total:" : ""));
							c.setCellStyle(this.createFontBigRed(wb));
						}
						// 合计部分，三个循环分别为，total合并部分，合并数据部分，剩余空白部分
						Object[] ob = cunt.get(0);
						for (int j = 7; j < 13; j++) {
							HSSFCell c = count_row.createCell(j);
							if ((j - 7) == 5) {
								c.setCellValue(null == ob[j - 7] ? "" : String.valueOf(ob[j - 7]));
							} else {
								c.setCellValue(DateUtil.formatHmsDate(ob[j - 7]));
							}
							c.setCellStyle(this.createFontBigRed(wb));
						}
						for (int j = 13; j < 18; j++) {
							HSSFCell c = count_row.createCell(j);
							c.setCellValue("");
							c.setCellStyle(this.createFontBigRed(wb));
						}
						sheet.addMergedRegion(new CellRangeAddress(list.size() + 2, list.size() + 2, 0, 6));
					}
				}
				return wb;
			}
		};
		Object[] total = new Object[1];
		total[0] = "Vehicle Run List";
		return ee.export(total, data, "", "No.", "Fleet", "Plate Number", "Driver", "Date", "Start Time", "End Time", "Period Run", "Period Drive", "Period Idle", "Period Air", "PeriodHot", "Run(km)",
				"Begin(km)", "End(km)", "Spead Avg(km/h)", "Fuel(L)", "Fuel Avg");
	}

	public List<Object[]> getTotals() {
		return totals;
	}

	private String rev(String value) {
		return null == value ? "00:00:00" : value;
	}
}
