package com.zdtx.ifms.specific.web.analy;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.SocketUtil;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.analy.Phenomenon;
import com.zdtx.ifms.specific.service.analy.PhenomenonManager;
import com.zdtx.ifms.specific.vo.analy.PhenomenonVO;

public class PhenomenonController extends URLSupport<Phenomenon>{

	private static final long serialVersionUID = -7959476375453117843L;
	
	@Autowired
	private PhenomenonManager pheMgr;
	
	private PhenomenonVO pheVo = new PhenomenonVO();
	private Phenomenon phe = new Phenomenon();
	private String vehiclename;
	
	public String index() {
		page = pheMgr.getPage(page, pheVo);
		return "index";
	}
	
	public String show() {
		phe = baseMgr.get(Phenomenon.class, id);
		return "show";
	}
	
	public String delete() {
		phe = baseMgr.get(Phenomenon.class, id);
		phe.setIsdelete("T");
		baseMgr.save(phe);
		return this.index();
	}
	
	public String cancelCode() throws IOException {
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
			String faultCode = SocketUtil.getFaultCodeStr(vehiclename, "001430340D");
			if (SocketUtil.sendSocket(faultCode)) {
				phe = baseMgr.get(Phenomenon.class, id);
				phe.setIsremove("Y");
				phe.setRemovetime(DateUtil.formatLongTimeDate(new Date()));
				phe.setRemover(getCurrentUser().getUserName());
				baseMgr.save(phe);
				out.print("<script language='JavaScript'>alert('Remove successfully!');location='index?highlight="
						+ phe.getFaultid() + "'</script>");
			} else {
				out.print("<script language='JavaScript'>alert('Remove failed!');location='index'</script>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Phenomenon getModel() {
		return null;
	}

	public PhenomenonVO getPheVo() {
		return pheVo;
	}

	public void setPheVo(PhenomenonVO pheVo) {
		this.pheVo = pheVo;
	}

	public Phenomenon getPhe() {
		return phe;
	}

	public void setPhe(Phenomenon phe) {
		this.phe = phe;
	}

	public String getVehiclename() {
		return vehiclename;
	}

	public void setVehiclename(String vehiclename) {
		this.vehiclename = vehiclename;
	}

}
