package com.zdtx.ifms.specific.web.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.model.system.PoiConf;
import com.zdtx.ifms.specific.service.system.PoiConfManager;
import com.zdtx.ifms.specific.vo.system.PoiConfVO;

/**
 * @description System Settings-POI-G-controller
 * @author Liu Jun
 * @since 2014-7-16 09:51:35
 */
public class GooglePoiConfController extends URLSupport<PoiConf> {
	
	private static final long serialVersionUID = -3470083754297944700L;
	
	@Autowired
	private PoiConfManager manager;
	
	private PoiConf poi = new PoiConf();
	private PoiConfVO vo = new PoiConfVO();
	private List<PoiConf> poiList = new ArrayList<PoiConf>();
	private List<KeyAndValue> coms = new ArrayList<KeyAndValue>();
    
	public String index() {
		try {
			page = manager.getBatch(page, vo, Utils.keysToArray(baseMgr.getComByAuthority(getCurrentUser().getUserID())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}

	public String add() {
		coms = baseMgr.getComByAuthority(getCurrentUser().getUserID());
		poiList = manager.getAll(true, "T");
		return "edit";
	}

	public String edit() {
		coms = baseMgr.getComByAuthority(getCurrentUser().getUserID());
		poiList = manager.getAll(true, "T", id);
		poi = baseMgr.get(PoiConf.class, id);
		return "edit";
	}

	public String show() {
		coms = baseMgr.getComByAuthority(getCurrentUser().getUserID());
		poiList = manager.getAll(true, "T", id);
		poi = baseMgr.get(PoiConf.class, id);
		return "show";
	}

	public String save() throws IOException {
		try {
			if (poi.getComid() != null) {
				Org o = baseMgr.get(Org.class, poi.getComid());
				if (o != null && o.getOrgName() != null) {
					poi.setComname(o.getOrgName());
				}
			}
			poi.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			poi.setCreater(getCurrentUser().getUserName());
			poi.setIsdelete("F");
			if (poi.getIsvisible() == null || "".equals(poi.getIsvisible())) {
				poi.setIsvisible("F");
			}
			baseMgr.save(poi);
			Utils.renderHtml(Constants.SUCCESS[0] + Utils.getBasePath() + "system/google-poi-conf?editble=1&highlight=" + poi.getPoiid() + Constants.SUCCESS[1]);
		} catch (Exception e) {
			Utils.renderText(Constants.ERROR);
			e.printStackTrace();
		}
		return null;
	}

	public String checkDuplicate() {
		if(manager.isDuplicate(vo.getComID(), vo.getPoiID(), vo.getCaption())) {
			jsonObject.put("result", "duplicate");
		} else {
			jsonObject.put("result", "pass");
		}
		return "jsonObject";
	}

	public String delete() {
		poi = baseMgr.get(PoiConf.class, id);
		poi.setIsdelete("T");
		baseMgr.save(poi);
		return index();
	}

	@Override
	public PoiConf getModel() {
		return this.poi;
	}

	public PoiConfVO getVo() {
		return vo;
	}

	public void setVo(PoiConfVO vo) {
		this.vo = vo;
	}

	public List<PoiConf> getPoiList() {
		return poiList;
	}

	public void setPoiList(List<PoiConf> poiList) {
		this.poiList = poiList;
	}

	public List<KeyAndValue> getComs() {
		return coms;
	}

	public void setComs(List<KeyAndValue> coms) {
		this.coms = coms;
	}
}