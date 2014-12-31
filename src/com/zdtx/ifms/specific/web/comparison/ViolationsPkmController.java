package com.zdtx.ifms.specific.web.comparison;

import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.service.comparison.ViolationsPkmManager;
import com.zdtx.ifms.specific.vo.comparison.ChartVO;
import com.zdtx.ifms.specific.vo.comparison.ViolationsPkmVO;

/**
 * @ClassName: ViolationsPkmController
 * @Description: comparison-Violations Per KM-controller
 * @author Leon Liu
 * @date 2013-9-26 16:12:55
 * @version V1.0
 */
public class ViolationsPkmController extends ReportBase<Object> {

	private static final long serialVersionUID = 9127000351454433814L;

	@Autowired
	private ViolationsPkmManager manager;

	private ChartVO vo = new ChartVO();

	public String getChartData() {
		List<ViolationsPkmVO> dataList = manager.getBatch(vo);
		jsonArray = JSONArray.fromObject(dataList);
		jsonObject.put("drivers", manager.getDriverNames(vo));	//所有司机列表
		jsonObject.put("dataList", jsonArray);	//数据集合
		return "jsonObject";
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