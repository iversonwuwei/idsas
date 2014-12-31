<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript" src="<%=basePath%>scripts/specific/vehicle/police.js"></script>
		<script type="text/javascript">
			$(function(){
				document.getElementById('description').onkeydown = function() {    
					if(this.value.length >= 300 && event.keyCode != 8) {
						event.returnValue = false; 
					}
				}
			});
		</script>
	</head>
	<body>
		<s:if test="%{policeID == null}">
			<custom:navigation father="Organization Management" model="Police" operate="Add" saveMethod="doSave();" homePath="vehicle/police" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Management" model="Police" operate="Edit" saveMethod="doSave();" homePath="vehicle/police" />
		</s:else>
		<form action="<%=basePath%>vehicle/police!create" name="queryResult" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<input type="hidden" name="policeID" id="policeID" value="${policeID}" />
			<input type="hidden" id="v_deviceID" name="vo.deviceID" value="${deviceID}" />
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="30%"><font color="red">*&nbsp;</font>Police Name:</td>
						<td width="70%" class="valid">
							<input type="text" id="policeName" name="policeName" value="${policeName}" maxlength="20" class="5,1,20" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Department:</td>
						<td>
							<custom:selCom name="deptID" value="${deptID}" />
							<s:hidden id="deptName" name="deptName" value="%{deptName}" />
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Fleet:</td>
						<td>
							<custom:selTeam name="fleetID" value="${fleetID}" />
							<s:hidden id="fleetName" name="fleetName" value="%{fleetName}" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Device:</td>
						<td>
							<s:select list="deviceList" id="deviceID" name="deviceID" value="%{deviceID}" listKey="key" listValue="value" headerKey="-1" headerValue="Please select" theme="simple" />
							<s:hidden id="deviceName" name="deviceName" value="%{deviceName}" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Gender:</td>
						<td>
							<s:select list="#{2:'female', 0:'hidden'}" id="gender" name="gender" value="%{gender}" listKey="key" listValue="value" headerKey="1" headerValue="male" theme="simple" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Email:</td>
						<td class="valid">
							<input type="text" id="email" name="email" value="${email}" style="width: 300px;" maxlength="30" class="5,1,30" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Phone:</td>
						<td class="valid">
							<input type="text" id="phone" name="phone" value="${phone}" maxlength="20" class="3,1,20" onblur="this.value=$.trim(this.value);" />
						</td>
					</tr>
					<tr>
						<td class="text_bg" width="13%">Description:<br />(300 characters)</td>
						<td height="60px;">
							<s:textarea id="description" name="description" theme="simple" maxlength="300" cssStyle="width:100%; height:100%;" />
						</td>
					</tr>
				</table>
			</div>
		</form>
		<script type="text/javascript">
			select_company.attachEvent('onChange', getDeviceByDept);
		</script>
	</body>
</html>