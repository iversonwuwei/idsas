package com.zdtx.ifms.specific.service.comparison;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.vo.comparison.ChartVO;
import com.zdtx.ifms.specific.vo.comparison.ViolationsPkmVO;

/**
 * @ClassName: DetailsManager
 * @Description: comparison-Violations Per KM-manager
 * @author Leon Liu
 * @date 2013-9-26 16:36:41
 * @version V1.0
 */
@Service
@Transactional
public class ViolationsPkmManager {

	@Autowired
	public BaseDao dao;

	/**
	 * 根据条件获取图表数据
	 * 
	 * @param vo
	 *            条件集合
	 * @return List<Chart>
	 */
	@SuppressWarnings("unchecked")
	public List<ViolationsPkmVO> getBatch(ChartVO vo) {
		String sql = "SELECT (BEH_SA + BEH_SB + BEH_SL + BEH_SR + BEH_SP + BEH_EO + BEH_IDLE + BEH_AC + BEH_NS)/RUNKM AS BEHCOUNT,"
				+ " DRIVER_ID AS DRIVERID, DRIVER_NAME AS DRIVERNAME, RIQI"
				+ " FROM T_BEH_NOT_DAY_SUM"
				+ " WHERE RUNKM IS NOT NULL AND RUNKM <> 0"
				+ " AND DRIVER_ID IS NOT NULL"
				+ " AND RIQI >= '" + vo.getBeginDate() + "' AND RIQI <= '" + vo.getEndDate() + "'";
		if (!Utils.isEmpty(vo.getDepartmentID()) && vo.getDepartmentID() != -1L) { // 按部门查询，没选则为全部部门
			sql += " AND DEPT_ID = " + vo.getDepartmentID();
		}
		sql += " ORDER BY RIQI, DRIVER_NAME ASC";
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		query.addScalar("behCount", DoubleType.INSTANCE);
		query.addScalar("driverID", LongType.INSTANCE);
		query.addScalar("driverName", StringType.INSTANCE);
		query.addScalar("riqi", StringType.INSTANCE);
		query.setResultTransformer(Transformers
				.aliasToBean(ViolationsPkmVO.class));
		return (List<ViolationsPkmVO>) query.list();
	}
	
	/**
	 * 获取所有司机名称
	 * @param departmentID
	 */
	public String getDriverNames(ChartVO vo) {
		String sql = "SELECT DISTINCT(DRIVER_NAME) AS KEY,"
				+ " 1 AS VALUE"
				+ " FROM T_BEH_NOT_DAY_SUM"
				+ " WHERE RUNKM IS NOT NULL AND RUNKM <> 0"
				+ " AND DRIVER_ID IS NOT NULL"
				+ " AND RIQI >= '" + vo.getBeginDate() + "' AND RIQI <= '" + vo.getEndDate() + "'";
		if (!Utils.isEmpty(vo.getDepartmentID()) && vo.getDepartmentID() != -1L) { // 按部门查询，没选则为全部部门
			sql += " AND DEPT_ID = " + vo.getDepartmentID();
		}
		sql += " ORDER BY DRIVER_NAME ASC";
		List<KeyAndValue> driverList = dao.getKeyAndValueBySQL(sql);
		if(driverList == null || driverList.size() == 0) {
			return null;
		}
		String res = "";
		for (int i = 0, j = driverList.size(); i < j; i++) {
			res += driverList.get(i).getKey() + "|";
		}
		return StringUtils.removeEnd(res, "|");
	}
}