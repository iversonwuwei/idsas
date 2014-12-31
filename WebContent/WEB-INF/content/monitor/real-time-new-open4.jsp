<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<style type="text/css">
			table { border:0; border-collapse:collapse; }
			table td {
				border: solid 1px #FFF;
			}
			table td div {
				width: 300px; height: 300px; line-height: 300px; text-align: center;
			}
		</style>
		<script type="text/JavaScript">
			$(function(){
			});
		</script>
	</head>
	<body>
		<table style="border:0; border-collapse:collapse;">
			<tr>
				<td>
					<div>
						<s:include value="/common/ipCam_mod.jsp">
							<s:param name="id" value="%{camList[0].camID}" /> 
							<s:param name="ip" value="%{camList[0].camIP}" />	
							<s:param name="port" value="'80'" />
							<s:param name="channel" value="%{camList[0].channel}" />	
							<s:param name="username" value="%{camList[0].userName}" /> 
							<s:param name="password" value="%{camList[0].password}" />
							<s:param name="playerType" value="'2'" />
							<s:param name="camType" value="%{camList[0].camType}" />
						</s:include>
					</div>
				</td>
				<td>
					<div>
						<s:include value="/common/ipCam_mod.jsp">
							<s:param name="id" value="%{camList[1].camID}" /> 
							<s:param name="ip" value="%{camList[1].camIP}" />	
							<s:param name="port" value="'81'" />
							<s:param name="channel" value="%{camList[1].channel}" />	
							<s:param name="username" value="%{camList[1].userName}" /> 
							<s:param name="password" value="%{camList[1].password}" />
							<s:param name="playerType" value="'2'" />
							<s:param name="camType" value="%{camList[1].camType}" />
						</s:include>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div>
						<s:include value="/common/ipCam_mod.jsp">
							<s:param name="id" value="%{camList[2].camID}" /> 
							<s:param name="ip" value="%{camList[2].camIP}" />	
							<s:param name="port" value="'82'" />
							<s:param name="channel" value="%{camList[2].channel}" />	
							<s:param name="username" value="%{camList[2].userName}" /> 
							<s:param name="password" value="%{camList[2].password}" />
							<s:param name="playerType" value="'1'" />
							<s:param name="camType" value="%{camList[2].camType}" />
						</s:include>
					</div>
				</td>
				<td>
					<div>
						<s:if test="%{camList[3].camID != null}">
							<s:include value="/common/ipCam_mod.jsp">
								<s:param name="id" value="%{camList[3].camID}" /> 
								<s:param name="ip" value="%{camList[3].camIP}" />	
								<s:param name="port" value="'83'" />
								<s:param name="channel" value="%{camList[3].channel}" />	
								<s:param name="username" value="%{camList[3].userName}" /> 
								<s:param name="password" value="%{camList[3].password}" />
								<s:param name="playerType" value="'1'" />
								<s:param name="camType" value="%{camList[3].camType}" />
							</s:include>
						</s:if>
						<s:else><h3><font color="#FFF">EMPTY</font></h3></s:else>
					</div>
				</td>
			</tr>
		</table>
	</body>
</html>