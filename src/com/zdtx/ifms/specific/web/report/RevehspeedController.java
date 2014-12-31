package com.zdtx.ifms.specific.web.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;



import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.URLSupport;
import com.zdtx.ifms.specific.model.query.Details;
import com.zdtx.ifms.specific.model.vehicle.Vehcile;
import com.zdtx.ifms.specific.service.report.RevehSpeedManager;
import com.zdtx.ifms.specific.vo.query.DetailsVo;

public class RevehspeedController extends URLSupport<Object> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3513601802612674686L;
	private DetailsVo dvo = new DetailsVo();
	private List<KeyAndValue> btlist = new ArrayList<KeyAndValue>();
	private String tim="";
	@Autowired
	private RevehSpeedManager rsMgr;
	
	private List<String> dates = new ArrayList<String>();
	
	private List<Details> dets = new ArrayList<Details>();
	
	@Override
	public String index() {
		if(dvo.getVehId()!=null){
			dvo.setVehcode(baseMgr.get(Vehcile.class,dvo.getVehId()).getVehiclename());
			if(dvo.getRiqi()==null||"".equals(dvo.getRiqi())){
				dvo.setRiqi(DateUtil.formatDate(new Date()));
			}
			if(dvo.getTimeBegin()==null||"".equals(dvo.getTimeBegin())){
				dvo.setTimeBegin("00:00");
			}
			if(dvo.getTimeEnd()==null||"".equals(dvo.getTimeEnd())){
				dvo.setTimeEnd("23:59");
			}
			List<List<String>> list= rsMgr.getAll(dvo);
			Utils.getSession().setAttribute("dvo", dvo);
			dets = rsMgr.getDets(dvo);
			for(int i =0;i<list.size();i++){
				Object o1 = list.get(i);
				Object[] os = (Object[])o1;
				int time1 = hmsToS((os[0]+"").substring(11, 19));
				boolean  b = true;
				if(dets.size()>0){
					
					Details d = dets.get(0);
					int time2 = hmsToS(d.getTimeEnd());
					if(time2<=time1){
						if(d.getBehType()==11L){
							dates.add("{\"otime\":\"" + hmsToS((os[0]+"").substring(11, 19)) + "\", \"speed\":\"" + os[1] + "\", \"memo\":\"a\"}");
						}
						if(d.getBehType()==12L){
							dates.add("{\"otime\":\"" + hmsToS((os[0]+"").substring(11, 19)) + "\", \"speed\":\"" + os[1] + "\", \"memo\":\"b\"}");
						}
						if(d.getBehType()==19L){
							dates.add("{\"otime\":\"" + hmsToS((os[0]+"").substring(11, 19)) + "\", \"speed\":\"" + os[1] + "\", \"memo\":\"e\"}");
						}
						if(d.getBehType()==13L){
							dates.add("{\"otime\":\"" + hmsToS((os[0]+"").substring(11, 19)) + "\", \"speed\":\"" + os[1] + "\", \"memo\":\"l\"}");
						}
						if(d.getBehType()==14L){
							dates.add("{\"otime\":\"" + hmsToS((os[0]+"").substring(11, 19)) + "\", \"speed\":\"" + os[1] + "\", \"memo\":\"r\"}");
						}
						if(d.getBehType()==15L){
							dates.add("{\"otime\":\"" + hmsToS((os[0]+"").substring(11, 19)) + "\", \"speed\":\"" + os[1] + "\", \"memo\":\"s\"}");
						}
						b=false;
						dets.remove(0);
					}
				}
				if(b){
					dates.add("{\"otime\":\"" + hmsToS((os[0]+"").substring(11, 19)) + "\", \"speed\":\"" + os[1] + "\", \"memo\":\"n\"}");
				}
			}
		}
		return "index";
	}

	public String clickp(){
		dvo = (DetailsVo)Utils.getSession()
				.getAttribute("dvo");
		dvo.setTimeBegin(chdvo(tim)[0]);
		dvo.setTimeEnd(chdvo(tim)[1]);
		List<List<String>> list= rsMgr.getAll(dvo);
		dets = rsMgr.getDets(dvo);
		for(int i =0;i<list.size();i++){
			Object o1 = list.get(i);
			Object[] os = (Object[])o1;
			int time1 = hmsToS((os[0]+"").substring(11, 19));
			boolean  b = true;
			if(dets.size()>0){
				
				Details d = dets.get(0);
				int time2 = hmsToS(d.getTimeEnd());
				if(time2<=time1){
					if(d.getBehType()==11L){
						dates.add("{\"otime\":\"" + hmsToS((os[0]+"").substring(11, 19)) + "\", \"speed\":\"" + os[1] + "\", \"memo\":\"a\"}");
					}
					if(d.getBehType()==12L){
						dates.add("{\"otime\":\"" + hmsToS((os[0]+"").substring(11, 19)) + "\", \"speed\":\"" + os[1] + "\", \"memo\":\"b\"}");
					}
					if(d.getBehType()==19L){
						dates.add("{\"otime\":\"" + hmsToS((os[0]+"").substring(11, 19)) + "\", \"speed\":\"" + os[1] + "\", \"memo\":\"e\"}");
					}
					if(d.getBehType()==13L){
						dates.add("{\"otime\":\"" + hmsToS((os[0]+"").substring(11, 19)) + "\", \"speed\":\"" + os[1] + "\", \"memo\":\"l\"}");
					}
					if(d.getBehType()==14L){
						dates.add("{\"otime\":\"" + hmsToS((os[0]+"").substring(11, 19)) + "\", \"speed\":\"" + os[1] + "\", \"memo\":\"r\"}");
					}
					b=false;
					dets.remove(0);
				}
			}
			if(b){
				dates.add("{\"otime\":\"" + hmsToS((os[0]+"").substring(11, 19)) + "\", \"speed\":\"" + os[1] + "\", \"memo\":\"n\"}");
			}
		}
		return "index";
	}
	public String ch(int i){
		if(i<10){
			return "0"+i;
		}else{
			return ""+i;
		}
	}
	public String[] chdvo(String str){
		String[] strs = new String[2];
		String[] inits = str.split(":");
		int sec = Integer.parseInt(inits[2]);
		int min = Integer.parseInt(inits[1]);
		int hour = Integer.parseInt(inits[0]);
		if(hour>0&&min>0){
			if(sec>=20&&sec<=40){
				strs[0]=inits[0]+":"+inits[1];
				if(min==59){
					strs[1]=ch(hour+1)+":"+"00";
				}else{
					strs[1]=inits[0]+":"+ch(min+1);
				}
			}
			if(sec<20){
				if(min==0){
					strs[0]=ch(hour-1)+":"+"59";
					strs[1]=inits[0]+":"+"01";
				}else if(min==59){
					strs[0]=inits[0]+":"+"58";
					strs[1]=ch(hour+1)+":"+"00";
				}else{
					strs[0]=inits[0]+":"+ch(min-1);
					strs[1]=inits[0]+":"+ch(min+1);
				}
			}
			if(sec>40){
				if(min==59){
					strs[0]=inits[0]+":"+"59";
					strs[1]=ch(hour+1)+":"+"01";
				}else{
					strs[0]=inits[0]+":"+inits[1];
					strs[1]=inits[0]+":"+ch(min+2);
				}
			}
		}else{
			strs[0]="00:00";
			if(sec>40){
				strs[1]="00:02";
			}else{
				strs[1]="00:01";
			}
		}
		return strs;
	}
	public int hmsToS(String hms){
		String[] ss = hms.split(":");
		int i = Integer.parseInt(ss[0])*3600+Integer.parseInt(ss[1])*60+Integer.parseInt(ss[2]);
		return i;
	}
	@Override
	public List<String> getModel() {
		return null;
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

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	public List<Details> getDets() {
		return dets;
	}

	public void setDets(List<Details> dets) {
		this.dets = dets;
	}

	public String getTim() {
		return tim;
	}

	public void setTim(String tim) {
		this.tim = tim;
	}
  
}