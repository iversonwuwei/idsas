package com.zdtx.ifms.specific.service.vehicle;

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
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.ExportExcel;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.vehicle.DriverView;
import com.zdtx.ifms.specific.vo.vehicle.DriverVO;

@Service
@Transactional
public class DriverListManager {
	@Autowired
	private BaseDao baseDao;

	public Page<DriverView> getBatch(Page<DriverView> page, DriverVO vo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DriverView.class);
		criteria.add(Restrictions.eq("isdelete", "F"));
		criteria.add(Restrictions.in("departmentid", (Long[])Struts2Util.getSession().getAttribute("userDepartment")));	//add authority
		if (null != vo.getDescription() && !"".equals(vo.getDescription())) {
			criteria.add(Restrictions.like("description", "%" + vo.getDescription() + "%").ignoreCase());
		}
		if (null != vo.getDrivernumber() && !"".equals(vo.getDrivernumber())) {
			criteria.add(Restrictions.like("drivernumber", "%" + vo.getDrivernumber() + "%").ignoreCase());
		}
		if (null != vo.getDepartmentid() && -1L != vo.getDepartmentid()) {
			criteria.add(Restrictions.eq("departmentid", vo.getDepartmentid()));
		}
		if (null != vo.getDriverid() && -1L != vo.getDriverid()) {// driverName
			criteria.add(Restrictions.eq("driverid", vo.getDriverid()));
		}
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(Order.asc("departmentid"));//
		orderList.add(Order.asc("drivernumber"));//
		orderList.add(Order.asc("drivername"));//
		Page<DriverView> pageResult=baseDao.getBatch(page,
				criteria.getExecutableCriteria(baseDao.getSession()),
				orderList);

		if (1 == pageResult.getCurrentPage()) {
			Utils.getSession().setAttribute("criteria_export", criteria);
			Utils.getSession().setAttribute("page_export", page);
			Utils.getSession().setAttribute("orderList_export", orderList);
		}
		return pageResult;
	}

	/**
	 * CheckDrivernameRpeat
	 * 
	 * @param vehiclename
	 * @return
	 */
	public String checkRepeat(String drivername) {
		List<?> list = baseDao.execute("FROM Driver D WHERE D.isdelete = 'F' AND D.drivername='" + drivername + "'");
		return String.valueOf(list.size());
	}
	/**
	 * CheckDriverNumberRpeat
	 * 
	 * @param vehiclename
	 * @return
	 */
	public String checkNumRepeat(String drivernumber) {
		List<?> list = baseDao.execute("FROM Driver D WHERE D.isdelete = 'F' AND D.drivernumber='" + drivernumber + "'");
		return String.valueOf(list.size());
	}

	/**
	 * 返回List<KeyAndValue>格式的Driver数据
	 * 
	 * @return
	 */
	public List<KeyAndValue> getVehicleArratyList() {
		String sql = "SELECT T.DRIVERID AS KEY,T.DRIVERNUMBER AS VALUE FROM T_CORE_DRIVER T WHERE T.ISDELETE='F' ORDER BY T.DRIVERNUMBER";
		return baseDao.getKeyAndValueBySQL(sql);
	}
	/**
	 * 返回List<KeyAndValue>格式的Driver  id+name
	 * 
	 * @return
	 */
	public List<KeyAndValue> getVehicleArrayList() {
		String sql = "SELECT T.DRIVERID AS KEY,T.DRIVERNAME AS VALUE FROM T_CORE_DRIVER T WHERE T.ISDELETE='F' ORDER BY T.DRIVERNAME";
		return baseDao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * 返回List<KeyAndValue>格式的Driver  id+name
	 * 
	 * @return
	 */
	public List<KeyAndValue> getVehicleArrayList(String name) {
		String sql = "SELECT T.DRIVERID AS KEY,T.DRIVERNAME AS VALUE FROM T_CORE_DRIVER T WHERE T.ISDELETE='F' ORDER BY T.DRIVERNAME";
		return baseDao.getKeyAndValueBySQL(sql);
	}
	
	public List<KeyAndValue> getDriverList(String name) {
		if (null == name || "".equals(name)) { // 验证入参是否为空
			String sql = "SELECT DRIVERID AS KEY,DRIVERNAME AS VALUE FROM T_CORE_DRIVER "
					+ " WHERE ISDELETE='F' and  DRIVERID not in (select max(driverid) from t_ifms_booking_detail where isdelete='F' group by  driverid)  and rownum<10 ";
			return baseDao.getKeyAndValueBySQL(sql);
		} else {
			String sql = "SELECT DRIVERID AS KEY,DRIVERNAME AS VALUE FROM T_CORE_DRIVER "
					+ " WHERE  ISDELETE='F' and DRIVERID not in (select max(driverid) from t_ifms_booking_detail where isdelete='F' group by  driverid) and  DRIVERNAME like '" + name+"%'  and  rownum<10  ";
			return baseDao.getKeyAndValueBySQL(sql);
		}
	}

	@SuppressWarnings("unchecked")
	public InputStream getExcel(String title) {
		List<DriverView> data = new ArrayList<DriverView>();

		Page<DriverView> page_export = (Page<DriverView>) Utils.getSession().getAttribute("page_export");
		DetachedCriteria criteria_export = (DetachedCriteria) Utils.getSession().getAttribute("criteria_export");
		List<Order> orderList_export = (List<Order>) Utils.getSession().getAttribute("orderList_export");
		page_export.setPageSize(page_export.getTotalCount());
		Page<DriverView> pageResult = baseDao.getBatch(page_export,
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
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
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
				HSSFCell cellrow07 = row2.createCell(6);
				cellrow07.setCellStyle(style);
				HSSFCell cellrow08 = row2.createCell(7);
				cellrow08.setCellStyle(style);
				HSSFCell cellrow09 = row2.createCell(8);
				cellrow09.setCellStyle(style);
				
				cellrow01.setCellValue("No.");
				cellrow02.setCellValue("Driver Name");
				cellrow03.setCellValue("Department");
				cellrow04.setCellValue("Card Number");
				cellrow05.setCellValue("Card Type");
				cellrow06.setCellValue("Gender");
				cellrow07.setCellValue("Email");
				cellrow08.setCellValue("Phone");
				cellrow09.setCellValue("Description");
				
				
				if (null != data && 0 != data.size()) {
					
					for (int i = 0; i < data.size(); i++) {
						DriverView a=(DriverView)data.get(i);
						
						HSSFRow row = sheet.createRow(i + 2);
						HSSFCell cell001 = row.createCell(0);
						cell001.setCellStyle(style);
						cell001.setCellValue(i + 1);
						HSSFCell cell002 = row.createCell(1);
						cell002.setCellStyle(style);
						cell002.setCellValue(a.getDrivername());
						HSSFCell cell003 = row.createCell(2);
						cell003.setCellStyle(style);
						cell003.setCellValue(a.getDepartmentname());
						HSSFCell cell004 = row.createCell(3);
						cell004.setCellStyle(style);
						cell004.setCellValue(a.getDrivernumber());
						HSSFCell cell005 = row.createCell(4);
						cell005.setCellStyle(style);
						if(a.getType()==null){
							cell005.setCellValue("Normal");
						}else if(a.getType().equals("0")){
							cell005.setCellValue("Normal");
						}else if(a.getType().equals("1")){
							cell005.setCellValue("Temporary");
						}
						HSSFCell cell006 = row.createCell(5);
						cell006.setCellStyle(style);
						cell006.setCellValue(a.getGender());
						HSSFCell cell007 = row.createCell(6);
						cell007.setCellStyle(style);
						cell007.setCellValue(a.getEmail());
						HSSFCell cell008 = row.createCell(7);
						cell008.setCellStyle(style);
						cell008.setCellValue(a.getPhone());
						HSSFCell cell009 = row.createCell(8);
						cell009.setCellStyle(style);
						cell009.setCellValue(a.getDescription());
						
						
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