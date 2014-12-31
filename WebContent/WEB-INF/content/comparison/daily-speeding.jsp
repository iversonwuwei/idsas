<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/flot.css" />
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/excanvas.min.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.orderBars.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/specific/comparison/daily-speeding.js"></script>
    </head>
	<body>
		<custom:navigation father="Performance Comparison" model="Daily Speeding Violations Per KM"  />
		<custom:searchBody>
			<custom:searchItem title="Department" side="first">
				<custom:selCom name="vo.departmentID" value="${vo.departmentID}"/>
			</custom:searchItem>
			<custom:searchItem title="Date">
			 	<custom:doubleDate id="select_date" beginName="vo.beginDate" endName="vo.endDate" beginValue="${vo.beginDate}" endValue="${vo.endDate}" />
			</custom:searchItem>
			<custom:searchItem side="last" width="40%">
				<custom:search onclick="doSearch();" />
			</custom:searchItem>
		</custom:searchBody>
		<div class="chart_main">
			<div style="width:100%; height: 5%; font-size:large; font-weight: bolder; text-align: center;">Daily Speeding Violations Per KM</div>
			<div id="barChart" style=" height: 95%;margin: auto 10px 30px 30px;"></div> 
		</div>
	</body>
</html>