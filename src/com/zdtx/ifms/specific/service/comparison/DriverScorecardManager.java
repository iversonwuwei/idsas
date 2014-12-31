package com.zdtx.ifms.specific.service.comparison;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.vo.comparison.ChartVO;
import com.zdtx.ifms.specific.vo.comparison.DriverScorecardVO;

@Service
@Transactional
public class DriverScorecardManager {

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
	public List<DriverScorecardVO> getBatch(ChartVO vo) {
		String sql = "SELECT (100 - SCORE) AS SCORE,"
				+ " DRIVER_ID AS DRIVERID, DRIVER_NAME AS DRIVERNAME, RIQI"
				+ " FROM T_BEH_NOT_DAY_SUM"
				+ " WHERE SCORE IS NOT NULL AND RUNKM IS NOT NULL AND RUNKM <> 0"
				+ " AND DRIVER_ID IS NOT NULL AND RIQI >= '"
				+ vo.getBeginDate() + "' AND RIQI <= '" + vo.getEndDate() + "'";
		if (!Utils.isEmpty(vo.getDepartmentID()) && vo.getDepartmentID() != -1L) { // 按部门查询，没选则为全部部门
			sql += " AND DEPT_ID = " + vo.getDepartmentID();
		}
		sql += " ORDER BY DRIVER_NAME, RIQI ASC";
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		query.addScalar("score", DoubleType.INSTANCE);
		query.addScalar("driverID", LongType.INSTANCE);
		query.addScalar("driverName", StringType.INSTANCE);
		query.addScalar("riqi", StringType.INSTANCE);
		query.setResultTransformer(Transformers
				.aliasToBean(DriverScorecardVO.class));
		return (List<DriverScorecardVO>) query.list();
	}
}