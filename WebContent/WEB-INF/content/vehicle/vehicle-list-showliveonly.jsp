<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=basePath%>css/gridding.css" rel="stylesheet" type="text/css" />
<title>Vehicle List</title>
<script type="text/JavaScript">
$(function(){
	var _height = $(window).height()
	- (null == $(".search_g").height() ? 0 :  $(".search_g").height())
//	$(".pop_one").height(_height-35);
});
function checkit(){
	
	location="<%=basePath%>vehicle/vehicle-list!showonly?id="+$('#vehicleid').val()+"&cdid="+$('#cdid').val()+"&soid="+$('#ssoft').val();
}

</script>
</head>
<body>
	<form name="queryResult" action="<%=basePath%>vehicle/vehicle-list!save" method="post">
		<div class="search" style="font-size: 12px;height: 14px">
			<div ><input type="hidden" id="vehicleid" value="${vehv.vehicleid}"/>
			Vehicle: ${vehv.vehiclename} &nbsp;&nbsp;
			 Channl:<s:select id="cdid" list="channllist" theme="simple" listKey="cdID" listValue="channel"></s:select>&nbsp;<br />
			 Video Plug-in:<s:select id="ssoft" list="#{'1':'VLC' ,'2':'Original player'}" theme="simple"></s:select>&nbsp;
			 <input type="button" value="ok" onclick="checkit()"/>
			 <%--
			<input type="radio" checked="checked">1×1</input>&nbsp;&nbsp;&nbsp;&nbsp; 
			<input type="radio" onclick="checkit(4)">1×2</input>&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio"  onclick="checkit(1)">2×2</input>&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio"  onclick="checkit(2)">2×4</input>&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio"  onclick="checkit(3)">4×4</input>&nbsp;&nbsp;&nbsp;&nbsp;
			  --%>
			
			
			</div>
		</div>
		
	</form>
</body>
</html>