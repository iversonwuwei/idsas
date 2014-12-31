<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<script type="text/javascript" src="<%=basePath%>scripts/common/layer/layer.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>scripts/specific/monitor/ip-cam.js"></script>
		<script type="text/javascript">
	 		function excel(){
     			if(0 == "${fn:length(page.result)}") {
	         		alert("No derived data!");
	         		return;
     			}
     			document.forms['queryResult'].action='<%=basePath%>vehicle/vehicle-list!exportDetail';
         		document.forms['queryResult'].submit();
         		document.forms['queryResult'].action='<%=basePath%>vehicle/vehicle-list!index';
     		}
		</script>
	</head>
	<body>
		<s:if test="%{editble == 1}">
			<custom:navigation father="Organization Management" model="Vehicle" addPath="vehicle/vehicle-list" excelMethod="excel()"  addMethod="add" />
		</s:if>
		<s:else>
			<custom:navigation father="Organization Query" model="Vehicle List" excelMethod="excel()" />
		</s:else>
		<form name="queryResult" action="<%=basePath%>vehicle/vehicle-list!index" method="post">
			<s:hidden name="editble" value="%{editble}" />
			<custom:searchBody row="2">
				<custom:searchItem title="Department" side="first">
					<custom:selCom name="vehvo.deptID" value="${vehvo.deptID}" />
				</custom:searchItem>
				<custom:searchItem title="Fleet">
					<custom:selTeam name="vehvo.fleetid" value="${vehvo.fleetid}" />
				</custom:searchItem>
				<custom:searchItem title="Plate Number">
					<s:textfield theme="simple" name="vehvo.vehiclename" maxlength="10" onblur="this.value=$.trim(this.value)"/>
				</custom:searchItem>
				<custom:searchItem title="Device" side="last">
					<s:select list="devicelist" name="vehvo.deviceid" listKey="key" listValue="value" headerKey="" headerValue="All" theme="simple" cssStyle="width: 120px;" />
				</custom:searchItem>
				<custom:searchItem title="Vehicle Type" side="first">
					<s:select list="typelist" name="vehvo.typeid" listKey="key" listValue="value" headerKey="" headerValue="All" theme="simple" cssStyle="width: 102px;" />
				</custom:searchItem>
				<custom:searchItem title="Vehicle Brand">
					<s:textfield theme="simple" name="vehvo.brand" maxlength="20" onblur="this.value=$.trim(this.value)"/>
				</custom:searchItem>
				<custom:searchItem title="Key Code">
					<s:textfield theme="simple" name="vehvo.keyCode" maxlength="20" onblur="this.value=$.trim(this.value)"/>
				</custom:searchItem>
				<custom:searchItem title="IP">
					<s:textfield theme="simple" name="vehvo.cctvip" maxlength="20" onblur="this.value=$.trim(this.value)"/>
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true">
					<custom:sort property="vehiclename" page="${page}" width="10%">Plate<br />Number</custom:sort>
					<custom:sort property="fleetname" page="${page}" width="9%">Fleet</custom:sort>
					<custom:sort property="deptname" page="${page}" width="10%">Department</custom:sort>
					<custom:sort property="devicename" page="${page}" width="10%">Device</custom:sort>
					<custom:sort property="typename" page="${page}" width="9%">Vehicle <br />Type</custom:sort>
					<custom:sort property="brandname" page="${page}" width="9%">Vehicle <br />Brand</custom:sort>
					<custom:sort property="keycode" page="${page}" width="10%">Key <br />Code</custom:sort>
					<custom:sort property="cctvip" page="${page}" width="10%">IP</custom:sort>
					<custom:sort property="description" page="${page}" width="13%">Description</custom:sort>				
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="vehicleid" index="i" var="v">
					<custom:tableItem>${v.vehiclename}</custom:tableItem>
					<custom:tableItem>${v.fleetname}</custom:tableItem>
					<custom:tableItem>${v.deptname}</custom:tableItem>
					<custom:tableItem>${v.devicename}</custom:tableItem>
					<custom:tableItem>${v.typename}</custom:tableItem>
					<custom:tableItem>${v.brandname}</custom:tableItem>
					<custom:tableItem>${v.keycode}</custom:tableItem>
					<custom:tableItem>
						<!-- 过滤掉null -->
						<c:choose>
							<c:when test="${fn:containsIgnoreCase(v.cctvip, 'null')}"></c:when>
							<c:otherwise>${v.cctvip}</c:otherwise>
						</c:choose>
					</custom:tableItem>
					<custom:tableItem>
						<div title="${v.description}" style="width:150px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis">
			        		<label>${v.description}</label>
			        	</div>
					</custom:tableItem>
					<custom:tableItem>
						<custom:show path="vehicle/vehicle-list" id="${v.vehicleid}" />
						<s:if test="%{editble == 1}">
							<custom:edit path="vehicle/vehicle-list" id="${v.vehicleid}" />
							<custom:del path="vehicle/vehicle-list" id="${v.vehicleid}" />
						</s:if>
					</custom:tableItem>
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