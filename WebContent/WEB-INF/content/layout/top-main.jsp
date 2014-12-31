<%
	String path = request.getContextPath() + "/";
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<link type="text/css" href="<%=basePath%>/css/welcome.css" rel="stylesheet" />
	</head>
	<body>
		<div class="welcome">
			<div class="user"><img src="<%=basePath%>/images/temp_34.gif" width="6" height="29" align="absmiddle" 
			/><img src="<%=basePath%>/images/dtree/ico_user.gif" width="16" height="16" align="absmiddle" 
			/>&nbsp;&nbsp;Welcome&nbsp;&nbsp;${session.mainUser.loginName}&nbsp;&nbsp;[${session.mainUser.userRole.roleName}]&nbsp;
					Current Position&nbsp;:&nbsp;<span style='font-weight:bold'>Welcome Page</span>
			</div>
		</div>
		<div style="width:100%; text-align:center; font-size:22px; font-family:微软雅黑; padding-top:200px;">欢迎光临驾驶行为监控系统</div>
	</body>
</html>