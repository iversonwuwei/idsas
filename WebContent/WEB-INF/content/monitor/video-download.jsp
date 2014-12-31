<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<script type="text/javascript" src="<%=basePath%>scripts/specific/monitor/video-download.js"></script>
	</head>
	<body>
		<custom:navigation father="Monitor" model="Video Download"/>
		<form name="queryResult" action="<%=basePath%>monitor/video-download!index" method="post">
			<s:hidden name="editble" value="%{editble}"/>
			<s:hidden id="deviceIP" name="deviceIP" value="%{deviceIP}"/>
			<custom:searchBody>
				<custom:searchItem title="Fleet" side="first">
					<custom:selTeam name="fleetID" value="${fleetID}" />
				</custom:searchItem>
				<custom:searchItem title="Vehicle Name">
					<custom:selVehName name="vehicleID" value="${vehicleID}" />
					<s:hidden id="v_vehicleName" name="vehicle_name" value="%{vehicle_name}" />
				</custom:searchItem>
				<custom:searchItem title="Date">
					<custom:date id="select_date" dateName="search_date" dateValue="${search_date}" dateFmt="yyyy-MM-dd" />
				</custom:searchItem>
				<custom:searchItem title="Time">
					<label class="input_date_l"><input 
						type="text" id="begin_time" name="begin_time" value="${begin_time}" onfocus="WdatePicker({dateFmt:'HH:mm:ss', maxDate:&quot;#F{$dp.$D('end_time')||'%y-%M-%d'}&quot;, readOnly:true});this.blur();" /></label><label class="input_date_r"><input
						type="text" id="end_time" name="end_time" value="${end_time}" onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:&quot;#F{$dp.$D('begin_time')}&quot;, maxDate:'%y-%M-%d', readOnly:true});this.blur();" />
					</label>
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="doSearch();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="true">
					<!-- <td width="10%"><label>Media Type</label></td> -->
					<td width="30%"><label>Storage Path</label></td>
					<!-- <td width="10%"><label>Resolution</label></td> -->
					<td width="20%"><label>Trigger Time</label></td>
					<td width="20%"><label>Begin Time</label></td>
					<td width="20%"><label>End Time</label></td>
				</custom:tableHeader>
				<custom:tableBody page="${page}" id="label" index="i" var="v">
					<%-- <custom:tableItem>${v.mediaType}</custom:tableItem> --%>
					<custom:tableItem>${v.destPath}</custom:tableItem>
					<%-- <custom:tableItem>${v.resolution}</custom:tableItem> --%>
					<custom:tableItem>${v.triggerTime}</custom:tableItem>
					<custom:tableItem>${v.beginTime}</custom:tableItem>
					<custom:tableItem>${v.endTime}</custom:tableItem>
					<custom:tableItem>
						<a href="#" onclick="doDownload('${v.destPath}')">
							<img src="<%=basePath%>images/tools_import.gif" title="Download" />
						</a>
					</custom:tableItem>
				</custom:tableBody>
			</custom:table>
		</form>
	</body>
</html>