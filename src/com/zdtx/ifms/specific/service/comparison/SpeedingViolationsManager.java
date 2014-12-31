package com.zdtx.ifms.specific.service.comparison;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.vo.comparison.ChartVO;
import com.zdtx.ifms.specific.vo.comparison.SpeedingViolationsVO;

/**
 * 
 * @ClassName: SpeedingViolationsManager
 * @Description: comparison-Speeding Violations-manager
 * @author: Leon Liu
 * @date: 2013-9-27 13:21:04
 * @version V1.0
 */
@Service
@Transactional
public class SpeedingViolationsManager {

	@Autowired
	private BaseDao dao;
	
	/**
	 * 根据条件获取图表数据
	 * @param vo 条件集合
	 * @return List<Chart>
	 */
	@SuppressWarnings("unchecked")
	public List<SpeedingViolationsVO> getBatch(ChartVO vo) {
		String[] dateRange = DateUtil.getThreeWeeksBeginAndEnd(vo.getBeginDate());
		String sql = "SELECT WEEKLY AS WEEK," +
								" SUM(SP1) AS SP1," +
								" SUM(SP2) AS SP2," +
								" SUM(SP3) AS SP3," +
								" MAX(SPV) AS SPV" +
								" FROM T_BEH_NOT_DAY_SUM" +
								" WHERE RUNKM IS NOT NULL";
		if (!Utils.isEmpty(vo.getDepartmentID()) && vo.getDepartmentID() != -1L) { // 按部门查询，没选则为全部部门
			sql += " AND DEPT_ID = " + vo.getDepartmentID();
		}
		sql += " AND RIQI >= '" + dateRange[0] +
					"' AND RIQI <= '" + dateRange[1] +
					"' GROUP BY WEEKLY" +
					" ORDER BY WEEKLY ASC";
		SQLQuery query = dao.getSession().createSQLQuery(sql);
		query.addScalar("week", StringType.INSTANCE);
		query.addScalar("sp1", DoubleType.INSTANCE);
		query.addScalar("sp2", DoubleType.INSTANCE);
		query.addScalar("sp3", DoubleType.INSTANCE);
		query.addScalar("spv", DoubleType.INSTANCE);
		query.setResultTransformer(Transformers.aliasToBean(SpeedingViolationsVO.class));
		return (List<SpeedingViolationsVO>) query.list();
	}
}