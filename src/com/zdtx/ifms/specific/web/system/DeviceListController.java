package com.zdtx.ifms.specific.web.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.Constants;
import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.utils.SocketUtil;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.model.monitor.CamDevice;
import com.zdtx.ifms.specific.model.monitor.Camera;
import com.zdtx.ifms.specific.model.monitor.Camera_;
import com.zdtx.ifms.specific.model.system.DeviceList;
import com.zdtx.ifms.specific.model.system.DeviceListLog;
import com.zdtx.ifms.specific.model.vehicle.Vehcile;
import com.zdtx.ifms.specific.service.system.DeviceListManager;
import com.zdtx.ifms.specific.service.system.DeviceStatusManager;
import com.zdtx.ifms.specific.vo.system.DeviceVo;

@SuppressWarnings("rawtypes")
public class DeviceListController extends ReportBase {

	private static final long serialVersionUID = 5848896218199836224L;
	
	@Autowired
	private DeviceListManager dlMgr;
	@Autowired
	private DeviceStatusManager dsMgr;
	
	private DeviceList device = new DeviceList();
	private DeviceVo deVo = new DeviceVo();
	
	// private List<KeyAndValue> searchList = new ArrayList<KeyAndValue>();
	private List<KeyAndValue> countList = new ArrayList<KeyAndValue>();
	private List<KeyAndValue> channelList = new ArrayList<KeyAndValue>();
	private Camera_[] cameras = new Camera_[16];
	private String[] cid = new String[16];
	private Long videoid1;
	private Long videoid2;
	private Long videoid3;
	private Long videoid4;
	private Long videoid5;
	private Long videoid6;
	private Long videoid7;
	private Long videoid8;
	private String videoname = "";
	private Long boxids[];
	private List<KeyAndValue> depts = new ArrayList<KeyAndValue>();

	@SuppressWarnings("unchecked")
	public String index() {
		depts = baseMgr.getDepartByAuthority(getCurrentUser().getUserID());
		// searchList = dsMgr.getList();
		page = dlMgr.getBatch(page, deVo, Utils.keysToArray(depts));
		return "index";
	}

	/***
	 * 导出
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exportDetail() {
		try {
			String title = "Device List";
			xlsFileName = disposeXlsName(title
					+ DateUtil.formatDate(new Date()));
			xlsStream = dlMgr.getExcel(title + DateUtil.formatDate(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}

	public String edit() {
		depts = baseMgr.getDepartByAuthority(getCurrentUser().getUserID());
		// searchList = dsMgr.getList();
		device = baseMgr.get(DeviceList.class, id);
		channelList = dlMgr.getChannelList(id, device.getDeptid());
		if (device.getO_unittype().equals("1")) {		//BNT5000
			List<CamDevice> camList = dlMgr.getChannel(id);
			if (camList.size() > 0) {
				for (int i = 0; i < camList.size(); i++) {
					CamDevice cd = camList.get(i);
					if (cd.getChannel() != null) {
						if (cd.getChannel() == 1) {
							videoid1 = cd.getCamID();
						} else if (cd.getChannel() == 2) {
							videoid2 = cd.getCamID();
						} else if (cd.getChannel() == 3) {
							videoid3 = cd.getCamID();
						} else if (cd.getChannel() == 4) {
							videoid4 = cd.getCamID();
						} else if (cd.getChannel() == 5) {
							videoid5 = cd.getCamID();
						} else if (cd.getChannel() == 6) {
							videoid6 = cd.getCamID();
						} else if (cd.getChannel() == 7) {
							videoid7 = cd.getCamID();
						} else if (cd.getChannel() == 8) {
							videoid8 = cd.getCamID();
						}
					}
				}
			}
		}
		return "edit";
	}

	public String add() {
		depts = baseMgr.getDepartByAuthority(getCurrentUser().getUserID());
		// searchList = dsMgr.getList();
		// channelList = dlMgr.getChannelList();
		return "add";
	}

	public String getcambydept() {
		List<KeyAndValue> cams = new ArrayList<KeyAndValue>();
		if (device.getO_deviceno() == null) {
			cams = dlMgr.getChannelList(id);
		} else {
			cams = dlMgr.getChannelList(device.getO_deviceno(), id);
		}
		Struts2Util.renderJson(JSONArray.fromObject(cams).toString());
		return null;
	}

	public String show() {
		// searchList = dsMgr.getList();
		// channelList = dlMgr.getChannelList(id);
		device = baseMgr.get(DeviceList.class, id);
		if (device.getO_unittype().equals("1")) {	//BNT5000
			List<CamDevice> camList = dlMgr.getChannel(id);
			if (camList.size() > 0) {
				for (int i = 0; i < camList.size(); i++) {
					CamDevice camDevice = camList.get(i);
					if (camDevice.getChannel() != null) {
						videoname += baseMgr.get(Camera.class, camDevice.getCamID()).getCameraName() + ";";
					}
				}
			}
		} else if (device.getO_unittype().equals("3")) {	//Police
			List<CamDevice> camList = dlMgr.getChannel(id);
			if (camList.size() > 0) {
				CamDevice camDevice = camList.get(0);
				videoname = baseMgr.get(Camera.class, camDevice.getCamID()).getCameraName();
			}
		}
		return "show";
	}

	public String create() throws IOException {
		PrintWriter out = Struts2Util.getResponse().getWriter();
		try {
			String username = getCurrentUser().getUserName();
			String now = DateUtil.formatLongTimeDate(new Date());
			if (device.getO_deviceno() != null) {	//edit
				DeviceList _device = baseMgr.get(DeviceList.class, device.getO_deviceno());
				DeviceListLog deviceLog = new DeviceListLog("0", _device, username, now);
				baseMgr.save(deviceLog);
			}
			device.setO_xiugairen(username);
			device.setO_xiugaishijian(now);
			if (device.getO_unittype().equals("1")) {	//BNT5000, 手动设置摄像头
				device.setO_channelcount(0);
				this.setChannelcount(videoid1, device);
				this.setChannelcount(videoid2, device);
				this.setChannelcount(videoid3, device);
				this.setChannelcount(videoid4, device);
				this.setChannelcount(videoid5, device);
				this.setChannelcount(videoid6, device);
				this.setChannelcount(videoid7, device);
				this.setChannelcount(videoid8, device);
			} else if (device.getO_unittype().equals("2")) { // A5设备通道数为8
				device.setO_channelcount(8);
				device.setO_videono(null);
				device.setO_videoname(null);
			} else if (device.getO_unittype().equals("3")) { // 单兵设备通道数为1
				device.setO_channelcount(1);
				device.setO_videono(null);
				device.setO_videoname(null);
			}
			if (device.getDeptid() == null) {
				device.setDeptname(null);
			} else {
				device.setDeptname(baseMgr.get(Org.class, device.getDeptid()).getOrgName());
			}
			baseMgr.save(device);
			if (device.getO_unittype().equals("1")) {	//BNT5000, 手动设置摄像头
				dlMgr.delChannel(device.getO_deviceno());
				this.saveCamDevice(device, videoid1, 1l);
				this.saveCamDevice(device, videoid2, 2l);
				this.saveCamDevice(device, videoid3, 3l);
				this.saveCamDevice(device, videoid4, 4l);
				this.saveCamDevice(device, videoid5, 5l);
				this.saveCamDevice(device, videoid6, 6l);
				this.saveCamDevice(device, videoid7, 7l);
				this.saveCamDevice(device, videoid8, 8l);
				if (dlMgr.getVeh(device.getO_deviceno()) != null) {
					Vehcile veh = dlMgr.getVeh(device.getO_deviceno());	//设置车辆IP为设备IP
					veh.setCctvip(device.getO_loginhost());
					baseMgr.save(veh);
				}
			}
			out.print(Constants.SUCCESS[0] + "device-list!index?editble=1&highlight=" + device.getO_deviceno() + "'</script>");
		} catch (Exception e) {
			out.print(Constants.ERROR);
			e.printStackTrace();
		} finally {
			out.close();
		}
		return this.index();
	}

	private void setChannelcount(Long videoid, DeviceList dld) {
		if (videoid != null && -1L != videoid) {
			if (dld.getO_channelcount() != null) {
				dld.setO_channelcount(dld.getO_channelcount() + 1);
			} else {
				dld.setO_channelcount(1);
			}
		} else {
			// if (dld.getO_channelcount() != null && dld.getO_channelcount() !=
			// 0) {
			// dld.setO_channelcount(dld.getO_channelcount() - 1);
			// } else {
			// dld.setO_channelcount(0);
			// }
		}
	}

	private void saveCamDevice(DeviceList device, Long videoid, long l) {
		if (videoid != null && -1L != videoid) {
			CamDevice cd = new CamDevice();
			Camera camera = baseMgr.get(Camera.class, videoid);
			camera.setIpAddress(Utils.isEmpty(device.getO_loginhost()) ? "" : device.getO_loginhost());
			cd.setCamID(videoid);
			cd.setChannel(l);
			cd.setDeviceID(device.getO_deviceno());
			cd.setCreater(getCurrentUser().getUserName());
			cd.setCreatime(DateUtil.formatLongTimeDate(new Date()));
			baseMgr.save(cd);
		}

	}

	public String vbatch() {
		if (boxids != null && boxids.length > 0) {
			String thresholdStr = "";
			for (int i = 0; i < boxids.length; i++) {
				DeviceList tdl = baseMgr.get(DeviceList.class, boxids[i]);
				tdl.setServerip(device.getServerip());
				tdl.setServerport(device.getServerport());
				if (tdl.getO_busname() != null) {
					thresholdStr = SocketUtil.setThresholdStr(
							String.valueOf((new Date()).getTime()).substring(0,10),
							tdl.getO_busname(),
							SocketUtil.setIPAndPort(tdl.getServerip(), tdl.getServerport()));
					boolean test = SocketUtil.sendSocket(thresholdStr);	//设置报警阀值
					if (test) {
						tdl.setIpstatus("T");
					} else {
						tdl.setIpstatus("F");
					}
				} else {
					tdl.setIpstatus("F");
				}
				baseMgr.save(tdl);
			}
		}
		Struts2Util.renderText("ok");
		return null;
	}

	/*
	 * 原edit方法 编辑4*4摄像头 public String edit() { searchList = dsMgr.getList(); dl
	 * = baseMgr.get(DeviceList.class, id); channelList =
	 * dlMgr.getChannelList(); List<CamDevice> clist = dlMgr.getChannel(id);
	 * 
	 * for (int i = 0; i < 16; i++) { cameras[i] = new Camera_(); }
	 * List<Integer> countlis = new ArrayList<Integer>(); for (int i = 0; i <
	 * clist.size(); i++) { CamDevice cd = clist.get(i); Camera c =
	 * baseMgr.get(Camera.class, cd.getCameraid());
	 * cameras[Integer.parseInt(cd.getChannel() + "") - 1] = change(c);
	 * countlis.add(Integer.parseInt(cd.getChannel() + "")); } for (int i = 1; i
	 * < 17; i++) { boolean b = true; for (int j = 0; j < countlis.size(); j++)
	 * { if (i == countlis.get(j)) { b = false; break; } } if (b) { KeyAndValue
	 * ky = new KeyAndValue(i + "", i + ""); countList.add(ky); } } return
	 * "edit"; }
	 */

	/*
	 * 原show方法 public String show() { dl = baseMgr.get(DeviceList.class, id);
	 * channelList = dlMgr.getChannelList(); List<CamDevice> clist =
	 * dlMgr.getChannel(id);
	 * 
	 * for (int i = 0; i < 16; i++) { cameras[i] = new Camera_(); } for (int i =
	 * 0; i < clist.size(); i++) { CamDevice cd = clist.get(i); Camera c =
	 * baseMgr.get(Camera.class, cd.getCameraid());
	 * cameras[Integer.parseInt(cd.getChannel() + "") - 1] = change(c); } return
	 * "show"; }
	 */

	public Camera_ change(Camera c) {
		Camera_ c_ = new Camera_();
		c_.setCameraid(c.getCameraID() + "");
		c_.setCameracode(c.getCameraCode());
		c_.setCameraname(c.getCameraName());
		return c_;
	}

	/*
	 * 原add方法 public String add() { for (int i = 1; i < 17; i++) { KeyAndValue
	 * ky = new KeyAndValue(i + "", i + ""); countList.add(ky); } searchList =
	 * dsMgr.getList(); channelList = dlMgr.getChannelList(); for (int i = 0; i
	 * < 16; i++) { cameras[i] = new Camera_(); } return "add"; }
	 */

	/*
	 * 原保存方法
	 * 
	 * @SuppressWarnings("unused") public String create() throws IOException {
	 * PrintWriter out = Struts2Util.getResponse().getWriter(); try { String
	 * username = getCurrentUser().getUserName(); String now =
	 * DateUtil.formatLongTimeDate(new Date()); dl.setO_xiugairen(username);
	 * dl.setO_xiugaishijian(now); dl.setO_unittype("1"); int count = 0; String
	 * videono = ""; String videoname = ""; for (int i = 0; i < cid.length; i++)
	 * { if (cid[i] != null && !"".equals(cid[i]) && !"-1".equals(cid[i])) {
	 * Camera c = baseMgr .get(Camera.class, Long.parseLong(cid[i])); videono +=
	 * c.getCameraCode() + ";"; videoname += c.getCameraName() + ";"; count++; }
	 * } dl.setO_videoname(videoname); dl.setO_channelcount(count);
	 * baseMgr.save(dl); dlMgr.delChannel(dl.getO_deviceno()); String ipstr =
	 * ""; for (int i = 0; i < cameras.length; i++) { if (cid[i] != null &&
	 * !"".equals(cid[i]) && !"-1".equals(cid[i])) { CamDevice cd = new
	 * CamDevice(); cd.setCameraid(Long.parseLong(cid[i]));
	 * cd.setChannel(Long.valueOf(i + 1)); cd.setDeviceid(dl.getO_deviceno());
	 * cd.setCreater(username); cd.setCreatime(now); baseMgr.save(cd); ipstr +=
	 * baseMgr.get(Camera.class, cd.getCameraid()) .getIpAddress() + " / "; } }
	 * 
	 * if (dlMgr.getVeh(dl.getO_deviceno()) != null) { Vehcile veh =
	 * dlMgr.getVeh(dl.getO_deviceno()); veh.setCctvip(ipstr);
	 * baseMgr.save(veh); } out.print(Constants.SUCCESS[0] +
	 * "device-list!index?editble=1&highlight=" + dl.getO_deviceno() +
	 * "'</script>"); } catch (Exception e) { out.print(Constants.ERROR);
	 * e.printStackTrace(); } finally { out.close(); } return this.index(); }
	 */

	public String checkDel() throws IOException {
		PrintWriter p = Struts2Util.getResponse().getWriter();
		try {
			if (dlMgr.checkDel(id)) {
				p.print("true");// 用户名重复
			} else {
				p.print("false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			p.close();
		}
		return null;
	}

	public String delete() {
		DeviceList tdl = baseMgr.get(DeviceList.class, id);
		DeviceListLog tdll = new DeviceListLog("1", tdl, getCurrentUser()
				.getUserName(), DateUtil.formatLongTimeDate(new Date()));
		baseMgr.save(tdll);
		List<CamDevice> cds = dlMgr.delCamDevice(tdl.getO_deviceno());
		// baseMgr.delete(tdl);
		for (CamDevice tcd : cds) {
			baseMgr.delete(tcd);
		}
		baseMgr.delete(DeviceList.class, id);
		return this.index();
	}

	public String checkEdit() throws IOException {
		PrintWriter p = Struts2Util.getResponse().getWriter();
		try {
			String names = Struts2Util.getParameter("names");// 从前台获得的登陆账号
			String ids = Struts2Util.getParameter("ids");// 从前台获得的用户ID
			if (dlMgr.checkEdit(ids, names)) {
				p.print("true");// 用户名重复
			} else {
				p.print("false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			p.close();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public String getCam() throws IOException {
		PrintWriter out = Utils.getResponse().getWriter();
		try {
			String cid = Struts2Util.getParameter("cname");// 从前台获得的登陆账号
			if (cid != null && !"".equals(cid)) {
				Camera c = baseMgr.get(Camera.class, Long.valueOf(cid));
				List list = new ArrayList<Object>();
				list.add(c.getCameraCode());
				list.add(c.getCameraName());
				list.add(c.getCameraID());
				JSONArray json = JSONArray.fromObject(list);
				out.print(json.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}

	public String checkAdd() throws IOException {
		PrintWriter p = Struts2Util.getResponse().getWriter();
		try {
			String names = Struts2Util.getParameter("names");// 从前台获得的登陆账号
			if (dlMgr.checkAdd(names)) {
				p.print("true");// 用户名重复
			} else {
				p.print("false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			p.close();
		}
		return null;
	}

	@Override
	public DeviceList getModel() {
		return device;
	}

	public DeviceVo getDeVo() {
		return deVo;
	}

	public void setDeVo(DeviceVo deVo) {
		this.deVo = deVo;
	}

	public List<KeyAndValue> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<KeyAndValue> channelList) {
		this.channelList = channelList;
	}

	public Camera_[] getCameras() {
		return cameras;
	}

	public void setCameras(Camera_[] cameras) {
		this.cameras = cameras;
	}

	public String[] getCid() {
		return cid;
	}

	public void setCid(String[] cid) {
		this.cid = cid;
	}

	public List<KeyAndValue> getCountList() {
		return countList;
	}

	public void setCountList(List<KeyAndValue> countList) {
		this.countList = countList;
	}

	public String getVideoname() {
		return videoname;
	}

	public void setVideoname(String videoname) {
		this.videoname = videoname;
	}

	public Long[] getBoxids() {
		return boxids;
	}

	public void setBoxids(Long[] boxids) {
		this.boxids = boxids;
	}

	public Long getVideoid1() {
		return videoid1;
	}

	public void setVideoid1(Long videoid1) {
		this.videoid1 = videoid1;
	}

	public Long getVideoid2() {
		return videoid2;
	}

	public void setVideoid2(Long videoid2) {
		this.videoid2 = videoid2;
	}

	public Long getVideoid3() {
		return videoid3;
	}

	public void setVideoid3(Long videoid3) {
		this.videoid3 = videoid3;
	}

	public Long getVideoid4() {
		return videoid4;
	}

	public void setVideoid4(Long videoid4) {
		this.videoid4 = videoid4;
	}

	public Long getVideoid5() {
		return videoid5;
	}

	public void setVideoid5(Long videoid5) {
		this.videoid5 = videoid5;
	}

	public Long getVideoid6() {
		return videoid6;
	}

	public void setVideoid6(Long videoid6) {
		this.videoid6 = videoid6;
	}

	public Long getVideoid7() {
		return videoid7;
	}

	public void setVideoid7(Long videoid7) {
		this.videoid7 = videoid7;
	}

	public Long getVideoid8() {
		return videoid8;
	}

	public void setVideoid8(Long videoid8) {
		this.videoid8 = videoid8;
	}

	public List<KeyAndValue> getDepts() {
		return depts;
	}

	public void setDepts(List<KeyAndValue> depts) {
		this.depts = depts;
	}
}