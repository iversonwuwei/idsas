<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<script type="text/JavaScript">
function excel(){
	 if(0 == "${fn:length(page.result)}"){
    	alert("There is no data to Export");
    	return;
	}
	document.queryResult.action='<%=basePath%>query/run-details!exportDetail';
    document.queryResult.submit();
    document.queryResult.action='<%=basePath%>query/run-details!index';
	}
	$(function() {
		if (0 < "${fn:length(page.result)}") {
			var mysuperTable = new superTable("maintable111", {
				headerRows : 1,
				fixedCols : 5
			});
		}
	});
</script>
<html>
<body>
	<custom:navigation father="Data Query" model="Vehicle Run List" excelMethod="excel()" />
	<form id="queryResult" name="queryResult" action="<%=basePath%>query/run-details!index" method="post">
		<custom:searchBody>
			<custom:searchItem title="Fleet" >
				<custom:selTeam name="rdsVo.org_id" value="${rdsVo.org_id}" />
				<%-- <custom:selCom name="rdsVo.org_id" value="${rdsVo.org_id}" /> --%>
			</custom:searchItem>
			<custom:searchItem title="Plate Number">
				<custom:selVeh name="rdsVo.veh_id" value="${rdsVo.veh_id}" />
			</custom:searchItem>
			<custom:searchItem title="Date">
					<custom:date dateName="rdsVo.riqi" dateValue="${rdsVo.riqi}" id="riqi" ></custom:date>
				</custom:searchItem>
			<custom:searchItem title="Driver">
				<custom:selDriver name="rdsVo.driver_id" value="${rdsVo.driver_id}" />
			</custom:searchItem>
			<custom:searchItem side="last" >
				<custom:search onclick="sub();" />
			</custom:searchItem>
		</custom:searchBody>
		<custom:table width="2000px" id="maintable111">
			<custom:tableHeader operate="false">
				<custom:sort property="org_name" page="${page}" width="6%">Fleet</custom:sort>
				<custom:sort property="vehcode" page="${page}" width="4%">Plate Number</custom:sort>
				<custom:sort property="driver_name" page="${page}" width="4%">Driver</custom:sort>
				<custom:sort property="riqi" page="${page}" width="5%">Date</custom:sort>
				<custom:sort property="time_start" page="${page}" width="6%">Start Time</custom:sort>
				<custom:sort property="time_end" page="${page}" width="6%">End Time</custom:sort>
				<custom:sort property="period_run" page="${page}" width="5%">Period Run</custom:sort>
				<custom:sort property="period_drive" page="${page}" width="5%">Period Drive</custom:sort>
				<custom:sort property="period_idle" page="${page}" width="5%">Period Idle</custom:sort>
				<custom:sort property="period_air" page="${page}" width="5%">Period Air</custom:sort>
				<custom:sort property="period_hot" page="${page}" width="5%">Period Hot</custom:sort>
				<custom:sort property="km_run" page="${page}" width="4%">Run(km)</custom:sort>
				<custom:sort property="km_begin" page="${page}" width="4%">Begin(km)</custom:sort>
				<custom:sort property="km_end" page="${page}" width="4%">End(km)</custom:sort>
				<custom:sort property="spead_avg" page="${page}" width="4%">Speed Avg(km/h)</custom:sort>
				<custom:sort property="fuel" page="${page}" width="4%">Fuel(L)</custom:sort>
				<custom:sort property="fuel_avg" page="${page}" width="4%">Fuel Avg</custom:sort>
			</custom:tableHeader>
			<custom:tableBody page="${page}" id="${run_id}" index="i" var="v">
				<custom:tableItem>${v.org_name}</custom:tableItem>
				<custom:tableItem>${v.vehcode}</custom:tableItem>
				<custom:tableItem>${v.driver_name}</custom:tableItem>
				<custom:tableItem>${v.riqi}</custom:tableItem>
				<custom:tableItem>${v.time_start}</custom:tableItem>
				<custom:tableItem>${v.time_end}</custom:tableItem>
				<custom:tableItem>
					<custom:formatTime value="${v.period_run}"></custom:formatTime>
				</custom:tableItem>
				<custom:tableItem>
					<custom:formatTime value="${v.period_drive}"></custom:formatTime>
				</custom:tableItem>
				<custom:tableItem>
					<custom:formatTime value="${v.period_idle}"></custom:formatTime>
				</custom:tableItem>
				<custom:tableItem>
					<custom:formatTime value="${v.period_air}"></custom:formatTime>
				</custom:tableItem>
				<custom:tableItem>
					<custom:formatTime value="${v.period_hot}"></custom:formatTime>
				</custom:tableItem>
				<custom:tableItem>${v.km_run}</custom:tableItem>
				<custom:tableItem>${v.km_begin}</custom:tableItem>
				<custom:tableItem>${v.km_end}</custom:tableItem>
				<custom:tableItem>${v.spead_avg}</custom:tableItem>
				<custom:tableItem>${v.fuel}</custom:tableItem>
				<custom:tableItem>${v.fuel_avg}</custom:tableItem>
			</custom:tableBody>
				<tr style="color: #990000;font-weight:bold; ;text-align: center;">
					<td colspan="7" >total:</td>
					<s:iterator value="count[0]" var="c" status="x" >
						<s:if test="#x.index != 5">
							<td><custom:formatTime value="${c}"></custom:formatTime></td>
						</s:if>
						<s:else>
							<td>${c}</td>
						</s:else>
					</s:iterator>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
		</custom:table>
		<div class="page">
			<s:include value="/common/page.jsp">
				<s:param name="pageName" value="'page'" />
				<s:param name="formName" value="'queryResult'" />
				<s:param name="linkCount" value="5" />
			</s:include>
		</div>
	</form>
</body>
</html>