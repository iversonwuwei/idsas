package com.zdtx.ifms.specific.service.query;

import java.io.*;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.service.BaseManager;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.ExportExcel;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.query.RunDaily;
import com.zdtx.ifms.specific.vo.query.RunDailyVo;

/**
 * @ClassName: RunDailyManager
 * @Description: 车辆运行报告-车辆运行日报、月报、汇总报告-业务层
 * @author JHQ
 * @date 2012年12月4日 10:29:14
 * @version V1.0
 */

@Service
@Transactional
public class RunDailyManager {
	@Autowired
	public BaseDao baseDao;
	@Autowired
	public BaseManager baseMgr;
	private List<Object> _count;
	private KeyAndValue _date = new KeyAndValue();

	@SuppressWarnings("unchecked")
	public void set_count(RunDailyVo rdvo) {
		Criteria criteria = baseDao.disposeSortCriteria(getRunDailyCriteria(rdvo, "1"), (List<Order>) Utils.getSession().getAttribute("o"), (Page<RunDaily>) Utils.getSession().getAttribute("p"));
		List<Object[]> list = criteria.list();
		List<RunDaily> data = parseTeam(list, "1");
		Double d1 = 0.0, d2 = 0.0, d3 = 0.0, d4 = 0.0, d5 = 0.0, d6 = 0.0;
		_count = new ArrayList<Object>();
		for (RunDaily r : data) {
			d1 += Double.valueOf(null == r.getPeriod_run() ? "0" : r.getPeriod_run());
			d2 += Double.valueOf(null == r.getPeriod_drive() ? "0" : r.getPeriod_drive());
			d3 += Double.valueOf(null == r.getPeriod_idle() ? "0" : r.getPeriod_idle());
			d4 += Double.valueOf(null == r.getPeriod_air() ? "0" : r.getPeriod_air());
			d5 += Double.valueOf(null == r.getPeriod_hot() ? "0" : r.getPeriod_hot());
			d6 += (null == r.getKm_run() ? 0 : r.getKm_run());
		}
		_count.add(d1);
		_count.add(d2);
		_count.add(d3);
		_count.add(d4);
		_count.add(d5);
		_count.add(d6);
	}

	private Criteria getRunDailyCriteria(RunDailyVo rdvo, String kind) {
		Criteria criteria = this.getSearchCriteria(rdvo, kind);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.sum("period_run"), "_period_run").add(Projections.sum("period_drive"), "_period_drive").add(Projections.sum("period_idle"), "_period_idle")
				.add(Projections.sum("period_air"), "_period_air").add(Projections.sum("period_hot"), "_period_hot").add(Projections.sum("km_run"), "_km_run")
				.add(Projections.min("km_begin"), "_km_begin").add(Projections.max("km_end"), "_km_end").add(Projections.sum("fuel"), "_fuel").add(Projections.max("day_id"), "_day_id")
				.add(Projections.max("vehcode"), "_vehcode").add(Projections.max("riqi"), "_riqi").add(Projections.min("time_start"), "_time_start").add(Projections.max("time_stop"), "_time_stop")
				.add(Projections.groupProperty("vehcode"), "_vehcode");
		if (1L == rdvo.getDaytype()) {
			projList.add(Projections.groupProperty("riqi"), "_riqi");
		}
		criteria.setProjection(projList);
		return criteria;
	};

	public Criteria getSearchCriteria(RunDailyVo rdvo, String kind) {
		Criteria criteria = baseDao.getSession().createCriteria(RunDaily.class);
		criteria.add(Restrictions.in("org_id", (Long[])Struts2Util.getSession().getAttribute("userFleet")));	//add authority
		_date = new KeyAndValue();
		if (null != rdvo.getVeh_id() && -1L != rdvo.getVeh_id()) {
			criteria.add(Restrictions.eq("veh_id", rdvo.getVeh_id()));
		}
		if (1L == rdvo.getDaytype()) {// Daily
			if (null != rdvo.getDate1() && !"".equals(rdvo.getDate1())) {
				criteria.add(Restrictions.eq("riqi", rdvo.getDate1()));
			}
		}
		if (2L == rdvo.getDaytype()) {// Weekly
			if (null != rdvo.getDate2() && !"".equals(rdvo.getDate2())) {
				_date.setKey(DateUtil.dateToWeek(rdvo.getDate2())[0]);
				_date.setValue(DateUtil.dateToWeek(rdvo.getDate2())[1]);
				criteria.add(Restrictions.ge("riqi", _date.getKey()));
				criteria.add(Restrictions.le("riqi", _date.getValue()));
			}
		}
		if (3L == rdvo.getDaytype()) {// Monthly
			if (null != rdvo.getDate3() && !"".equals(rdvo.getDate3())) {
				_date.setKey(rdvo.getDate3() + "-01");
				_date.setValue(DateUtil.getLastDayOfMonth(rdvo.getDate3()));
				criteria.add(Restrictions.ge("riqi", _date.getKey()));
				criteria.add(Restrictions.le("riqi", _date.getValue()));
			}
		}
		if (4L == rdvo.getDaytype()) {// Yearly
			if (null != rdvo.getDate4() && !"".equals(rdvo.getDate4())) {
				_date.setKey(rdvo.getDate4().split("-")[0] + "-01-01");
				_date.setValue(rdvo.getDate4().split("-")[0] + "-12-31");
				criteria.add(Restrictions.ge("riqi", _date.getKey()));
				criteria.add(Restrictions.le("riqi", _date.getValue()));
			}
		}
		return criteria;
	}

	public Page<RunDaily> getAll(Page<RunDaily> page, RunDailyVo rdvo, String kind) {
		Utils.getSession().setAttribute("p", page);
		Utils.getSession().setAttribute("o", this.getOderBy(rdvo.getDaytype()));
		set_count(rdvo);
		return this.getRunDaily(page, this.getRunDailyCriteria(rdvo, kind), this.getOderBy(rdvo.getDaytype()), kind);

	}

	private List<Order> getOderBy(Long timetype) {
		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.asc("vehcode"));
		if (1L == timetype) {
			orders.add(Order.desc("riqi"));
		}
		return orders;
	}

	// public String date(String date) {
	// return DateUtil.getNextMonth(date);
	// }

	@SuppressWarnings("unchecked")
	public Page<RunDaily> getRunDaily(Page<RunDaily> page, Criteria criteria, List<Order> orders, String kind) {
		page.setTotalCount(criteria.list().size());
		criteria.setFirstResult(page.getFirst());
		criteria.setMaxResults(page.getPageSize());
		criteria = baseDao.disposeSortCriteria(criteria, orders, page);
		List<Object[]> list = criteria.list();
		page.setResult(this.parseTeam(list, kind));
		return page;
	}

	private List<RunDaily> parseTeam(List<Object[]> list, String kind) {
		List<RunDaily> rd = new ArrayList<RunDaily>();
		for (Object[] o : list) {
			RunDaily run = new RunDaily();
			if (null != o[0]) {
				run.setPeriod_run(o[0].toString());
			}
			if (null != o[1]) {
				run.setPeriod_drive(o[1].toString());
			}
			if (null != o[2]) {
				run.setPeriod_idle(o[2].toString());
			}
			if (null != o[3]) {
				run.setPeriod_air(o[3].toString());
			}
			if (null != o[4]) {
				run.setPeriod_hot(o[4].toString());
			}
			if (null != o[5]) {
				run.setKm_run(Float.valueOf(o[5].toString()));
			}
			if (null != o[6]) {
				run.setKm_begin(Float.valueOf(o[6].toString()));
			}
			if (null != o[7]) {
				run.setKm_end(Float.valueOf(o[7].toString()));
			}
			if (null != o[8]) {
				run.setFuel(Float.valueOf(o[8].toString()));
			}
			if (null != o[9]) {
				run.setDay_id(Long.valueOf(o[9].toString()));
			}
			if ("1".equals(kind)) {// || "2".equals(kind)
				if (null != o[10]) {
					run.setVehcode(o[10].toString());
				}
				if ("1".equals(kind)) {
					if (null != o[11]) {
						run.setRiqi(o[11].toString());
					}
					if (null != o[12]) {
						run.setTime_start(o[12].toString());
					}
					if (null != o[13]) {
						run.setTime_stop(o[13].toString());
					}
				}
			} else {
				// if ("3".equals(kind)) {
				// if (null != o[10]) {
				// run.setOrg_name(o[10].toString());
				// }
				// }
			}

			rd.add(run);
		}
		return rd;
	}

	@SuppressWarnings("unchecked")
	public InputStream createExcel(RunDailyVo rdvo, String kind) {
		List<RunDaily> data = new ArrayList<RunDaily>();
		Criteria criteria = baseDao.disposeSortCriteria(getRunDailyCriteria(rdvo, kind), (List<Order>) Utils.getSession().getAttribute("o"), (Page<RunDaily>) Utils.getSession().getAttribute("p"));
		List<Object[]> list = criteria.list();
		data = parseTeam(list, kind);
		ExportExcel ee = new ExportExcel() {
			@Override
			protected HSSFWorkbook disposeData(HSSFWorkbook wb, Object[] obj, List<?> data) throws IOException {
				Long type = (Long) obj[0];
				String date_star = null == Utils.getSession().getAttribute("date_star") ? "" : Utils.getSession().getAttribute("date_star").toString();
				String date_end = null == Utils.getSession().getAttribute("date_end") ? "" : Utils.getSession().getAttribute("date_end").toString();
				HSSFSheet sheet = wb.getSheetAt(0);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (1L == type ? 13 : 12)));
				HSSFRow rowss = sheet.createRow(0);
				rowss.setHeightInPoints(20);
				HSSFCell hssCell = rowss.createCell(0);
				hssCell = this.createCell(wb, hssCell, "Vehicle Run Query Export");
				HSSFCellStyle style = this.createStyle(wb);
				HSSFCellStyle red = this.createFontBigRed(wb);
				if (null != data && 0 != data.size()) {
					for (int i = 1; i <= data.size(); i++) {
						HSSFRow row = sheet.createRow(i + 1);
						RunDaily rd = (RunDaily) data.get(i - 1);
						HSSFCell cell = row.createCell(0);// index
						cell.setCellStyle(style);
						cell.setCellValue(i);
						cell = row.createCell(1);// Vehicle
						cell.setCellStyle(style);
						cell.setCellValue(rd.getVehcode());
						if (1L == type) {
							cell = row.createCell(2);// Date
							cell.setCellStyle(style);
							cell.setCellValue(rd.getRiqi());
							cell = row.createCell(3);// TimeStar
							cell.setCellStyle(style);
							cell.setCellValue(null == rd.getTime_start() ? "00:00:00" : rd.getTime_start());
							cell = row.createCell(4);// TimeStop
							cell.setCellStyle(style);
							cell.setCellValue(null == rd.getTime_stop() ? "00:00:00" : rd.getTime_stop());
						} else {
							cell = row.createCell(2);// DateBegin
							cell.setCellStyle(style);
							cell.setCellValue(date_star);
							cell = row.createCell(3);// DateEnd
							cell.setCellStyle(style);
							cell.setCellValue(date_end);
						}

						cell = row.createCell(1L == type ? 5 : 4);// PeiodRun
						cell.setCellStyle(style);
						cell.setCellValue(null == rd.getPeriod_run() ? "00:00:00" : DateUtil.formatHmsDate(rd.getPeriod_run()));
						cell = row.createCell(1L == type ? 6 : 5);// PeiodDrive
						cell.setCellStyle(style);
						cell.setCellValue(null == rd.getPeriod_drive() ? "00:00:00" : DateUtil.formatHmsDate(rd.getPeriod_drive()));
						cell = row.createCell(1L == type ? 7 : 6);// Peiodide
						cell.setCellStyle(style);
						cell.setCellValue(null == rd.getPeriod_idle() ? "00:00:00" : rd.getPeriod_idle());
						cell = row.createCell(1L == type ? 8 : 7);// PeiodAir
						cell.setCellStyle(style);
						cell.setCellValue(null == rd.getPeriod_air() ? "00:00:00" : rd.getPeriod_air());
						cell = row.createCell(1L == type ? 9 : 8);// PeiodHot
						cell.setCellStyle(style);
						cell.setCellValue(null == rd.getPeriod_hot() ? "00:00:00" : rd.getPeriod_hot());
						cell = row.createCell(1L == type ? 10 : 9);// Kmrun
						cell.setCellStyle(style);
						cell.setCellValue(null == rd.getKm_run() ? "" : rd.getKm_run().toString());
						cell = row.createCell(1L == type ? 11 : 10);// KmBegin
						cell.setCellStyle(style);
						cell.setCellValue(null == rd.getKm_begin() ? "" : rd.getKm_begin().toString());
						cell = row.createCell(1L == type ? 12 : 11);// KmEnd
						cell.setCellStyle(style);
						cell.setCellValue(null == rd.getKm_end() ? "" : rd.getKm_end().toString());
						cell = row.createCell(1L == type ? 13 : 12);// KmEnd
						cell.setCellStyle(style);
						cell.setCellValue(null == rd.getFuel() ? "" : rd.getFuel().toString());
					}
				}
				List<Object> count = (List<Object>) Utils.getSession().getAttribute("_count");
				if (null != count && 0 != count.size()) {
					int count_row = data.size() + 2;
					HSSFRow row = sheet.createRow(count_row);
					HSSFCell cell = row.createCell(0);
					cell = row.createCell(0);
					cell.setCellStyle(red);
					cell.setCellValue("total:");
					cell = row.createCell(1);
					cell.setCellStyle(red);
					cell = row.createCell(2);
					cell.setCellStyle(red);
					cell = row.createCell(3);
					cell.setCellStyle(red);
					if (1L == type) {
						cell = row.createCell(4);
						cell.setCellStyle(red);
					}
					sheet.addMergedRegion(new CellRangeAddress(count_row, count_row, 0, (1L == type ? 4 : 3)));
					for (int j = 0; j < count.size(); j++) {
						cell = row.createCell((1L == type ? 5 : 4) + j);
						cell.setCellStyle(red);
						if (j == 5) {
							cell.setCellValue(Utils.doubleFmt(count.get(j)));
						} else {
							cell.setCellValue(DateUtil.formatHmsDate(count.get(j)));
						}
					}
					cell = row.createCell(1L == type ? 11 : 10);
					cell.setCellStyle(red);
					cell = row.createCell(1L == type ? 12 : 11);
					cell.setCellStyle(red);
					cell = row.createCell(1L == type ? 13 : 12);
					cell.setCellStyle(red);
					cell.setCellValue("");
				}
				return wb;
			}
		};
		Object[] total = new Object[1];
		total[0] = rdvo.getDaytype();
		if (1L == rdvo.getDaytype()) {
			return ee.export(total, data, "", "No.", "Plate Number", "Date", "Start Time", "End Time", "Period Run", "Period Drive", "Period Idle", "Period Air", "Period Hot", "Run(Km)", "Begin(Km)",
					"End(Km)", "Fuel(L)");
		} else {
			return ee.export(total, data, "", "No.", "Plate Number", "Start Date", "End Date", "Period Run", "Period Drive", "Period Idle", "Period Air", "Period Hot", "Run(Km)", "Begin(Km)", "End(Km)",
					"Fuel(L)");
		}
	}

	public List<Object> get_count() {
		return _count;
	}

	public KeyAndValue get_date() {
		return _date;
	}

	public void set_date(KeyAndValue _date) {
		this._date = _date;
	}
}