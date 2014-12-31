package com.zdtx.ifms.specific.web.comparison;

import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.service.comparison.SubViolationsPkmManager;
import com.zdtx.ifms.specific.vo.comparison.ChartVO;
import com.zdtx.ifms.specific.vo.comparison.SubViolationsPkmVO;

/**
 * @ClassName: SubViolationsPkmController
 * @Description: comparison-Sub-violations Per KM-controller
 * @author Leon Liu
 * @date 2013-9-25 11:09:04
 * @version V1.0
 */
public class SubViolationsPkmController extends URLSupport<Object> {

	private static final long serialVersionUID = -1636591381210361442L;

	@Autowired
	SubViolationsPkmManager manager;

	private ChartVO vo = new ChartVO();

	public String index() {
		return "index";
	}

	public String getChartData() {
		List<SubViolationsPkmVO> dataList = manager.getBatch(vo);
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