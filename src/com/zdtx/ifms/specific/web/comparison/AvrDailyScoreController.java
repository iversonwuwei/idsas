package com.zdtx.ifms.specific.web.comparison;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.service.comparison.AvrDailyScoreManager;
import com.zdtx.ifms.specific.vo.comparison.ChartVO;

/**
 * @ClassName: AvrDailyScoreController
 * @Description: comparison-Average Daily Score-controller
 * @author Leon Liu
 * @date 2013-9-25 8:51:56
 * @version V1.0
 */
public class AvrDailyScoreController extends URLSupport<Object> {

	private static final long serialVersionUID = -6798573455854696310L;

	@Autowired
	AvrDailyScoreManager manager;
	
	private ChartVO vo = new ChartVO(); 
	
	public String index() {
		return "index";
	}
	
	public String getChartData() {
		List<KeyAndValue> dataList = manager.getBatch(vo);
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