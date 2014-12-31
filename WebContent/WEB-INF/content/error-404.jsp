<%
	String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<body>
		<center style="cursor: pointer;" title="Home Page" onclick="window.parent.location='<%=path%>'">
			<div style="margin-top: 200px;">
				<span style="font-size: 24px;">Sorry,The page you requested could not be found!</span>
				<br />
				<span style="font-size: 160px;">404</span>
			</div>
		</center>
	</body>
</html>