package com.zdtx.ifms.specific.web.comparison;

import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.service.comparison.SpeedingViolationsManager;
import com.zdtx.ifms.specific.vo.comparison.ChartVO;
import com.zdtx.ifms.specific.vo.comparison.SpeedingViolationsVO;

/**
 * 
 * @ClassName: SpeedingViolationsController
 * @Description: comparison-Speeding Violations-controller
 * @author: Leon Liu
 * @date: 2013-9-27 下午1:05:59
 * @version V1.0
 */
public class SpeedingViolationsController extends URLSupport<Object> {

	private static final long serialVersionUID = -6381630768161970769L;

	@Autowired
	private SpeedingViolationsManager manager;

	private ChartVO vo = new ChartVO();

	public String index() {
		return "index";
	}

	public String getChartData() {
		List<SpeedingViolationsVO> dataList = manager.getBatch(vo);
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