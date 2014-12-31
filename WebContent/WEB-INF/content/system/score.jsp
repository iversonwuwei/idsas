<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript">
			function sub1() {
				$('#queryResult').attr('action', '<%=basePath%>system/score!save');
				$("#queryResult").submit();
			}
		</script>
	</head>
	<body>
		<custom:navigation father="System Settings" model="Score Standard" saveMethod="sub1();" />
		<form name="queryResult" id="queryResult" action="<%=basePath%>system/score!index" method="post" >
			<custom:searchBody row="1">
				<custom:searchItem title="Vehicle Type" side="first">
					<%-- <custom:select list="${vehicleTypeList}" name="scoVo.vehId" value="${scoVo.vehId}" width="200px" /> --%>
					<s:select list="vehicleTypeList" name="scoVo.vehId" id="vehId" theme="simple" listKey="key" listValue="value" cssStyle="width:200px;" />
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="sub();"/>
				</custom:searchItem>
			</custom:searchBody>
			<custom:table>
				<custom:tableHeader operate="false">
					<td width="15%">Vehicle Type</td>
					<td width="15%">Alerting Type</td>
					<td width="20%">Max Count</td>
					<td width="20%">Max Time(min)</td>
					<td width="20%">Score</td>
				</custom:tableHeader>
				<s:iterator value="scoreList" var="u" status="r">
					<tr class="Reprot_td" id="tr${v.scoreid}">
				      	<td>
	       					<input type="hidden" value="${u.scoreid}" name="scoreList[${r.index}].scoreid" style="text-align: right;" />
							<label>${r.index + 1}</label>
						</td>
						<td>${u.vehicletype}</td>
						<td>${u.type}</td>
						<td>
							<input type="text" style="max-width:40%;background-color: #FFF5EE;text-align: right;" 
								name="scoreList[${r.index}].maxcount" value="${u.maxcount}" id="maxcount_${u.typeid}" maxlength="5" 
									onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" />
						</td>
						<td>
						<c:if test="${u.typeid!=11 &&u.typeid!=12 &&u.typeid!=13 &&u.typeid!=14 }">
							<input type="text" style="max-width:40%;background-color: #FFF5EE;text-align: right;" 
								name="scoreList[${r.index}].maxtime" value="${u.maxtime}" id="maxtime_${u.typeid}" maxlength="5" 
									onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" />min
						</c:if>
							<c:if test="${u.typeid==11 ||u.typeid==12 ||u.typeid==13 ||u.typeid==14 }">
							--------
							</c:if>		
						</td>
						<td>
							<input type="text" style="max-width:40%;background-color: #FFF5EE;text-align: right;"
								name="scoreList[${r.index}].weight" value="${u.weight}" id="weight_${u.typeid}" maxlength="3" 
									onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" />
						</td>
				</s:iterator>
			</custom:table>
		</form>
	</body>
	<script type="text/javascript">
		var vehId = dhtmlXComboFromSelect("vehId");
		vehId.enableFilteringMode(true);
	</script>
</html>