<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/system/score.js"></script>
		<script type="text/javascript">
		</script>
	</head>
	<body>
		<custom:navigation father="System Settings" model="Score Standard" operate="Edit" saveMethod="sub2();" homePath="system/score" />
		<form action="<%=basePath%>system/score!save" id="queryResult" name="queryResult" method="post">
			<input type="hidden" name="sco.scoreid" value="${sco.scoreid}"/>
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="18%">Vehicle Type:</td>
						<td width="82%" style="text-align: left;" class="valid">
							${sco.vehicletype}
							<input type="hidden" name="sco.vehicletypeid" value="${sco.vehicletypeid}" />
							<input type="hidden" name="sco.vehicletype" value="${sco.vehicletype}"/>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Type:</td>
						<td style="text-align: left;" class="valid">
							${sco.type}
							<input type="hidden" name="sco.typeid" value="${sco.typeid}" />
							<input type="hidden" name="sco.type" id="type" value="${sco.type}" />
						</td>
					</tr>
					<tr>
						<td class="text_bg">Max Count:</td>
						<td style="text-align: left;" class="valid">
							<input type="text" id="maxcount" name="sco.maxcount" value="${sco.maxcount}" maxlength="6" class="0,1,20"/>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Deduct Points:</td>
						<td style="text-align: left;" class="valid">
							<input type="text" id="deductpoints" name="sco.deductpoints" value="${sco.deductpoints}" maxlength="6" class="0,1,20"/>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Max Time:</td>
						<td style="text-align: left;" class="valid">
							<input type="text" id="maxtime" name="sco.maxtime" value="${sco.maxtime}" maxlength="6" class="0,1,20"/>s
						</td>
					</tr>
					<tr>
						<td class="text_bg">Deduct Time:</td>
						<td style="text-align: left;" class="valid">
							<input type="text" id="deductime" name="sco.deductime" value="${sco.deductime}" maxlength="6" class="0,1,20"/>s
						</td>
					</tr>
					<tr>
						<td class="text_bg">Weight:</td>
						<td style="text-align: left;" class="valid">
							<input type="text" id="weight" name="sco.weight" value="${sco.weight}" maxlength="3" class="0,1,20"/>
						</td>
					</tr>
			</table>
		</div>
	</form>
</body>
</html>