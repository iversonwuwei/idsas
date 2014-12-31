package com.zdtx.ifms.specific.web.comparison;

import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.service.comparison.DriverScorecardManager;
import com.zdtx.ifms.specific.vo.comparison.ChartVO;
import com.zdtx.ifms.specific.vo.comparison.DriverScorecardVO;

/**
 * @ClassName: DriverScorecardVO
 * @Description: comparison-Driver Scorecard-controller
 * @author: Leon Liu
 * @date: 2013-9-26 16:58:11
 * @version V1.0
 */
public class DriverScorecardController extends URLSupport<Object>  {

	private static final long serialVersionUID = -2800224330177450989L;
	@Autowired
	private DriverScorecardManager manager;

	private ChartVO vo = new ChartVO();

	public String getChartData() {
		List<DriverScorecardVO> dataList = manager.getBatch(vo);
		jsonArray = JSONArray.fromObject(dataList);
		return "jsonArray";
	}

	@Override
	public Object getModel() {
		return null;
	}

	public ChartVO getVo() {
		return vo;
	}

	public void setVo(ChartVO vo) {
		this.vo = vo;
	}
}