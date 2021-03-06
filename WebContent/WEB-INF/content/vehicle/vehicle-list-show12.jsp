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
	-( null == $(".welcome").height() ? 0 :  $(".welcome").height())
	- (null == $(".search_g").height() ? 0 :  $(".search_g").height())
	- (null == $(".page").height() ? 0 :  $(".page").height());
	$(".pop_one").height(_height-35);
});
function checkit(id){
	
	if(id==1){
		location="<%=basePath%>vehicle/vehicle-list/"+$('#vehicleid').val()+"/showlivet";
	}
	if(id==2){
		location="<%=basePath%>vehicle/vehicle-list/"+$('#vehicleid').val()+"/showlivef";
	}
	if(id==3){
		location="<%=basePath%>vehicle/vehicle-list/"+$('#vehicleid').val()+"/showlive";
	}
	if(id==4){
		location="<%=basePath%>vehicle/vehicle-list/"+$('#vehicleid').val()+"/showliveone";
	}
}
</script>
</head>
<body>
	<custom:navigation father="Organization Management" model="Vehicle" operate="Live" homePath="vehicle/vehicle-list?editble=1" />
	<form name="queryResult" action="<%=basePath%>vehicle/vehicle-list!save" method="post">
		<div class="search" style="font-size: 15px;">
			<div style="padding-top: 8px;padding-left: 20px"><input type="hidden" id="vehicleid" value="${vehv.vehicleid}"/>
			Vehicle Name: ${vehv.vehiclename} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Device:${vehv.devicename}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" onclick="checkit(4)">1×1</input>&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" checked="checked">1×2</input>&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio"  onclick="checkit(1)">2×2</input>&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio"  onclick="checkit(2)">2×4</input>&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio"  onclick="checkit(3)">4×4</input>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
		</div>
		<div class="pop_one">
			<ul style="height: 99%">
				<li style="border-top: solid 1px #000;border-left:  solid 1px #000;width: 49.7%">
					<s:include value="/common/ipCam_mod.jsp">
						<s:param name="id" value="'channel1'" /> 
						<s:param name="ip" value="%{cameras[0].ipaddress}" />	
						<s:param name="channel" value="'1'" />	
						<s:param name="username" value="%{cameras[0].username}" /> 
						<s:param name="password" value="%{cameras[0].password}" />
					</s:include>
				</li>
				<li style="border-top: solid 1px #000;width: 49.7%">
					<s:include value="/common/ipCam_mod.jsp">
						<s:param name="id" value="'channel2'" /> 
						<s:param name="ip" value="%{cameras[1].ipaddress}" />	
						<s:param name="channel" value="'2'" />	
						<s:param name="username" value="%{cameras[1].username}" /> 
						<s:param name="password" value="%{cameras[1].password}" />
					</s:include>
				</li>
			</ul>
		</div>
	</form>
</body>
</html>