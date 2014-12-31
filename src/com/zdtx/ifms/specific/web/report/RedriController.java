package com.zdtx.ifms.specific.web.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.KeyAndValues;
import com.zdtx.ifms.common.utils.Page;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.base.Dict;
import com.zdtx.ifms.specific.model.query.Details;
import com.zdtx.ifms.specific.model.system.Score;
import com.zdtx.ifms.specific.service.query.DetailsManager;
import com.zdtx.ifms.specific.service.query.RepDailyManager;
import com.zdtx.ifms.specific.service.report.RedriManager;
import com.zdtx.ifms.specific.vo.query.DetailsVo;

/**
 * @ClassName: DetailsController
 * @Description: 不规范驾驶报告-不规范驾驶明细-控制层
 * @author JHQ
 * @date 2012年11月28日 16:40:05
 * @version V1.0
 */

public class RedriController extends ReportBase<List<String>> {
	
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
	@Autowired
	private RedriManager redMgr;
	
	private List<Score> scorelist = new ArrayList<Score>();
	private List<String> dates = new ArrayList<String>();
	private String ges="";
	private String les="";
	private boolean flag = false;
	
	private Long driid;
	private String typestr="";
	private String driname="";
	private String ajaxd1="";
	private String ajaxd2="";
	private Object[] count;//合计
	private Object[] os;//明细

	@Override
	public String index() {
		listDict=repdMgr.getDict();
		btlist = detMgr.getCoeList(201000L);
		orglist = baseMgr.getFleetByAuthority(getCurrentUser().getUserID());
		if(dvo.getBehCont()==null){
			dvo.setBehCont("1");
			dvo.setDate1(DateUtil.formatDate(new Date()));
		}
		if("1".equals(dvo.getBehCont())&&"".equals(dvo.getDate1())){
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
		page = detMgr.getAllDri(page, dvo);
		List<List<String>> l =page.getResult();
		
		if(l.size()>0){
			Object c = detMgr.getAll(dvo,false).get(0);
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
			for(int i=0;i<listDict.size();i++){
				dates.add("{\"driver\":\"" + listDict.get(i).getDictName() + "\", \"count\":\"" + (count[i+2]==null?"0":count[i+2])+ "\"}");
			}
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
		return super.index();
	}
	
	@SuppressWarnings("unused")
	private void initDates(Page<?> page) {
		@SuppressWarnings("unchecked")
		List<Object[]> list = (List<Object[]>) page.getResult();
		for (Object[] object : list) {
			dates.add("{\"driver\":\"" + object[0] + "\", \"count\":\"" + 1 + "\"}");
		}
	}
	
	public String getAll(){
		listDict=repdMgr.getDict();
		List<KeyAndValue> list = new ArrayList<KeyAndValue>();
		Object[] o = (Object[])Struts2Util.getSession().getAttribute("redricount");
		for(int i=0;i<listDict.size();i++){
			list.add(new KeyAndValue(listDict.get(i).getDictName(), (o[i+2]==null?"0":o[i+2])+""));
		}
		jsonObject = new JSONObject();
		if(list == null || list.size() == 0) {	// get no data
			jsonObject.put("result", "empty");
			return "jsonObject";
		} else {
			jsonArray = JSONArray.fromObject(list);
			return "jsonArray";
		}
	}
	public String getDridet(){
		DetailsVo dvo1 = (DetailsVo) Struts2Util.getSession().getAttribute("redridvo");
		List<KeyAndValues> list = detMgr.getDridet(driid,typestr,dvo1);

		jsonObject = new JSONObject();
		if(list == null || list.size() == 0) {	// get no data
			jsonObject.put("result", "empty");
			return "jsonObject";
		} else {
			jsonArray = JSONArray.fromObject(list);
			return "jsonArray";
		}
	}
	public String getPie() {
		dvo.setDriverId(Long.parseLong(driname));
		dvo.setBehCont("5");
		dvo.setDate1(ajaxd1.trim().replace("/","").replace("n","").replace("t","").substring(0,10));
		dvo.setDate2(ajaxd2.trim().replace("/","").replace("n","").replace("t","").substring(0,10));
		page = detMgr.getAllDri(page, dvo);
		
		List<List<String>> l =page.getResult();
		listDict=repdMgr.getDict();
		List<KeyAndValue> list = new ArrayList<KeyAndValue>();
		if(l.size()>0){
			Object o = l.get(0);
			Object[] os = (Object[])o;
			for(int i=0;i<listDict.size();i++){
				list.add(new KeyAndValue(listDict.get(i).getDictName(), (os[i+7]==null?"0":os[i+7])+""));
			}
		}
		
		jsonObject = new JSONObject();
		if(list == null || list.size() == 0) {	// get no data
			jsonObject.put("result", "empty");
			return "jsonObject";
		} else {
			jsonArray = JSONArray.fromObject(list);
			return "jsonArray";
		}
	}
	
	/*public String exportDetail() {
		listDict = repdMgr.getDict();
		try {
			this.index();
			String title = "Report Driver";
			super.xlsFileName = disposeXlsName(title + DateUtil.formatDate(new Date()));
			Object[] obj = new Object[1];
			obj[0]=listDict;
			super.xlsStream = detMgr.getRedriData(obj,page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}*/
	public String exportDetail() {
		try {
			this.index();
			String title = "Report_Driver";
			xlsFileName = title + "_" + DateUtil.formatLongTimeDateWithoutSymbol(new Date()) + ".xlsx";
			super.xlsStream = redMgr.getReqDriverExcel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "xlsx";
	}
	public String show(){
		if("1".equals(dvo.getBehCont())){
			ges = dvo.getRiqi();
			les = dvo.getRiqi();
		}
		if("2".equals(dvo.getBehCont())){
			ges = DateUtil.dateToWeek(dvo.getRiqi())[0];
			les = DateUtil.dateToWeek(dvo.getRiqi())[1];
		}
		if("3".equals(dvo.getBehCont())){
			String mont = dvo.getRiqi().substring(0, 7);
			ges = mont + "-01";
			les = DateUtil.getLastDayOfMonth(mont);
		}
		if("4".equals(dvo.getBehCont())){
			String mont = dvo.getRiqi().substring(0, 4);
			ges = mont + "-01-01";
			les = mont + "-12-31";
		}
		Object o = detMgr.getAllDriShow(id, dvo).get(0);
		os = (Object[])o;
		if(os[5]!=null&&!"".equals(os[5])){
			scorelist = detMgr.getScores(os[5]+"");
			
		}else{
			listDict=repdMgr.getDict();
			for(int i=0;i<listDict.size();i++){
				Score s = new Score();
				s.setType(listDict.get(i).getDictName());
				scorelist.add(s);
			}
		}
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
	public List<Score> getScorelist() {
		return scorelist;
	}
	public void setScorelist(List<Score> scorelist) {
		this.scorelist = scorelist;
	}
	public Object[] getOs() {
		return os;
	}
	public void setOs(Object[] os) {
		this.os = os;
	}
	public Long getDriid() {
		return driid;
	}
	public void setDriid(Long driid) {
		this.driid = driid;
	}
	public String getTypestr() {
		return typestr;
	}
	public void setTypestr(String typestr) {
		this.typestr = typestr;
	}

}