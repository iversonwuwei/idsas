package com.zdtx.ifms.specific.service.task;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.ExportExcel;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.task.Schedule;
import com.zdtx.ifms.specific.model.vehicle.Driver;
import com.zdtx.ifms.specific.model.vehicle.Vehcile;
import com.zdtx.ifms.specific.vo.task.ScheduleVO;

@Service
@Transactional
public class ScheduleManager {

	@Autowired
	private BaseDao baseDao;
	
	public Page<Schedule> getPage(Page<Schedule> page, ScheduleVO schVo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Schedule.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		List<Order> orderList = new ArrayList<Order>();
		if(schVo.getReturntype()!=null){
			if(schVo.getReturntype()==1l){
				criteria.add(Restrictions.isNotNull("endtime"));
			}else if(schVo.getReturntype()==2l){
				criteria.add(Restrictions.isNull("endtime"));
			}
		}
		
		if (!Utils.isEmpty(schVo.getVehicleid()) && -1l != schVo.getVehicleid()) { 
			criteria.add(Restrictions.eq("vehicleid", schVo.getVehicleid()));
		}
		if (!Utils.isEmpty(schVo.getDriverid())) { 
			criteria.add(Restrictions.ilike("drivername", "%" + schVo.getDriverid().trim() + "%"));
		}
		if (!Utils.isEmpty(schVo.getRouteid()) && -1 != schVo.getRouteid()) { 
			criteria.add(Restrictions.eq("routeid", schVo.getRouteid()));
		}
		if (!Utils.isEmpty(schVo.getDuty())) {
			criteria.add(Restrictions.ilike("duty", "%" + schVo.getDuty() + "%"));
		}
		if (!Utils.isEmpty(schVo.getTimeMin())) {
			criteria.add(Restrictions.ge("startime", schVo.getTimeMin()));
		}
		if (!Utils.isEmpty(schVo.getTimeMax())) {
			criteria.add(Restrictions.le("endtime", schVo.getTimeMax()));
		}
		orderList.add(Order.desc("startime"));
		orderList.add(Order.desc("endtime"));
		
		Page<Schedule> pageResult=baseDao
				.getBatch(page,
						criteria.getExecutableCriteria(baseDao.getSession()),
						orderList);
		
		if (1 == pageResult.getCurrentPage()) {
			Utils.getSession().setAttribute("criteria_export", criteria);
			Utils.getSession().setAttribute("page_export", page);
			Utils.getSession().setAttribute("orderList_export", orderList);
		}
		return pageResult;
	}
	
	public Page<Schedule> getPage2(Page<Schedule> page, ScheduleVO schVo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Schedule.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		List<Order> orderList = new ArrayList<Order>();
		criteria.add(Restrictions.isNotNull("endtime"));
		criteria.add(Restrictions.neProperty("drivername", "redrivername"));
		if (!Utils.isEmpty(schVo.getVehicleid()) && -1l != schVo.getVehicleid()) { 
			criteria.add(Restrictions.eq("vehicleid", schVo.getVehicleid()));
		}
		if (!Utils.isEmpty(schVo.getDriverid())) { 
			criteria.add(Restrictions.ilike("drivername", "%" + schVo.getDriverid().trim() + "%"));
		}
		if (!Utils.isEmpty(schVo.getRedriverid())) { 
			criteria.add(Restrictions.ilike("redrivername", "%" + schVo.getRedriverid().trim() + "%"));
		}
		if (!Utils.isEmpty(schVo.getRouteid()) && -1 != schVo.getRouteid()) { 
			criteria.add(Restrictions.eq("routeid", schVo.getRouteid()));
		}
		if (!Utils.isEmpty(schVo.getDuty())) {
			criteria.add(Restrictions.ilike("duty", "%" + schVo.getDuty() + "%"));
		}
		if (!Utils.isEmpty(schVo.getTimeMin())) {
			criteria.add(Restrictions.ge("startime", schVo.getTimeMin()));
		}
		if (!Utils.isEmpty(schVo.getTimeMax())) {
			criteria.add(Restrictions.le("endtime", schVo.getTimeMax()));
		}
		orderList.add(Order.desc("startime"));
		orderList.add(Order.desc("endtime"));
		
		Page<Schedule> pageResult=baseDao
				.getBatch(page,
						criteria.getExecutableCriteria(baseDao.getSession()),
						orderList);
		
//		if (1 == pageResult.getCurrentPage()) {
//			Utils.getSession().setAttribute("criteria_export", criteria);
//			Utils.getSession().setAttribute("page_export", page);
//			Utils.getSession().setAttribute("orderList_export", orderList);
//		}
		return pageResult;
	}

	public List<KeyAndValue> getRouteList() {
		String sql = "SELECT T.GEOFENCINGID AS KEY,T.CAPTION AS VALUE FROM T_IFMS_GEOFENCING T WHERE T.ISDELETE = 'F' ORDER BY UPPER(CAPTION) ASC";
		return baseDao.getKeyAndValueBySQL(sql);
	}

	public List<KeyAndValue> getScheduleList(Long id) {
		String sql = "";
		if (-1 == id) {
			sql = "select t.scheduleid as key,t.startime||','||t.endtime as value from t_ifms_schedule t where t.isdelete = 'F'";
		} else {
			sql = "select t.scheduleid as key,t.startime||','||t.endtime as value from t_ifms_schedule t where t.isdelete = 'F' and t.vehicleid = " + id;
		}
		return baseDao.getKeyAndValueBySQL(sql);	
	}

	public String getCheckOutTime(Long scheduleid, Long vehicleid, String startime, String endtime) {
		String whereSql = "";
		if (null != scheduleid && -1 != scheduleid) {
			whereSql = " and scheduleid <> " + scheduleid;
		}
		String hql = "From Schedule s where s.isdelete = 'F' and s.vehicleid = " + vehicleid + " and (('" + startime + "' > s.planstartime and '" + startime + "' < s.planendtime) or ('" + endtime + "' > s.planstartime and '" + endtime + "' < s.planendtime)) " + whereSql;
		try {
			if (0 != baseDao.execute(hql).size()) {
				return "repeat";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "norepeat";
	}

	public String getCheckDriverTime(Long scheduleid, Long driverid,
			String startime, String endtime) {
		String whereSql = "";
		if (null != scheduleid && -1 != scheduleid) {
			whereSql = " and scheduleid <> " + scheduleid;
		}
		String hql = "From Schedule s where s.isdelete = 'F' and s.driverid = " + driverid + 
				" and (('" + startime + "' > s.planstartime and '" + startime + "' < s.planendtime) " +
						"or ('" + endtime + "' > s.planstartime and '" + endtime + "' < s.planendtime)) " + whereSql;
		if (0 != baseDao.execute(hql).size()) {
			return "repeat";
		}
		return "norepeat";
	}

	public Vehcile getVehByCode(String code1) {
		Criteria criteria=baseDao.getSession().createCriteria(Vehcile.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		criteria.add(Restrictions.eq("keycode", code1));
		List<Vehcile> vehs=baseDao.getAll(criteria);
		if(vehs!=null && vehs.size()!=0){
			return vehs.get(0);
		}else{
			
			return null;
		}
	}

	public Driver getDriverByCode(String code1) {
		Criteria criteria=baseDao.getSession().createCriteria(Driver.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		criteria.add(Restrictions.eq("drivernumber", code1));
		List<Driver> vehs=baseDao.getAll(criteria);
		if(vehs!=null && vehs.size()!=0){
			return vehs.get(0);
		}else{
			return null;
		}
	}

	public Schedule getHuiche(Vehcile tv) {
		Criteria criteria=baseDao.getSession().createCriteria(Schedule.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
//		criteria.add(Restrictions.eq("driverid", td.getDriverid()));
		criteria.add(Restrictions.eq("vehicleid", tv.getVehicleid()));
		criteria.add(Restrictions.isNotNull("startime"));
		criteria.add(Restrictions.isNull("endtime"));
		
		List<Schedule> vehs=baseDao.getAll(criteria);
		if(vehs!=null && vehs.size()!=0){
			return vehs.get(0);
		}else{
			return null;
		}
	}

	
	public Schedule getZhengchang(Vehcile tv, Driver td, Date ti) {
		Criteria criteria=baseDao.getSession().createCriteria(Schedule.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		criteria.add(Restrictions.eq("plandriverid", td.getDriverid()));
		criteria.add(Restrictions.eq("planvehicleid", tv.getVehicleid()));
		
		criteria.add(Restrictions.isNotNull("planstartime"));
		criteria.add(Restrictions.isNotNull("planendtime"));
		criteria.add(Restrictions.isNull("startime"));
		criteria.add(Restrictions.isNull("endtime"));
		
		List<Schedule> vehs=baseDao.getAll(criteria);
		if(vehs!=null && vehs.size()!=0){
			if(vehs.size()==1){
				return vehs.get(0);
			}else{
				int j=0;
				long x=0;
				for(int i=0;i<vehs.size();i++){
					Date tdate= DateUtil.parseMinute(vehs.get(i).getPlanstartime());
					if(i==0){
						x=Math.abs(ti.getTime()-tdate.getTime());
						j=i;
					}else{
						if(Math.abs(ti.getTime()-tdate.getTime())<x){
							j=i;
						}
					}
				}
				return vehs.get(j);
			}
		}else{
			return null;
		}
	}

	
	public Schedule getLinshika(Vehcile tv, Date ti) {
		Criteria criteria=baseDao.getSession().createCriteria(Schedule.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		criteria.add(Restrictions.eq("planvehicleid", tv.getVehicleid()));
		criteria.add(Restrictions.isNotNull("planstartime"));
		criteria.add(Restrictions.isNotNull("planendtime"));
		criteria.add(Restrictions.isNull("startime"));
		criteria.add(Restrictions.isNull("endtime"));
		
		
		List<Schedule> vehs=baseDao.getAll(criteria);
		if(vehs!=null && vehs.size()!=0){
			if(vehs.size()==1){
				return vehs.get(0);
			}else{
				int j=0;
				long x=0;
				for(int i=0;i<vehs.size();i++){
					Date tdate= DateUtil.parseMinute(vehs.get(i).getPlanstartime());
					if(i==0){
						x=Math.abs(ti.getTime()-tdate.getTime());
						j=i;
					}else{
						if(Math.abs(ti.getTime()-tdate.getTime())<x){
							j=i;
						}
					}
				}
				return vehs.get(j);
			}
		}else{
			return null;
		}
	}

	public Schedule getLinshiche( Driver td, Date ti) {
		Criteria criteria=baseDao.getSession().createCriteria(Schedule.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		criteria.add(Restrictions.eq("plandriverid", td.getDriverid()));
		criteria.add(Restrictions.isNotNull("planstartime"));
		criteria.add(Restrictions.isNotNull("planendtime"));
		criteria.add(Restrictions.isNull("startime"));
		criteria.add(Restrictions.isNull("endtime"));
		
		
		List<Schedule> vehs=baseDao.getAll(criteria);
		if(vehs!=null && vehs.size()!=0){
			if(vehs.size()==1){
				return vehs.get(0);
			}else{
				int j=0;
				long x=0;
				for(int i=0;i<vehs.size();i++){
					Date tdate= DateUtil.parseMinute(vehs.get(i).getPlanstartime());
					if(i==0){
						x=Math.abs(ti.getTime()-tdate.getTime());
						j=i;
					}else{
						if(Math.abs(ti.getTime()-tdate.getTime())<x){
							j=i;
						}
					}
				}
				return vehs.get(j);
			}
		}else{
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public InputStream getExcel( String title) {
	List<Schedule> data = new ArrayList<Schedule>();

	Page<Schedule> page_export = (Page<Schedule>) Utils.getSession()
			.getAttribute("page_export");
	DetachedCriteria criteria_export = (DetachedCriteria) Utils
			.getSession().getAttribute("criteria_export");
	List<Order> orderList_export = (List<Order>) Utils.getSession()
			.getAttribute("orderList_export");
	criteria_export.add(Restrictions.isNotNull("driverid"));
	criteria_export.add(Restrictions.isNotNull("vehicleid"));
	criteria_export.add(Restrictions.isNotNull("startime"));
	page_export.setPageSize(page_export.getTotalCount());
	Page<Schedule> pageResult = baseDao.getBatch(page_export,
			criteria_export.getExecutableCriteria(baseDao.getSession()),
			orderList_export);

	if (null != pageResult) {
		if (0 != pageResult.getResult().size()) {
			data = pageResult.getResult();

		}
	}
	
	ExportExcel ee = new ExportExcel() {
		
		@Override
		protected HSSFWorkbook disposeData(HSSFWorkbook wb, Object[] total,
				List<?> data) throws IOException {
			
			HSSFSheet sheet = wb.getSheetAt(0);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
			HSSFRow rowss = sheet.createRow(0);
			rowss.setHeightInPoints(20);
			HSSFCell hssfCell = rowss.createCell(0);
			hssfCell = this.createCell(wb, hssfCell, total[0].toString());
			HSSFCellStyle style = this.createStyle(wb);
			HSSFRow row2 = sheet.createRow(1);
			HSSFCell cellrow01 = row2.createCell(0);
			cellrow01.setCellStyle(style);
			HSSFCell cellrow02 = row2.createCell(1);
			cellrow02.setCellStyle(style);
			HSSFCell cellrow03 = row2.createCell(2);
			cellrow03.setCellStyle(style);
			HSSFCell cellrow04 = row2.createCell(3);
			cellrow04.setCellStyle(style);
			HSSFCell cellrow05 = row2.createCell(4);
			cellrow05.setCellStyle(style);
			HSSFCell cellrow06 = row2.createCell(5);
			cellrow06.setCellStyle(style);
			
			cellrow01.setCellValue("No.");
			cellrow02.setCellValue("Driver");
			cellrow03.setCellValue("Return Driver");
			cellrow04.setCellValue("Vehicle Name");
			cellrow05.setCellValue("Start Time");
			cellrow06.setCellValue("End Time");
			
			
			
			if (null != data && 0 != data.size()) {
				for (int i = 0; i < data.size(); i++) {
					
					HSSFRow row = sheet.createRow(i + 2);
					
					Schedule o = (Schedule) data.get(i);
					HSSFCell cell001 = row.createCell(0);
					cell001.setCellStyle(style);
					cell001.setCellValue(i + 1);
					HSSFCell cell002 = row.createCell(1);
					cell002.setCellStyle(style);
					cell002.setCellValue(o.getDrivername());
					HSSFCell cell003 = row.createCell(2);
					cell003.setCellStyle(style);
					cell003.setCellValue(o.getRedrivername());
					HSSFCell cell004 = row.createCell(3);
					cell004.setCellStyle(style);
					cell004.setCellValue(o.getVehiclenumber());
					HSSFCell cell005 = row.createCell(4);
					cell005.setCellStyle(style);
					cell005.setCellValue(o.getStartime());
					HSSFCell cell006 = row.createCell(5);
					cell006.setCellStyle(style);
					cell006.setCellValue(o.getEndtime());
					
					
				}
			}
			
			
			return wb;
		}
	};
	
	
	Object[] total = new Object[1];
	total[0] = title;
	String str = title;
	return ee.export(total, data, str);
	}
	
}
