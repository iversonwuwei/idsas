package com.zdtx.ifms.specific.web.system;

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
import com.zdtx.ifms.specific.model.system.VehBrand;
import com.zdtx.ifms.specific.service.system.VehicleBrandManager;

/**
 * 车辆类型
 * 
 * @author LiuGuilong
 * @since 2012-04-26
 */
public class VehicleBrandController extends URLSupport<VehBrand> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5935088523528522793L;
	@Autowired
	private VehicleBrandManager typeMgr;
	private VehBrand vehbrand;
	
	 private List<KeyAndValue> coms=new ArrayList<KeyAndValue>();
	public String index() {
		page = typeMgr.getBatch(page,Utils.keysToArray(baseMgr.getComByAuthority(getCurrentUser().getUserID())));
		return "index";
	}

	public String add() {
		coms=baseMgr.getComByAuthority(getCurrentUser().getUserID());
		return "add";
	}

	public String edit() {
		coms=baseMgr.getComByAuthority(getCurrentUser().getUserID());
		vehbrand = baseMgr.get(VehBrand.class, id);
		return "add";
	}
	public String show() {
		coms=baseMgr.getComByAuthority(getCurrentUser().getUserID());
		vehbrand = baseMgr.get(VehBrand.class, id);
		return "show";
	}
	

	public String save() {
		try {
			
			if(vehbrand.getComid()!=null){
				Org o=baseMgr.get(Org.class, vehbrand.getComid());
				if(o!=null&&o.getOrgName()!=null){
					vehbrand.setComname(o.getOrgName());
				}
			}
			vehbrand.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			vehbrand.setCreater(getCurrentUser().getUserName());
			baseMgr.save(vehbrand);
			highlight = vehbrand.getId();
			// 删除旧的图片，在上传的时候不能删除，所以在这里做删除了
//			typeMgr.deleteOldImages(IMAGE_PATH);
			Utils.renderHtml(Constants.SUCCESS[0] + Utils.getBasePath() + "system/vehicle-brand?editble=1&highlight=" + highlight + Constants.SUCCESS[1]);
		} catch (Exception e) {
			e.printStackTrace();
			Utils.renderHtml(Constants.ERROR);
		}
		return null;
	}

	public String check() {
		String name = Utils.getParameter("type");
		Utils.renderText(typeMgr.checkRepeat(name,id));
		return null;
	}

	public String delete() {
		vehbrand = baseMgr.get(VehBrand.class, id);
		vehbrand.setIsdelete("T");
		baseMgr.save(vehbrand);
		return index();
	}




	/**
	 * 看这个车辆类型在不在用着
	 * 
	 * @return
	 */
	public String checkUsed() {
		Utils.renderText(typeMgr.checkTypeUsed(id).toString());
		return null;
	}



	public VehBrand getVehbrand() {
		return vehbrand;
	}

	public void setVehbrand(VehBrand vehbrand) {
		this.vehbrand = vehbrand;
	}

	public List<KeyAndValue> getComs() {
		return coms;
	}

	public void setComs(List<KeyAndValue> coms) {
		this.coms = coms;
	}

	@Override
	public VehBrand getModel() {
		return null;
	}

}
