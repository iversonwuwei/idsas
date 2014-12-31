<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath() + "/";
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<script type="text/JavaScript">
			$(function(){
				zdcomaudio.PlayNet("172.100.100.35", 18026, "demo3", 2, '${deviceName}', 1, 350, 350);
			});
			
			function disConnect() {
				zdcomaudio.CloseOCX();
			}
		</script>
	</head>
	<body style="width: 600px; height: 600px;" onbeforeunload="disConnect();">
		<object name='zdcomaudio' classid='clsid:7E6CC141-11E8-41AE-A1BE-CF6606B1DB48'
				codebase='<%=basePath%>cabs/ZdcomAudio.cab#version=1,0,9,0'
				width='100%' height='100%'>
		</object>
	</body>
</html>