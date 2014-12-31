package com.zdtx.ifms.specific.web.comparison;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.service.comparison.ViolationsIndexManager;
import com.zdtx.ifms.specific.vo.comparison.ChartVO;
import com.zdtx.ifms.specific.vo.comparison.ViolationsIndexVO;

/**
 * 
 * @ClassName: ViolationsIndexController
 * @Description: comparison-Violations Index-controller
 * @author: Leon Liu
 * @date: 2013-9-27 上午10:38:27
 * @version V1.0
 */
public class ViolationsIndexController extends URLSupport<Object>  {

	private static final long serialVersionUID = 3155460243537275701L;
	
	@Autowired
	private ViolationsIndexManager manager;

	private ChartVO vo = new ChartVO(); 
	
	public String index() {
		return "index";
	}
	
	public String getChartData() {
		List<ViolationsIndexVO> dataList = manager.getBatch(vo);
		ViolationsIndexVO benchmark = new ViolationsIndexVO();		//创建benchmark项
		benchmark.setWeek("BenchMark");	//赋值来源不详，暂定为ppt中的数据
		benchmark.setBehSA(0.1D);
		benchmark.setBehSB(0.5D);
		benchmark.setBehSL(0.5D);
		benchmark.setBehSR(0.5D);
		benchmark.setBehIndex(0.1D + 0.5D + 0.5D + 0.5D);
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