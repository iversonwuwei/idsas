<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<%
	String baseWsPath = "ws://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>css/dtree.css"/>
		<style>
			.dTreeNode a p {
				text-align: center;
			}
		</style>
    	<script type="text/JavaScript" src="<%=basePath%>scripts/common/dtree/feat-dtree.js"></script>
    	<script type="text/JavaScript">
	    	var baseWsPath = "<%=baseWsPath%>";
			var socket;
			
			$(function() {
				openWebSocket();		//开启websocket
			});
    		
    		//开启一个websocket
			function openWebSocket() {
				var wsUrl = baseWsPath + 'websocket/card_old?id=${mainUser.userID}';
				socket = new WebSocket(wsUrl);
				socket.onopen = function(event) {
					console.log('Card websocket opened!');
				};
				socket.onmessage = function(event) {
					if(self.parent.frames["mainFrame"].xxxaaa == true) {
						self.parent.frames["mainFrame"].carder(event.data);
					} else {
						d.openTo(1701, true);	
						self.parent.frames["mainFrame"].location = basePath + "task/schedule?editble=1&cardid=" + event.data;
					}
				};
				socket.onclose = function(event) {
					console.log('Card websocket closed!');
				};
			};
			
			/**
			 * 主动关闭socket
			 */
			function closeWebSocket() {
				socket.close();
			}
		</script>
	</head>
	<body onbeforeunload="closeWebSocket()">
		<div class="left">
			<div class="left_t"></div>
			<div class="dtree">
				<script type="text/javascript">
					var d = new dTree("d", basePath + 'images/dtree/');
					d.config.closeOthers = true;
					<s:iterator value="roleFeatList" var="t" status="r">
						d.add("${t.featID}","${t.fatherID}","${t.featName}", "${t.url}" == "" ? "" : basePath + "${t.url}");
					</s:iterator>
					document.write(d);
					if("${id}" != null && "${id}" != ""){
						d.openTo(1701, true);						
					} else {
						d.openTo(1101, true);
					}
				</script>
			</div>
		</div>
	</body>
</html>