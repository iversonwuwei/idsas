package com.zdtx.ifms.specific.service.authority;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.SQLQuery;
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
import com.zdtx.ifms.specific.model.authority.Feat;
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.model.authority.Role;
import com.zdtx.ifms.specific.model.authority.User;
import com.zdtx.ifms.specific.vo.authority.UserVo;

/**
 * 权限管理-角色管理-业务层
 * 
 * @author Leon Liu
 * @since 2013-9-23 11:20:21
 */
@Service
@Transactional
public class RoleManager {
	
	@Autowired
	private BaseDao baseDao;

	public Page<Role> getBatch(Page<Role> page, UserVo uv,String aflag,Long[] comids) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Role.class);
		criteria.add(Restrictions.eq("isDelete", "F"));
	if(!aflag.equals("0")){
		criteria.add(Restrictions.in("comid", comids));
	}
		
		List<Order> orderList = new ArrayList<Order>();
		try {
			if (!Utils.isEmpty((uv.getRoleName()))) {
				criteria.add(Restrictions.ilike("roleName", "%" + uv.getRoleName().trim() + "%"));
			}
			orderList.add(Order.asc("roleID"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Page<Role> pageResult=baseDao.getBatch(page,
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
	 * 验证角色名是否重复
	 * @param roleName 角色名称
	 * @return true:重复
	 * @return false:不重复
	 */
	@SuppressWarnings("rawtypes")
	public Boolean checkDuplicate(String roleID, String roleName,Long id,String aflag) {
	
		if(aflag.equals("0")){
			String sql = "";
			if(Utils.isEmpty(roleID)) {
				sql = "SELECT 1 FROM T_CORE_ROLE WHERE ISDELETE = 'F' AND aflag='0' ";
			} else {
				sql = "SELECT 1 FROM T_CORE_ROLE WHERE ISDELETE='F' AND aflag='0'  AND ROLE_ID <> " + roleID + " ";
			}
			SQLQuery query = baseDao.getSession().createSQLQuery(sql);
			List list = query.list();
			if(list == null || list.size() == 0) {
				return false;
			} else {
				return true;
			}
		}else{
			String sql = "";
			if(Utils.isEmpty(roleID)) {
				sql = "SELECT 1 FROM T_CORE_ROLE WHERE ISDELETE = 'F' AND comid="+id+" AND ROLENAME = '" + roleName + "'";
			} else {
				sql = "SELECT 1 FROM T_CORE_ROLE WHERE ISDELETE='F' AND comid="+id+"  AND ROLE_ID <> " + roleID + " AND ROLENAME = '" + roleName + "'";
			}
			SQLQuery query = baseDao.getSession().createSQLQuery(sql);
			List list = query.list();
			if(list == null || list.size() == 0) {
				return false;
			} else {
				return true;
			}
		}
		
	}

	/**
	 * 拼装feat数组成dtree可识别字符串
	 * @param roleFeats
	 * @return String feats字符串
	 */
	public String getFeatString(Long[] roleFeats) {
		String featStr = "";
		featStr = "";
		if (0 != roleFeats.length) {
			featStr = "{funcs:[";
			for (int j = 0; j < roleFeats.length; j++) {
				featStr += "{menu:'" + roleFeats[j] + "'}";
				if (j != roleFeats.length - 1) {
					featStr += ",";
				}
			}
			featStr += "]}";
		}
		return featStr;
	}

	/**
	 * 角色存储
	 * 
	 * @param feat_Add_Str
	 *            保存权限字符串
	 * @param role
	 *            创建的角色
	 * @param currentUser
	 *            创建角色的用户
	 */
	public void saveOrUpdateRole(String feat_Add_Str, Role role, User currentUser) throws Exception {
		String[] feat_Add_StrTemp = feat_Add_Str.split(",");							// 从前台获得功能权限字符串进行拆分
		Long[] feat_Add_Array = new Long[feat_Add_StrTemp.length];	// 转换成long[]类型 保存到role的feats属性中
		for (int i = 0; i < feat_Add_Array.length; i++) {
			if ("".equals(feat_Add_StrTemp[i].trim())) {
				continue;
			}
			feat_Add_Array[i] = Long.parseLong(feat_Add_StrTemp[i]);
		}
		role.setCreater(currentUser.getUserName());
		role.setIsDelete("F");
		if(role.getComid()==null){
			role.setComname(null);
		}else{
			role.setComname(baseDao.get(Org.class, role.getComid()).getOrgName());
		}
		role.setCreateTime(DateUtil.formatLongTimeDate(new Date()));
		role.setFeats(feat_Add_Array);
		baseDao.save(role);
	}

	/**
	 * 根据featID数组获取Feat list
	 * @param featIDArr
	 * @return	List<Feat>
	 */
	public List<Feat> getFeatByList(Long[] featIDArr) {
		List<Feat> featList = new ArrayList<Feat>();
		for (int i = 0, j =  featIDArr.length; i < j; i++) {
			featList.add(baseDao.get(Feat.class, featIDArr[i]));
		}
		return featList;
	}

	/**
	 * 根据用户权限获得用户可见角色
	 * @param currentUser 当前登录用户
	 * @return
	 */
	public List<KeyAndValue> getRoleList(Long [] coms, boolean isSuperAdmin) {
		String sql = "";
		sql = " SELECT ROLE_ID AS KEY, ROLENAME AS VALUE "
				+ " FROM T_CORE_ROLE WHERE ISDELETE = 'F'";
		if (!isSuperAdmin) {
			sql += " AND COMID in ( " +Arrays.toString(coms).replace('[', ' ').replace(']', ' ') +") ";
		}
		sql += " ORDER BY INLEVEL";
		return baseDao.getKeyAndValueBySQL(sql);
	}

	@SuppressWarnings("unchecked")
	public InputStream getExcel( String title) {
		List<User> data = new ArrayList<User>();

		Page<User> page_export = (Page<User>) Utils.getSession().getAttribute("page_export");
		DetachedCriteria criteria_export = (DetachedCriteria) Utils.getSession().getAttribute("criteria_export");
		List<Order> orderList_export = (List<Order>) Utils.getSession().getAttribute("orderList_export");
		page_export.setPageSize(page_export.getTotalCount());
		Page<User> pageResult = baseDao.getBatch(page_export,
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
				cellrow01.setCellValue("No.");
				cellrow02.setCellValue("Role Name");
				cellrow03.setCellValue("Company");
				cellrow04.setCellValue("Description");
				if (null != data && 0 != data.size()) {
					for (int i = 0; i < data.size(); i++) {
						Role r=(Role)data.get(i);
						HSSFRow row = sheet.createRow(i + 2);
						HSSFCell cell001 = row.createCell(0);
						cell001.setCellStyle(style);
						cell001.setCellValue(i + 1);
						HSSFCell cell002 = row.createCell(1);
						cell002.setCellStyle(style);
						cell002.setCellValue(r.getRoleName());
						HSSFCell cell003 = row.createCell(2);
						cell003.setCellStyle(style);
						cell003.setCellValue(r.getComname());
						HSSFCell cell004 = row.createCell(3);
						cell004.setCellStyle(style);
						cell004.setCellValue(r.getDescription());
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