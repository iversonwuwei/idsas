<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="../css/top.css"/>
	</head>
	<body>
    	<div class="top">
    		<ul>
          		<li style="float:left; height:34px; width:500px; margin-left:20px;margin-top:15px;">
          			<img src="<%=basePath%>images/Title01.jpg" width="559px" height="42px" />
          		</li>
		  		<li>
		  			<a href="#" onclick="logout();">
		  				<img src="<%=basePath%>images/dtree/ico_exit.gif" align="absmiddle" border="0" />Logout
		  			</a>
		  		</li>
			</ul>
		</div>
	</body>
</html>