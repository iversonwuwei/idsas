<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<script type="text/javascript" src="<%=basePath%>scripts/common/layer/layer.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>scripts/specific/monitor/ip-cam.js"></script>
		<script type="text/javascript">
			$(function() {
				select_model.setComboValue($('#v_select_model').val());
			});
			
			function doSearch() {
				$('#v_select_model').val(select_model.getSelectedValue());
				beforeSubmit();
				document.queryResult.submit();
			}
			
			function excel(){
		     	if(0 == "${fn:length(page.result)}"){
		         	alert("No derived data!");
		         	return;
		     	}
		     	document.forms['queryResult'].action='<%=basePath%>monitor/ip-cam!exportDetail';
				document.forms['queryResult'].submit();
				document.forms['queryResult'].action='<%=basePath%>monitor/ip-cam!index';
			}
			
			function delCam(id) {
				if(confirm('Be sure you want to delete this data?')) {
					$.ajax({
						type: "POST",
				     	async: true,
				     	url: basePath + "monitor/ip-cam!del",
				  	 	data: {'id' : id},
				  	 	dataType: "text",
				  	 	success: function(data) {
					  		if(data == "false") {
					  			alert("This camera has been bound equipment");
					  		} else if(data == "true") {
					  			location=basePath+"monitor/ip-cam/"+id+"/delete?editble=1";
					  		}
						}
					});
				}
			}
		</script>
	</head>
	<body onbeforeunload="Disconnect();">
		<s:if test="%{editble == 1}">
			<custom:navigation father="Organization Management" model="Camera" excelMethod="excel()" addPath="monitor/ip-cam" addMethod="editNew">
				<%--
				<s:if test="authLevel == 0">
				<li>
					<a href="<%=basePath%>monitor/ip-cam!editUser?editble=1" style="width: 60px; margin-left: -20px;">
						<img src="<%=basePath%>images/dtree/ico_user.gif" border="0" align="absmiddle" title="Edit User" /> User</a>
				</li>
				</s:if>
				<li>
					<a href="#" onclick="getCamTimes();" style='width: 60px; margin-left: -25px;'>
						<img src="<%=basePath%>images/input_hour_bg.gif" border="0" align="absmiddle" title="Display Camera's Time" /> Time</a>
				</li>
				 --%>
			</custom:navigation>
		</s:if>
		<s:else>
			<custom:navigation father="Organization Management" model="Camera" />
		</s:else>
		<form name="queryResult" action="<%=basePath%>monitor/ip-cam!index" method="post">
			<s:hidden name="editble" value="%{editble}"/>
			<custom:searchBody>
				<custom:searchItem title="Department" side="first"> 
					<custom:selCom name="vo.departmentid" value="${vo.departmentid}" />
				</custom:searchItem>
				<custom:searchItem title="Camera Name">
					<input type="text" name="vo.camName" value="${vo.camName}" />
				</custom:searchItem>
				<custom:searchItem title="Camera Model">
					<s:select id="select_model" list="modelList" listKey="key" listValue="value" headerKey="-1" headerValue="" theme="simple" />
					<input type="hidden" id="v_select_model" name="vo.modelID" value="${vo.modelID}" />
				</custom:searchItem>
				<custom:searchItem title="IP">
					<input type="text" name="vo.camIP" value="${vo.camIP}" />
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="doSearch();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader>
					<custom:sort property="cameraName" page="${page}" width="15%">Camera Name</custom:sort>
					<custom:sort property="deptname" page="${page}" width="15%">Department</custom:sort>
					<custom:sort property="modelID.modelName" page="${page}" width="15%">Camera Model</custom:sort>
					<custom:sort property="ipAddress" page="${page}" width="15%">IP</custom:sort>
					<custom:sort property="adminName" page="${page}" width="15%">Login Username</custom:sort>
					<custom:sort property="adminPass" page="${page}" width="15%">Login Password</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="cameraID" index="i" var="v">
					<custom:tableItem>${v[1].cameraName}</custom:tableItem>
					<custom:tableItem>${v[1].deptname}</custom:tableItem>
					<custom:tableItem>${v[0].modelName}</custom:tableItem>
					<custom:tableItem>${v[1].ipAddress}</custom:tableItem>
					<custom:tableItem>${v[1].adminName}</custom:tableItem>
					<custom:tableItem>${v[1].adminPass}</custom:tableItem>
					<s:if test="%{editble == 1}">
					<custom:tableItem>
						<custom:show path="monitor/ip-cam" id="${v[1].cameraID}" />
						<custom:edit path="monitor/ip-cam" id="${v[1].cameraID}" />
						<a onclick="delCam(${v[1].cameraID});" href="#"><img title="Delete" src="<%=basePath%>images/dtree/ico_delete.gif"></a>
					</custom:tableItem>
					</s:if>
				</custom:tableBody>
			</custom:table>
			<s:include value="/common/page.jsp">
				<s:param name="pageName" value="'page'" />
				<s:param name="formName" value="'queryResult'" />
				<s:param name="linkCount" value="5" />
			</s:include>
			<script type="text/javascript">
				var select_model = dhtmlXComboFromSelect_cascade('select_model', 100);
				select_model.enableFilteringMode(true);
			</script>
		</form>
	</body>
</html>