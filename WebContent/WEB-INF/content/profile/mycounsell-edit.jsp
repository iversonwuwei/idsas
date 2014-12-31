<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
<head>
	<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/profile/counsell.js"></script>
	</head>
	<body>
		<custom:navigation father="Profile" model="My Counselling List" operate="Edit" saveMethod="sub2();" homePath="profile/mycounsell" />
		<form action="<%=basePath%>profile/mycounsell!create" name="queryResult" method="post">
			<input type="hidden" name="cs.counseling" id="counseling" value="${cs.counseling}" /> 
			<input type="hidden" name="cs.isdelete" id="isdelete" value="${cs.isdelete}" /> 
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="18%">User:</td>
						<td width="82%" style="text-align: left;" class="valid">
						${cs.username}
						<input type="hidden" name="cs.username" id="username" value="${cs.username}" /> 
						<input type="hidden" name="cs.userid" id="userid" value="${cs.userid}" /> 
						</td>
					</tr>
					<tr>
						<td class="text_bg">Driver:</td>
						<td style="text-align: left;" class="valid">
						<s:select  list="driList" listKey="key" listValue="value" name="cs.driverid" id="driverid" theme="simple" />
						<input type="hidden" name="cs.drivername" id="drivername" value="${cs.drivername}" /> 
						</td>
					</tr>
					<tr>
						<td class="text_bg">Start Time:</td>
						<td style="text-align: left;">
							<label class="input_date">
							<input id="startime" name="cs.startime" value="${cs.startime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm', maxDate:'#F{$dp.$D(\'endtime\')||\'%y-%M-%d %H:%m\'}'})"/>
							</label>
						</td>
					</tr>
					<tr>
						<td class="text_bg">End Time:</td>
						<td style="text-align: left;">
							<label class="input_date">
							<input id="endtime" name="cs.endtime" value="${cs.endtime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm', maxDate:'%y-%M-%d %H:%m',minDate:'#F{$dp.$D(\'startime\')}'})"/>
							</label>
						</td>
					</tr>
					<tr>
						<td class="text_bg">Remark:</td>
						<td style="text-align: left;"  class="valid"><input type="text" id="remark" name="cs.remark" value="${cs.remark}" maxlength="30" class="5,1,30" /></td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>