package com.zdtx.ifms.specific.web.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.system.Score;
import com.zdtx.ifms.specific.service.system.ScoreManager;
import com.zdtx.ifms.specific.service.system.VehicleTypeManager;
import com.zdtx.ifms.specific.vo.system.ScoreVO;

/***
 * 
 * @author zy
 *
 */
public class ScoreController extends URLSupport<Score>{

	private static final long serialVersionUID = -4769175896990420202L;

	@Autowired
	private ScoreManager scoreMgr;
	@Autowired
	private VehicleTypeManager vehtypMgr;
	
	
	private Score sco = new Score();
	private ScoreVO scoVo = new ScoreVO();
	private List<KeyAndValue> vehicleTypeList = new ArrayList<KeyAndValue>();
	private List<KeyAndValue> sortList = new ArrayList<KeyAndValue>();
	private List<Score> scoreList = new ArrayList<Score>();

	public String index() {
		vehicleTypeList = vehtypMgr.getVehicleTypeList(getCurrentUser());
		scoreList = scoreMgr.getScoreList(scoVo,vehicleTypeList);
		return "index";
	}
	
	public String save() throws IOException {
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
			int count = 0;
			/*
			 * 先用循环把权重总数算出来，然后在判断，如果总数大于100或者小于100不让保存，等于100才可以保存
			 */
			for (int i = 0; i < scoreList.size(); i++) {
				Score sco = baseMgr.get(Score.class, scoreList.get(i).getScoreid());
				sco.setWeight(scoreList.get(i).getWeight());
				if (null != sco.getWeight()) {
					count += sco.getWeight();
				}
			}
			if (100 == count) {
				scoreMgr.saveSco(scoreList,getCurrentUser(),true); //权重总数等于100,传true
				out.print(Constants.SUCCESS[0]+"score!index?editble=1&scoVo.vehId="+ scoVo.getVehId() + Constants.SUCCESS[1]);
			}
			if (100 < count) {
				scoreMgr.saveSco(scoreList,getCurrentUser(),false); //权重总数大于100,传false
				out.print("<script type='text/JavaScript'>alert('Aggregate weights of each Vehicle Type can not be greater than 100!');location='score!index?editble=1&scoVo.vehId="+ scoVo.getVehId() +"';</script>");
			}
			if (100 > count) {
				scoreMgr.saveSco(scoreList,getCurrentUser(),false); //权重总数小于100,传false
				out.print("<script type='text/JavaScript'>alert('Aggregate weights of each Vehicle Type can not be less than 100!');location='score!index?editble=1&scoVo.vehId="+ scoVo.getVehId() +"'</script>");
			}
		} catch (Exception e) {
			out.print(Constants.ERROR);
			e.printStackTrace();
		}
		return null;
	}
	
	public String edit() {
		sco = baseMgr.get(Score.class, id);
		return "edit";
	}
	
	public String checkTypeAndSort() throws IOException {
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
			String sortId = Struts2Util.getParameter("sortid");
			String vehTypeId = Struts2Util.getParameter("vehTypeId");
			Boolean istrue = scoreMgr.checkTypeAndSort(sortId, vehTypeId);
			if (istrue) {
				out.print("Y");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}
	
	@Override
	public Score getModel() {
		return null;
	}

	public Score getSco() {
		return sco;
	}

	public void setSco(Score sco) {
		this.sco = sco;
	}

	public List<KeyAndValue> getVehicleTypeList() {
		return vehicleTypeList;
	}

	public void setVehicleTypeList(List<KeyAndValue> vehicleTypeList) {
		this.vehicleTypeList = vehicleTypeList;
	}

	public List<KeyAndValue> getSortList() {
		return sortList;
	}

	public void setSortList(List<KeyAndValue> sortList) {
		this.sortList = sortList;
	}

	public List<Score> getScoreList() {
		return scoreList;
	}

	public void setScoreList(List<Score> scoreList) {
		this.scoreList = scoreList;
	}

	public ScoreVO getScoVo() {
		return scoVo;
	}

	public void setScoVo(ScoreVO scoVo) {
		this.scoVo = scoVo;
	}


}
