package com.zdtx.ifms.specific.web.report;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.base.Dict;
import com.zdtx.ifms.specific.model.query.Details;
import com.zdtx.ifms.specific.service.query.DetailsManager;
import com.zdtx.ifms.specific.service.query.RepDailyManager;
import com.zdtx.ifms.specific.vo.query.DetailsVo;

/**
 * @ClassName: DetailsController
 * @Description: 不规范驾驶报告-不规范驾驶明细-控制层
 * @author JHQ
 * @date 2012年11月28日 16:40:05
 * @version V1.0
 */

public class RedepController extends ReportBase<List<String>> {

	private static final long serialVersionUID = 9127000351454433814L;
	private Details det = new Details();
	private DetailsVo dvo = new DetailsVo();
	private List<KeyAndValue> btlist = new ArrayList<KeyAndValue>();
	private List<Dict> listDict = new ArrayList<Dict>();
	private List<KeyAndValue> orglist = new ArrayList<KeyAndValue>(); // 车队
	@Autowired
	private DetailsManager detMgr;
	@Autowired
	private RepDailyManager repdMgr;

	private String ges = "";
	private String les = "";
	private boolean flag = false;
	private List<String> dates = new ArrayList<String>();// 图数据

	@Override
	public String index() {
		listDict = repdMgr.getDict();
		btlist = detMgr.getCoeList(201000L);
		orglist = baseMgr.getFleetByAuthority(getCurrentUser().getUserID());
		if(dvo.getBehCont()==null){
			dvo.setBehCont("1");
			dvo.setDate1(DateUtil.formatDate(new Date()));
		}
		if("2".equals(dvo.getBehCont())&&"".equals(dvo.getDate2())){
			dvo.setDate2(DateUtil.formatDate(new Date()));
		}
		if("3".equals(dvo.getBehCont())&&"".equals(dvo.getDate3())){
			dvo.setDate3(DateUtil.formatMonth(new Date()));
		}
		if("4".equals(dvo.getBehCont())&&"".equals(dvo.getDate4())){
			dvo.setDate4(DateUtil.formatYear(new Date()));
		}
		Utils.getSession().setAttribute("dvo", dvo);
		page = detMgr.getAll(page, dvo);
		
		if ("1".equals(dvo.getBehCont()) && dvo.getDate1() != null && !"".equals(dvo.getDate1())) {
			ges = dvo.getDate1();
			les = dvo.getDate1();
			flag = true;
		}
		if ("2".equals(dvo.getBehCont()) && dvo.getDate2() != null && !"".equals(dvo.getDate2())) {
			ges = DateUtil.dateToWeek(dvo.getDate2())[0];
			les = DateUtil.dateToWeek(dvo.getDate2())[1];
			flag = true;
		}
		if ("3".equals(dvo.getBehCont()) && dvo.getDate3() != null && !"".equals(dvo.getDate3())) {
			ges = dvo.getDate3() + "-01";
			les = DateUtil.getLastDayOfMonth(dvo.getDate3());
			flag = true;
		}
		if ("4".equals(dvo.getBehCont()) && dvo.getDate4() != null && !"".equals(dvo.getDate4())) {
			ges = dvo.getDate4().split("-")[0] + "-01-01";
			les = dvo.getDate4().split("-")[0] + "-12-31";
			flag = true;
		}
		
		initDates(page,dvo);
		return super.index();
	}

	public String exportDetail() {
		try {
			this.index();
			String title = "Report_Department";
			xlsFileName = title + "_" + DateUtil.formatLongTimeDateWithoutSymbol(new Date()) + ".xlsx";
			super.xlsStream = detMgr.getRepDeptExcel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "xlsx";
	}

	public String show() {
		det = baseMgr.get(Details.class, id);
		return "show";
	}

	@Override
	public List<String> getModel() {
		return null;
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

	public List<Dict> getListDict() {
		return listDict;
	}

	public void setListDict(List<Dict> listDict) {
		this.listDict = listDict;
	}

	public String getGes() {
		return ges;
	}

	public void setGes(String ges) {
		this.ges = ges;
	}

	public String getLes() {
		return les;
	}

	public void setLes(String les) {
		this.les = les;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	@SuppressWarnings("unchecked")
	private void initDates(Page<?> page,DetailsVo dvo) {
		List<Object[]> list = (List<Object[]>) page.getResult();
		for (Object[] object : list) {
			Double d = 0d;
			if(object[4]!=null){
				d = Double.parseDouble(object[4]+"");
			}
			int i = Integer.parseInt(object[5]+"");
			Double o = 0d;
			if ("1".equals(dvo.getBehCont())){
				o = 100-d/i;
			}
			if ("2".equals(dvo.getBehCont())){
				o = 100-d/i/7;
			}
			if ("3".equals(dvo.getBehCont())){
				o = 100-d/i/30;
			}
			if ("4".equals(dvo.getBehCont())){
				o = 100-d/i/365;
			}
			if(o<0){
				o=0d;
			}
			DecimalFormat df = new DecimalFormat("###0.00");
			dates.add("{\"driver\":\"" + object[1] + "\", \"count\":\"" + df.format(o) + "\"}");
		}
	}
}