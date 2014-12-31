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
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.model.vehicle.VehcileView;
import com.zdtx.ifms.specific.vo.vehicle.VehicleVO;

@Service
@Transactional
public class VehicleListManager {
	@Autowired
	private BaseDao baseDao;

	public Page<VehcileView> getBatch(Page<VehcileView> page, VehicleVO vo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(VehcileView.class);
		criteria.add(Restrictions.in("fleetid", (Long[])Struts2Util.getSession().getAttribute("userFleet")));	//add authority
		criteria.add(Restrictions.eq("isdelete", "F"));
		if (null != vo.getDeptID() && -1L != vo.getDeptID()) {
			criteria.add(Restrictions.eq("deptID", vo.getDeptID()));
		}
		if (null != vo.getVehiclename() && !"".equals(vo.getVehiclename())) {
			criteria.add(Restrictions.like("vehiclename", "%" + vo.getVehiclename() + "%").ignoreCase());
		}
		if (null != vo.getLicenseplate() && !"".equals(vo.getLicenseplate())) {
			criteria.add(Restrictions.like("licenseplate", "%" + vo.getLicenseplate() + "%").ignoreCase());
		}
		if (null != vo.getCctvip() && !"".equals(vo.getCctvip())) {
			criteria.add(Restrictions.like("cctvip", "%" + vo.getCctvip() + "%").ignoreCase());
		}
		if (null != vo.getDescription() && !"".equals(vo.getDescription())) {
			criteria.add(Restrictions.like("description", "%" + vo.getDescription() + "%").ignoreCase());
		}
		if (null != vo.getTypeid() && -1L != vo.getTypeid()) {
			criteria.add(Restrictions.eq("typeid", vo.getTypeid()));
		}
		if (null != vo.getFleetid() && -1L != vo.getFleetid()) {
			criteria.add(Restrictions.eq("fleetid", vo.getFleetid()));
		}
		if (null != vo.getDeviceid() && -1L != vo.getDeviceid()) {
			criteria.add(Restrictions.eq("deviceid", vo.getDeviceid()));
		}
		if (null != vo.getBrand() && !"".equals(vo.getBrand())) {
			criteria.add(Restrictions.like("brandname", "%" + vo.getBrand() + "%").ignoreCase());
		}
		if (null != vo.getKeyCode() && !"".equals(vo.getKeyCode())) {
			criteria.add(Restrictions.like("keycode", "%" + vo.getKeyCode() + "%").ignoreCase());
		}
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(Order.asc("fleetname"));//
		orderList.add(Order.asc("typename"));//
		orderList.add(Order.asc("vehiclename"));//
		Page<VehcileView> pageResult=baseDao.getBatch(page,
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
	 * CheckRpeat
	 * 
	 * @param vehiclename
	 * @return
	 */
	public String checkRepeat(String vehiclename) {
		List<?> list = baseDao.execute("FROM Vehcile V WHERE V.isdelete = 'F' AND V.vehiclename='" + vehiclename + "'");
		return String.valueOf(list.size());
	}
	
	/**
	 * keycode
	 * 
	 * @param vehiclename
	 * @return
	 */
	public String checkKey(String vehiclename,Long id) {
//		String sql="FROM Vehcile V WHERE V.isdelete = 'F' AND V.keycode='" + vehiclename + "'";
//		if(id!=null){
//			sql +="  AND v.vehicleid<>"+id;
//		}
//		List<?> list = baseDao.execute(sql);
		String sql="select vehicleid as key,vehicleid as value from T_CORE_VEHICLE where isdelete='F' and keycode='"+vehiclename+"'";
		if(id!=null && !id.equals("")){
			sql +=" and vehicleid<>"+id;
		}
		List<KeyAndValue> list=baseDao.getKeyAndValueBySQL(sql);
		return String.valueOf(list.size());
	}

	/**
	 * CheckRpeat
	 * 
	 * @param vehiclename
	 * @return
	 */
	public String checkLicenseplateRepeat(String licenseplate) {
		List<?> list = baseDao.execute("FROM Vehcile V WHERE V.isdelete = 'F' AND V.licenseplate='" + licenseplate + "'");
		return String.valueOf(list.size());
	}

	/**
	 * 返回List<KeyAndValue>格式的车辆数据
	 * 
	 * @return
	 */
	public List<KeyAndValue> getVehicleArratyList() {
		String sql = "SELECT T.VEHICLEID AS KEY,T.VEHICLENAME AS VALUE FROM T_CORE_VEHICLE T WHERE T.ISDELETE='F' ORDER BY T.VEHICLENAME";
		return baseDao.getKeyAndValueBySQL(sql);
	}

	public List<KeyAndValue> getDatas(String text, String value) {
		StringBuffer sql = new StringBuffer();
		if (null == value || "".equals(value)) {
			sql.append("SELECT * FROM (SELECT T.VEHICLEID KEY , T.VEHICLENAME VALUE FROM T_CORE_VEHICLE T WHERE  T.VEHICLENAME LIKE '%" + text + "%' OR T.VEHICLENAME LIKE '%" + text + "%') WHERE ROWNUM <11");
		} else {
			sql.append("SELECT T.VEHICLEID KEY , T.VEHICLENAME VALUE FROM T_CORE_VEHICLE T WHERE  T.VEHICLEID =" + value);
		}
		return baseDao.getKeyAndValueBySQL(sql.toString());
	}

	public Object[] getftbd(Long id,Long vehid) {
		Object[] a=new Object[4];
		Org o=baseDao.get(Org.class, id);
		String fleet="select t.org_id as key,t.orgname as value from T_CORE_ORG t where t.isdelete='F' and t.inlevel=3 and t.parentid ="+id;
		String type="select t.typeid as key ,t.type as value from T_CORE_VEH_TYPE t  where t.isdelete='F' and t.comid="+o.getParentID();
		String brand="select t.id as key,t.name as value from T_CORE_VEH_brand t  where t.isdelete='F' and t.comid="+o.getParentID();
		String device="select t.o_deviceno as key,t.o_devicename as value from t_core_unite t where t.deptid="+id +" and t.o_unittype in ('1', '2')  and " +(vehid==null?(" t.o_busid is null"):(" ( t.o_busid is null or t.o_busid="+vehid+" )"));
		a[0]=baseDao.getKeyAndValueBySQL(fleet);
		a[1]=baseDao.getKeyAndValueBySQL(type);
		a[2]=baseDao.getKeyAndValueBySQL(brand);
		a[3]=baseDao.getKeyAndValueBySQL(device);
		return a;
	}

	@SuppressWarnings("unchecked")
	public InputStream getExcel(String title) {
		List<VehcileView> data = new ArrayList<VehcileView>();
		Page<VehcileView> page_export = (Page<VehcileView>) Utils.getSession().getAttribute("page_export");
		DetachedCriteria criteria_export = (DetachedCriteria) Utils.getSession().getAttribute("criteria_export");
		List<Order> orderList_export = (List<Order>) Utils.getSession().getAttribute("orderList_export");
		page_export.setPageSize(page_export.getTotalCount());
		Page<VehcileView> pageResult = baseDao.getBatch(page_export,
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
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
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
				HSSFCell cellrow10 = row2.createCell(9);
				cellrow10.setCellStyle(style);
				
				cellrow01.setCellValue("No.");
				cellrow02.setCellValue("Plate Number");
				cellrow03.setCellValue("Fleet");
				cellrow04.setCellValue("Department");
				
				cellrow05.setCellValue("Device");
				cellrow06.setCellValue("Vehicle Type");
				cellrow07.setCellValue("Vehicle Brand");
				cellrow08.setCellValue("Key Code");
				cellrow09.setCellValue("IP");
				cellrow10.setCellValue("Description");
				
				
				if (null != data && 0 != data.size()) {
					
					for (int i = 0; i < data.size(); i++) {
						VehcileView a=(VehcileView)data.get(i);
						
						HSSFRow row = sheet.createRow(i + 2);
						HSSFCell cell001 = row.createCell(0);
						cell001.setCellStyle(style);
						cell001.setCellValue(i + 1);
						HSSFCell cell002 = row.createCell(1);
						cell002.setCellStyle(style);
						cell002.setCellValue(a.getVehiclename());
						HSSFCell cell003 = row.createCell(2);
						cell003.setCellStyle(style);
						cell003.setCellValue(a.getFleetname());
						HSSFCell cell004 = row.createCell(3);
						cell004.setCellStyle(style);
						cell004.setCellValue(a.getDeptname());
						
						HSSFCell cell005 = row.createCell(4);
						cell005.setCellStyle(style);
						cell005.setCellValue(a.getDevicename());
						
						HSSFCell cell006 = row.createCell(5);
						cell006.setCellStyle(style);
						cell006.setCellValue(a.getTypename());
						
						HSSFCell cell007 = row.createCell(6);
						cell007.setCellStyle(style);
						cell007.setCellValue(a.getBrandname());
						
						HSSFCell cell008 = row.createCell(7);
						cell008.setCellStyle(style);
						cell008.setCellValue(a.getKeycode());
						
						HSSFCell cell009 = row.createCell(8);
						cell009.setCellStyle(style);
						cell009.setCellValue(a.getCctvip());
						
						HSSFCell cell010 = row.createCell(9);
						cell010.setCellStyle(style);
						cell010.setCellValue(a.getDescription());
						
						
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

	public List<KeyAndValue> getVehbyFleet(Long id) {
		String sql="select t.vehicleid as key ,t.vehiclename as value from t_core_vehicle t where t.isdelete='F' and t.fleetid="+id;
		
		
		return baseDao.getKeyAndValueBySQL(sql);
	}
}
