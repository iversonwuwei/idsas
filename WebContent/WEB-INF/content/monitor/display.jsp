<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/OpenLayers-2.12/OpenLayers.js"></script>
	
	</head>
	<body>
		<custom:navigation father="Monitor" model="Playback"/>
		<form name="queryResult" action="<%=basePath%>monitor/display!index" method="post">
			<s:hidden name="editble" value="%{editble}"/>
			<custom:searchBody>
					<custom:searchItem title="Fleet" side="first">
						<custom:selTeam name="vo.fleetID" value="${vo.fleetID}" />
					</custom:searchItem>
					<custom:searchItem title="Vehicle Name">
						<custom:selVehName name="vo.vehID" value="${vo.vehID}" />
					</custom:searchItem>
					<custom:searchItem side="last" width="40%">
						<custom:search onclick="sub();"/>
					</custom:searchItem>
				</custom:searchBody>
			<custom:table>
				<custom:tableHeader>
					<custom:sort property="fleetname" page="${page}" width="30%">Fleet</custom:sort>
					<custom:sort property="vehiclename" page="${page}" width="30%">Vehicle Name</custom:sort>
					<custom:sort property="licenseplate" page="${page}" width="30%">Plate Number</custom:sort>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="vehicleid" index="i" var="v">
					<custom:tableItem>${v.fleetname}</custom:tableItem>
					<custom:tableItem>${v.vehiclename}</custom:tableItem>
					<custom:tableItem>${v.licenseplate}</custom:tableItem>
					<s:if test="%{editble == 1}">
						<custom:tableItem>
							<custom:show path="monitor/display" id="${v.vehicleid}" />
						</custom:tableItem>
					</s:if>
				</custom:tableBody>
			</custom:table>
			<s:include value="/common/page.jsp">
				<s:param name="pageName" value="'page'" />
				<s:param name="formName" value="'queryResult'" />
				<s:param name="linkCount" value="5" />
			</s:include>
		</form>
	</body>
</html>