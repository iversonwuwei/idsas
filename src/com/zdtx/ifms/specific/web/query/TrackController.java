package com.zdtx.ifms.specific.web.query;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.query.Track;
import com.zdtx.ifms.specific.service.query.TrackManager;
import com.zdtx.ifms.specific.vo.query.TrackVO;

public class TrackController extends ReportBase<Track> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2313317573767606327L;
	@Autowired
	private TrackManager trackMgr;
	private TrackVO trvo=new TrackVO();
	public String index(){
		trackMgr.getPage(page,trvo);
		return "index";
	}
	
	@Override
	public Track getModel() {
		return null;
	}
	
	public String exportDetail(){
		try {
			String title  = "Tracking Report";
			xlsFileName = disposeXlsName(title );
			xlsStream = trackMgr.getData();
    	} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}

	public TrackVO getTrvo() {
		return trvo;
	}

	public void setTrvo(TrackVO trvo) {
		this.trvo = trvo;
	}
	
}
