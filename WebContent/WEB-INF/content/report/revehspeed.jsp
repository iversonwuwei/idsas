<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/flot.css" />
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/excanvas.min.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.resize.min.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/specific/report/reveh.js"></script>
	    <script language="javascript" type="text/javascript">
		    $(document).ready(function() {
		    	initTableWidth();
		    	buildMap();
		     });
		    
		    function initTableWidth() {
	    		var h = $(window).height()
	    		-( null == $(".welcome").height() ? 0 :  $(".welcome").height())
	    		- (null == $(".search_g").height() ? 0 :  $(".search_g").height())-30; 
	    		$("#line_chart").height(h+"px");
	    	}
		    
		    function dosome(str){
		    	document.kindForm.action='<%=basePath%>report/revehspeed!clickp?tim='+str;
		    	document.kindForm.submit();
		    }
	    </script>
    </head>
	<body>
		<custom:navigation father="Vehicle Report" model="Event Analysis"/>
		<form id="kindForm" name="queryResult" action="<%=basePath%>report/revehspeed!index" method="post">
			<s:hidden id="dataList" value="%{dates}" />
			 <custom:searchBody>
				<custom:searchItem title="Fleet" >
				<custom:selTeam name="dvo.orgId" value="${dvo.orgId}" />
				</custom:searchItem>
				<custom:searchItem title="Plate Number">
					<custom:selVeh name="dvo.vehId" value="${dvo.vehId}" />
				</custom:searchItem>
				 <custom:searchItem title="Date" width="40%">
					<div style="float:left;"><custom:date dateName="dvo.riqi" dateValue="${dvo.riqi}" id="riqi" ></custom:date>&nbsp;&nbsp;</div>
					<div style="float:left;background:url(../images/bg_double.jpg) no-repeat #ffffff;"><label class="input_k"><s:textfield name="dvo.timeBegin" id="input_date1" theme="simple" onfocus="WdatePicker({dateFmt:'HH:mm', maxDate:\"#F{$dp.$D('input_date2')}\"})" cssStyle="width:60px;"/></label><label class="input_kr"><s:textfield name="dvo.timeEnd" id="input_date2" theme="simple" onfocus="WdatePicker({dateFmt:'HH:mm', minDate:\"#F{$dp.$D('input_date1')}\"})" cssStyle="width:80px;" /></label></div>
				</custom:searchItem>
				<custom:searchItem side="last" width="40%">
					<custom:search onclick="sub();" />
				</custom:searchItem>
			</custom:searchBody>
			<div id="line_chart" style="width:98%;height:95%; min-height:300px;margin: auto 10px 30px 30px;"></div>
		</form>
	</body>
</html>