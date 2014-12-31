package com.zdtx.ifms.specific.service.authority;

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
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
//import com.zdtx.ifms.common.utils.CipherUtil;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.ExportExcel;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.model.authority.Role;
import com.zdtx.ifms.specific.model.authority.User;
import com.zdtx.ifms.specific.model.query.DriverTree;
import com.zdtx.ifms.specific.vo.authority.UserVo;

/**
 * @Description: 权限管理-帐号管理-业务层
 * @author Zhang Yi
 * @since 2013年4月26日
 */
@Service
@Transactional
public class UserManager {

	@Autowired
	private BaseDao baseDao;

	/**
	 * 验证登陆账号
	 * @param username 用户名
	 * @param password	密码
	 * @return 用户对象
	 */
	@SuppressWarnings("unchecked")
	public User checkLogin(String username, String password) {
//		String hql = "FROM User u where u.loginName = '" + username
//				+ "' and u.password = '" + password + "' and u.isDelete = 'F'";	//先使用不加密密码
//		+ "' and u.password = '" + (password.length() == 32 ? password :  CipherUtil.generatePassword(password)) + "' and u.isDelete = 'F'";
		String hql = "FROM User u where u.loginName = ? and u.password = ? and u.isDelete = 'F'";	//先使用不加密密码
		List<User> users = baseDao.getSession().createQuery(hql).setString(0, username).setString(1, password).list();
		if (1 == users.size()) {
			return users.get(0);
		}
		return null;
	}

	/**
	 * 获得用户可操作系统
	 * @param userID	用户ID
	 * @return	List<KeyAndValue>key:系统ID;value:系统名称
	 */
	public List<KeyAndValue> getSystem(Long userID,Long roleId) {
		if(null == userID) {
			return null;
		}
		String sql="";
		if(isAllFeat(roleId)){
			sql = "SELECT FEATNAME AS KEY, URL AS VALUE" +
					" FROM T_CORE_FEAT" +
					" WHERE PARENTID = 0 AND ISDELETE = 'F' ORDER BY FEAT_ID ASC";
		}else{
			sql = "SELECT FEATNAME AS KEY, URL AS VALUE" +
					" FROM T_CORE_FEAT" +
					" WHERE PARENTID = 0 AND ISDELETE = 'F' AND FEAT_ID IN" +
					" (SELECT B.FEAT_ID FROM T_CORE_USERINFO A,T_CORE_ROLE_FEAT B " +
					"WHERE A.ROLE_ID = B.ROLE_ID AND A.USERID = " + userID + ") ORDER BY FEAT_ID ASC";
		}
		return baseDao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * 判断是否为超管
	 * @param roleID
	 * @return true:是超管；false:不是超管
	 */
	public boolean isAllFeat(Long roleID) {
		int countNum = 0;// 存放符合FEAT_ID = 0 集合的个数
		String sql = "SELECT 1 FROM T_CORE_ROLE_FEAT WHERE ROLE_ID = " + roleID
				+ " AND FEAT_ID = -100";
		List<?> countList = baseDao.getSession().createSQLQuery(sql).list();
		if (countList != null) {
			countNum = countList.size();
		}
		return countNum > 0 ? true : false;
	}
	
	/**
	 * 条件查询
	 * 
	 * @param page
	 *            封装成page返回
	 * @param user
	 *            用于权限判定
	 * @param userVo
	 *            查询条件
	 * @return
	 */
	public Page<User> getBatch(User user, Page<User> page, boolean isSuperAdmin, UserVo userVo,Long[] coms,Long[] deps) {
//		Criteria criteria = baseDao.getSession().createCriteria(User.class);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		List<Order> orderList = new ArrayList<Order>();
		criteria.add(Restrictions.eq("isDelete", "F"));
		criteria.createAlias("userRole", "userRole", Criteria.LEFT_JOIN);
		criteria.createAlias("userOrg", "userOrg", Criteria.LEFT_JOIN);
		if(!isSuperAdmin ) {	//不是超管，也不是所属总公司操作人
//			criteria.add(Restrictions.eq("userOrg.orgID", user.getUserOrg().getOrgID()));
			criteria.add(Restrictions.or(	Restrictions.in("userOrg.orgID", coms), 	Restrictions.in("userOrg.orgID", deps)));
			
		
		}
		if (!Utils.isEmpty(userVo.getLoginName())) { // 按登录名
			criteria.add(Restrictions.ilike("loginName", "%" + userVo.getLoginName().trim() + "%"));
		}
		if (!Utils.isEmpty(userVo.getUserName())) { // 按用户名
			criteria.add(Restrictions.ilike("userName", "%" + userVo.getUserName().trim() + "%"));
		}
		if (!Utils.isEmpty(userVo.getPhone())) { // 按电话号
			criteria.add(Restrictions.ilike("mobilephone", "%" + userVo.getPhone().trim() + "%"));
		}
		if (!Utils.isEmpty(userVo.getOrgID())) { // 按用户所在机构
			criteria.add(Restrictions.eq("userOrg.orgID", Long.parseLong(userVo.getOrgID())));
		}
	
		Page<User> pageResult=baseDao.getBatch(page,
				criteria.getExecutableCriteria(baseDao.getSession()),
				orderList);

		if (1 == pageResult.getCurrentPage()) {
			Utils.getSession().setAttribute("criteria_export", criteria);
			Utils.getSession().setAttribute("page_export", page);
			Utils.getSession().setAttribute("orderList_export", orderList);
		}
		return pageResult;

	}
	
	
	@SuppressWarnings("unchecked")
	public InputStream getExcel(String title) {
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
				HSSFCell cellrow08 = row2.createCell(7);
				cellrow08.setCellStyle(style);
				
				cellrow01.setCellValue("No.");
				cellrow02.setCellValue("Account");
				cellrow03.setCellValue("Role");
				cellrow04.setCellValue("Company");
				cellrow05.setCellValue("Name");
				cellrow06.setCellValue("Email");
				cellrow07.setCellValue("Phone");
				cellrow08.setCellValue("Description");
				
				
				if (null != data && 0 != data.size()) {
					
					for (int i = 0; i < data.size(); i++) {
						Object[] a=(Object[])data.get(i);
						Org o=(Org)a[0];
						Role r=(Role)a[1];
						User u=(User)a[2];
						HSSFRow row = sheet.createRow(i + 2);
						HSSFCell cell001 = row.createCell(0);
						cell001.setCellStyle(style);
						cell001.setCellValue(i + 1);
						HSSFCell cell002 = row.createCell(1);
						cell002.setCellStyle(style);
						cell002.setCellValue(u.getLoginName());
						HSSFCell cell003 = row.createCell(2);
						cell003.setCellStyle(style);
						cell003.setCellValue((r!=null?(r.getRoleName()!=null?r.getRoleName():""):""));
						HSSFCell cell004 = row.createCell(3);
						cell004.setCellStyle(style);
						cell004.setCellValue((o!=null? (o.getOrgName()!=null?o.getOrgName():""):""));
						HSSFCell cell005 = row.createCell(4);
						cell005.setCellStyle(style);
						cell005.setCellValue(u.getUserName());
						HSSFCell cell006 = row.createCell(5);
						cell006.setCellStyle(style);
						cell006.setCellValue(u.getE_mail());
						HSSFCell cell007 = row.createCell(6);
						cell007.setCellStyle(style);
						cell007.setCellValue(u.getMobilephone());
						HSSFCell cell008 = row.createCell(7);
						cell008.setCellStyle(style);
						cell008.setCellValue(u.getDescription());
						
						
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

	/**
	 * 通过用户信息取出符合条件的 用户的id
	 * 
	 * @param user
	 *            用户信息
	 * @return
	 */
	public List<KeyAndValue> getUserid(User user) {
		if (Utils.isEmpty(user)) { // 验证入参是否为空
			return null;
		}

		String sql = " SELECT USERID AS KEY,USERID AS VALUE FROM T_CORE_USERINFO   "
				+ " WHERE USERID IN ( "
				+ " SELECT USERID FROM  T_CORE_USER_DATA "
				+ " WHERE ORG_ID IN(  "
				+ " SELECT  ORG_ID  FROM T_CORE_ORG   "
				+ " WHERE ISDELETE ='F' AND ORG_ID IN(  "
				+ " SELECT DISTINCT ORG_ID FROM  T_CORE_USER_DATA  "
				+ " WHERE ISDELETE ='F' AND USERID = "
				+ user.getUserID()
				+ " )  AND INLEVEL >="
				+ user.getUserRole().getInLevel()
				+ " ))";

		return baseDao.getKeyAndValueBySQL(sql);
	}

	/**
	 * 获得表中所有信息 封装成List<Org>
	 * 
	 * @return List<Org>
	 */
	public List<Org> getAllOrg() {
		return baseDao.getAll(Org.class);
	}

	/**
	 * 保存用户信息
	 * 
	 * @param currentUser
	 *            当前用户
	 * @param user
	 *            待删除的用户
	 */
	public void saveUser(Long userDepart, String dataStr, User currentUser, User user) {
		Org o=null;
		if(userDepart!=null){
			o=baseDao.get(Org.class, userDepart);
		}
		try {
			String dataStr1 = DateUtil.formatLongTimeDate(new Date());
			String[] menuArray = dataStr.split(",");
			Long[] orgs = new Long[menuArray.length];
			for (int i = 0; i < menuArray.length; i++) {
				orgs[i] = Long.parseLong(menuArray[i]);
			}
			if (user.getUserID() == null) {
				user.setPassword("123");	//先使用不加密密码
//				user.setPassword(CipherUtil.generatePassword("123"));
			} else {
				user.setPassword(user.getPassword());
			}
			user.setCreater(currentUser.getUserName());
			user.setCreateTime(dataStr1);
			user.setUserOrg(o);
			user.setOrgs(orgs);
			user.setIsDelete("F");
		} catch (Exception e) {
			e.printStackTrace();
		}
		baseDao.save(user);
	}

	/**
	 * 添加时验证用户登录名是否重复
	 * 
	 * @param name
	 * @return
	 */
	public Boolean checkAdd(String name) {
		Boolean check = false;
		try {
			if (0 != baseDao.execute(
					"FROM User WHERE loginName = '" + name
							+ "' AND isdelete='F'").size()) {
				check = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}
	
	/**
	 * 编辑时验证用户登录名是否重复
	 * 
	 * @param name
	 * @return
	 */
	public Boolean checkEdit(String name, String userID) {
		Boolean check = false;
		try {
			if (0 != baseDao.execute(
					"FROM User WHERE loginName = '" + name
							+ "' and userID <> "+ userID +" AND isdelete='F'").size()) {
				check = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}
	
	/**
	 * 添加时验证用户卡号是否重复
	 * 
	 * @param name
	 * @return
	 */
	public Boolean checkAddCode(String code) {
		Boolean check = false;
		try {
			if (0 != baseDao.execute(
					"FROM User WHERE userCode = '" + code
							+ "' AND isdelete='F'").size()) {
				check = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}
	
	/**
	 * 编辑时验证用户卡号是否重复
	 * 
	 * @param name
	 * @return
	 */
	public Boolean checkEditCode(String code, String userID) {
		Boolean check = false;
		try {
			if (0 != baseDao.execute(
					"FROM User WHERE userCode = '" + code
							+ "' and userID <> "+ userID +" AND isdelete='F'").size()) {
				check = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}

	/**
	 * 通过子节点ID得到父节点ID
	 * 
	 * @param 子节点
	 * @return 父节点ID
	 */
	public String getFatherOrgID(String id) {
		String sql = " SELECT ORG_ID "
	               + " FROM T_CORE_ORG "
				   + " WHERE ISDELETE = 'F' " 
	               + " AND INLEVEL = 1 "
	               + " AND INTYPE = 1 "
				   + " START WITH ORG_ID = "
	               + id
				   + " CONNECT BY ORG_ID = PRIOR PARENTID ";
		if(baseDao.getSession().createSQLQuery(sql).list().size()==0){
			return "";
		}else{
		    return baseDao.getSession().createSQLQuery(sql).list().get(0).toString();
		}
	}

	/**
	 * 通过用户id获得可见公司列表
	 * 
	 * @param userID
	 *            登录用户id
	 * @return 可见的机构列表
	 */
	public List<KeyAndValue> getOrgBySuper(Long userID) {
		if (Utils.isEmpty(userID)) { // 验证入参是否为空
			return null;
		}
		String sql = " SELECT ORG_ID AS KEY,USERID AS VALUE FROM T_CORE_USER_DATA "
				+ " WHERE USERID =  " + userID;
		return baseDao.getKeyAndValueBySQL(sql);
	}

	@SuppressWarnings("unchecked")
	public List<Org> getOrgList(User user, boolean isSuperAdmin) {
		String hql = "";
		if (isSuperAdmin) {
			hql = " FROM Org o where o.isDelete = 'F' and o.flag = 2 ORDER BY o.orgName asc ";
			return baseDao.execute(hql);
		} else {
			List<KeyAndValue> orgList = null;
			orgList = getOrgBySuper(user.getUserID());
			Long[] orgArr = null;
			orgArr = Utils.keysToArray(orgList);// 可见的机构列表封装成long[]
			hql = "FROM Org o where o.isDelete = 'F' and o.flag = 2 and (o.orgID in (:orgArr) or o.parentID="+user.getUserOrg().getOrgID()+" ) ORDER BY o.orgName asc ";
			Query query = baseDao.getSession().createQuery(hql);
			query.setParameterList("orgArr", orgArr);
			return query.list();
		}
	}
	
	/**
	 * 获得可见机构列表
	 * 
	 * @param 
	 *            
	 * @return 可见的机构列表
	 */
	@SuppressWarnings("unchecked")
	public List<Org> getOrgDataList() {
		return baseDao.getSession().createSQLQuery("select * from T_CORE_ORG t  where t.isDelete = 'F' AND( t.org_id=3 or t.parentid=3 ) or t.parentid in (select o.org_id from T_CORE_ORG o where o.isDelete = 'F' AND  o.parentid=3 )").addEntity(Org.class).list();

	}

	public List<DriverTree> getDriverList() {
		String hql = "From DriverTree";
		return baseDao.execute(hql);
	}
	
	/***
	 * 查看页面用到的司机list
	 * @param userid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DriverTree> getDriverList2(Long userid) {
		String sql ="select * from V_TREE_DRIVER where drivernUMBER IS NULL OR driverid in(select d.org_id+3000000 from T_CORE_USER_DATA d where d.userid=" + userid + ")";
		return baseDao.getSession().createSQLQuery(sql).addEntity(DriverTree.class).list();
	}

	/**
	 * 
	 * @param datas
	 *            用户可见权限字符串
	 * @return
	 */
	public String getOrgStr(Long[] datas) {
		String dataStr = "";
		// 组装成字符串
		if (0 != datas.length) {
			dataStr = "{funcs:[";
			for (int j = 0; j < datas.length; j++) {
				dataStr += "{menu:'" + datas[j] + "'}";
				if (j != datas.length - 1) {
					dataStr += ",";
				}
			}
			dataStr += "]}";
		}
		return dataStr;
	}

	/**
	 * 修改密码
	 * 
	 * @param userID
	 *            用户id
	 * @param password
	 *            旧密码
	 * @param newPassword
	 *            新密码
	 * @return
	 */
	public String changePsd(String userID, String password, String newPassword) {
		if (Utils.isEmpty(userID) || Utils.isEmpty(password)
				|| Utils.isEmpty(newPassword)) {
			return "empty";
		}
		User user = baseDao.get(User.class, Long.valueOf(userID));
		if ("123".equals(newPassword) && "resetbeilv".equals(password)) { // 重置密码
			user.setPassword("123");	//先使用不加密密码
//			user.setPassword(CipherUtil.generatePassword("123"));
			try {
				baseDao.save(user);
				return "true";
			} catch (Exception e) {
				e.printStackTrace();
				return "false";
			}
		} else if (user.getPassword().trim().equals(password)) {
			user.setPassword(newPassword);
//		} else if (CipherUtil.validatePassword(user.getPassword().trim(), password)) {
//			user.setPassword(CipherUtil.generatePassword(newPassword));
			try {
				baseDao.save(user);
				return "true";
			} catch (Exception e) {
				e.printStackTrace();
				return "false";
			}
		} else {
			return "psdErr";
		}
	}
	
	public List<KeyAndValue> getUsers(){
		String sql = " SELECT USERID AS KEY,USERNAME AS VALUE FROM T_CORE_USERINFO "
				+ " WHERE ISDELETE = 'F' ORDER BY USERNAME";
		return baseDao.getKeyAndValueBySQL(sql);
	}

	public String checkLoginName(String names, String code, String userName, Long userID) {
		String whereSql = "";
		if (null != userID && -1 != userID) {
			whereSql = "AND userID <> " + userID;
		}
		try {
			if (0 != baseDao.execute(
					"FROM User WHERE loginName = '" + names
							+ "' AND isDelete='F'" + whereSql).size()) {
				return "loginNameRep";
			}
			if (0 != baseDao.execute(
					"FROM User WHERE userCode = '" + code
							+ "' AND isDelete='F'" + whereSql).size()) {
				return "userCodeRep";
			}
			if (0 != baseDao.execute(
					"FROM User WHERE userName = '" + userName
					+ "' AND isDelete='F'" + whereSql).size()) {
				return "userNameRep";
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "err";
		}
	}

	
}