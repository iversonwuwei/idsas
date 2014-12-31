package com.zdtx.ifms.common.utils.tags;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zdtx.ifms.common.utils.Utils;
import com.zdtx.ifms.specific.service.monitor.IpCamManager;

/**
 * @ClassName: Tag_IpCamWin
 * @Description: 自定义标签-弹窗显示实时视频
 * @author Leon Liu
 * @date 2013-3-29 上午10:49:28
 * @version V1.0
 */
public class Tag_IpCamWin extends TagSupport {

	private static final long serialVersionUID = 2608793018979292758L;
	
	private String id;
	private String username;
	private String password;
	private String ip;
	private String vehicleID;
	private String deviceID;
	
	@Override
	public int doStartTag() throws JspException {
		ServletContext servletContext = ServletActionContext.getServletContext();	//获取程序全局变量
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext); 
		IpCamManager ipCamManager = applicationContext.getBean(IpCamManager.class);
		String vehName = ipCamManager.getVehNameByVehIDOrDeviceIDOrCamIP(vehicleID, deviceID, ip);	//获取车辆名称
		if(Utils.isEmpty(vehName) || Utils.isEmpty(servletContext.getAttribute(vehName))) {	//获取不到车辆名称或者车辆名称不在全局在线列表中，直接返回
			return SKIP_BODY;
		}
		if(vehicleID != null || deviceID != null) {	//非直接参数
			String[] camInfo = ipCamManager.getCamInfoByVehOrDevice(vehicleID, deviceID);
			ip = camInfo[0];
			username = camInfo[1];
			password = camInfo[2];
		}
		if(!Utils.isEmpty(ip)) {
//			boolean ipcamtype=ip.equals("192.168.80.120");
			boolean ipcamtype=false;
			String[] fname=new String[3];
			if(ipcamtype){
				fname[0]="tag_Disconnectvs";
				fname[1]="tag_openVideoWinvs";
				fname[2]="tag_closeVideoWinvs";
			}else{
				fname[0]="tag_Disconnect";
				fname[1]="tag_openVideoWin";
				fname[2]="tag_closeVideoWin";
			}
			String content = "";
			String objecthtml="";
			String stop="";
			String playstr="";
			String basePath = Utils.getBasePath();
			content = "<body onbeforeunload='"+fname[0]+"();'>" +
									"<img onclick=\""+fname[1]+"('" + id + "', '" + username + "', '" + password + "', '" + ip + "');\" src=\"" + basePath + "images/livetou.png\" title=\"play\" style=\"width:16px; height:16px; right:5px; cursor:pointer\" alt=\"play\" />" +
									"<script>" +
										"var isMsie = /msie/.test(navigator.userAgent.toLowerCase());" +
										"var isMozilla = /firefox/.test(navigator.userAgent.toLowerCase());" +
										"var isWebkit = /webkit/.test(navigator.userAgent.toLowerCase());" +
										"var isOpera = /opera/.test(navigator.userAgent.toLowerCase());" +
					
										"function "+fname[1]+"(id, uName, pwd, camIP) {" +
											"var RTSPCtl = document.getElementById('RTSPCtl');" +
											"if(RTSPCtl) {" +	//如果当前存在RTSPCtl控件则返回
												"return;" +
											"}" +
											"$('body').append(\"<div id='divBackGround' class='divBackGround' ></div>\");" +
											"$('body').append(\"<div id='openVideoDiv' class='openVideoDiv' ></div>\");" +
											"$('#openVideoDiv').css('left', ($(window).width()-750)/2).css('top',($(window).height()-450)/2).show('fast');" +
											"$('body').append(\"<div id='closeVideoDiv' class='closeDiv' ><img src='" + basePath + "images/button_xx.gif' style='cursor: pointer;' onclick='"+fname[2]+"();' /></div>\");" +
											"$('#closeVideoDiv').css('left', (($(window).width()-750)/2)+745).css('top', (($(window).height()-450)/2)-10).show('fast');";
		
	
			
	
	if(ipcamtype){
//		objecthtml="<object id='RTSPCtl'  type='application/x-CamV' codebase='" + basePath + "CamV_H264.cab#Version=6,1,0,139'  style='height: 100%; width: 100%;' onfocus='document.body.focus()'>";
//		playstr="setTimeout(playvs(camIP),1000);";
//		stop="rtsp.Stop();";
		objecthtml="<object id='RTSPCtl'  type='application/x-CamV' codebase='" + basePath + "CamV_H264.cab#Version=6,1,0,139'  style='height: 100%; width: 100%;' onfocus='document.body.focus()'>";
		playstr="setTimeout(\"document.getElementById('RTSPCtl').MediaURL='rtsp://\"+camIP+\"/mpeg4/quad/media.amp'\",1000);";
		playstr+="setTimeout(\"document.getElementById('RTSPCtl').Play()\",1000);";
		stop="rtsp.Stop();";
	}else{
		objecthtml= "<object id='RTSPCtl' classid='CLSID:1384A8DE-7296-49DA-B7F8-8A9A5984BE55' codebase='" + basePath + "cabs/AxRTSP.cab#version=1,0,0,190' style='height: 100%; width: 100%;' onfocus='document.body.focus()'>"
				+ "<embed type='application/x-vlc-plugin' id='vlc_embed'"
				+ "autoplay='true' width='100%' height='100%'"
					+ "target='rtsp://\" + uName + \":\" + pwd + \"@\" + camIP + \"/stream1' />"
						+ "</object>";
		playstr=	"var rtsp = getElementById('RTSPCtl');" +
				"rtsp.Set_Path('stream1');" +
				"rtsp.Set_ID(uName);" +
				"rtsp.Set_PW(pwd);" +
				"rtsp.Set_URL(camIP);" +
				"rtsp.Connect();" +
				"rtsp.Set_Mute(1);";

		stop="rtsp.Disconnect();";
	}
	
			
			
			String ieVideoContent = 
											"<div align='center' style='height: 440px;'>"
												+ objecthtml
										+ "</div>";
			
					content += "$('#openVideoDiv').append(\"" + ieVideoContent + "\");" +
											"if(isMsie) {"+
												"with (document) {" +
												playstr +
												"}" +
											"}" +
										"}" +
					
										"function "+fname[2]+"() {" +
											"if(isMsie) {" +
												""+fname[0]+"();" +
											"} else if(isMozilla || isWebkit || isOpera){" +
												"$('#vlc_embed').remove();" +
											"}" +
											"$('#divBackGround').remove();" +
											"$('#openVideoDiv').remove();" +
											"$('#closeVideoDiv').remove();" +
										"}" +
										"function playvs(ip){var rtsp = document.getElementById('RTSPCtl'); document.getElementById('RTSPCtl').MediaURL='rtsp://'+ip+':554/mpeg4/quad/media.amp'; document.getElementById('RTSPCtl').Play();}"+
										"function "+fname[0]+"() {" +
											"with (document) {" +
												"var rtsp = getElementById('RTSPCtl');" +
												"if(!rtsp) {" +
													"return;" +
												"}" +
												stop +
											"}" +
										"}" +
									"</script>" +
								"</body>";
			try {
				pageContext.getOut().print(content);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return SKIP_BODY;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setVehicleID(String vehicleID) {
		this.vehicleID = vehicleID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
}