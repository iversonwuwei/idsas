package com.zdtx.ifms.specific.service.authority;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.model.authority.Feat;
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.model.authority.User;

/**
 * @ClassName: OrgManager
 * @Description: 权限管理-组织机构-业务层
 * @author Kevin Feng
 * @date 2012年9月11日 14:33:05
 * @version V1.0
 */

@Service
@Transactional
public class OrgManager {
	@Autowired
	private BaseDao baseDao;

	public List<Org> getTop() {
		String hql = "FROM Org o where o.fatherOrg = -1 and o.isDelete = 'F' order by o.orgID asc";
		return baseDao.execute(hql);
	}

	public List<Org> getCompany() {
		String hql = "FROM Org o where o.inLevel = 1 and o.isDelete = 'F' order by o.orgID asc";
		return baseDao.execute(hql);
	}

	public List<Org> getChild(long id) {
		String hql = "FROM Org o where o.fatherOrg = " + id
				+ " and o.isDelete = 'F' order by o.inType asc,o.orgID asc";
		return baseDao.execute(hql);
	}
	
	public boolean checkName2(Long id,String name) {
		String sql="select o.org_id as key,o.orgname as value from T_CORE_org o  where o.isdelete='F' and o.inlevel in(2,3) and o.orgname='"+name.trim()+"'  start with o.org_id =(select org_id from T_CORE_org   where isdelete='F' and inlevel=1  start with org_id ="+id+"  CONNECT BY  org_id = PRIOR  fatherorg) CONNECT BY o.fatherorg = PRIOR o.org_id";
		baseDao.getKeyAndValueBySQL(sql).size();
		return baseDao.getKeyAndValueBySQL(sql).size()>0?false:true;
	}
	/**
	 * describe 用于角色
	 * @为姜海泉提供的接口
	 * @param userID
	 * @return
	 */
	public List<KeyAndValue> getOrgNames(Long userID){
		String sql="select o.org_id as key,o.orgname as value from T_CORE_org o  " +
				"where o.isdelete='F' and " +
				"o.org_id in (SELECT U.ORG_ID FROM T_CORE_USER_DATA U WHERE U.USERID = "+userID+")"+
				" ORDER BY o.org_id asc";
		return baseDao.getKeyAndValueBySQL(sql);
	}
	
	/**
	 * describe 用于获得所在部门
	 * @为姜海泉提供的接口
	 * @param 
	 * @return
	 */
	public List<KeyAndValue> getOrgNames(){
		String sql="SELECT O.ORG_ID AS KEY, O.ORGNAME AS VALUE FROM T_CORE_ORG O  " +
				        "WHERE O.ISDELETE='F' " +
				        " ORDER BY O.ORG_ID ASC";
		return baseDao.getKeyAndValueBySQL(sql);
	}
	
	public boolean checkName(String name, Long fatherid, Long id) {
		if (null == id) {
			String hql = "";
			hql = "FROM Org o where o.fatherOrg = " + fatherid
					+ " and o.orgName = '" + name.trim()
					+ "' and o.isDelete = 'F'";
			if (baseDao.execute(hql).size() > 0) {
				return false;
			} else {
				return true;
			}
		} else {
			String hql = "";
			hql = "FROM Org o where o.fatherOrg = " + fatherid
					+ " and o.orgName = '" + name.trim() + "'"
					+ " and o.orgID <> " + id
					+ " and o.isDelete = 'F'";
			if (baseDao.execute(hql).size() > 0) {
				return false;
			} else {
				return true;
			}
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
		String sql = "SELECT ORG_ID AS KEY,USERID AS VALUE FROM T_CORE_USER_DATA "
				+ " WHERE USERID =  " + userID;
		return baseDao.getKeyAndValueBySQL(sql);
	}

	/**
	 * 列表封装成 list<org>
	 *
	 * @param user
	 * @return list<org>
	 */
	@SuppressWarnings("unchecked")
	public List<Org> getAll(User user) {
		String hql = "";
		List<KeyAndValue> orgList = null;
		orgList = getOrgBySuper(user.getUserID());
		Long[] orgArr = null;
		orgArr = Utils.keysToArray(orgList);// 可见的机构列表封装成long[]
		if (0 == user.getUserRole().getInLevel()) { // 超级管理员全部可见
			hql = "FROM Org o where o.isDelete = 'F'";
			return baseDao.execute(hql);
		} else {
			hql = "FROM Org o where o.isDelete = 'F' and o.orgID in (:orgArr)";
			Query query = baseDao.getSession().createQuery(hql);
			query.setParameterList("orgArr", orgArr);
			return query.list();
		}
	}
	public Feat getFeatById(long id) {
		String hql = "FROM Feat f where f.isDelete = 'F' and f.featID = " + id;
		return (Feat) baseDao.execute(hql).get(0);
	}

	/**
	 * @param 根据机构名称搜索
	 * @param name
	 * @return
	 */
	public List<KeyAndValue> searchOrg(String name) {
		List<KeyAndValue> list = new ArrayList<KeyAndValue>();
		list = baseDao.getKeyAndValueBySQL("SELECT ORG_ID AS KEY, ORGNAME AS VALUE "
				+ " FROM T_CORE_ORG WHERE ISDELETE = 'F' START WITH ORG_ID IN "
				+ " ( SELECT T.ORG_ID FROM T_CORE_ORG T WHERE T.ORGNAME LIKE '%" + name
				+ "%') CONNECT BY PRIOR FATHERORG = ORG_ID");
		return list;
	}

	/**
	 * 取新显示顺序号
	 */
	public String getNewDisplayNo(Long parentID) {
		if (Utils.isEmpty(parentID)) {
			return null;
		}
		String sql = "SELECT MAX(ORGSORT) AS KEY,MAX(ORGSORT) AS VALUE FROM T_CORE_ORG WHERE FATHERORG = "
				+ parentID;
		return baseDao.getKeyAndValueBySQL(sql).get(0).getKey() == null ? "0"
				: baseDao.getKeyAndValueBySQL(sql).get(0).getKey();
	}
	
	/**
	 * 删除部位方法
	 * @param id 要删除的部位ID
	 * @param userName 操作人名
	 */
	public void destory(Long id,  String userName) {
		StringBuilder sql = new StringBuilder("UPDATE T_CORE_ORG SET ISDELETE = 'T', CREATETIME = SYSDATE, ");
		sql.append("CREATER = '");
		sql.append("超级管理员");
		sql.append("' WHERE ORG_ID IN (SELECT ORG_ID FROM T_CORE_ORG WHERE ISDELETE = 'F' AND INLEVEL IS NOT NULL START WITH ORG_ID = ");
		sql.append(id);
		sql.append(" CONNECT BY PRIOR ORG_ID = FATHERORG)");
		baseDao.getSession().createSQLQuery(sql.toString()).executeUpdate();
	}
	
	public boolean checkName3(Long id, String name) {
		String sql="select o.org_id as key,o.orgname as value from T_CORE_org o  where o.isdelete='F' and o.inlevel in(2,3) and o.orgname='"+name.trim()+"' and o.org_id<>"+id+"  start with o.org_id =(select org_id from T_CORE_org   where isdelete='F' and inlevel=1  start with org_id ="+id+"  CONNECT BY  org_id = PRIOR  fatherorg) CONNECT BY o.fatherorg = PRIOR o.org_id";
		baseDao.getKeyAndValueBySQL(sql).size();
		return baseDao.getKeyAndValueBySQL(sql).size()>0?false:true;
	}
	/**
	 * @title 用于依次找出父节点，直至根节点
	 * @param id
	 * @return
	 */
	public List<String> getFathers(Long id) {
		List<String> fathers = new ArrayList<String>();
		String sql = "SELECT T.ORG_ID FROM T_CORE_ORG T WHERE T.ISDELETE = 'F' START WITH T.ORG_ID = " + id
				+ " CONNECT BY PRIOR T.FATHERORG = T.ORG_ID";
		SQLQuery query = baseDao.getSession().createSQLQuery(sql);
		for (Object obj : query.list()) {
			fathers.add(obj.toString());
		}
		return fathers;
	}
	
}
