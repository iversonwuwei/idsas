package com.zdtx.ifms.specific.web.query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.base.Dict;
import com.zdtx.ifms.specific.model.query.RepDaily;
import com.zdtx.ifms.specific.service.query.RepDailyManager;
import com.zdtx.ifms.specific.vo.query.RepDailyVo;


/**
 * @ClassName: RepDailyController
 * @Description: 不规范驾驶报告-不规范驾驶日报-控制层
 * @author JHQ
 * @date 2012年11月29日 11:23:50
 * @version V1.0
 */

public class RepDailyController extends ReportBase<List<String>> {

	private static final long serialVersionUID = 5910524102159983979L;
	
	private RepDaily repd = new RepDaily();
	private RepDailyVo rdvo = new RepDailyVo();
	private List<Dict> listDict = new  ArrayList<Dict>(); 
	@Autowired
	private RepDailyManager repdMgr;

	private String ges="";
	private String les="";
	private boolean flag = false;
	private Object[] count;
/*	@Autowired
	private DictManager dictMgr;*/

	@Override
	public String index() {
		listDict=repdMgr.getDict();
		Utils.getSession().setAttribute("rdvo", rdvo);
		if (null == rdvo.getBehCont()) {
			rdvo.setBehCont("1");
			rdvo.setDate1(DateUtil.formatDate(new Date()));
		}
		if("1".equals(rdvo.getBehCont())&&"".equals(rdvo.getDate1())){
			rdvo.setDate1(DateUtil.formatDate(new Date()));
		}
		if("2".equals(rdvo.getBehCont())&&"".equals(rdvo.getDate2())){
			rdvo.setDate2(DateUtil.formatDate(new Date()));
		}
		if("3".equals(rdvo.getBehCont())&&"".equals(rdvo.getDate3())){
			rdvo.setDate3(DateUtil.formatMonth(new Date()));
		}
		if("4".equals(rdvo.getBehCont())&&"".equals(rdvo.getDate4())){
			rdvo.setDate4(DateUtil.formatYear(new Date()));
		}
				
		page = repdMgr.getAll(page, rdvo);
		List<List<String>> l =page.getResult();
		if(l.size()>0){
			Object c = repdMgr.getAllCount(rdvo).get(0);
			count = (Object[])c;
			Utils.getSession().setAttribute("count", count);
		}
		int all = page.getTotalCount();
		if("1".equals(rdvo.getBehCont())){
			count[1] = (all*100 - Double.parseDouble(count[1]+""))/all;
		}
		if("2".equals(rdvo.getBehCont())){
			count[1] = (all*100 - Double.parseDouble(count[1]+"")/7)/all;
		}
		if("3".equals(rdvo.getBehCont())){
			count[1] = (all*100 - Double.parseDouble(count[1]+"")/30)/all;
		}
		if("4".equals(rdvo.getBehCont())){
			count[1] = (all*100 - Double.parseDouble(count[1]+"")/365)/all;
		}
		
		
		if("1".equals(rdvo.getBehCont())&&rdvo.getDate1()!=null&&!"".equals(rdvo.getDate1())){
			ges = rdvo.getDate1();
			les = rdvo.getDate1();
			flag=true;
		}
		if("2".equals(rdvo.getBehCont())&&rdvo.getDate2()!=null&&!"".equals(rdvo.getDate2())){
			ges = DateUtil.dateToWeek(rdvo.getDate2())[0];
			les = DateUtil.dateToWeek(rdvo.getDate2())[1];
			flag=true;
		}
		if("3".equals(rdvo.getBehCont())&&rdvo.getDate3()!=null&&!"".equals(rdvo.getDate3())){
			ges = rdvo.getDate3() + "-01";
			les = getLastDayOfMonth(rdvo.getDate3());
			flag=true;
		}
		if("4".equals(rdvo.getBehCont())&&rdvo.getDate4()!=null&&!"".equals(rdvo.getDate4())){
			ges = rdvo.getDate4().split("-")[0] + "-01-01";
			les = rdvo.getDate4().split("-")[0] + "-12-31";
			flag=true;
		}
		Utils.getSession().setAttribute("ges", ges);
		Utils.getSession().setAttribute("les", les);
		Utils.getSession().setAttribute("rdvo", rdvo);
		return super.index();
	}
	
	public String exportDetail(){
		this.index();
		String title  = "Report Query";
		xlsFileName = disposeXlsName(title + DateUtil.formatDate(new Date()));
		Object[] obj = new Object[4];
		obj[0] =listDict;
		obj[1] = Utils.getSession().getAttribute("ges");
		obj[2] = Utils.getSession().getAttribute("les");
		obj[3] = Utils.getSession().getAttribute("rdvo");
		xlsStream = repdMgr.getRep((RepDailyVo)Utils.getSession().getAttribute("rdvo"),obj);
		return "xls";
	}
	/**
	 * 根据月份获得这一月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String getLastDayOfMonth(String date) {
		Date date_begin = DateUtil.parse(date + "-01");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date_begin);
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date lastDate = calendar.getTime();
		lastDate.setDate(lastDay);
		return DateUtil.formatDate(lastDate);
	}
	@Override
	public List<String> getModel() {
		return null;
	}

	public RepDaily getRepd() {
		return repd;
	}

	public void setRepd(RepDaily repd) {
		this.repd = repd;
	}

	public RepDailyVo getRdvo() {
		return rdvo;
	}

	public void setRdvo(RepDailyVo rdvo) {
		this.rdvo = rdvo;
	}

	public List<Dict> getListDict() {
		return listDict;
	}

	public void setListDict(List<Dict> listDict) {
		this.listDict = listDict;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
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

	public Object[] getCount() {
		return count;
	}

	public void setCount(Object[] count) {
		this.count = count;
	}

}