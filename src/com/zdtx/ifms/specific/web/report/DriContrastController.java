package com.zdtx.ifms.specific.web.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
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

public class DriContrastController extends ReportBase<List<String>> {
	
	private static final long serialVersionUID = 9127000351454433814L;
	private Details det = new Details();
	private DetailsVo dvo = new DetailsVo();
	private List<KeyAndValue> btlist = new ArrayList<KeyAndValue>();
	private List<Dict> listDict = new  ArrayList<Dict>(); 
	private List<KeyAndValue> orglist = new ArrayList<KeyAndValue>(); //车队
	@Autowired
	private DetailsManager detMgr;
	@Autowired
	private RepDailyManager repdMgr;
	
	private List<String> dates = new ArrayList<String>();
	private String ges="";
	private String les="";
	private boolean flag = false;
	
	private String driname="";
	private String ajaxd1="";
	private String ajaxd2="";
	private Object[] count;

	private void initDates(Page<?> page) {
		@SuppressWarnings("unchecked")
		List<Object[]> list = (List<Object[]>) page.getResult();
		for (Object[] object : list) {
			dates.add("{\"driver\":\"" + object[2] + "\", \"count\":\"" +object[6]+","+object[7]+","+object[8]+","+object[9]+","+object[10]+","+object[11]+","+object[12]+","+object[13]+","+object[14]+"\"}");
		}
//		String aa ="";
//		aa.substring(0, aa.length()-1);
	}
	public String index() {
		// 默认选择一个部门
		if(null == dvo.getOrgId()){
			List<KeyAndValue> listkey = baseMgr.getDepartByAuthority(getCurrentUser().getUserID());	
			if(null != listkey && listkey.size()>0){
				dvo.setOrgId(Long.valueOf(listkey.get(0).getKey()));
			}
		}
		
		Long[] dictids = {11L,12L,13L,14L,15L,16L,17L,18L,19L};
		listDict=repdMgr.getDict(dictids);
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
		Struts2Util.getSession().setAttribute("redridvo", dvo);
		page = detMgr.getAllDri_(page, dvo);
		List<List<String>> l =page.getResult();
		
		if(l.size()>0){
			Object c = detMgr.getAll(dvo,true).get(0);
			count = (Object[])c;
			int all = page.getTotalCount();
			if("1".equals(dvo.getBehCont())){
				count[1] = (all*100 - Double.parseDouble(count[1]+""))/all;
			}
			if("2".equals(dvo.getBehCont())){
				count[1] = (all*100 - Double.parseDouble(count[1]+"")/7)/all;
			}
			if("3".equals(dvo.getBehCont())){
				count[1] = (all*100 - Double.parseDouble(count[1]+"")/30)/all;
			}
			if("4".equals(dvo.getBehCont())){
				count[1] = (all*100 - Double.parseDouble(count[1]+"")/365)/all;
			}
			initDates(page);
		}
		Struts2Util.getSession().setAttribute("redricount", count);
		if("1".equals(dvo.getBehCont())&&dvo.getDate1()!=null&&!"".equals(dvo.getDate1())){
			ges = dvo.getDate1();
			les = dvo.getDate1();
			flag=true;
		}
		if("2".equals(dvo.getBehCont())&&dvo.getDate2()!=null&&!"".equals(dvo.getDate2())){
			ges = DateUtil.dateToWeek(dvo.getDate2())[0];
			les = DateUtil.dateToWeek(dvo.getDate2())[1];
			flag=true;
		}
		if("3".equals(dvo.getBehCont())&&dvo.getDate3()!=null&&!"".equals(dvo.getDate3())){
			ges = dvo.getDate3() + "-01";
			les = DateUtil.getLastDayOfMonth(dvo.getDate3());
			flag=true;
		}
		if("4".equals(dvo.getBehCont())&&dvo.getDate4()!=null&&!"".equals(dvo.getDate4())){
			ges = dvo.getDate4().split("-")[0] + "-01-01";
			les = dvo.getDate4().split("-")[0] + "-12-31";
			flag=true;
		}
		return "index";
	}
	public String exportDetail() {
		try {
			this.index();
			String title = "Report_Driver_Contrast";
			xlsFileName = title + "_" + DateUtil.formatLongTimeDateWithoutSymbol(new Date()) + ".xlsx";
			super.xlsStream = detMgr.getRepDriverContrastExcel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "xlsx";
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
	public String getDriname() {
		return driname;
	}
	public void setDriname(String driname) {
		this.driname = driname;
	}
	public String getAjaxd1() {
		return ajaxd1;
	}
	public void setAjaxd1(String ajaxd1) {
		this.ajaxd1 = ajaxd1;
	}
	public String getAjaxd2() {
		return ajaxd2;
	}
	public void setAjaxd2(String ajaxd2) {
		this.ajaxd2 = ajaxd2;
	}
	public Object[] getCount() {
		return count;
	}
	public void setCount(Object[] count) {
		this.count = count;
	}

}