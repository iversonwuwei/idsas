<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript" src="<%=basePath%>scripts/specific/vehicle/fleet-list.js"></script>
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
		<s:if test="%{orgID == null}">
			<custom:navigation father="Organization Management" model="Fleet" operate="Add" saveMethod="doSave();" homePath="vehicle/fleet-list" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Management" model="Fleet" operate="Edit" saveMethod="doSave();" homePath="vehicle/fleet-list" />
		</s:else>
		<form action="<%=basePath%>vehicle/fleet-list!create" name="queryResult" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<input type="hidden" name="orgID" id="orgID" value="${orgID}" />
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="30%"><font color="red">*&nbsp;</font>Fleet Name:</td>
						<td width="70%" class="valid">
							<input type="text" id="orgName" name="orgName" value="${orgName}" style="width: 200px;" maxlength="20" class="5,1,20" onblur="this.value=$.trim(this.value);">
						</td>
					</tr>
					<tr>
						<td class="text_bg"><font color="red">*&nbsp;</font>Department:</td>
						<td>
							<custom:selCom name="parentID" value="${parentID}" width="200"/>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Description:<br />(300 characters)</td>
						<td height="60px;">
							<s:textarea id="description" name="description" theme="simple" maxlength="300" cssStyle="width:100%; height:100%;" />
					 	</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>