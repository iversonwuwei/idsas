<%
	if (null != session.getAttribute("mainUser")) {
		response.sendRedirect("layout/welcome");
	} else {
		response.sendRedirect("login");
	}
%>