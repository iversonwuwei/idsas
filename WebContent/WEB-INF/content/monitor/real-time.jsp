<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<%
	String basePathws = "ws://" + request.getServerName() + ":" + request.getServerPort()+ path; 
//	String basePathws = "ws://" + request.getServerName() + ":" + request.getServerPort()+ "/idsasws/"; 
%>
<html>
	<head>
		<link href="<%=basePath%>css/main2.css" rel="stylesheet" type="text/css">
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/OpenLayers-2.12/OpenLayers.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/specific/map/olayers.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.min.js"></script>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
		<style type="text/css">
			html { height: 100% }
			body { height: 100%; margin: 0; padding: 0 }
			#map_canvas { height: 100% }
			
			.divBackGround {
				filter: alpha(opacity =   70);	/* 修改IE浏览器的透明度 */
				-moz-opacity: 0.7;	/* 修改MOZ浏览器的透明度 */
				opacity: 0.7;	/* 修改MOZ浏览器的透明度 */
				left:0;
				top:0;width:100%; height:100%;
				background: #ccc;
				position: absolute; /* 绝对路径 */
				z-index: 1199;
				text-align: center;
				/**padding: 20px;**/
				display: none;
			}
			
			.mainInputTreeDiv {
				width: 350px;
				height: 280px;
				z-index: 1200;
				position: absolute;
				display: none;
				background-color: white;
			}
			
			.mainInputTreeDiv2 {
				width: 20px;
				height: 20px;
				z-index: 1201;
				position: absolute;
				display: none;
			}
			
			.mainInputTreeDiv3 {
				width: 315px;
				height: 160px;
				z-index: 1200;
				position: absolute;
				display: none;
				background-color: white;
			}

			.buslis {
			    border-bottom: 1px solid #DDDDDD;
			    border-collapse: collapse;
			    border-left: 1px solid #DDDDDD;
			    width: 97.5%;
			}
			
			.buslis thead {
			    background: none repeat scroll 0 0 #65A7D3;
			    font-weight: bolder;
			    height: 25px;
			    text-align: center;
			}
			
			.buslis td {
			    border-right: 1px solid #DDDDDD;
			    border-top: 1px solid #DDDDDD;
			    empty-cells: show;
			    height: 20px;
			    text-align: center;
			    white-space: nowrap;
			}
		</style>
		<script type="text/JavaScript">
			var basePathws = '<%=basePathws%>';
			var busname = "all";
			var socket;
			var layersObje = new Array();
			var position1s = new Array();
			var police = new Array();
			var data = [];
			var totalPoints = 30;
			var busna;
			var plot;
    		var firstbus;
    		var alertcount = 0;
    		var points2 = [];
			
			function getData(sp) {
				if (data.length > 0)
					data = data.slice(1);
				// Do a random walk
				while (data.length < totalPoints) {
					data.push(sp);
				}
				// Zip the generated y values with the x values
				var res = [];
				for (var i = 0; i < data.length; ++i) {
					res.push([i, data[i]])
				}
				return res;
			}
			
			function busdivdis() {
				var a = $("#buslis").css("display");
				if(a == 'block'){ 
					$("#buslis").slideUp("slow");
				} else {
					$("#buslis").slideDown("slow");
				}
			}
			
			/**
			 * 增加轨迹(轮询)
			 */
			function addLine(x,y) {
				var pointb = new OpenLayers.Geometry.Point(x,y).transform(fromProjection, toProjection);
				points2.push(pointb);
				lineFeature[1] = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(points2), null, style_green);	//轨迹对象
				vectorLayer.addFeatures(lineFeature[1]);
			}
			
			/**
			 * 删除所有轨迹
			 */
			function cleanLine(){
				map.removeLayer(vectorLayer);
				vectorLayer = [];
				points2 = [];
				vectorLayer = new OpenLayers.Layer.Vector("Routes");	//容器图层
				map.addLayer(vectorLayer);
			}
			
			/**
			 * 新建车辆列表数据（轮询）
			 * @param strary	定位数据	"{bus, 车辆名称/policeDeviceName, 经度, 纬度, 驾驶员名称, 车辆所属, 发动机温度, 车厢内温度, 车辆告警状态, 速度, 方位角, 当前时间, 车辆名称/policeName}"
			 */
			function newbustr(strary) {
				var str = "<tr id='bustr" + Object[strary[13]] + "'></tr>";
				var std = "<td>" + strary[1] + "</td>" +	//vehicle name
						  "<td>" + (strary[5] == '1' ? '' : strary[4]) + "</td>" +	//driver name
						  "<td>" + (strary[5] == '1' ? '' : strary[5]) + "</td>" +		//route
						  "<td id='busy" + strary[1] + "'>" + parseFloat(strary[2]).toFixed(3) + "</td>" +	//Longitude
						  "<td id='busx" + strary[1] + "'>" + parseFloat(strary[3]).toFixed(3) + "</td>";	//Latitude
				if(strary[5] == '1') {	//单兵设备，只显示速度
					std += "<td>" + strary[9] + "km/h</td>";
				} else {	//车辆设备，显示chart图
					std += "<td id='bussd" + strary[1] + "'>" + strary[9] + "km/h &nbsp; <img alt='chart' style='width:17px; height:17px; cursor:pointer' title='chart' src='" + basePath + "images/chart_c.GIF' onclick=\"ShowChart(\'" + strary[1] + "\')\"></td>"; //Speeding
				}
				std += "<td id='busfwj" + strary[1] + "'>" + strary[11] + "</td>";	//Azimuth
				var std2 = "<td id='busvideo" + strary[1] + "'>";	//Live Video
				if(strary[13] != "") {
					if(strary[5] == '1') {	//单兵设备
						std2 += "<img alt='live' style='width:20px; height:20px; cursor:pointer' title='live' src='" + basePath + "images/livetou.png' onclick=\"ShowVideoOCX(\'" + police[strary[1]] + "\')\">";	//小窗口
					} else {
						std2 += "<img alt='live' style='width:20px; height:20px; cursor:pointer' title='live' src='" + basePath + "images/livetou.png' onclick=\"ShowVideoIPCam(\'" + Object[strary[13]] + "\')\">";	//小窗口
					}
				}
				std2 += "&nbsp; </td>";
				if($("#bustr" + Object[strary[13]]).length == 0) {	//判断如果该车辆或police数据不存在则增加一行
					$("#bustbody").append(str);
					$("#bustr" + Object[strary[13]]).append(std + std2);
				} else {
					$("#bustr" + Object[strary[13]]).remove();	//存在先删除改行
					if(busname == '' || busname == 'all'|| busname == Object[strary[13]]) {
						$("#bustbody").prepend(str);	//在第一行插入
					} else {
						$("#bustr" + busname).after(str);	//在下一行插入
					}
					$("#bustr" + Object[strary[13]]).prepend(std + std2);
				}		
				if(Object[strary[13]] == busname) {	//选中车辆改背景色
					$("#bustr" + busname).css("background-color", "#6495DC");
				}
				addBusFilter(strary[1], Object[strary[13]]);
			}
			
			/**
			 * 显示alert列表
			 */
			function showalertno() {
				$("#yuandian").html(alertcount);
				if(alertcount > 99){
					$("#yuandian").css("width", "40px");
				} else {
					$("#yuandian").css("width", "20px");
				}
				$("#yuandian").css("display", "block");
			}
			
			/**
			 * 关闭alert列表
			 */
			function displayalertno() {
				if($("#yuandian").css("display") == 'none') {
					$("#alertdiv").slideUp("slow");
					$("#alertdiv").html("");
					cleanAllMarkers();
					$("#leftAlertDiv").css("display", "none");
					$("#bottomRightAlertDiv").css("display", "none");
				} else {
					$("#yuandian").css("display", "none");
					$("#alertdiv").slideDown("slow");
					alertcount=0;
				}
			}
		
			/**
			 * 关闭alert列表
			 */
			function creatAlertNo() {
				if($("#alertdiv").css("display") == 'none') {
					alertcount = alertcount + 1;
					showalertno();
				}
			}
			
			/**
			 * 报警信息
			 */
			function creatAlert(time, name, x, y, flag, text) {
				//flag 4 speeding
				var alerttext = "";
				if(flag == 3) {
					alerttext = "Idle";
				} else if(flag == 4) {
					alerttext = "Speeding";
				} else if(flag == 5) {
					alerttext = "Sudden acceleration";
				} else if(flag == 6) {
					alerttext = "Sudden braking";
				} else if(flag == 7) {
					alerttext = "Sudden left";
				} else if(flag == 8) {
					alerttext = "Sudden right";
				} else if(flag == 9){
					alerttext = "Neutral slide";
				} else if(flag == 10) {
					alerttext = "Idle air conditioning";
				} else if(flag == 11) {
					alerttext = "Engine overspeed";
				} else if(flag == 12){
					alerttext = "Into the Geo Fencing";
				} else if(flag == 13){
					alerttext = "Exit the Geo Fencing";
				}
				var alertstr = 'Vehicle Name:' + name + '</br>' +
							   'Event Name: ' + alerttext + '</br>' +
							   'Time: ' + time + '</br>' +
							   'Longitude:' + parseFloat(x).toFixed(3) + '</br>' +
							   'Latitude:' + parseFloat(y).toFixed(3);
				if(flag == 4) {
					alertstr += "</br>Speed: " + text + "km/h";
				}
				leftAlert(alertstr);	//左侧弹出
				if(flag == 12 || flag == 13){	//出入围栏 右下角弹出
					downrightalert(alertstr)
				}
				rightAlert(alertstr);
				creatAlertNo();
				//creatAlertDot(116.415698, 39.967851,'3',str1+'<br/>'+str2);
				creatAlertDot(x, y, flag, alertstr);
			}
			
			//增加左侧alertdiv
			function leftAlert(alertStr) {
				$("#left1span").html(alertStr);
				$("#leftAlertDiv").css("display", "block");
				$("#leftAlertDiv").fadeOut(300).fadeIn(300).fadeOut(300).fadeIn(300); 
			}
			
			//增加右侧alert列表
			function rightAlert(alertStr){
				var divStr = "<div style='margin-left: 5px;height: 90px; width: 250px;background-color: white;left: 70px;box-shadow:0px 0px 5px 3px #919191; margin-top:4px'>" +
								"<div style='float: left;'>" +
								  "<img src='" + basePath + "images/55033.gif' width='30px' height='30px' style='margin-left:3px; margin-top:2px;'>" +
							  	"</div>" +
							  	"<div style='float:left;'>" + alertStr + "</div>" +
							  "</div>";
				$("#alertdiv").prepend(divStr);
			}
			
			//增加右下角 围栏使用
			function downrightalert(alertStr){
				$("#downrightspan").html(alertStr);
				$("#bottomRightAlertDiv").css("display","block");
				$("#bottomRightAlertDiv").fadeOut(300).fadeIn(300).fadeOut(300).fadeIn(300); 
			}
			
			/**
			 * 增加事件Marker点
			 */
			function addMarkers(id, point, flag, content) {	
				var size = new OpenLayers.Size(20, 30);
				var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
				var icon;
				var marker1 = null;
				if(flag == 3) {
					icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-blue.png', size, offset);
					marker1 = new OpenLayers.Marker(point, icon);
				} else if(flag == 4) {
					icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-purple.png', size, offset);
					marker1 = new OpenLayers.Marker(point, icon);
				} else if(flag == 5) {
					icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-teal.png', size, offset);
					marker1 = new OpenLayers.Marker(point, icon);
				} else if(flag == 6) {
					icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-magenta.png', size, offset);
					marker1 = new OpenLayers.Marker(point, icon);
				} else if(flag == 7) {
					icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-yellow.png', size, offset);
					marker1 = new OpenLayers.Marker(point, icon);
				} else if(flag == 8) {
					icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-orange.png', size, offset);
					marker1 = new OpenLayers.Marker(point, icon);
				} else if(flag == 9) {
					icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-gray.png', size, offset);
					marker1 = new OpenLayers.Marker(point, icon);
				} else if(flag == 10) {
					icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-lime.png', size, offset);
					marker1 = new OpenLayers.Marker(point, icon);
				} else if(flag == 11) {
					icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-white.png', size, offset);
					marker1 = new OpenLayers.Marker(point, icon);
				} else if(flag == 12) {
					icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-black.png', size, offset);
					marker1 = new OpenLayers.Marker(point, icon);
				} else if(flag == 13) {
					icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-black.png', size, offset);
					marker1 = new OpenLayers.Marker(point, icon);
				}
				markLayer[flag-1].addMarker(marker1);
				marker1.events.register('click', marker1, function(evt) {	//单击事件
					//增加弹出框
					if(null != content && "" != content) {
						var popup = new OpenLayers.Popup.FramedCloud(id, point, null, content, null, true);
						map.addPopup(popup);
					}
				});
			}
			
			/**
			 * 增加报警事件Marker点
			 */
			function creatAlertDot(x, y, flag, str){
				var alertPosition = new OpenLayers.LonLat(x,y).transform(fromProjection, toProjection)
				addMarkers("speed", alertPosition, flag, str);
			}
			
			/**
			 * 开启一个websocket
			 */
			var chat = function() {
				var urlStr = '?ids=${mainUser.loginName}&pw=${mainUser.password}';
				socket = new WebSocket(basePathws + 'busmap.do' + urlStr);
				//打开Socket 
				socket.onopen = function(event) {
					//socket.send($("#busname").val());
					//addText("连接成功！");
					//document.getElementById("send_btn").disabled = false;
				};
				socket.onmessage = function(event) {
					var strary = ("" + event.data).split(",");
					console.log(strary);
					if(strary[0] == "bus") {
						if(typeof(Object[strary[1]]) == 'undefined') {	//车牌号或police名称不存在直接终止
							return;
						}
						if(busname=='all' || busname == Object[strary[13]]) {
							//接到gps数据
							if(firstbus == null){
								firstbus = Object[strary[13]];
							}
							if(firstbus != null && firstbus != "true" && firstbus != Object[strary[13]]) {	//车辆切换
								firstbus = "true";
								cleanLine();	//切换车辆时清除轨迹
							}
							if(busname != null && busname != "" && busname != "all") {	//如果选择了一辆车
								if(Object[strary[13]] == busname) {
									newMacket(strary, true);
									addLine(parseFloat(strary[2]).toFixed(10), parseFloat(strary[3]).toFixed(10));
								}
							} else {
								if(firstbus == "true"){
									newMacket(strary, false);
								} else {
									newMacket(strary, true);
									if(parseFloat(strary[2]) != 0 && parseFloat(strary[3]) != 0){
										addLine(parseFloat(strary[2]).toFixed(10), parseFloat(strary[3]).toFixed(10));
									}
								}
							}
						}
						newbustr(strary);
						if(busna == strary[1]) {
							update(strary[9]);
						}
					//接到警告数据
					} else if(strary[0] == "busalert" && (busname == 'all' || busname == Object[strary[7]])) {
						if(typeof(Object[strary[7]])=='undefined') {
							return;
						}
						creatAlert(strary[1], strary[2], strary[3], strary[4], strary[5], strary[6]);
					} else if (strary[0] == "busout" && (busname == 'all' || busname == Object[strary[2]])) {
						if(typeof(Object[strary[2]]) == 'undefined'){
							return;
						}
						var straryv = new Array();
						straryv[1] = strary[2];
						straryv[2] = strary[3];
						straryv[3] = strary[4];
						straryv[4] = "";
						straryv[5] = "";
						straryv[8] = "";
						straryv[9] = strary[5];
						straryv[10] = 1;
						straryv[11] = "";
						straryv[11] = strary[6];
						straryv[13] = strary[2];
						busred(straryv[13], straryv[2], straryv[3]);
						newbustr(straryv);
					} else if(strary[0] == "logout"){
						alert("Account logged elsewhere");
						this.close();
					}
				};
				socket.onclose = function(event) {};
			};
			
			var send = function(obj) {	//过滤
				cleanAllMarkers();
				$("#yuandian").css("display","none");
				alertcount = 0;
				$("#leftAlertDiv").css("display","none");
				if($("#buslist").val() != null) {
					if((firstbus != $("#buslist").val()) && ($("#buslist").val() != busname)) {
						cleanLine();
					}
					if($("#buslist").val() == "all") {
						sendall();
						$("#bustr" + busname).css("background-color", "#FFFFFF");
					} else {
						$("#bustr" + busname).css("background-color", "#FFFFFF");
						socket.send("busname:" + $("#buslist").val());
						cleanOther($("#buslist").val());
						if(position1s[$("#buslist").val()] != null) {
							map.setCenter(position1s[$("#buslist").val()], 11);
						}
						if(layersObje[$("#buslist").val()] != null){
							markLayer[0].addMarker(layersObje[$("#buslist").val()]);
						}
						$("#bustr" + $("#buslist").val()).css("background-color", "#6495DC");
						var xtr = $("#bustr" + $("#buslist").val());
						$(xtr).remove();
						$("#bustbody").prepend(xtr);
					}
					busname = $("#buslist").val();
				}
			}
			
			var sendall = function(obj){
				socket.send("busname:all");
				//socket.send("B08552");
				//send_text.value="";
				//busname="all";
			}
			
			/**
			 * 清除所有地图标记点
			 */
			function cleanAll(){
				for(var ai = 0; ai < layersObje.length; ai++) {
					if('undefined' != typeof(layersObje[ai])){
						markLayer[0].removeMarker(layersObje[ai]);
					}
				}
			}
			
			/**
			 * 清除选中数据以外的所有地图标记点
			 * @param busname	车牌号/police名称
			 */
			function cleanOther(busname){
				for(var ai = 0; ai < layersObje.length; ai++) {
					if('undefined' != typeof(layersObje[ai])){
						if(ai != Number(busname)){
							markLayer[0].removeMarker(layersObje[ai]);
						}
					}
				}
			}
		
			/**
			 * 更新下拉菜单
			 * @param busname	车牌号
			 * @param busid		车辆ID
			 */
			function addBusFilter(busname, busid) {
				if($("option[id^='bus'][value='" + busid + "']").val() == undefined){
					/**过滤下拉菜单*/
					$("#buslist").append("<option value='" + busid + "' id='bus" + busname + "'>" + busname + "</option>");
				}
			}
		
			/**
			 * 在地图上增加车辆图标、方位角（轮询）
			 * @param strary	定位数据	"{bus, 车辆名称/policeDeviceName, 经度, 纬度, 驾驶员名称, 车辆所属, 发动机温度, 车厢内温度, 车辆告警状态, 速度, 方位角, 当前时间, 车辆名称/policeName}"
			 * @param flag		true:一定为数据坐标为中心点；false:不更新中心点
			 */
			function newMacket(strary, flag) {
				if(parseFloat(strary[2]) != 0 && parseFloat(strary[3]) != 0) {
					var position1 = new OpenLayers.LonLat(parseFloat(strary[2]).toFixed(10), parseFloat(strary[3]).toFixed(10)).transform(fromProjection, toProjection);
					var size = new OpenLayers.Size(30,35);
					var offset = new OpenLayers.Pixel(-(size.w/2), -(size.h/2));
					var icon2 = new OpenLayers.Icon(basePath + 'images/fangweijiao/' + strary[10] + '.png', size, offset);
					if(strary[9] == 0) {	//如果速度为0，显示圆点
						var size2 = new OpenLayers.Size(36,36);
						var offset2 = new OpenLayers.Pixel(-(size2.w/2), -(size2.h/2));
						icon2 = new OpenLayers.Icon(basePath + 'images/fangweijiao/118.gif', size2, offset2);
					}
					var layersObjb = new OpenLayers.Marker(position1, icon2);
					if(flag == true){
						map.setCenter(position1);	//中心点
					}
					layersObjb.events.register('click', layersObjb, function(evt) {	//单击事件
						//增加弹出框
						if(null != strary[1] && "" != strary[1]) {
							var popup1;
							if(strary[5] == '1') {	//police设备
								var content = 'Police Name: ' + strary[1] + '</br>' + 'Device Name:' + police[strary[1]];
								popup1 = new OpenLayers.Popup.FramedCloud(strary[1], position1, null, content, null, true);
							} else {
								popup1 = new OpenLayers.Popup.FramedCloud(strary[1], position1, null, strary[1], null, true);
							}
							map.addPopup(popup1);
						}
					});
					position1s[Object[strary[13]]] = position1;
					markLayer[0].addMarker(layersObjb);
					if(layersObje[Object[strary[13]]] != null) {
						markLayer[0].removeMarker(layersObje[Object[strary[13]]]);
					}
					layersObje[Object[strary[13]]]=layersObjb;
				}
			}
		
			/**
			 *增加离线数据地图标记
			 */
			function busred(name, x, y) {
				var position1 = new OpenLayers.LonLat(parseFloat(x).toFixed(10), parseFloat(y).toFixed(10)).transform(fromProjection, toProjection);
				var size2 = new OpenLayers.Size(36,36);
				var offset2 = new OpenLayers.Pixel(-(size2.w/2), -(size2.h/2));
				icon2 = new OpenLayers.Icon(basePath + 'images/fangweijiao/119.gif', size2, offset2);
				var layersObjb = new OpenLayers.Marker(position1,icon2);
				layersObjb.events.register('click', layersObjb, function(evt) {	//单击事件
					//增加弹出框
					/* if(null != name && "" != name) {
						var popup1 = new OpenLayers.Popup.FramedCloud(null, position1, null, name, null, true);
						map.addPopup(popup1);
					} */
					if(null != name && "" != name) {
						var popup1;
						if(typeof(police[name]) != 'undefined') {	//police设备
							var content = 'Police Name: ' + name + '</br>' + 'Device Name:' + police[name];
							popup1 = new OpenLayers.Popup.FramedCloud(name, position1, null, content, null, true);
						} else {	//vehicle设备
							popup1 = new OpenLayers.Popup.FramedCloud(name, position1, null, name, null, true);
						}
						map.addPopup(popup1);
					}
				});
				position1s[Object[name]] = position1;
				markLayer[0].addMarker(layersObjb);
				if(layersObje[Object[name]] != null){
					markLayer[0].removeMarker(layersObje[Object[name]]);
				}
				layersObje[Object[name]] = layersObjb;
			}
		
			function addPoiMarkers(x, y, ico, content) {
				var point = new OpenLayers.LonLat(x,y).transform(fromProjection, toProjection);
				var size = new OpenLayers.Size(30, 30);
				var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
				var marker1 = null;
				var icon = new OpenLayers.Icon(basePath + 'images/' + ico + '_.png', size, offset);
				marker1 = new OpenLayers.Marker(point, icon);
				markLayer[1].addMarker(marker1);
				marker1.events.register('click', marker1, function(evt) {	//单击事件
					//增加弹出框
					if(null != content && "" != content) {
						var popup = new OpenLayers.Popup.FramedCloud('click', point, null, content, null, true);
						map.addPopup(popup);
					}
				});
				map.events.register('zoomend', marker1, function(evt) {	//缩放事件
					if(map.getZoom() > 10 && !marker1.isDrawn()){
						markLayer[1].addMarker(marker1);
					} else if(map.getZoom()<=10 && marker1.isDrawn()){
						markLayer[1].removeMarker(marker1);
					}
				});
			}
			
			/**
			 * create ocx video div
			 * closeAll();
			 */
			function CreatVideoDivOCX() {
				$("body").append("<div id='divBackGroundOCX' class='divBackGround'></div>");
				$("body").append("<div id='mainInputTreeDivOCX' class='mainInputTreeDiv'></div>");
				$("#mainInputTreeDivOCX").css("right", 0).css("bottom", 0);
				$("body").append("<div id='mainInputTreeDiv2OCX' class='mainInputTreeDiv2'><img src='" + basePath + "images/button_xx.gif' onmousemove=\"this.src='" + basePath + "images/button_xx2.gif'\" onmouseout=\"this.src='" + basePath + "images/button_xx.gif'\" style='cursor: pointer;' onclick='closeOCX();'/></div>");
				$("#mainInputTreeDiv2OCX").css("right",345).css("bottom",275);
				$("#mainInputTreeDivOCX").append("<div style='background-color:white;width:100%;height:90%'><object name='zdcomaudio' classid='clsid:7E6CC141-11E8-41AE-A1BE-CF6606B1DB48' codebase='" + basePath + "cabs/ZdcomAudio.cab#version=1,0,9,0' width='100%' height='100%'></object></div>");
				$("#mainInputTreeDivOCX").append("<div style='background-color:#419CD8;width:100%;height:40px'><img id='beginButtonOCX' src='" + basePath + "images/video/1.png' height='39px' style='margin-left:175px; cursor:pointer;'></img></div>")
			}
	
			/**
			 * ocx 视频
			 */
			function ShowVideoOCX(deviceID) {	//ocx 视频
				$("#mainInputTreeDivOCX").css("display", "block");
				$("#mainInputTreeDiv2OCX").css("display", "block");
				$("#beginButtonOCX").attr("onclick", "playOCX(\'" + deviceID + "\')");
				//playOCX(deviceID);
			}
			
			/**
			 * 播放视频
			 */
			function playOCX(deviceID) {
				//返回值：0--正常， 1--网络连接错误, 2--参数错误
				var r = zdcomaudio.PlayNet("172.100.100.35", 18026, "${mainUser.loginName}", 2, deviceID, 1, 350, 250);//大连本地  
				if(r == 0) {
					$("#beginButtonOCX").attr("src", basePath + "images/video/2.png");
					$("#beginButtonOCX").attr("onclick", "stopOCX(" + deviceID + ")");
				} else {
					alert("Video service has been shut down!");
				}
			}
			
			function stopOCX(deviceID) {
				zdcomaudio.CloseOCX();
				$("#beginButtonOCX").attr("src", basePath + "images/video/1.png");
				$("#beginButtonOCX").attr("onclick", "playOCX(" + deviceID + ")");
			}
			
			function closeOCX() {
				zdcomaudio.CloseOCX();
				$("#beginButtonOCX").attr("src", basePath + "images/video/1.png");
				$("#divBackGroundOCX").css("display","none");
				$("#mainInputTreeDivOCX").css("display","none");
				$("#mainInputTreeDiv2OCX").css("display","none");
			}
			
			/**
			 * create IP cam video div
			 * closeIPCam();
			 */
			function CreatVideoDivIPCam() {
				$("body").append("<div id='divBackGround' class='divBackGround'></div>");
				$("body").append("<div id='mainInputTreeDiv' class='mainInputTreeDiv'></div>");
				$("#mainInputTreeDiv").css("right",0).css("bottom",0);
				$("body").append("<div id='mainInputTreeDiv2' class='mainInputTreeDiv2'><img src='" + basePath + "images/button_xx.gif' onmousemove=\"this.src='" + basePath + "images/button_xx2.gif'\" onmouseout=\"this.src='" + basePath + "images/button_xx.gif'\" style='cursor:pointer;' onclick='closeIPCam()'/></div>");
				$("#mainInputTreeDiv2").css("right",345).css("bottom",275);
				$("#mainInputTreeDiv").append("<div id='ipcamdiv' style='background-color:white;width:100%;height:100%'></div>");
			}

			/**
			 * ipcam 小窗 视频
			 */
			function ShowVideoIPCam(vehcode) {
				$("#mainInputTreeDiv").css("display","block");
				$("#mainInputTreeDiv2").css("display","block");
				$("#ipcamdiv").html("<iframe name='ipframe' height='280px' width='350px'  frameborder='0' src='" + basePath + "vehicle/vehicle-list/" + vehcode + "/showliveonly'></iframe>");
			}

			function closeIPCam() {
				try {
					ipframe.window.disconn();  
				} catch (e) {
				}
				$("#ipcamdiv").html("");
				$("#divBackGround").css("display","none");
				$("#mainInputTreeDiv").css("display","none");
				$("#mainInputTreeDiv2").css("display","none");
			}
			
			function update(sp) {
				plot.setData([getData(sp)]);
				plot.draw();
			}
			
			/**
			 * 曲线图
			 */
			function CreatChartDiv() {
				$("body").append("<div id='divBackGround22' style='display:none;background:white; height: 175px;width: 315px;position: absolute;z-index: 1199;bottom: 83px;'><span>Vehicle Name:</span><span id='charspan'></span> <div id='mainInputTreeDiv1' class='mainInputTreeDiv3' ></div></div>");
				$("#mainInputTreeDiv1").css("left",0).css("top",15);
				$("body").append("<div id='mainInputTreeDiv3' class='mainInputTreeDiv2' ><img src='<%=basePath%>images/button_xx.gif'onmousemove=\"this.src='<%=basePath%>images/button_xx2.gif'\" onmouseout=\"this.src='<%=basePath%>images/button_xx.gif'\" style='cursor: pointer;' onclick='closeChart()'/></div>");
				$("#mainInputTreeDiv3").css("left",310).css("bottom",250);
			}
			
			function ShowChart(vehCode) {
				$("#charspan").text(vehCode);
				$("#divBackGround22").css("display", "block");
				$("#mainInputTreeDiv1").css("display", "block");
				$("#mainInputTreeDiv3").css("display", "block");
				data = [];
				plot = $.plot("#mainInputTreeDiv1", [ getData() ], {
					series: {
						shadowSize: 0	// Drawing is faster without shadows
					},
					yaxis: {
						min: 0,
						max: 125,
						ticks:6
					},
					xaxis: {
						show: false
					}
				});
				busna = vehCode;
			}
	
			function closeChart(){
				$("#divBackGround22").css("display","none");
				$("#mainInputTreeDiv1").css("display","none");
				$("#mainInputTreeDiv3").css("display","none");
				data=[];
				busna="";
			}
			
			$(function() {
				$('#markerDiv').width($(window).width());
				initMap();
				<s:iterator value="poilist" var="b">
					addPoiMarkers( '${b.longitude}', '${b.latitude}', '${b.icon}', '${b.caption}')
				</s:iterator>
				<s:iterator value="vehDevlist" var="vv">
					Object['${vv.key}']='${vv.value}'; //Object[车牌号/Police名称] = 车辆ID/Police设备ID
				</s:iterator>
				<s:iterator value="policeDevList" var="p">
					police['${p.value}']='${p.key}'; //police[Police名称] = 设备名称
				</s:iterator>
				<s:iterator value="lasts" var="vv" >
					var straryv = new Array();
					straryv[1] = '${vv.o_busname}';
					straryv[2] = parseFloat(Number('${vv.o_longitude}')/60).toFixed(5);
					straryv[3] = parseFloat(Number('${vv.o_latitude}')/60).toFixed(5);
					straryv[4] = "";
					straryv[5] = "";
					straryv[8] = "";
					straryv[9] = '${vv.o_speed}';
					straryv[10] = 1;
					straryv[11] = '${vv.o_direction}';
					straryv[13] = '${vv.o_busname}';
					if(typeof(Object[straryv[1]]) != 'undefined') {
						busred(straryv[13], straryv[2], straryv[3]);
						newbustr(straryv);
					}
				</s:iterator>
				CreatVideoDivIPCam();	//ipcam视频（车辆）
				CreatVideoDivOCX();		//ocx视频(单兵)
				CreatChartDiv();
				busdivdis();
        		chat();
			});
		</script>
	</head>
	<body>
		<div id="basicMap" style="width:100%; height:95%;">
			<div id="leftAlertDiv" style="z-index: 1005; position: absolute;height:90px; width: 250px;background-color: white;left: 70px;box-shadow:0px 0px 1px 3px #919191;display: none;">
				<div style="float: left;">
					<img alt="alert" src="<%=basePath%>images/55033.gif" width="30px" height="30px" style="margin-left: 3px;margin-top: 2px">
				</div>
				<div id="left1span" style="float: left;"></div>
			</div>
			<div id="bottomRightAlertDiv" style="z-index: 1400; position: absolute;height:90px; width: 250px;background-color: white;right: 0px;box-shadow:0px 0px 1px 3px #919191;display: none;bottom: 86px">
				<div style="float: left;">
					<img alt="alert" src="<%=basePath%>images/55033.gif" width="30px" height="30px" style="margin-left: 3px;margin-top: 2px">
				</div>
				<div id="downrightspan" style="float: left;"></div>
			</div>
		
			<div id="topdiv" style="box-shadow:3px 1px 1px 2px #424242;text-align:center;width: 100px;height: 27px;background-color: #337DAF;z-index: 1005; position: absolute;right: 0px;border-radius:0px 0px 0px 15px">
				<div style="margin-top: 5px">
					<a href="#" style="color: white;text-decoration:none;" onclick="displayalertno()">Alert</a>
				</div>
				<div id="yuandian" style="box-shadow:0px 0px 1px 2px #424242;z-index: 1025; position: absolute;background-color: red;color:white;width: 20px;height: 20px;border-radius:10px 10px 10px 10px;color: white;display: none;font-size: 15px"> 0</div>
			</div>
			<div id="alertdiv" style="width: 275px;height: 500px;z-index: 1015; position: absolute;right: 0px;top: 27px;display: none;overflow: auto;"></div>
			<div id="buslis" style="width: 100%;height:150px;background-color:white;z-index: 1005; position: absolute;bottom: 80px;box-shadow:0px 0px 1px 3px #919191;overflow: auto;">
				<table class="zhzd" style="width: 100%;" id="stable">
					<thead class="Reprot_Head">
						<tr>
							<td>Vehicle/Police</td>
							<td>Driver Name</td>
							<td>Route</td>
							<td>Longitude</td>
							<td>Latitude</td>
							<td>Speeding</td>
							<td>Azimuth</td>
							<td>Live Video</td>
						</tr>
					</thead>
					<tbody id="bustbody" style="text-align: center;"></tbody>
				</table>
			</div>
			<div id="botdiv" style="width: 100%;height:83px;background-color: #337DAF;z-index: 1005; position: absolute;bottom: 0px;min-width: 1050px">
				<div style="margin-top: 10px;margin-left: 20px;width: 250px;float: left;">
					<a href="#" style="color: white;text-decoration:none;font-size: 15px;text-align:center; padding:2px;border:solid 1px #E9F0F9; border-bottom:solid 1px #8599AD;border-right:solid 1px #8599AD; background:#337DAF;" onclick="busdivdis()">
						Summary
					</a>
					|
					<select id="buslist" onchange="send(this.value)">
						<option value="all">All</option>
					</select>
				</div>
				<div id="markerDiv" style="margin-top: 5px;float:left;color: white;overflow:auto;">
					<ul style="width: 1250px;">
						<li style="display:inline;"><img src="<%=basePath%>images/fangweijiao/34fb.png" style="height: 31px; " align="absmiddle" />Vehicle</li>
						<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-magenta.png" style="height: 30px;" align="absmiddle"/>Sudden braking</li>
						<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-teal.png" style="height: 30px;" align="absmiddle"/>Sudden acceleration</li>
						<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-yellow.png" style="height: 30px;" align="absmiddle"/>Sudden left</li>
						<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-orange.png" style="height: 30px;" align="absmiddle"/>Sudden right</li>
						<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-purple.png" style="height: 30px;" align="absmiddle"/>Speeding</li>
						<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-gray.png" style="height: 30px;" align="absmiddle"/>Neutral slide</li>
						<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-blue.png" style="height: 30px;" align="absmiddle"/>Idle</li>
						<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-lime.png" style="height: 30px;" align="absmiddle"/>Idle air conditioning</li>
						<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-white.png" style="height: 30px;" align="absmiddle"/>Engine overspeed</li>		
						<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-black.png" style="height: 30px;" align="absmiddle"/>Geo Fencing notice</li>	
					</ul>
				</div>
			</div>
		</div>
	</body>
</html>