<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/query/vehicle-tree.js"></script>
		<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/common/jsTree.v.1.0rc2/_lib/jquery.js"></script>
		<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/common/jsTree.v.1.0rc2/jquery.jstree.js"></script>
	</head>
	<body>
		<custom:navigation father="Organization Query" model="Vehicle Tree"/>
		<div class="search">
			<table style="width: 100%; text-align: left;">
				<tbody>
					<tr style="height: 40px;">
						<td style="width: 15%;text-align: right;">Plate Number：</td>
						<td style="width: 8%;"><input id="search_text"></td>
						<td style="width: 84%;" class="button_input">&nbsp;&nbsp;&nbsp;<a href="#"><img onclick="search();" src="<%=basePath%>images/button_search.jpg" title="Search" />
						</a></td>
					</tr>
					<tr>
						<td colspan="3">
							<div id="orgTree" style="width: 450px; height: 500px; float: left; background: #eee; overflow: auto;"></div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</body>
</html>