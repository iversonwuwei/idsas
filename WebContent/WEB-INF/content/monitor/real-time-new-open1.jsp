<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html>
	<head>
		<script type="text/JavaScript">
			$(function(){
			});
		</script>
	</head>
	<body style="width: 600px; height: 600px;">
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
	</body>
</html>