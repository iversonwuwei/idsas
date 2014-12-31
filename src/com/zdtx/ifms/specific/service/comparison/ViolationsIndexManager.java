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
import com.zdtx.ifms.specific.vo.comparison.ViolationsIndexVO;

/**
 * 
 * @ClassName: ViolationsIndexManager
 * @Description: comparison-Violations Index-manager
 * @author: Leon Liu
 * @date: 2013-9-27 上午10:29:18
 * @version V1.0
 */
@Service
@Transactional
public class ViolationsIndexManager {
	
	@Autowired
	public BaseDao dao;
	/**
	 * 根据条件获取图表数据
	 * @param vo 条件集合
	 * @return List<Chart>
	 */
	@SuppressWarnings("unchecked")
	public List<ViolationsIndexVO> getBatch(ChartVO vo) {
		String[] dateRange = DateUtil.getThreeWeeksBeginAndEnd(vo.getBeginDate());
		String sql = "SELECT WEEKLY AS WEEK," +
								" (SUM(BEH_SA) + SUM(BEH_SB) + SUM(BEH_SL) + SUM(BEH_SR))/SUM(RUNKM) AS BEHINDEX," +
								" SUM(BEH_SA)/SUM(RUNKM) AS BEHSA," +
								" SUM(BEH_SB)/SUM(RUNKM) AS BEHSB," +
								" SUM(BEH_SL)/SUM(RUNKM) AS BEHSL," +
								" SUM(BEH_SR)/SUM(RUNKM) AS BEHSR" +
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
		query.addScalar("behIndex", DoubleType.INSTANCE);
		query.addScalar("behSA", DoubleType.INSTANCE);
		query.addScalar("behSB", DoubleType.INSTANCE);
		query.addScalar("behSL", DoubleType.INSTANCE);
		query.addScalar("behSR", DoubleType.INSTANCE);
		query.setResultTransformer(Transformers
				.aliasToBean(ViolationsIndexVO.class));
		return (List<ViolationsIndexVO>) query.list();
	}
}