/**
 * @File: AvrDailyScoreManager.java
 * @path: idsas - com.zdtx.ifms.specific.service.comparison
 */
package com.zdtx.ifms.specific.service.comparison;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdtx.ifms.common.dao.BaseDao;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.vo.comparison.ChartVO;

/**
 * @ClassName: AvrDailyScoreManager
 * @Description: comparison-Average Daily Score-manager
 * @author: Leon Liu
 * @date: 2013-9-25 8:51:35
 * @version V1.0
 */
@Service
@Transactional
public class AvrDailyScoreManager {

	@Autowired
	private BaseDao dao;
	
	/**
	 * 根据条件获取图表数据
	 * @param vo 条件集合
	 * @return List<Chart>
	 */
	public List<KeyAndValue> getBatch(ChartVO vo) {
		String[] dateRange = DateUtil.getThreeWeeksBeginAndEnd(vo.getBeginDate());
		String sql = "SELECT SUM(AVRS) / COUNT(1) AS KEY, WEEKLY AS VALUE" +
								" FROM ( SELECT SUM(SCORE) / COUNT(DRIVER_ID) AS AVRS, WEEKLY, RIQI" +
								" FROM T_BEH_NOT_DAY_SUM" +
								" WHERE DRIVER_ID IS NOT NULL";
		if(!Utils.isEmpty(vo.getDepartmentID()) && vo.getDepartmentID() != -1L) {	//按部门查询，没选则为全部部门
			sql += " AND DEPT_ID = " + vo.getDepartmentID() + " GROUP BY DEPT_ID, WEEKLY, RIQI)";
		} else {
			sql += " GROUP BY WEEKLY, RIQI)";
		}
		sql += " WHERE RIQI >= '" + dateRange[0] +
					"' AND RIQI <= '" + dateRange[1] +
					"' GROUP BY WEEKLY ORDER BY WEEKLY ASC";
		return dao.getKeyAndValueBySQL(sql);
	}
}