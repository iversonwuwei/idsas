<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<script type="text/javascript" src="<%=basePath%>scripts/common/layer/layer.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>scripts/specific/wmrs/real.js"></script>
		<style type="text/css">
		
		.openVideoDiv1 {
    background-color: #FFFFFF;
    border-color: #2383C0;
    border-radius: 10px 10px 10px 10px;
    border-style: solid;
    border-width: 2px;
    height: 450px;
    padding: 8px;
    position: absolute;
    width: 500px;
    z-index: 1200;
}
		
		</style>
		
		
		<script type="text/javascript">
		function openVideoWin() {
			$("body").append("<div id='divBackGround' class='divBackGround' ></div>");
			$("body").append("<div id='openVideoDiv' class='openVideoDiv1' ></div>");
			$("#openVideoDiv").css("left",($(window).width()-500)/2).css("top",($(window).height()-410)/2).show("fast");
			$("body").append("<div id='closeVideoDiv' class='closeDiv' ><img src='" + basePath + "images/button_xx.gif'onmousemove=\"this.src='" + basePath + "images/button_xx2.gif'\" onmouseout=\"this.src='" + basePath + "images/button_xx.gif'\" style='cursor: pointer;' onclick='closeVideoWin();'/></div>");
			$("#closeVideoDiv").css("left",(($(window).width()-500)/2)+495).css("top",(($(window).height()-410)/2)-10).show("fast");
			var ieVideoContent = "<div align='center' style='height: 500px;'>"
													+ "<div id='tips' style='left: 0px; width: 100px; height: 24px; text-align: center; color: #515151; font-family: Arial; font-size: 10pt; display: none; position: absolute; z-index: 2; background-image: url(\"" + basePath + "images/video/note-01.gif\");'/>"
													+ "<div style='height: 1px; font-size: 0px;'/>"
													+ "<br/>"
													+ "<table width='100%' border='0' cellSpacing='0' cellPadding='0' style='height: 430px;'>"
													+ "<tbody>"
														+ "<tr>"
															+ "<td align='center' height='100%'>"
																+ "<object id='ocx' classid='CLSID:96F064F1-2ACD-4E01-97B8-897739DF8516' codebase='" + basePath + "cabs/MRS_ActiveX_Control.ocx'  WIDTH='100%' HEIGHT='100%' onfocus='document.body.focus()' >"
																+ "<PARAM NAME='ServerAddress' VALUE='192.168.80.110'> "
																+ "<PARAM NAME='DeviceID' VALUE='2938'> "
																+ "<PARAM NAME='VideoChannelIndex' VALUE=0>" 
																+ "<PARAM NAME='AudioChannelIndex' VALUE=0>"
																+ "<PARAM NAME='UserName' VALUE=''>"
																+ "<PARAM NAME='Password' VALUE=''>" 
																+"</object>"
															+ "</td>"
														+ "</tr>"
													+ "</tbody>"
												+ "</table>"
												+ "</div>";
			$("#openVideoDiv").append(ieVideoContent);
			//initAxRTSP(username, passwd, camIP);
		}
		</script>
	</head>
	<body onbeforeunload="Disconnect();">
		<s:if test="%{editble == 1}">
			<custom:navigation father="Monitor" model="IP Camera" addPath="monitor/ip-cam" addMethod="editNew">
				<s:if test="authLevel == 0">
					<li>
						<a href="<%=basePath%>monitor/ip-cam!editUser?editble=1"><img src="<%=basePath%>images/dtree/ico_user.gif" title="Edit User" /> User</a>
					</li>
				</s:if>
				<li>
					<a href="#" onclick="getCamTimes();"><img src="<%=basePath%>images/input_hour_bg.gif" title="Display Camera's Time" />Time</a>
				</li>
			</custom:navigation>
		</s:if>
		<s:else>
			<custom:navigation father="Monitor" model="IP Camera" />
		</s:else>
		<form name="queryResult" action="<%=basePath%>wmrs/real!index" method="post">
		
		
		
			<div class="main" style="height: 415px;"><div class="sBase">
			
			
			
			<table cellspacing="0" cellpadding="0" class="zhzd" id="maintable" style="margin: 0px;">
			
			<thead>
			
			
			<tr class="Reprot_Head">
			
			
			<td width="3%" rowspan="1"><label>No.</label></td>
			<td width="15%" rowspan="1" colspan="1" onclick="resort('cameraName', 1 );" style="cursor:pointer">Camera Name</td>
			<td width="15%" rowspan="1" colspan="1" onclick="resort('cameraCode', 1 );" style="cursor:pointer">Camera ID</td>
			<td width="20%" rowspan="1" colspan="1" onclick="resort('modelID.modelName', 1 );" style="cursor:pointer">Camera ServerIP</td>
			<td width="3%">Operate</td>	
				</tr></thead>
				<tbody>
				<tr>
				<td>1</td>
				<td>PMRS Device 2938</td>
				<td>2938</td>
				<td>192.168.80.110</td>
				<td align="center">
				<a href="#" onclick="openVideoWin()">
				<img title="View" width="16px" src="<%=basePath %>images/livetou.png">
				</a>
				
				</td>
				</tr>
				
				</tbody>
				
				
				</table>
				
				
				
			<s:include value="/common/page.jsp">
				<s:param name="pageName" value="'page'" />
				<s:param name="formName" value="'queryResult'" />
				<s:param name="linkCount" value="5" />
			</s:include>
		</form>
	</body>
</html>