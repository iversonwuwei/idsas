package com.zdtx.ifms.specific.web.profile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.profile.Counsell;
import com.zdtx.ifms.specific.model.profile.CounsellView;
import com.zdtx.ifms.specific.service.profile.CounsellManager;
import com.zdtx.ifms.specific.service.vehicle.DriverListManager;
import com.zdtx.ifms.specific.vo.profile.CounsellVo;

public class MycounsellController extends ReportBase<CounsellView> {

	private static final long serialVersionUID = -5353801042915202249L;

	@Autowired
	private CounsellManager manager;
	@Autowired
	private DriverListManager driverListManager;

	private List<KeyAndValue> driList = new ArrayList<KeyAndValue>();
	private Counsell cs = new Counsell();
	private CounsellVo csVo = new CounsellVo();

	public String index() {
		csVo.setOid(getCurrentUser().getUserID());
		page = manager.getBatch(page, csVo);
		return "index";
	}

	public String edit() {
		driList = driverListManager.getVehicleArrayList();
		cs = baseMgr.get(Counsell.class, id);
		return "edit";
	}

	public String add() {
		driList = driverListManager.getVehicleArrayList();
		cs.setUsername(getCurrentUser().getUserName());
		cs.setUserid(getCurrentUser().getUserID());
		return "add";
	}

	public String create() throws IOException {
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
			cs.setCreater(getCurrentUser().getUserName());
			cs.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			cs.setIsdelete("F");
			if (!Utils.isEmpty(cs.getDriverid())) { // 数据有司机
				KeyAndValue departInfo = manager.getDepartmentByDriver(cs
						.getDriverid());
				if (departInfo != null) {
					cs.setDepartid(Long.valueOf(departInfo.getKey()));
					cs.setDepartname(departInfo.getValue());
				}
			}
			baseMgr.save(cs);
			out.print(Constants.SUCCESS[0] + "mycounsell!index?highlight="
					+ cs.getCounseling() + "'</script>");
		} catch (Exception e) {
			out.print(Constants.ERROR);
			e.printStackTrace();
		} finally {
			out.close();
		}
		return this.index();
	}

	public String delete() {
		cs = baseMgr.get(Counsell.class, id);
		cs.setIsdelete("T");
		cs.setCreater(getCurrentUser().getUserName());
		cs.setCreatime(DateUtil.formatLongTimeDate(new Date()));
		baseMgr.save(cs);
		return this.index();
	}

	@Override
	public CounsellView getModel() {
		return null;
	}

	public Counsell getCs() {
		return cs;
	}

	public void setCs(Counsell cs) {
		this.cs = cs;
	}

	public List<KeyAndValue> getDriList() {
		return driList;
	}

	public void setDriList(List<KeyAndValue> driList) {
		this.driList = driList;
	}

	public CounsellVo getCsVo() {
		return csVo;
	}

	public void setCsVo(CounsellVo csVo) {
		this.csVo = csVo;
	}
}