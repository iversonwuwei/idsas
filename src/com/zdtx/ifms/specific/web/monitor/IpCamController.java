/**
 * @File: IpCamController.java
 * @path: idsas - com.zdtx.ifms.specific.web.monitor
 */
package com.zdtx.ifms.specific.web.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.zdtx.ifms.common.utils.DateUtil;
import com.zdtx.ifms.common.utils.KeyAndValue;
import com.zdtx.ifms.common.utils.Struts2Util;
import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.common.web.ReportBase;
import com.zdtx.ifms.specific.model.authority.Org;
import com.zdtx.ifms.specific.model.authority.User;
import com.zdtx.ifms.specific.model.monitor.CamDevice;
import com.zdtx.ifms.specific.model.monitor.CamUser;
import com.zdtx.ifms.specific.model.monitor.Camera;
import com.zdtx.ifms.specific.model.system.CamModel;
import com.zdtx.ifms.specific.model.vehicle.VehcileView;
import com.zdtx.ifms.specific.service.monitor.IpCamManager;
import com.zdtx.ifms.specific.vo.monitor.CamUserVO;
import com.zdtx.ifms.specific.vo.monitor.IpCamVO;

/**
 * @ClassName: IpCamController
 * @Description:
 * @author: Leon Liu
 * @date: 2013-7-8 下午1:43:44
 * @version V1.0
 */
public class IpCamController extends ReportBase<Camera> {

	private static final long serialVersionUID = -360810423074867588L;

	@Autowired
	private IpCamManager manager;

	private Camera camera = new Camera();
	private IpCamVO vo = new IpCamVO();
	private List<KeyAndValue> modelList = new ArrayList<KeyAndValue>();
	private CamUserVO camUserVO = new CamUserVO();
	private Long authLevel;
	private Long cdid;

	public String index() {
		authLevel = Long.valueOf(((User) Struts2Util.getSession().getAttribute(
				"mainUser")).getUserRole().getInSystem());
		page = manager.getBatch(page, vo);
		modelList = manager.getCamModels();
		return "index";
	}

	/***
	 * 导出
	 * 
	 * @return
	 */
	public String exportDetail() {
		try {
			String title = "Camera List";
			xlsFileName = disposeXlsName(title
					+ DateUtil.formatDate(new Date()));
			xlsStream = manager.getExcel(title
					+ DateUtil.formatDate(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "xls";
	}

	/****
	 * false no video false2 The vehicle is powered off, so the related video is
	 * unavailable. false3 no ip false4 no type false5 no cam
	 */
	public String mod1() {
		CamDevice cdv = baseMgr.get(CamDevice.class, cdid);
		VehcileView vehv = baseMgr.get(VehcileView.class, id);
		Camera cam = baseMgr.get(Camera.class, cdv.getCamID());

		String restr = "false";
		String strs = "";
		List<String> videos = new ArrayList<String>();
		List<String> videos2 = new ArrayList<String>();

		if (vehv.getCctvip() == null || "".equals(vehv.getCctvip())) {
			restr = "false3";
		} else if (cam.getModelID() == null
				|| cam.getModelID().getType() == null
				|| "".equals(cam.getModelID().getType())) {
			restr = "false4";
		} else {
			int port = (int) (80 + (cdv.getChannel() - 1l));
			if (cam.getModelID().getType().equals("1")) {
				strs = manager.getvideolist2(vehv.getCctvip(), vo.getDate(),
						vo.getTime(), "Admin", "Admin", port + "");
				if (!"false".equals(strs)) {
					List<String> tvideos = null;
					tvideos = Utils.getpath2(vehv.getCctvip(), strs);
					if (tvideos != null) {
						videos2.addAll(tvideos);
					}
				}
				for (String urls : videos2) {
					String path1 = ServletActionContext.getRequest()
							.getContextPath() + "/";
					String basePath2 = ServletActionContext.getRequest()
							.getScheme()
							+ "://"
							+ ServletActionContext.getRequest().getServerName()
							+ ":"
							+ ServletActionContext.getRequest().getServerPort()
							+ path1;
					String basePath = ServletActionContext.getRequest()
							.getSession().getServletContext().getRealPath("")
							+ "/video";
					String path = basePath + "/" + id;
					String uname = "";
					uname = urls.substring(urls.lastIndexOf("/") + 1);

					uname = urls.split(",")[1];
					File file = new File(path + "/" + uname);
					if (file.exists()) {
					} else {
						videodown(urls.split(",")[0], urls.split(",")[1], path,
								port + "");
					}
					restr = basePath2 + "video/" + id + "/" + uname;
				}
				if ("false".equals(strs)) {
					restr = "false2";
				} else if (videos == null || videos.size() == 0) {
					restr = "false";
				}

			} else if (cam.getModelID().getType().equals("2")) {
				int port1 = (int) (554 + (cdv.getChannel() - 1l));
				String[] ary = new String[2];
				ary[0] = vehv.getCctvip();
				ary[1] = "rtsp://"
						+ vehv.getCctvip()
						+ ":"
						+ port1
						+ "/mod.sdp?stime="
						+ DateUtil.toHHmmssPlusType2(-50, vo.getDate() + " "
								+ vo.getTime())
						+ "&etime="
						+ DateUtil.toHHmmssPlusType2(0,
								vo.getDate() + " " + vo.getTime())
						+ "&loctime=1";
				restr = ary[1];
			}
		}

		Struts2Util.renderJson("{\"result\":\"" + restr + "\"}");
		/****
		 * false no video false2 The vehicle is powered off, so the related
		 * video is unavailable. false3 no ip false4 no type false5 no cam
		 */
		return null;

	}

	/****
	 * false no video false2 The vehicle is powered off, so the related video is
	 * unavailable. false3 no ip false4 no type false5 no cam
	 */
	public String mod2() {
		CamDevice cdv = baseMgr.get(CamDevice.class, cdid);
		VehcileView vehv = baseMgr.get(VehcileView.class, id);
		Camera cam = baseMgr.get(Camera.class, cdv.getCamID());

		String restr = "false";

		if (vehv.getCctvip() == null || "".equals(vehv.getCctvip())) {
			restr = "false3";
		} else if (cam.getModelID() == null
				|| cam.getModelID().getType() == null
				|| "".equals(cam.getModelID().getType())) {
			restr = "false4";
		} else {
			if (cam.getModelID().getType().equals("1")) {
				restr = "false6";
			} else if (cam.getModelID().getType().equals("2")) {
				int port1 = (int) (554 + (cdv.getChannel() - 1l));
				String[] ary = new String[2];
				ary[0] = vehv.getCctvip();
				ary[1] = "rtsp://"
						+ vehv.getCctvip()
						+ ":"
						+ port1
						+ "/mod.sdp?stime="
						+ DateUtil.toHHmmssPlusType2(0,
								vo.getDate() + " " + vo.getTime())
						+ "&etime="
						+ DateUtil.toHHmmssPlusType2(0,
								vo.getDate() + " " + vo.getEtime())
						+ "&loctime=1";
				restr = ary[1];
			}
		}

		Struts2Util.renderJson("{\"result\":\"" + restr + "\"}");
		/****
		 * false no video false2 The vehicle is powered off, so the related
		 * video is unavailable. false3 no ip false4 no type false5 no cam
		 */
		return null;

	}

	/****
	 * false no video false2 The vehicle is powered off, so the related video is
	 * unavailable. false3 no ip false4 no type false5 no cam
	 */
	public String mod3() {
		CamDevice cdv = baseMgr.get(CamDevice.class, cdid);
		VehcileView vehv = baseMgr.get(VehcileView.class, id);
		Camera cam = baseMgr.get(Camera.class, cdv.getCamID());

		String restr = "false";

		if (vehv.getCctvip() == null || "".equals(vehv.getCctvip())) {
			restr = "false3";
		} else if (cam.getModelID() == null
				|| cam.getModelID().getType() == null
				|| "".equals(cam.getModelID().getType())) {
			restr = "false4";
		} else {
			if (cam.getModelID().getType().equals("1")) {
				restr = "false6";
			} else if (cam.getModelID().getType().equals("2")) {
				int port1 = (int) (554 + (cdv.getChannel() - 1l));
				String[] ary = new String[2];
				ary[0] = vehv.getCctvip();
				ary[1] = "rtsp://" + vehv.getCctvip() + ":" + port1
						+ "/mod.sdp?stime="
						+ DateUtil.toHHmmssPlusType2(0, vo.getDate())
						+ "&etime="
						+ DateUtil.toHHmmssPlusType2(0, vo.getTime())
						+ "&loctime=1";
				restr = ary[1];
			}
		}

		Struts2Util.renderJson("{\"result\":\"" + restr + "\"}");
		/****
		 * false no video false2 The vehicle is powered off, so the related
		 * video is unavailable. false3 no ip false4 no type false5 no cam
		 */
		return null;

	}

	public String mod() {
		List<KeyAndValue> cams = manager.getCamipAndType(id);
		if (cams != null && cams.size() != 0) {
			KeyAndValue kv = cams.get(0);
			if (kv.getKey() == null || "".equals(kv.getKey())) {
				Struts2Util.renderJson("{\"result\":\"noip\"}");
			} else if (kv.getValue() == null || kv.getValue().equals("")) {
				Struts2Util.renderJson("{\"result\":\"notype\"}");
			} else {
				if (kv.getValue().equals("1")) {
					String[] ary = new String[3];
					ary[0] = kv.getValue();
					// http://192.168.80.150/operator/set_param.cgi?xml&recording.playback.clockunit=Range:
					// clock=20131119T154800Z-20131119T155030Z
					ary[1] = "http://"
							+ kv.getKey()
							+ "/operator/set_param.cgi?xml&recording.playback.clockunit=Range: clock="
							+ DateUtil.toHHmmssPlusType2(-28850, vo.getDate()
									+ " " + vo.getTime())
							+ "-"
							+ DateUtil.toHHmmssPlusType2(-28800, vo.getDate()
									+ " " + vo.getTime()) + "";
					manager.exeUrl(ary[1], "Admin", "Admin");
					ary[2] = "rtsp://" + kv.getKey() + "/playback";
					Struts2Util.renderJson("{\"result\":\"" + ary[2] + "\"}");
				} else if (kv.getValue().equals("2")) {
					String[] ary = new String[2];
					ary[0] = kv.getValue();
					ary[1] = "rtsp://"
							+ kv.getKey()
							+ "/mod.sdp?stime="
							+ DateUtil.toHHmmssPlusType2(-50, vo.getDate()
									+ " " + vo.getTime())
							+ "&etime="
							+ DateUtil.toHHmmssPlusType2(0, vo.getDate() + " "
									+ vo.getTime()) + "&loctime=1";

					Struts2Util.renderJson("{\"result\":\"" + ary[1] + "\"}");
				}
			}
		} else {
			Struts2Util.renderJson("{\"result\":\"false\"}");
		}

		return null;
	}

	public String gvideo() {
		// /**
		// * type=true; 1.x
		// * type=false; 2.x
		// * **/
		// boolean type=false;
		// List<String> videos=new ArrayList<String>();
		// List<String> videos2=new ArrayList<String>();
		// List<KeyAndValue> cams=manager.getCamips(id);
		// String strs="";
		// for(KeyAndValue c:cams){
		//
		// if(type){
		// strs=manager.getvideo3(c.getKey(), vo.getDate(), vo.getTime(),
		// "root", "root");
		// //strs=manager.getvideo3("192.168.80.120", "2013-11-12", "14:57:05",
		// "root", "root");
		// }else{
		// strs=manager.getvideolist2(c.getKey(), vo.getDate(), vo.getTime(),
		// "Admin", "Admin");
		// }
		//
		// if(!"false".equals(strs)){
		// List<String> tvideos=null;
		// if(type){
		// tvideos=Utils.getpath3(c.getKey(),strs);
		// //tvideos=Utils.getpath3("192.168.80.120",strs);
		// }else{
		// tvideos=Utils.getpath2(c.getKey(),strs);
		// }
		// if(tvideos!=null){
		// videos2.addAll(tvideos);
		// }
		// }
		// }
		//
		//
		// for(String urls: videos2){
		// String path1 = ServletActionContext.getRequest().getContextPath() +
		// "/";
		// String basePath2 = ServletActionContext.getRequest().getScheme() +
		// "://" + ServletActionContext.getRequest().getServerName() + ":"
		// +ServletActionContext.getRequest().getServerPort() + path1;
		// String basePath =
		// ServletActionContext.getRequest().getSession().getServletContext().getRealPath("")+"/video";
		// String path=basePath+"/"+id;
		// String uname="";
		//
		// if(type){
		// uname=urls.substring(urls.indexOf("NCMF//")+"NCMF//".length()).replaceAll("/",
		// "");
		// }else{
		// uname=urls.substring(urls.lastIndexOf("/")+1);
		// }
		//
		// if(type){
		// File file = new File(path+"/"+uname);
		// if(file.exists()) {
		// }else{
		// videodown2(urls, uname, path);
		// }
		// }else{
		// uname= urls.split(",")[1];
		// File file = new File(path+"/"+uname);
		// if(file.exists()) {
		// }else{
		// videodown(urls.split(",")[0], urls.split(",")[1], path);
		// }
		// }
		// videos.add(basePath2+"video/"+id+"/"+uname);
		// }
		// if("false".equals(strs)){
		// Struts2Util.renderJson("{\"result\":\"false2\"}");
		// }else if(videos==null || videos.size()==0){
		// Struts2Util.renderJson("{\"result\":\"false\"}");
		// }else{
		// List<String> videosort=new ArrayList<String>();
		// for(int i=videos.size()-1;i>=0;i--){
		// videosort.add(videos.get(i));
		// }
		// JSONArray js=JSONArray.fromObject(videosort);
		// Struts2Util.renderJson(js.toString());
		// }
		return null;
	}

	/**
	 * 获得列表 通过时间和日期
	 */
	public void videodown(String ip, String name, String path, String port) {
		try {
			String url = "http://" + ip + ":" + port
					+ "/cgi-bin/storage.cgi?xml&save=" + Utils.toSaveStr(name);
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
					"Admin", "Admin"); // 账号密码证书
			HttpClient httpClient = new HttpClient();
			httpClient.getParams().setAuthenticationPreemptive(true); // 抢先认证，只发一次请求
			httpClient.getState().setCredentials(AuthScope.ANY, credentials);
			HttpMethod method = new GetMethod(url);
			method.setDoAuthentication(true);
			httpClient.executeMethod(method);
			Utils.saveFile(path, name, method.getResponseBody());
			method.releaseConnection();

		} catch (Exception e) {
		}
	}

	/**
	 * 获得列表 通过时间和日期
	 */
	public void videodown2(String url, String name, String path) {
		try {
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
					"root", "root"); // 账号密码证书
			HttpClient httpClient = new HttpClient();
			httpClient.getParams().setAuthenticationPreemptive(true); // 抢先认证，只发一次请求
			httpClient.getState().setCredentials(AuthScope.ANY, credentials);
			HttpMethod method = new GetMethod(url);
			method.setDoAuthentication(true);
			httpClient.executeMethod(method);
			Utils.saveFile(path, name, method.getResponseBody());
			method.releaseConnection();

		} catch (Exception e) {
		}
	}

	public String editNew() {
		authLevel = Long.valueOf(((User) Struts2Util.getSession().getAttribute(
				"mainUser")).getUserRole().getInSystem());
		camUserVO = manager.getCamUsers();
		modelList = manager.getCamModels();
		return "edit";
	}

	public String edit() {
		authLevel = Long.valueOf(((User) Struts2Util.getSession().getAttribute(
				"mainUser")).getUserRole().getInSystem());
		camera = baseMgr.get(Camera.class, id);
		camUserVO = manager.getCamUsers();
		modelList = manager.getCamModels();
		return "edit";
	}

	public String show() {
		authLevel = Long.valueOf(((User) Struts2Util.getSession().getAttribute(
				"mainUser")).getUserRole().getInSystem());
		camera = baseMgr.get(Camera.class, id);
		camUserVO = manager.getCamUsers();
		modelList = manager.getCamModels();
		return "show";
	}

	/**
	 * modify IP camera's data
	 */
	public String modifyCam() {
		authLevel = Long.valueOf(((User) Struts2Util.getSession().getAttribute(
				"mainUser")).getUserRole().getInSystem());
		if (authLevel == 2L) { // viewer can not
			Struts2Util
					.sendMsg("Viewer can not config the IP camera's authority!");
			return null;
		}
		//CamUser camUser = manager.getCamUserByAuthLevel(authLevel); // get
																	// operator's
																	// authority
		if (Utils.isEmpty(camera.getCameraID())
				|| Utils.isEmpty(camera.getIpAddress())) { // new camera
			try {
				CamModel camModel = baseMgr
						.get(CamModel.class, vo.getModelID());
				camera.setModelID(camModel);
				camera.setCreater(getCurrentUser().getUserName());
				camera.setCreatime(DateUtil.formatLongTimeDate(new Date()));
				camera.setIsDelete("F");
				if (camera.getDeptid() != null) {
					Org torg = baseMgr.get(Org.class, camera.getDeptid());
					if (torg != null) {
						camera.setDeptname(torg.getOrgName());
					} else {
						camera.setDeptname(null);
					}
				} else {
					camera.setDeptname(null);
				}
				baseMgr.save(camera);
				Struts2Util
						.printSuccess("monitor/ip-cam", camera.getCameraID());
			} catch (Exception e) {
				Struts2Util.printError(e);
			}
		} else {
			Camera oldCam = baseMgr.get(Camera.class, camera.getCameraID()); // database's
																				// user
			oldCam.setCameraName(camera.getCameraName());
			oldCam.setOsd(camera.getOsd());
			oldCam.setViewerName(camera.getViewerName());
			oldCam.setViewerPass(camera.getViewerPass());
			oldCam.setOperatorName(camera.getOperatorName());
			oldCam.setOperatorPass(camera.getOperatorPass());
			oldCam.setAdminPass(camera.getAdminPass());
			oldCam.setAdminName(camera.getAdminName());

			try {
				CamModel camModel = baseMgr
						.get(CamModel.class, vo.getModelID());
				oldCam.setModelID(camModel);
				oldCam.setCameraCode(camera.getCameraCode());
				oldCam.setCreater(getCurrentUser().getUserName());
				oldCam.setCreatime(DateUtil.formatLongTimeDate(new Date()));

				if (camera.getDeptid() != null) {
					Org torg = baseMgr.get(Org.class, camera.getDeptid());
					if (torg != null) {
						oldCam.setDeptname(torg.getOrgName());
						oldCam.setDeptid(camera.getDeptid());
					} else {
						oldCam.setDeptname(null);
						oldCam.setDeptid(null);
					}
				} else {
					oldCam.setDeptname(null);
					oldCam.setDeptid(null);
				}

				baseMgr.save(oldCam);
				Struts2Util
						.printSuccess("monitor/ip-cam", oldCam.getCameraID());
			} catch (Exception e) {
				Struts2Util.printError(e);
			}
		}
		return null;
	}

	/**
	 * AJAX验证ip和名称是否重复
	 * 
	 * @return JSON
	 */
	public String checkDuplicate() {
		String newIP = vo.getNewIP();
		String camCode = vo.getCamCode();
		String camID = vo.getCamID();
		String res = manager.checkDuplicate(camID, newIP, camCode);
		jsonObject.put("result", res);
		return "jsonObject";
	}
	
	/**
	 * 验证同部门下名称是否重复
	 * @return
	 */
	public String checkNameDuplicate() {
		if(manager.isDuplicate(vo.getDepartmentid(), vo.getCamID(), vo.getCamName())) {
			jsonObject.put("result", "duplicate");
		} else {
			jsonObject.put("result", "pass");
		}
		return "jsonObject";
	}

	public String editUser() {
		camUserVO = manager.getCamUsers();
		return "edit-user";
	}

	/**
	 * save camera users
	 * 
	 * @return
	 */
	public String modifyUser() {
		try {
			CamUser camUser = manager.getCamUserByAuthLevel(1L); // get
																	// operator's
																	// authority
			manager.setCamUsers(camUserVO, camUser.getUserPass());
			Struts2Util.printSuccess("monitor/ip-cam", 0L);
		} catch (Exception e) {
			e.printStackTrace();
			Struts2Util.sendMsg("Failed to save the data!");
		}
		return null;
	}

	/**
	 * init the IP Camera vo.ip, vo.camCode needed
	 * 
	 * @return
	 */
	public String initIpCam() {
		Long auth_level = Long.valueOf(((User) Struts2Util.getSession()
				.getAttribute("mainUser")).getUserRole().getInSystem()); // get
																			// user's
																			// camera
																			// permissions
		if (auth_level == 2L) { // viewer does not have authority
			return "index";
		} else {
			CamUser camUser = manager.getCamUserByAuthLevel(auth_level); // get
																			// operator's
																			// authority
			if (auth_level == 0L) { // admin can set the authority
				camUserVO = manager.getCamUsers(); // Get 3 level users
				manager.setIpParam(vo.getCamIP(), "system.account.password0",
						camUserVO.getAdminPass(), camUser.getUserName(),
						camUser.getUserPass()); // Set admin account's password
				manager.setIpParam(vo.getCamIP(), "system.account.name1",
						camUserVO.getOperatorName(), camUser.getUserName(),
						camUser.getUserPass()); // Set operator account's name
				manager.setIpParam(vo.getCamIP(), "system.account.password1",
						camUserVO.getOperatorPass(), camUser.getUserName(),
						camUser.getUserPass()); // Set operator account's
												// password
				manager.setIpParam(vo.getCamIP(), "system.account.privilege1",
						"1", camUser.getUserName(), camUser.getUserPass()); // Set
																			// operator
																			// account's
																			// privilege
				manager.setIpParam(vo.getCamIP(), "system.account.state1", "1",
						camUser.getUserName(), camUser.getUserPass()); // Set
																		// operator
																		// account's
																		// enabled
				manager.setIpParam(vo.getCamIP(), "system.account.name2",
						camUserVO.getViewerName(), camUser.getUserName(),
						camUser.getUserPass()); // Set viewer account's name
				manager.setIpParam(vo.getCamIP(), "system.account.password2",
						camUserVO.getViewerPass(), camUser.getUserName(),
						camUser.getUserPass()); // Set viewer account's password
				manager.setIpParam(vo.getCamIP(), "system.account.privilege2",
						"2", camUser.getUserName(), camUser.getUserPass()); // Set
																			// viewer
																			// account's
																			// privilege
				manager.setIpParam(vo.getCamIP(), "system.account.state2", "1",
						camUser.getUserName(), camUser.getUserPass()); // Set
																		// viewer
																		// account's
																		// enabled
				/**
				 * Enable or disable authentication of camera's system 0:
				 * disable; 1: enable
				 */
				manager.setIpParam(vo.getCamIP(), "system.account.auth", "1",
						camUser.getUserName(), camUser.getUserPass()); // Enable
			}

			/**
			 * OSD control. 0: disable; 1: enable.
			 */
			manager.setIpParam(vo.getCamIP(), "video.osd.control", "1",
					camUser.getUserName(), camUser.getUserPass());
			/**
			 * Display date/time on OSD. 0: disable; 1: enable.
			 */
			manager.setIpParam(vo.getCamIP(), "video.osd.dt_control", "1",
					camUser.getUserName(), camUser.getUserPass());
			/**
			 * Additional text on OSD. set it ip camera's code
			 */
			manager.setIpParam(vo.getCamIP(), "video.osd.text",
					vo.getCamCode(), camUser.getUserName(),
					camUser.getUserPass());
			/**
			 * Display format of date. 0: YYYY/MM/DD; 1: MM/DD/YYYY; 2:
			 * DD/MM/YYYY.
			 */
			manager.setIpParam(vo.getCamIP(), "system.datetime.format", "0",
					camUser.getUserName(), camUser.getUserPass());
			/**
			 * Timezone. 0: GMT +12;... 3: GMT +9; 4: GMT +8; 5: GMT +7;... 24:
			 * GMT -12
			 */
			manager.setIpParam(vo.getCamIP(), "system.datetime.timezone", "4",
					camUser.getUserName(), camUser.getUserPass());
			/**
			 * Enable system date/time sync with NTP server. 0: Set Manually.;
			 * 1: Synchronize with PC. ;2: Synchronize with NTP Server; 3: Keep
			 * Current Date & Time.
			 */
			manager.setIpParam(vo.getCamIP(), "system.datetime.method", "2",
					camUser.getUserName(), camUser.getUserPass());
			/**
			 * The interval of periodically sync with NTP server. 0, 1:
			 * reserved; 2: Daily; 3: Weekly; 4: Hourly; 5: Monthly.
			 */
			manager.setIpParam(vo.getCamIP(), "system.datetime.ntpinterval",
					"2", camUser.getUserName(), camUser.getUserPass());
			/**
			 * Video codec. 0: H.264; 1: Mpeg4; 2: MJPEG; 3: Disable (default
			 * value = H.264); 4: Disable (default value = Mpeg4); 5: Disable
			 * (default value = MJPEG)
			 */
			manager.setIpParam(vo.getCamIP(), "video.channel0.videocodec", "0",
					camUser.getUserName(), camUser.getUserPass());
			// enable stream 2
			manager.setIpParam(vo.getCamIP(), "video.channel1.videocodec", "0",
					camUser.getUserName(), camUser.getUserPass());
			/**
			 * FPS.Frames per second 1: 1 fps; 2: 2 fps; 3: 3 fps; 5: 5 fps; 10:
			 * 10 fps; 15: 15 fps; 20: 20 fps; 25: 25 fps; 30: 30 fps
			 */
			manager.setIpParam(vo.getCamIP(), "video.channel0.fps", "10",
					camUser.getUserName(), camUser.getUserPass());
			manager.setIpParam(vo.getCamIP(), "video.channel1.fps", "5",
					camUser.getUserName(), camUser.getUserPass());
			/**
			 * Video resolution. 9: 1280x720; 11: 1920x1080; 12: 800x450; 13:
			 * 640x360; 14: 480x270; 15: 320x180
			 */
			manager.setIpParam(vo.getCamIP(), "video.channel0.resolution",
					"12", camUser.getUserName(), camUser.getUserPass());
			manager.setIpParam(vo.getCamIP(), "video.channel1.resolution",
					"13", camUser.getUserName(), camUser.getUserPass());
			/**
			 * Enable vertical flip function(上下翻转，摄像头安装形式相关). 0: disable; 2:
			 * enable
			 */
			manager.setIpParam(vo.getCamIP(), "video.image.flip", "0",
					camUser.getUserName(), camUser.getUserPass());
			/**
			 * Working environment 0: Indoor; 1: Outdoor
			 */
			manager.setIpParam(vo.getCamIP(), "video.image.env", "1",
					camUser.getUserName(), camUser.getUserPass());
		}
		Struts2Util.printSuccess("monitor/ip-cam", 0L);
		return "";
	}

	/**
	 * 获取摄像头系统时间
	 * 
	 * @return JSON
	 */
	public String getCamTime() {
		String cam_ip = vo.getCamIP();
		KeyAndValue nameAndPass = manager.getCamAdminByIP(cam_ip);
		String resTime = manager.getCamTime(cam_ip, nameAndPass.getKey(),
				nameAndPass.getValue());
		jsonObject.put("camTime", resTime);
		if (resTime.equals("Connection timeout!")
				|| resTime.equals("Connection refused!")
				|| resTime.equals("Unauthorized!")
				|| !manager.checkCamTime(resTime)) {
			jsonObject.put("camTimeErr", "1");
		}
		return "jsonObject";
	}

	public String del() {
		//Camera tc = baseMgr.get(Camera.class, id);
		// tc.setIsDelete("T");
		List<CamDevice> cds = manager.getCD(id);
		if (cds != null && cds.size() != 0) {
			Struts2Util.renderText("false");
		} else {
			Struts2Util.renderText("true");
		}
		return null;
	}

	public String delete() {
		Camera tc = baseMgr.get(Camera.class, id);
		tc.setIsDelete("T");
		baseMgr.save(tc);
		return this.index();
	}

	/**
	 * 获取当前页所有摄像头系统时间
	 * 
	 * @return JSON
	 */
	public String getCamTimes() {
		String[] camInfos = vo.getCamIP().split(";"); // 获取当前页的摄像头IP
		String camTimes = "";
		String camID = "";
		String camIP = "";
		for (int i = 0; i < camInfos.length; i++) {
			camID = camInfos[i].split("\\|")[0];
			camIP = camInfos[i].split("\\|")[1];
			KeyAndValue nameAndPass = manager.getCamAdminByIP(camIP); // 获取摄像头超管账号
			String resTime = "";
			if (nameAndPass == null) {
				resTime = "Unauthorized!";
			} else {
				resTime = manager.getCamTime(camIP, nameAndPass.getKey(),
						nameAndPass.getValue()); // 获取摄像头系统时间
			}
			// 如果时间查询异常或者不准确，则增加标红
			if (resTime.equals("Connection error!")
					|| resTime.equals("Connection timeout!")
					|| resTime.equals("Connection refused!")
					|| resTime.equals("Unauthorized!")
					|| !manager.checkCamTime(resTime)) {
				camTimes += camID + "_" + resTime + ";";
			} else {
				camTimes += camID + "|" + resTime + ";";
			}
		}
		jsonObject.put("camTimes", camTimes);
		return "jsonObject";
	}

	@Override
	public Camera getModel() {
		return this.camera;
	}

	public IpCamVO getVo() {
		return vo;
	}

	public void setVo(IpCamVO vo) {
		this.vo = vo;
	}

	public List<KeyAndValue> getModelList() {
		return modelList;
	}

	public void setModelList(List<KeyAndValue> modelList) {
		this.modelList = modelList;
	}

	public CamUserVO getCamUserVO() {
		return camUserVO;
	}

	public void setCamUserVO(CamUserVO camUserVO) {
		this.camUserVO = camUserVO;
	}

	public Long getAuthLevel() {
		return authLevel;
	}

	public void setAuthLevel(Long authLevel) {
		this.authLevel = authLevel;
	}

	public Long getCdid() {
		return cdid;
	}

	public void setCdid(Long cdid) {
		this.cdid = cdid;
	}

}