package com.zdtx.ifms.specific.service.system;

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
import org.hibernate.Criteria;
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
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.monitor.CamDevice;
import com.zdtx.ifms.specific.model.system.DeviceListCamtype;
import com.zdtx.ifms.specific.model.vehicle.Vehcile;
import com.zdtx.ifms.specific.vo.system.DeviceVo;

@Service
@Transactional
public class DeviceListManager {

	@Autowired
	private BaseDao baseDao;

	public Page<DeviceListCamtype> getBatch(Page<DeviceListCamtype> page,
			DeviceVo deVo, Long[] deps) {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(DeviceListCamtype.class);
		criteria.add(Restrictions.in("deptid", deps));
		if (deVo.getDeptID() != null && deVo.getDeptID() != -1L) { // 按部门
			criteria.add(Restrictions.eq("deptid", deVo.getDeptID()));
		}
		if (!Utils.isEmpty(deVo.getName())) { // 按设备名称
			criteria.add(Restrictions.like("o_devicename",
					"%" + deVo.getName().trim() + "%").ignoreCase());
		}
		if (!Utils.isEmpty(deVo.getVname())) { // 按设备名称
			criteria.add(Restrictions.like("o_busname",
					"%" + deVo.getVname().trim() + "%").ignoreCase());
		}
		if (!Utils.isEmpty(deVo.getHost())) { // 按IP
			criteria.add(Restrictions.like("o_loginhost",
					"%" + deVo.getHost().trim() + "%").ignoreCase());
		}
		if (!Utils.isEmpty(deVo.getDeviceType())
				&& !deVo.getDeviceType().equals("-1")) { // 设备类型
			criteria.add(Restrictions.eq("o_unittype", deVo.getDeviceType()));
		}
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(Order.asc("o_devicename"));
		Page<DeviceListCamtype> pageResult = baseDao
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

	/**
	 * @param deviceType
	 *            设备类型 1：车载设备；2：单兵设备
	 * @author LiuGuilong
	 * @since 2013-04-26
	 * @return
	 */
//	public List<KeyAndValue> getDeviceList(int deviceType) {
//		String sql = "SELECT T.O_DEVICENO AS KEY, T.O_DEVICENAME AS VALUE FROM T_CORE_UNITE T WHERE T.O_UNITTYPE = '"
//				+ deviceType + "' ORDER BY T.O_DEVICENAME";
//		return baseDao.getKeyAndValueBySQL(sql);
//	}

	public List<KeyAndValue> getChannelList(Long deptid) {
		if (deptid == null) {
			return new ArrayList<KeyAndValue>();
		}
		String sql = "SELECT T.cameraid AS KEY, t.cameraname AS VALUE FROM T_CORE_CAMERA T WHERE T.deptid="
				+ deptid
				+ " and T.cameraid NOT IN (SELECT C.CAMERAID FROM T_CORE_CAMERA_DEVICE C)  ORDER BY T.cameraid";
		return baseDao.getKeyAndValueBySQL(sql);
	}

	public List<KeyAndValue> getChannelList(Long id, Long deptid) {
		if (deptid == null) {
			return new ArrayList<KeyAndValue>();
		}
		String sql = "SELECT T.cameraid AS KEY, t.cameraname AS VALUE FROM T_CORE_CAMERA T WHERE  T.deptid="
				+ deptid
				+ " and  T.cameraid NOT IN (SELECT C.CAMERAID FROM T_CORE_CAMERA_DEVICE C WHERE C.deviceid <> "
				+ id + ")  ORDER BY T.cameraid";
		return baseDao.getKeyAndValueBySQL(sql);
	}

	public void delChannel(Long id) {
		String hql = "Delete CamDevice t where t.deviceID = " + id;
		baseDao.executeUpdate(hql);
	}

	public CamDevice getChannel(Long id, Integer i) {
		String hql = "From CamDevice where deviceID = " + id
				+ " and channel = " + i;
		List<CamDevice> list = baseDao.execute(hql);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<CamDevice> getChannel(Long id) {
		String hql = "From CamDevice where deviceID = " + id + "  order by channel";
		return baseDao.execute(hql);
	}

	public Vehcile getVeh(Long id) {
		String hql = "From Vehcile where deviceid = " + id
				+ " and isdelete = 'F'";
		List<Vehcile> list = baseDao.execute(hql);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 获得没有安装在车上的设备列表
	 * 
	 * @author LiuGuilong
	 * @since 2013-05-06
	 * @return
	 */
	public List<KeyAndValue> getUnusedDeviceList() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.O_DEVICENO AS KEY, T.O_DEVICENAME AS VALUE FROM T_CORE_UNITE T WHERE T.O_UNITTYPE = '1' AND T.O_DEVICENO NOT IN");
		sql.append("(SELECT V.DEVICEID FROM T_CORE_VEHICLE V WHERE V.ISDELETE = 'F' AND V.DEVICEID IS NOT NULL GROUP BY V.DEVICEID)");
		sql.append("ORDER BY T.O_DEVICENAME");
		return baseDao.getKeyAndValueBySQL(sql.toString());
	}

	public Boolean checkAdd(String name) {
		Boolean check = false;
		try {
			if (0 != baseDao.execute(
					"FROM DeviceList WHERE o_devicename = '" + name + "'")
					.size()) {
				check = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}

	public Boolean checkEdit(String userID, String name) {
		Boolean check = false;
		try {
			if (0 != baseDao.execute(
					"FROM DeviceList WHERE o_devicename = '" + name
							+ "' and o_deviceno <> " + userID).size()) {
				check = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}

	public Boolean checkDel(Long id) {// true 设备在车上
		Boolean check = false;
		try {
			if (0 != baseDao.execute(
					"FROM Vehcile WHERE deviceid = " + id
							+ " and isdelete = 'F'").size()) {
				check = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}

	public List<CamDevice> delCamDevice(Long o_deviceno) {
		Criteria criteria = baseDao.getSession()
				.createCriteria(CamDevice.class);
		criteria.add(Restrictions.eq("deviceID", o_deviceno));
		List<CamDevice> cds = baseDao.getAll(criteria);
		return cds;
	}

	@SuppressWarnings("unchecked")
	public InputStream getExcel(String title) {
		List<DeviceListCamtype> data = new ArrayList<DeviceListCamtype>();
		Page<DeviceListCamtype> page_export = (Page<DeviceListCamtype>) Utils
				.getSession().getAttribute("page_export");
		DetachedCriteria criteria_export = (DetachedCriteria) Utils
				.getSession().getAttribute("criteria_export");
		List<Order> orderList_export = (List<Order>) Utils.getSession()
				.getAttribute("orderList_export");
		page_export.setPageSize(page_export.getTotalCount());
		Page<DeviceListCamtype> pageResult = baseDao.getBatch(page_export,
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
				HSSFCell cellrow07 = row2.createCell(6);
				cellrow07.setCellStyle(style);

				cellrow01.setCellValue("No.");
				cellrow02.setCellValue("Device Name");
				cellrow03.setCellValue("Department");
				cellrow04.setCellValue("Device Type");
				cellrow05.setCellValue("Vehicle/Police");
				cellrow06.setCellValue("IP");
				cellrow07.setCellValue("Channel Count");

				if (null != data && 0 != data.size()) {

					for (int i = 0; i < data.size(); i++) {
						DeviceListCamtype a = (DeviceListCamtype) data.get(i);

						HSSFRow row = sheet.createRow(i + 2);
						HSSFCell cell001 = row.createCell(0); // No.
						cell001.setCellStyle(style);
						cell001.setCellValue(i + 1);

						HSSFCell cell002 = row.createCell(1); // Device Name
						cell002.setCellStyle(style);
						cell002.setCellValue(a.getO_devicename());

						HSSFCell cell003 = row.createCell(2); // Department
						cell003.setCellStyle(style);
						cell003.setCellValue(a.getDeptname());

						HSSFCell cell004 = row.createCell(3); // Device Type
						cell004.setCellStyle(style);
						if (a.getO_unittype() == null) {
							cell004.setCellValue("");
						} else if (a.getO_unittype().equals("1")) {
							cell004.setCellValue("BNT5000");
						} else if (a.getO_unittype().equals("2")) {
							cell004.setCellValue("A5");
						} else if (a.getO_unittype().equals("3")) {
							cell004.setCellValue("Police");
						}

						HSSFCell cell005 = row.createCell(4); // Vehicle/Police
						cell005.setCellStyle(style);
						cell005.setCellValue(a.getO_busname());

						HSSFCell cell006 = row.createCell(5); // IP
						cell006.setCellStyle(style);
						cell006.setCellValue(a.getO_loginhost());

						HSSFCell cell007 = row.createCell(6); // Channel Count
						cell007.setCellStyle(style);
						cell007.setCellValue(a.getO_channelcount() == null ? ""
								: ("" + a.getO_channelcount()));
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
