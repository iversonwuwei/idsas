package com.zdtx.ifms.specific.web.query;

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

public class DetailsController extends ReportBase<Details> {

	private static final long serialVersionUID = 2495462420702295722L;

	private String kind;
	private String kinds;
	private Details det = new Details();
	private DetailsVo dvo = new DetailsVo();
	private List<KeyAndValue> btlist = new ArrayList<KeyAndValue>();
	private List<KeyAndValue> orglist = new ArrayList<KeyAndValue>(); //车队
	@Autowired
	private DetailsManager detMgr;


	@Override
	public String index() {
		btlist = detMgr.getCoeList(201000L);
		orglist = baseMgr.getFleetByAuthority(getCurrentUser().getUserID());
		if("1".equals(kinds)){
			dvo.setRiqi(DateUtil.formatDate(new Date()));
		}
		page = detMgr.getBetch(page, dvo);
		return super.index();
	}

	public String exportDetail(){
		try {
			List<Details> list = new ArrayList<Details>();
			String title  = "Event List";
			xlsFileName = disposeXlsName(title + DateUtil.formatDate(new Date()));
			xlsStream = detMgr.getData(list);
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}
	
	public String show(){
		det = baseMgr.get(Details.class, id);
		if(kind!=null&&"1".equals(kind)){
			return "showg";
		}else{
			return "show";
		}
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

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getKinds() {
		return kinds;
	}

	public void setKinds(String kinds) {
		this.kinds = kinds;
	}

}