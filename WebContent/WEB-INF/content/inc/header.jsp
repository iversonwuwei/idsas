<%
	String path = request.getContextPath() + "/";
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<!DOCTYPE HTML>
<%@include file="taglib.jsp" %>
<%@include file="import.jsp" %>
<script type="text/JavaScript">
	var basePath = "<%=basePath%>";
	var contextPath = "<%=path%>";
	var isMsie = /msie/.test(navigator.userAgent.toLowerCase());
	var isMozilla = /firefox/.test(navigator.userAgent.toLowerCase());
	var isWebkit = /webkit/.test(navigator.userAgent.toLowerCase());
	var isOpera = /opera/.test(navigator.userAgent.toLowerCase());
	var chartColorArr = [ '#4572A7', '#AA4643', '#89A54E', '#71588F', '#4198AF', '#DB843D', '#93A9CF', '#D19392', '#B9CD96', '#A99BBD' ];
	$(function(){
		if ("" != "${highlight}") { //Add Highlight
			$("#tr${highlight}").css("background", "#6495DC");
		}
	});
</script>