/**
 * @File: IpCamController.java
 * @path: idsas - com.zdtx.ifms.specific.web.monitor
 */
package com.zdtx.ifms.specific.web.system;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.system.CamModel;
import com.zdtx.ifms.specific.service.system.CamModelManager;

/**
 * @ClassName: CamModelController
 * @Description: system-IP Camera Model-controller
 * @author: Leon Liu
 * @date: 2013-7-24 下午2:35:33
 * @version V1.0
 */
public class CamModelController extends URLSupport<CamModel>{

	private static final long serialVersionUID = -4863856110702807933L;

	@Autowired
	private CamModelManager manager;
	
	private CamModel camModel = new CamModel();
	private String modelNameStr;	//for query

	public String index() {
		page = manager.getBatch(page, modelNameStr);
		return "index";
	}
	
	public String edit() {
		camModel = baseMgr.get(CamModel.class, id);
		return "edit";
	}
	public String show() {
		camModel = baseMgr.get(CamModel.class, id);
		return "show";
	}
	
	public String create() {
		try {
			camModel.setCreater(getCurrentUser().getUserName());
			camModel.setCreateTime(DateUtil.formatLongTimeDate(new Date()));
			camModel.setIsDelete("F");
			baseMgr.save(camModel);
			Struts2Util.printSuccess("system/cam-model", camModel.getModelID());
		} catch (Exception e) {
			Struts2Util.printError(e);
		}
		return null;
	}
	
	public String destory() {
		if(manager.checkDelete(id)) {	//check if the record has been associated to the camera
			camModel = baseMgr.get(CamModel.class, id);
			camModel.setCreater(getCurrentUser().getUserName());
			camModel.setCreateTime(DateUtil.formatLongTimeDate(new Date()));
			camModel.setIsDelete("T");
			baseMgr.save(camModel);
			return this.index();
		} else {
			Struts2Util.sendMsg("This record has been associated to the camera!");
			return null;
		}
	}
	
	public String isDuplicate() {
		Long model_id = null;
		if(!Utils.isEmpty(Struts2Util.getParameter("model_id"))) {
			model_id = Long.valueOf(Struts2Util.getParameter("model_id"));
		}
		if(manager.isDuplicate(model_id, Struts2Util.getParameter("model_name"))) {
			jsonObject.put("result", "DUP");
		} else {
			jsonObject.put("result", "SIN");
		}
		return "jsonObject";
	}
	
	@Override
	public CamModel getModel() {
		return this.camModel;
	}

	public String getModelNameStr() {
		return modelNameStr;
	}

	public void setModelNameStr(String modelNameStr) {
		this.modelNameStr = modelNameStr;
	}
}