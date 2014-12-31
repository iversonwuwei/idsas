package com.zdtx.ifms.specific.web.comparison;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.service.comparison.DistanceCoveredManager;
import com.zdtx.ifms.specific.vo.comparison.ChartVO;

/**
 * @ClassName: DistanceCoveredController
 * @Description: comparison-Distance Covered-controller
 * @author Leon Liu
 * @date 2013-9-24 下午1:32:06
 * @version V1.0
 */
public class DistanceCoveredController extends ReportBase<Object> {

	private static final long serialVersionUID = 9173702565438285389L;
	
	@Autowired
	DistanceCoveredManager manager;
	
	private ChartVO vo = new ChartVO(); 
	
	public String index() {
		return "index";
	}
	
	public String getChartData() {
		List<KeyAndValue> dataList = manager.getBatch(vo);
		jsonArray = JSONArray.fromObject(dataList);
		return "jsonArray";
	}
	
	public String exportExcel() {
		String title = "Distance_Covered";
		xlsFileName = title + "_" + DateUtil.formatLongTimeDateWithoutSymbol(new Date()) + ".xlsx";
		try {
			xlsStream = manager.getExcel(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "xlsx";
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