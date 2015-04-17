<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath() + "/";
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--
	title：摄像头封装
	Useage (JSP):
	<s:include value="/common/ipCam_mod.jsp">
		<s:param name="id" value="'cam001'" /> : 唯一标记
		<s:param name="ip" value="'192.168.80.150'" />	: Camera's IP	连接IP
		<s:param name="port" value="'334'" /> : 端口号
		<s:param name="channel" value="'1'" /> : Camera's channel	通道号
		<s:param name="username" value="'Admin'" /> : 用户名
		<s:param name="password" value="'Admin'" /> : 密码
		<s:param name="playerType" value="'1'" /> : 1 vlc; 2 原厂
		<s:param name="camType" value="'1'" /> : 1 ipCam; 2 vedio server
	</s:include>
	rtsp://Admin:Admin@10.1.210.4:554/stream1
	rtsp://203.117.57.107:554/live.sdp
--%>
<head>
	<c:if test="${param.playerType == '2'}">
		<c:if test="${param.camType == 1}">
			<script type="text/javascript" src="<%=basePath%>scripts/specific/monitor/ip-cam.js"></script>
		</c:if>
	</c:if>
</head>
<c:if test="${param.playerType == '1'}">
	<body onbeforeunload="disConnect();" style="background-color: #000000; width: 100%; height: 100%;">
		<object id='${param.id}' codebase='<%=basePath%>cabs/axvlc.cab#version=2,0,0,0'
			classid='clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921'
			style='width: 100%; height: 100%; top: 0;'>
			<c:if test="${param.camType == '1'}">
				<param name='mrl' value="rtsp://Admin:Admin@${param.ip}:${param.port}/stream${param.channel}" />
			</c:if>			
			<c:if test="${param.camType == null || param.camType == '2'}">
				<param name='mrl' value="rtsp://${param.ip}:${param.port}/live.sdp" />
			</c:if>		
			<param name='volume' value='50' />
			<param name='autoplay' value='false' />
			<param name='controls' value='false' />
		</object>
		<script type="text/javascript">
			function disConnect() {
				var vlc = document.getElementById('${param.id}');
				if(vlc) {
					vlc.playlist.stop();
				}
			}
		</script>
	</body>
</c:if>
<c:if test="${param.playerType == '2'}">
	<script language="JavaScript">
		alert(${param.camType});
	</script>
	<c:if test="${param.camType == 1}">
		<body onbeforeunload="Disconnect();" style="background-color: #000000; width: 100%; height: 100%;">
			<object id="${param.id}"
				classid="CLSID:1384A8DE-7296-49DA-B7F8-8A9A5984BE55"
				codebase="<%=basePath%>cabs/AxRTSP.cab#version=1,0,0,190"
				style="height: 95%; width: 100%; top: 0;">
				<param name="wmode" value="opaque"> 
				<embed type='application/x-vlc-plugin' id='${param.id}_embed'
					autoplay='true' loop='no' width='100%' height='100%'
					target="rtsp://${param.username}:${param.password}@${param.ip}:${param.port}/stream${param.channel}" />
			</object>
			<img id='but_${param.id}' onmouseover="mouseOver('${param.id}');" onmouseout="mouseOut('${param.id}');" 
				onclick="FullScreen('${param.id}');" 
				src='<%=basePath%>images/video/full-1.gif' hspace='1' style='cursor: pointer;height: 23px;width: 23px;float: right;' title="Full Screem"/>
			
			<%-- <object type="application/x-vlc-plugin" id="${param.id}" height="100%" width="100%"
				codebase="<%=basePath%>cabs/axvlc.cab#version=2,0,6,0"
				classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921">
					<param name='mrl' value='rtsp://${param.username}:${param.password}@${param.ip}/stream${param.channel}' />
					<param name='autoplay' value='true' />
					<embed type='application/x-vlc-plugin' id='${param.id}_embed'
		   								autoplay='true' width='100%' height='100%'
		   								target='rtsp://${param.username}:${param.password}@${param.ip}/stream${param.channel}' />
			</object> --%>
			<script>
				var outImg = new Array(basePath + 'images/video/snapshot-1.gif', basePath + 'images/video/record-1.gif',
						basePath + 'images/video/open-1.gif', basePath + 'images/video/full-1.gif', basePath + 'images/video/digital_zoom-1.gif');
				var overImg = new Array(basePath + 'images/video/snapshot-2.gif', basePath + 'images/video/record-2.gif',
						basePath + 'images/video/open-2.gif', basePath + 'images/video/full-2.gif', basePath + 'images/video/digital_zoom-2.gif');
				var downImg = new Array(basePath + 'images/video/snapshot-4.gif', basePath + 'images/video/record-4.gif',
						basePath + 'images/video/open-4.gif', basePath + 'images/video/full-4.gif', basePath + 'images/video/digital_zoom-4.gif');
				
				if (typeof(rtsps) == "undefined") {
					var rtsps = new Object();
				}
				
				function FullScreen(id) {
					with (document) {
						var rtsp = getElementById(id);
						rtsp.Set_Full(1);
						rtsp.Set_Fit(0);
					}
				}
			  	
				function Connect() {
					reloadHeight('${param.id}');
					with (document) {
				   		if('${param.ip}' == '') {
				   			return;
				   		}
				   		rtsps['${param.id}'] = getElementById('${param.id}');
				   		if(isMsie) {
					   		rtsps['${param.id}'].Set_Path('stream' + '${param.channel}');
					   		rtsps['${param.id}'].Set_ID('${param.username}');
					   		rtsps['${param.id}'].Set_PW('${param.password}');
					   		rtsps['${param.id}'].Set_URL('${param.ip}');
					   		rtsps['${param.id}'].Connect();
					   		rtsps['${param.id}'].Set_Mute(1);
				   		}
				   	}
				}
			
				function Disconnect() {
					if (typeof(rtsps) == "undefined") {
						return;
					}
					for(var key in rtsps) {
						if(isMsie) {
				   			rtsps[key].Disconnect();
						} else {
							rtsps[key].playlist.stop();
						}
					}
				}
				
				function addOnLoadEvent(func) {
					var oldonload = window.onload;
					if (typeof window.onload != 'function') {
						window.onload = func;
					} else {
						window.onload = function() {
							oldonload();
							func();
						};
					}
				}
				 
				function reloadHeight(id) {
					var tableHeight = $('#' + id).parent().height();
					$('#' + id).height(tableHeight - 25);
					if(!isMsie) {
						$('#' + id + '_embed').height(tableHeight - 25);
					}
				}
				 
		 		function mouseOver(id) {
					document.getElementById('but_' + id).src = basePath + 'images/video/full-4.gif';
				}
		
				function mouseOut(id) {
					document.getElementById('but_' + id).src = basePath + 'images/video/full-1.gif';
				}
				
				addOnLoadEvent(Connect);
			</script>
		</body>
	</c:if>
	<c:if test="${param.camType == null || param.camType == 2}">
		<body style="background-color: #000000; width: 100%; height: 100%;">
		alert(${param.ip})
			<!--<object style="height: 95%; width: 100%; top: 0;" id="${param.id}" classid="CLSID:70EDCF63-CA7E-4812-8528-DA1EA2FD53B6" codeBase="<%=basePath%>cabs/VitaminCtrl_4.0.0.13.cab#version=4,0,0,13" standby="Loading plug-in..." style="width: 730px; height: 510px;">
				<PARAM name="Url" value="rtsp://${param.ip}:${param.port}/live.sdp"/>
				<PARAM NAME="UserName" VALUE="${param.username}">
				<PARAM NAME="Password" VALUE="${param.password}">
			</object>-->
		<object style="height: 95%; width: 100%; top: 0;" id="${param.id}" classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921" codebase="http://download.videolan.org/pub/videolan/vlc/last/win32/axvlc.cab" standby="Loading plug -in ...">
			<param name="mrl" value="rtsp://rtsp://203.117.57.131:41781/live.sdp">
			<param name="username" value="${param.username}"/>
			<param name="password" value="${param.password}"/>
			<param name="autostart" value="true" />
			<param name="allowfullscreen" value="false" />
			<param name="autoplay" value="true"/>
			<param name='loop' value='false' />
			<param name='controls' value='true' />
			<param name="volume" value="40"/>
		</object>
		<!--<object type='application/x-vlc-plugin' pluginspage="http://www.videolan.org/" id='vlc' events='false' width="720" height="410">
			<param name='mrl' value='rtsp://${param.ip}:${param.port}/live.sdp' />
			<param name="username" value="${param.username}"/>
			<param name="password" value="${param.password}"/>
			<param name='volume' value='50' />
			<param name='autoplay' value='true' />
			<param name='loop' value='false' />
			<param name='fullscreen' value='false' />
			<param name='controls' value='false' />
		</object>-->
		</body>
	</c:if>
</c:if>