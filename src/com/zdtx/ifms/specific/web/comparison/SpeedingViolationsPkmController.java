package com.zdtx.ifms.specific.web.comparison;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.service.comparison.SpeedingViolationsPkmManager;
import com.zdtx.ifms.specific.vo.comparison.ChartVO;
import com.zdtx.ifms.specific.vo.comparison.SpeedingViolationsVO;

/**
 * 
 * @ClassName: SpeedingViolationsPkmController
 * @Description: comparison-Speeding Violations Per KM-controller
 * @author: Leon Liu
 * @date: 2013-9-27 13:42:51
 * @version V1.0
 */
public class SpeedingViolationsPkmController extends URLSupport<Object> {

	private static final long serialVersionUID = -6381630768161970769L;

	@Autowired
	private SpeedingViolationsPkmManager manager;

	private ChartVO vo = new ChartVO();

	public String index() {
		return "index";
	}

	public String getChartData() {
		List<SpeedingViolationsVO> dataList = manager.getBatch(vo);
		SpeedingViolationsVO benchmark = new SpeedingViolationsVO();	//创建benchmark项
		benchmark.setWeek("BenchMark");	//赋值来源不详，暂定为ppt中的数据
		benchmark.setSp1(0.1D);	
		benchmark.setSp2(0D);
		benchmark.setSp3(0D);
		benchmark.setSpv(dataList.get(0).getSpv());	//将第一个数据的速度标准值赋给BenchMark
		jsonArray = JSONArray.fromObject(dataList);
		jsonObject = JSONObject.fromObject(benchmark);	//在List开头位置插入benchmark项
		jsonArray.add(0, jsonObject);
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