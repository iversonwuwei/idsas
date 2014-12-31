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

function disconn(){

try{
rtsps['channel1'].playlist.stop();
}catch(e){
	
}

$("#channel1").remove();
}
</script>
</head>
<body>
	<form name="queryResult" action="<%=basePath%>vehicle/vehicle-list!save" method="post">
		<div class="search" style="font-size: 12px;height: 14px">
			<div ><input type="hidden" id="vehicleid" value="${vehv.vehicleid}"/>
			Vehicle Name: ${vehv.vehiclename} &nbsp;&nbsp;&nbsp;&nbsp;Device:${vehv.devicename}&nbsp;&nbsp;
			<!-- 
			<input type="radio" checked="checked">1×1</input>&nbsp;&nbsp;&nbsp;&nbsp; 
			<input type="radio" onclick="checkit(4)">1×2</input>&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio"  onclick="checkit(1)">2×2</input>&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio"  onclick="checkit(2)">2×4</input>&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio"  onclick="checkit(3)">4×4</input>&nbsp;&nbsp;&nbsp;&nbsp;
			 -->
			
			</div>
		</div>
		<div class="pop_one" style="height: 265px;width: 340px">
			<ul style="height: 99%">
				<li style="border-top: solid 1px #000;border-left:  solid 1px #000;width: 99.6%">
					<s:include value="/common/ipCam_mod.jsp">
						<s:param name="id" value="'channel1'" /> 
						<s:param name="ip" value="%{cameras[0].ipAddress}" />	
						<s:param name="channel" value="'1'" />	
						<s:param name="username" value="%{cameras[0].adminName}" /> 
						<s:param name="password" value="%{cameras[0].adminPass}" />
						<s:param name="type" value="%{cameras[0].mac}" />
						<s:param name="stype" value="%{cameras[0].creater}" />
						<s:param name="port" value="%{cameras[0].gateway}" />
					</s:include>
				</li>
			</ul>
		</div>
	</form>
</body>
</html>