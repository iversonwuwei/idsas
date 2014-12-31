package com.zdtx.ifms.specific.web.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.query.Details;
import com.zdtx.ifms.specific.service.query.DetailsManager;
import com.zdtx.ifms.specific.vo.query.DetailsVo;

/**
 * @ClassName: DetailsController
 * @Description: 不规范驾驶报告-不规范驾驶明细-控制层
 * @author JHQ
 * @date 2012年11月28日 16:40:05
 * @version V1.0
 */

public class DetailshowController extends ReportBase<Details> {

	private static final long serialVersionUID = -6860329715100665788L;
	private Details det = new Details();
	private DetailsVo dvo = new DetailsVo();
	private List<KeyAndValue> btlist = new ArrayList<KeyAndValue>();
	private List<KeyAndValue> orglist = new ArrayList<KeyAndValue>(); //车队
	@Autowired
	private DetailsManager detMgr;

	private String sdu="";
	private String selectype;//进入页面时不执行查询

	@Override
	public String index() {
		btlist = detMgr.getCoeList(201000L);
		orglist = baseMgr.getFleetByAuthority(getCurrentUser().getUserID());
		if(!"".equals(sdu)&&sdu.contains(",")){
			dvo.setTimeBegin(sdu.split(",")[0]);
			dvo.setTimeEnd(sdu.split(",")[1]);
			dvo.setRiqi(sdu.split(",")[0].split(" ")[0]);
		}
		if("true".equals(selectype)){
			page = detMgr.getBetch(page, dvo);
		}
		return super.index();
	}

	public String exportDetail() {
		String title = "Report Trip";
		xlsFileName = disposeXlsName(title + DateUtil.formatDate(new Date()));
		try {
			xlsStream = detMgr.getTrip(dvo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}
	
	public String show(){
		det = baseMgr.get(Details.class, id);
		return "show";
	}
	
	@Override
	public Details getModel() {
		return det;
	}

	public Details getDet() {
		return det;
	}

	public void setDet(Details det) {
		this.det = det;
	}

	public DetailsVo getDvo() {
		return dvo;
	}

	public void setDvo(DetailsVo dvo) {
		this.dvo = dvo;
	}

	public List<KeyAndValue> getBtlist() {
		return btlist;
	}

	public void setBtlist(List<KeyAndValue> btlist) {
		this.btlist = btlist;
	}

	public List<KeyAndValue> getOrglist() {
		return orglist;
	}

	public void setOrglist(List<KeyAndValue> orglist) {
		this.orglist = orglist;
	}

	public String getSdu() {
		return sdu;
	}

	public void setSdu(String sdu) {
		this.sdu = sdu;
	}

	public String getSelectype() {
		return selectype;
	}

	public void setSelectype(String selectype) {
		this.selectype = selectype;
	}

}