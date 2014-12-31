package com.zdtx.ifms.specific.service.task;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
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
import com.zdtx.ifms.specific.model.task.Mileageoil;
import com.zdtx.ifms.specific.vo.task.FuelMileageVo;

/**
 * @ClassName: FuelMileageManager
 * @Description: Task Management-Fuel Mileage-Manager
 * @author JiangHaiquan
 * @date 2013-05-03 10:15:18
 * @version V1.0
 */

@Service
@Transactional
public class FuelMileageManager {

	@Autowired
	private BaseDao baseDao;
	
	public Page<Mileageoil> getBetch(Page<Mileageoil> page, FuelMileageVo fmvo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Mileageoil.class);
		if(fmvo.getVehicleid()!=null  && fmvo.getVehicleid()!=-1){
			criteria.add(Restrictions.eq("vehicleid", fmvo.getVehicleid()));
		}
		if(fmvo.getTypeid()!=null && fmvo.getTypeid()!=-1l){
			criteria.add(Restrictions.eq("typeid", fmvo.getTypeid()));
		}
		
		if(fmvo.getStartdate()!=null  && !"".equals(fmvo.getStartdate())){
			criteria.add(Restrictions.ge("riqi", fmvo.getStartdate()));
		}
		if(fmvo.getEnddate()!=null  && !"".equals(fmvo.getEnddate())){
			criteria.add(Restrictions.le("riqi", fmvo.getEnddate()));
		}
		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(Order.asc("riqi"));
		Page<Mileageoil> pageResult = baseDao.getBatch(page, criteria.getExecutableCriteria(baseDao.getSession()), orderList);
		if(1 == pageResult.getCurrentPage()){
			Struts2Util.getSession().setAttribute("criteria_export", criteria);
			Struts2Util.getSession().setAttribute("page_export", page);
			Struts2Util.getSession().setAttribute("order_export", orderList);
		}
		return pageResult;
	}
	
	public List<KeyAndValue> getVehicleNameByVehid(Long vehid) {
		String sql = "SELECT DISTINCT A.VEHICLEID AS KEY, A.LICENSEPLATE AS VALUE " +
				"FROM T_CORE_VEHICLE A " +
				"WHERE A.ISDELETE = 'F' AND A.VEHICLEID ="+vehid;
		return baseDao.getKeyAndValueBySQL(sql);
	}
	
	@SuppressWarnings("unchecked")
	public InputStream getData(Page<Mileageoil> page,List<Mileageoil> list,FuelMileageVo fmvo) {

		//Page<Mileageoil> pageResult = this.getBetch(page, fmvo);
		
		
		
		Page<Mileageoil> page_export = (Page<Mileageoil>)Struts2Util.getSession().getAttribute("page_export");
		DetachedCriteria criteria_export = (DetachedCriteria)Struts2Util.getSession().getAttribute("criteria_export");
		List<Order> orderList_export = (List<Order>)Struts2Util.getSession().getAttribute("orderList_export");
		page_export.setPageSize(page_export.getTotalCount());
		Page<Mileageoil> pageResult = baseDao.getBatch(page_export, criteria_export.getExecutableCriteria(baseDao.getSession()), orderList_export);
		
		
		
		
		List<Mileageoil> data = new ArrayList<Mileageoil>();

		if (null != pageResult) {
			if (0 != pageResult.getResult().size()) {
				data = pageResult.getResult();
			}
		}
		ExportExcel ee = new ExportExcel() {

			@Override
			protected HSSFWorkbook disposeData(HSSFWorkbook wb, Object[] total,List<?> data) throws IOException {
				HSSFSheet sheet = wb.getSheetAt(0);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
				HSSFRow rowss = sheet.createRow(0);
				rowss.setHeightInPoints(20);
				HSSFCell hssfCell = rowss.createCell(0);
				hssfCell = this.createCell(wb, hssfCell, "Fuel Mileage");
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
				cellrow02.setCellValue("Plate Number");
				cellrow03.setCellValue("Vehicle Type");
				cellrow04.setCellValue("Date");
				cellrow05.setCellValue("Mileage");
				cellrow06.setCellValue("Fuel consumption");
				if (null != data && 0 != data.size()) {
					for (int i = 0; i < data.size(); i++) {
						HSSFRow row = sheet.createRow(i + 2);
						Mileageoil o = (Mileageoil) data.get(i);
						HSSFCell cell001 = row.createCell(0);
						cell001.setCellStyle(style);
						cell001.setCellValue(i + 1);
						HSSFCell cell002 = row.createCell(1);
						cell002.setCellStyle(style);
						cell002.setCellValue(o.getVehiclename());
						HSSFCell cell003 = row.createCell(2);
						cell003.setCellStyle(style);
						cell003.setCellValue(o.getTypename());
						HSSFCell cell004 = row.createCell(3);
						cell004.setCellStyle(style);
						cell004.setCellValue(o.getRiqi());
						HSSFCell cell005 = row.createCell(4);
						cell005.setCellStyle(style);
						cell005.setCellValue(o.getMileage()+"m");
						HSSFCell cell006 = row.createCell(5);
						cell006.setCellStyle(style);
						cell006.setCellValue(o.getOilcost()==null?"0":o.getOilcost()+"L");
						
					}
				}
				return wb;
			}

		};

		Object[] total = new Object[1];
		total[0] = "";
		String str = "Fuel Mileage";
		return ee.export(total, data, str);
	}
	
	public String doubleNo(Object inner) {
		if (null == inner) {
			return "0";
		}
		DecimalFormat df = new DecimalFormat("#################.##");
		return  df.format(inner).toString();
	}
}
