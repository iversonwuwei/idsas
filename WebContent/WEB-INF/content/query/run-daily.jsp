<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/JavaScript">
function timeTypeChange(t){
	$("#date1,#date2,#date3,#date4").hide().val("");//隐藏那四个日期框
	$("#date"+t).show();
	IntDate();
}
    function excel(){
	    if(0 == "${fn:length(page.result)}"){
    	   alert("There is no data to Export");
    	   return;
	    }
	    document.queryResult.action='<%=basePath%>query/run-daily!exportDetail';
        document.queryResult.submit();
        document.queryResult.action='<%=basePath%>query/run-daily!index';
	}
	$(function() {
		$("#daytype").change();// 初始页面触发日期类型的change事件
		if (1 == "${rdvo.daytype}") {// 然后赋值
			$("#date1").val("${rdvo.date1}")
		}
		if (2 == "${rdvo.daytype}") {
			$("#date2").val("${rdvo.date2}")
		}
		if (3 == "${rdvo.daytype}") {
			$("#date3").val("${rdvo.date3}")
		}
		if (4 == "${rdvo.daytype}") {
			$("#date4").val("${rdvo.date4}")
		}
	});
	function sub(){
		IntDate();
		beforeSubmit();
		document.queryResult.submit();
	}
	function IntDate(){
		if (2 == $("#daytype").val() && "" == $("#date2").val()) {
			$("#date2").val("${today}")
		}
		if (3 == $("#daytype").val() && "" == $("#date3").val()) {
			$("#date3").val("${today}".substring(0, 7));
		}
		if (4 ==  $("#daytype").val()&& "" == $("#date4").val()) {
			$("#date4").val("${today}".substring(0, 4))
		}
	}
</script>
</head>
<body>
	<custom:navigation father="Data Query" model="Vehicle Run Query" excelMethod="excel()" />
	<form id="queryResult" name="queryResult" action="<%=basePath%>query/run-daily!index" method="post">
		<input type="hidden" name="kind" value="${kind}"></input>
		<custom:searchBody>
			<custom:searchItem title="Plate Number" side="first">
				<custom:selVeh name="rdvo.veh_id" value="${rdvo.veh_id}" />
			</custom:searchItem>
			<custom:searchItem title="Stat Type">
				<s:select id="daytype" name="rdvo.daytype" list="#{1:'Daily',2:'Weekly',3:'Monthly',4:'Yearly'}" theme="simple" onchange="timeTypeChange(this.value)"></s:select>
			</custom:searchItem>
			<custom:searchItem title="Date">
					<custom:date dateName="rdvo.date1" dateValue="${rdvo.date1}" id="date1" />
					<custom:date dateName="rdvo.date2" dateValue="${rdvo.date2}" id="date2" isShowWeek="true" />
					<custom:date dateName="rdvo.date3" dateValue="${rdvo.date3}" id="date3" dateFmt="yyyy-MM" />
					<custom:date dateName="rdvo.date4" dateValue="${rdvo.date4}" id="date4" dateFmt="yyyy" />
			</custom:searchItem>
			<custom:searchItem side="last" width="40%">
				<custom:search onclick="sub();" />
			</custom:searchItem>
		</custom:searchBody>
		<custom:table>
			<custom:tableHeader width="1%" operate="false">
				<custom:sort property="_vehcode" page="${page}" width="3%">Plate Number</custom:sort>
				<s:if test="%{rdvo.daytype == 1}">
					<custom:sort property="_riqi" page="${page}" width="3%">Date</custom:sort>
					<custom:sort property="_time_start" page="${page}" width="3%">Start Time</custom:sort>
					<custom:sort property="_time_stop" page="${page}" width="3%">End Time</custom:sort>
				</s:if>
				<s:else>
					<td style="width: 3%;">Start Date</td>
					<td style="width: 3%;">End Date</td>
				</s:else>
				<custom:sort property="_period_run" page="${page}" width="3%">Period Run</custom:sort>
				<custom:sort property="_period_drive" page="${page}" width="3%">Period Drive</custom:sort>
				<custom:sort property="_period_idle" page="${page}" width="3%">Period Idle</custom:sort>
				<custom:sort property="_period_air" page="${page}" width="3%">Period Air</custom:sort>
				<custom:sort property="_period_hot" page="${page}" width="3%">Period Hot</custom:sort>
				<custom:sort property="_km_run" page="${page}" width="2%">Run(Km)</custom:sort>
				<custom:sort property="_km_begin" page="${page}" width="2%">Begin(Km)</custom:sort>
				<custom:sort property="_km_end" page="${page}" width="2%">End(Km)</custom:sort>
				<custom:sort property="_fuel" page="${page}" width="2%">Fuel(L)</custom:sort>
			</custom:tableHeader>
			<custom:tableBody page="${page}" id="day_id" index="i" var="v">
				<custom:tableItem>${v.vehcode}</custom:tableItem>
				<s:if test="%{rdvo.daytype == 1}">
					<custom:tableItem>${v.riqi}</custom:tableItem>
					<custom:tableItem>${v.time_start}</custom:tableItem>
					<custom:tableItem>${v.time_stop}</custom:tableItem>
				</s:if>
				<s:else>
					<td>${date_star}</td>
					<td>${date_end}</td>
				</s:else>
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
				<custom:tableItem>${v.fuel}</custom:tableItem>
				<c:if test="${fn:length(page.result)==i+1 }">
				<tr style="color: #990000; font-weight: bold;; text-align: center;">
				<td <s:if test='%{rdvo.daytype == 1}'>colspan="5"</s:if><s:else>colspan="4"</s:else>>total:</td>
				<s:iterator value="count" var="c" status="idx">
					<td>
						<c:if test="${idx.index == 5}">
							<fmt:formatNumber value="${c}" pattern="#.##" />
						</c:if>
						<c:if test="${idx.index != 5}">
							<custom:formatTime value="${c}" />
						</c:if>
					</td>
				</s:iterator>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			</c:if>
			</custom:tableBody>
			<!-- 合计部分 -->
			<%-- <tr style="color: #990000; font-weight: bold;; text-align: center;">
				<td <s:if test='%{rdvo.daytype == 1}'>colspan="5"</s:if><s:else>colspan="4"</s:else>>total:</td>
				<s:iterator value="count" var="c" status="idx">
					<td>
						<c:if test="${idx.index == 5}">
							<fmt:formatNumber value="${c}" pattern="#.##" />
						</c:if>
						<c:if test="${idx.index != 5}">
							<custom:formatTime value="${c}" />
						</c:if>
					</td>
				</s:iterator>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr> --%>
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