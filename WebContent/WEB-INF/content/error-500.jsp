<%
	String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<body>
		<center style="cursor: pointer;" title="Home Page" onclick="window.parent.location='<%=path%>'">
			<div style="margin-top: 200px;">
				<span style="font-size: 24px;">An error occurred, please contact the administrator!</span>
				<br />
				<span style="font-size: 160px;">500</span>
			</div>
		</center>
	</body>
</html>