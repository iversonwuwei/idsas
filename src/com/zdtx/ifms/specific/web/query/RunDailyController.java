package com.zdtx.ifms.specific.web.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.query.RunDaily;
import com.zdtx.ifms.specific.service.query.RunDailyManager;
import com.zdtx.ifms.specific.vo.query.RunDailyVo;

/**
 * @ClassName: DetailsController
 * @Description: 车辆运行报告-车辆运行日报、月报、汇总报告-控制层
 * @author JHQ
 * @date 2012年12月4日 10:37:19
 * @version V1.0
 */

public class RunDailyController extends ReportBase<RunDaily> {
	private static final long serialVersionUID = 2495462420702295722L;

	@Autowired
	private RunDailyManager rdMgr;

	private String kind;
	private RunDaily rd = new RunDaily();
	private RunDailyVo rdvo = new RunDailyVo();
	private List<Object> count = new ArrayList<Object>();
	private List<KeyAndValue> btlist = new ArrayList<KeyAndValue>();
	private String date_star;
	private String date_end;
	private static String today = DateUtil.formatDate(new Date());

	@Override
	public String index() {
		try {
			Utils.getSession().setAttribute("rdvo", rdvo);
			if (null == rdvo.getDaytype()) {
				rdvo.setDaytype(1L);
				rdvo.setDate1(DateUtil.formatDate(new Date()));
			}
			page = rdMgr.getAll(page, rdvo, kind);
			count = rdMgr.get_count();
			Utils.getSession().setAttribute("_count", count);
			if (-1L != rdvo.getDaytype()) {
				date_star = rdMgr.get_date().getKey();
				date_end = rdMgr.get_date().getValue();
				Utils.getSession().setAttribute("date_star", date_star);
				Utils.getSession().setAttribute("date_end", date_end);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}

	public String exportDetail() {
		xlsFileName = disposeXlsName("Vehicle Run Query Export" + DateUtil.formatDate(new Date()));
		RunDailyVo _vo = (RunDailyVo) Utils.getSession().getAttribute("rdvo");
		xlsStream = rdMgr.createExcel(_vo, kind);  
		return "xls";
	}

	@Override
	public RunDaily getModel() {
		return null;
	}

	public List<KeyAndValue> getBtlist() {
		return btlist;
	}

	public void setBtlist(List<KeyAndValue> btlist) {
		this.btlist = btlist;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public RunDailyVo getRdvo() {
		return rdvo;
	}

	public void setRdvo(RunDailyVo rdvo) {
		this.rdvo = rdvo;
	}

	public RunDaily getRd() {
		return rd;
	}

	public void setRd(RunDaily rd) {
		this.rd = rd;
	}

	public List<Object> getCount() {
		return count;
	}

	public void setCount(List<Object> count) {
		this.count = count;
	}

	public String getDate_star() {
		return date_star;
	}

	public void setDate_star(String date_star) {
		this.date_star = date_star;
	}

	public String getDate_end() {
		return date_end;
	}

	public void setDate_end(String date_end) {
		this.date_end = date_end;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		RunDailyController.today = today;
	}

}