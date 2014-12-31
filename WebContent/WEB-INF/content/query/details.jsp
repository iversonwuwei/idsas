<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
       <script type="text/javascript">
       $(document).ready(function() {
	        if(0 < "${fn:length(page.result)}"){
	        	var mysuperTable = new superTable("maintable5422",{headerRows :1,fixedCols :3});
	        }
	     });
	        function excel(){
	        	if(0 == "${fn:length(page.result)}"){
	            	alert("No derived data!");
	            	return;
	        	}
	        	document.forms['kindForm'].action='<%=basePath%>query/details!exportDetail';
	            document.forms['kindForm'].submit();
	            document.forms['kindForm'].action='<%=basePath%>query/details!index';
	        }
       </script>
    </head>
	<body>
		<custom:navigation father="Driver Report" model="Event List" excelMethod="excel()"/>
		<form id="kindForm" name="queryResult" action="<%=basePath%>query/details!index" method="post">
			 <custom:searchBody>
				<custom:searchItem title="Fleet" side="first">
					<custom:selTeam name="dvo.orgId" value="${dvo.orgId}" />
				</custom:searchItem>
			<custom:searchItem title="Plate Number">
				<custom:selVeh name="dvo.vehId" value="${dvo.vehId}" />
			</custom:searchItem>
			<custom:searchItem title="Driver">
				<custom:selDriver name="dvo.driverId" value="${dvo.driverId}" />
			</custom:searchItem>
				<custom:searchItem title="Date">
					<custom:date dateName="dvo.riqi" dateValue="${dvo.riqi}" id="riqi" ></custom:date>
				</custom:searchItem>
				<custom:searchItem title="Type">
				    <s:select list="btlist" listKey="key" listValue="value" headerKey="-1" headerValue="ALL" theme="simple" id="behType" name="dvo.behType"></s:select>
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="sub();" />
				</custom:searchItem>
			</custom:searchBody>
			<custom:table id="maintable5422" width="1210px">
				<custom:tableHeader operate="false" width="50px">
					<custom:sort property="driver_name" page="${page}" width="60px">Driver</custom:sort>
					<custom:sort property="vehcode" page="${page}" width="100px">Plate Number</custom:sort>
					<custom:sort property="dept_name" page="${page}" width="90px">Department</custom:sort>
					<custom:sort property="orgName" page="${page}" width="70px">Fleet</custom:sort>
					<custom:sort property="riqi" page="${page}" width="90px">Date</custom:sort>
					<custom:sort property="timeBegin" page="${page}" width="80px">Start Time</custom:sort>
					<custom:sort property="timeEnd" page="${page}" width="80px">End Time</custom:sort>
					<custom:sort property="timeCont" page="${page}" width="100px">Continuance</custom:sort>
					<custom:sort property="behType" page="${page}" width="90px">Type</custom:sort>
					<custom:sort property="vehSpeed" page="${page}" width="60px">Value</custom:sort>
					<custom:sort property="longitude" page="${page}" width="90px">Lng</custom:sort>
					<custom:sort property="latitude" page="${page}" width="90px">Lat</custom:sort>
					<td width="70px">Update</td>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="notId" index="i" var="v">
					<custom:tableItem>${v.driver_name}</custom:tableItem>
					<custom:tableItem>${v.vehcode}</custom:tableItem>
					<custom:tableItem>${v.dept_name}</custom:tableItem>
					<custom:tableItem>${v.orgName}</custom:tableItem>
					<custom:tableItem>${v.riqi}</custom:tableItem>
					<custom:tableItem>${v.timeBegin}</custom:tableItem>
					<custom:tableItem>${v.timeEnd}</custom:tableItem>
					<custom:tableItem><custom:formatTime value="${v.timeCont}"></custom:formatTime></custom:tableItem>
					<custom:tableItem>${v.beh_name}</custom:tableItem>
					<custom:tableItem>${v.vehSpeed}</custom:tableItem>
					<custom:tableItem>${v.longitude}</custom:tableItem>
					<custom:tableItem>${v.latitude}</custom:tableItem>
					<td>
						<c:if test="${kind=='1'}">
							<custom:show path="query/details" method="show?kind=1" id="${v.notId}"/>
						</c:if>
						<c:if test="${kind!='1'}">
							<custom:show path="query/details" id="${v.notId}"/>
						</c:if>
					</td>
				</custom:tableBody>
			</custom:table> 
			<div class="page">
  				<s:include value="/common/page.jsp">
					<s:param name="pageName" value="'page'"/>
					<s:param name="formName" value="'queryResult'"/>
					<s:param name="linkCount" value="5"/>
				</s:include>
			</div>
		</form>
	</body>
</html>