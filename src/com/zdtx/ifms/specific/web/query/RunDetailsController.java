package com.zdtx.ifms.specific.web.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.query.RunDetails;
import com.zdtx.ifms.specific.service.query.RunDetailsManger;
import com.zdtx.ifms.specific.vo.query.RunDetailsVo;

/**
 * Data Query-Run Details
 * 
 * @author Lonn
 * @since 2013-04-03
 */
public class RunDetailsController extends ReportBase<RunDetails> {

	/**
	 * 
	 */
	private String kind="";
	private static final long serialVersionUID = 5039028737170576908L;
	private RunDetails rds = new RunDetails();
	private RunDetailsVo rdsVo = new RunDetailsVo();
	private List<KeyAndValue> btlist = new ArrayList<KeyAndValue>();
	@Autowired
	private RunDetailsManger rdMgr;
	private List<Object[]> count = new ArrayList<Object[]>();

	
	public String index() {
		try {
			if("1".equals(kind)){
				rdsVo.setRiqi(DateUtil.formatDate(new Date()));
			}
			page = rdMgr.getBetch(page, rdsVo);
			count = rdMgr.getTotals();
			Utils.getSession().setAttribute("_count", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.index();
	}

	public String exportDetail() {
		String title = "Vehicle Run List";
		xlsFileName = disposeXlsName(title + DateUtil.formatDate(new Date()));
		try {
			xlsStream = rdMgr.getCNGALL(rdsVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}

	@Override
	public RunDetails getModel() {
		return null;
	}

	public RunDetails getRds() {
		return rds;
	}

	public void setRds(RunDetails rds) {
		this.rds = rds;
	}

	public RunDetailsVo getRdsVo() {
		return rdsVo;
	}

	public void setRdsVo(RunDetailsVo rdsVo) {
		this.rdsVo = rdsVo;
	}

	public List<KeyAndValue> getBtlist() {
		return btlist;
	}

	public void setBtlist(List<KeyAndValue> btlist) {
		this.btlist = btlist;
	}

	public List<Object[]> getCount() {
		return count;
	}

	public void setCount(List<Object[]> count) {
		this.count = count;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}
 
}