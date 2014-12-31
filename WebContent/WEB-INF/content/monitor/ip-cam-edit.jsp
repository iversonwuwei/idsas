<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<script type="text/javascript" src="<%=basePath%>scripts/specific/monitor/ip-cam.js"></script>
		<script type="text/javascript">
			$(function() {
				if($('#v_select_model').val() != null && $('#v_select_model').val() != "") {
					select_model.setComboValue($('#v_select_model').val());
				}
			});
			
			function doSub() {
				if($('#camName').val() == null || $('#camName').val() == "") {
					alert('Please enter the camera name!');
					return false;
				}
				var departmentID = select_company.getSelectedValue();
				if(departmentID == null || departmentID == "-1" || departmentID == "") {
					alert("Place select a department!");
					return;
				}
				var modelID = select_model.getSelectedValue();
				if(modelID == null || modelID == "-1" || modelID == "") {
					alert("Place select a camera model!");
					return;
				}
//				if($('#camIP').val() == null || $('#camIP').val() == "") {
//					alert('Camera IP should not be empty!');
//					return false;
//				}
				if($('#adminPass').length > 0) {
					if($('#adminPass').val() == null || $('#adminPass').val() == "") {
						alert('Please enter the administrator password!');
						return false;
					}
				}
				$('#v_select_model').val(modelID);
				checkDuplicate();
			}
			
			/**
			 * check if the police name is duplicate
			 */
			function checkDuplicate() {
				$.ajax({
				    type: "POST",
				    async: false,
				    dataType: 'JSON',
				    data :{
				    	'vo.departmentid' : select_company.getSelectedValue(),
				    	'vo.camID' : $.trim($("#camID").val()),
				    	'vo.camName' : $.trim($("#camName").val())
				    },
				    url : basePath + "monitor/ip-cam!checkNameDuplicate",
				    success : function(d) {
						if("pass" != d.result){
							alert("The camera name already exists!");
							return;
						} else {
							beforeSubmit();
							document.queryResult.submit();
						}
				    },
				    error : function(d) {
						alert("ERROR!");
						window.history.back();
				    }
				});
			}
		</script>
	</head>
	<body>
		<s:if test="%{cameraID == null}">
			<custom:navigation father="Organization Management" model="Camera" operate="Add" homePath="monitor/ip-cam" saveMethod="doSub();" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Management" model="Camera" operate="Edit" homePath="monitor/ip-cam" saveMethod="doSub();" />
		</s:else>
		<form name="queryResult" action="<%=basePath%>monitor/ip-cam!modifyCam" method="post">
			<s:hidden id="camID" name="cameraID" value="%{cameraID}" />
			<s:hidden name="editble" value="%{editble}"/>
			<s:hidden name="ipAddress" value="%{ipAddress}"/>
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="30%"><font color="red">*&nbsp;</font>Camera Name:</td>
						<td width="70%" class="valid">
							<input type="text" id="camName" name="cameraName" value="${cameraName}" maxlength="20" class="5,1,20" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Department:</td>
						<td>
							<custom:selCom name="deptid" value="${deptid}" width="180" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Camera Model:</td>
						<td>
							<s:select id="select_model" list="modelList" listKey="key" listValue="value" headerKey="-1" headerValue="" theme="simple" />
							<input id="v_select_model" type="hidden" name="vo.modelID" value="${modelID.modelID}" />
						</td>
					</tr>
					<s:if test="authLevel == 0">
						<tr>
							<td class="text_bg"><font color="red">*&nbsp;</font>Camera Login Username:</td>
							<td>
								<s:if test="%{cameraID == null}">
									<input type="text" id="adminName" name="adminName" value="Admin" maxlength="20" />
								</s:if>
								<s:else>
									<input type="text" id="adminName" name="adminName" value="${adminName}" maxlength="20" />
								</s:else>
							</td>
						</tr>
						<tr>
							<td class="text_bg"><font color="red">*&nbsp;</font>Camera Login Password:</td>
							<td>
								<s:if test="%{cameraID == null}">
									<input type="text" id="adminPass" name="adminPass" value="Admin" maxlength="20" />
								</s:if>
								<s:else>
									<input type="text" id="adminPass" name="adminPass" value="${adminPass}" maxlength="20" />
								</s:else>
							</td>
						</tr>
					</s:if>
					<%-- <tr>
						<td class="text_bg">video:</td>
						<td style="text-align: left; height:300px;">
							<s:include value="/common/ipCam_mod.jsp">
								<s:param name="id" value="'cam001'" />
								<s:param name="ip" value="'192.168.80.150'" />
								<s:param name="channel" value="'1'" />
								<s:param name="username" value="'Admin'" />
								<s:param name="password" value="'Admin'" />
							</s:include>
						</td>
					</tr>
					<tr>
						<td class="text_bg">video:</td>
						<td style="text-align: left; height:300px;">
							<s:include value="/common/ipCam_mod.jsp">
								<s:param name="id" value="'cam003'" />
								<s:param name="ip" value="'192.168.80.150'" />
								<s:param name="channel" value="'1'" />
								<s:param name="username" value="'Admin'" />
								<s:param name="password" value="'Admin'" />
							</s:include>
						</td>
					</tr> --%>
					<%-- <tr>
						<td class="text_bg">video:</td>
						<td style="text-align: left;">
							<object type="application/x-vlc-plugin" id="vlc" events="True"
								codebase="<%=basePath%>cabs/axvlc.cab#version=2,0,6,0"
       							classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921"
       							width="400" height="300">
									<param name='mrl' value='rtsp://Admin:Admin@192.168.80.150/stream1' />
									<param name='volume' value='50' />
									<param name='autoplay' value='true' />
									<embed type="application/x-vlc-plugin"
         								autoplay="true" loop="yes" width="400" height="300"
         								target="rtsp://Admin:Admin@192.168.80.150/stream1" />
							</object>
							<object width="400" height="300" 
								id="RTSPCtl" 
								classid="CLSID:1384A8DE-7296-49DA-B7F8-8A9A5984BE55" 
								codeBase="<%=basePath%>cabs/AxRTSP.cab#version=1,0,0,190"
								onfocus="document.body.focus()">
								<param name='autoplay' value='true' />
							</object>
						</td>
					</tr> --%>
				</table>
			</div>
			<script>
				var select_model = dhtmlXComboFromSelect_cascade('select_model', 180);
				select_model.enableFilteringMode(true);
			</script>
		</form>
	</body>
</html>