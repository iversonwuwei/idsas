<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<%
    String baseWsPath = "ws://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<html>
<head>
    <%-- 禁止缩放当前页 --%>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <link type="text/css" href="<%=basePath%>scripts/common/dtree/dtree.css" rel="stylesheet"/>
    <link type="text/css" href="<%=basePath%>css/specific/monitor/real-time.css" rel="stylesheet"/>
    <script type="text/javascript" src="<%=basePath%>scripts/common/OpenLayers-2.12/OpenLayers.js"
            charset="UTF-8"></script>
    <script type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.min.js" charset="UTF-8"></script>
    <script type="text/JavaScript" src="<%=basePath%>scripts/common/dtree/input-dtree.js" charset="UTF-8"></script>
    <script type="text/javascript" src="<%=basePath%>scripts/specific/monitor/real-time.js" charset="UTF-8"></script>
    <style type="text/css">
        #search_button {
            padding-top: 0.2em;
            padding-right: 0.5em;
            padding-bottom: 0.2em;
            padding-left: 0.5em;
        }
    </style>
    <script type="text/JavaScript">
        var baseWsPath = "<%=baseWsPath%>";
        var socket;

        $(function () {
            $('#markerDiv').width($(window).width());
            initMap();
            <s:iterator value="poiList" var="v">
            addPoiMarkers('${v.longitude}', '${v.latitude}', '${v.icon}', '${v.caption}');	//增加POI点
            </s:iterator>
            <s:iterator value="vehDevlist" var="v">
            targetArr['${v.key}'] = '${v.value}'; //targetArr[车牌号/Police名称] = 车辆ID/Police设备ID(用于权限控制)
            </s:iterator>
            <s:iterator value="policeDevList" var="p">
            policeArr['${p.value}'] = '${p.key}'; //policeArr[Police名称] = 设备名称
            </s:iterator>
            <s:iterator value="lastPositionList" var="v">
            if (typeof(targetArr['${v.o_busname}']) != 'undefined') {
                addOffLineMarker('${v.o_busname}', parseFloat(Number('${v.o_longitude}') / 60).toFixed(5), parseFloat(Number('${v.o_latitude}') / 60).toFixed(5));	//增加下线车辆点
            }
            </s:iterator>
            openWebSocket();		//开启websocket
            $("#search_show_all, #search_show_info, #search_button").button();
            $("#targetTreeDiv").height($(window).height() - 100);
        });

        window.onresize = function () {
            $("#targetTreeDiv").height($(window).height() - 100);
        }


        function addAllOffLineMarker() {

        }

        /**
         * 开启一个websocket
         */
        function openWebSocket() {
            var wsUrl = baseWsPath + 'websocket/realtime_old';
            socket = new WebSocket(wsUrl);
            //打开Socket
            socket.onopen = function (event) {
                console.log('Real time websocket opened!');
            };
            socket.onmessage = function (event) {
                var gpsDataArr = (event.data + "").split(",");	//拆分数据
                console.log(gpsDataArr);
                if (typeof(targetArr[gpsDataArr[1]]) == 'undefined') {	//车牌号或police名称不存在直接终止(权限控制)
                    return;
                }
                if (selectedTarget != 'all' && gpsDataArr[1] != selectedTarget) {	//当前非显示全部，并且当前数据非显示数据，直接返回
                    return;
                }
                //接到定位数据
                if (gpsDataArr[0] == "gpsData") {
                    //"gpsdata, 车辆名称, 经度, 纬度, 驾驶员名称, 车辆所属, 车辆告警状态, 速度, 处理后方位角, 发送时系统时间"
                    if (selectedTarget == 'all') {
                        addOnLineMarker(gpsDataArr, false);		//增加在线定位数据
                    } else if (selectedTarget == gpsDataArr[1]) {	//显示单独车辆
                        addOnLineMarker(gpsDataArr, true);		//增加在线定位数据
                        //Added by Walden
                        var speed = gpsDataArr[7];
                        if(speed > 0){
                        	var duration = durationcounter(gpsDataArr[9]);
                            addRouteIn5mins(parseFloat(gpsDataArr[2]).toFixed(10), parseFloat(gpsDataArr[3]).toFixed(10), duration);	//监控单独车辆时显示轨迹
                        }
                    }
                    //接到警告数据
                } else if (gpsDataArr[0] == "alertData") {
                    //"alertData, 车辆/单兵名称, 报警类型, 经度, 纬度, 时间, 报警值"
                    creatAlert(gpsDataArr[1], gpsDataArr[2], gpsDataArr[3], gpsDataArr[4], gpsDataArr[5], gpsDataArr[6]);
                    //接到下线通知
                } else if (gpsDataArr[0] == "offline") {
                    //"offline, 车辆/单兵名称, 经度, 纬度, 时间"
                    addOffLineMarker(gpsDataArr[1], gpsDataArr[2], gpsDataArr[3]);	//增加下线标记name, lon, lat
                }
            };
            socket.onclose = function (event) {
                console.log('Real time websocket closed!');
            };
        }
        ;

        /**
         * 主动关闭socket
         */
        function closeWebSocket() {
            socket.close();
        }
		
		
        //Opens all nodes which have the content like content parameter
        function doSearch() {
            var content = $('#search_target').val();
            if (content == null || content == '') {
                return;
            }
            d.closeAll();
            for (var n = 0; n < d.aNodes.length; n++) {
                if (d.aNodes[n].name.indexOf('treeContent') > 0) {
                    document.getElementById('treeContent_' + d.aNodes[n].id).style.fontWeight = 'normal';	//Recovery old result's style
                    if (d.aNodes[n].name.indexOf(content) > 0) {
                        if (d.aNodes[n].pid != d.root.id) {
                            d.nodeStatus(true, d.aNodes[n]._p._ai, d.aNodes[n]._p._ls);	//Open father node
                            document.getElementById('treeContent_' + d.aNodes[n].id).style.fontWeight = 'bolder';	//Change new result's style
                        }
                    }
                }
            }
        }

        //For showing the vehicle which is offline. By Walden
        function vehClicked(id) {
            doSelectTarget(id);
            if($('#treeLight_' + id).attr('src') == basePath + 'images/fangweijiao/offline.png'){
                <s:iterator value="lastPositionList" var="v">
                if ('${v.o_busname}' === id) {
                    addOffLineMarker('${v.o_busname}', parseFloat(Number('${v.o_longitude}') / 60).toFixed(5), parseFloat(Number('${v.o_latitude}') / 60).toFixed(5));	//增加下线车辆点
                }
                </s:iterator>
            }
        }

        //Calculate the mins duration. By Walden
        var preTime = '';
        var duration = 0;
        function durationcounter(dateTime){
            var preDate = getMin(dateTime);
        	if(preTime===''){
        		preTime = preDate;;
        		return duration;
        	}
        	duration = Math.floor((preDate - preTime)%3600000/60000);
        	preTime = preDate;
        	alert(duration);
        	return duration;
        }

        //Getting the date without seconds. By Walden
        function getMin(dateTime){
            var date = new Date(dateTime);
            var year = date.getYear();
            var month = date.getMonth();
            var day = date.getDay();
            var hour = date.getHours();
            var mins = date.getMinutes();
            var newDate = new Date(year, month, day, hour, mins);
            return newDate;
        }
    </script>
</head>
<body style="height: 100%" onbeforeunload="closeWebSocket()">
<div id="leftPartDiv">
    <div id="targetSearchDiv">
        Target: <input type="text" id="search_target" style="width: 100px;"/>
        <input type="button" id="search_button" value=" Search " onclick="doSearch();"/>

        <h2 align="center" style="width: 100%;">TARGET LIST</h2>
    </div>
    <div id="targetTreeDiv">
        <script type="text/javascript">
            var d = new dTree("d", basePath + 'images/dtree/');
            d.config.closeOthers = false;
            d.add("0", "-1", "All Fleets", "");
            var content = '';
            <s:iterator value="targetList" var="t" status="r">
            if ("${t.targetType}" == "1") {	//fleet
                content = "${t.targetName}";
            } else {						//vehicle/police
                content = "<span id=\"treeContent_${t.targetID}\" style=\"cursor:pointer;border-radius: 4px 4px 4px 4px;\" onclick=\"vehClicked('${t.targetName}')\">"
                + "${t.targetName} "
                + "</span>";
                if ("${t.targetType}" == "2") {	//vehilce
                    content += "<img alt='live' style='width:15px; height:15px; cursor:pointer; float: right; padding-right:00px;' title='live' src='" + basePath + "images/livetou.png' onclick=\"openVehCamWin(\'" + '${t.targetName}' + "\')\">";
                } else if ("${t.targetType}" == "3") {	//police
                    content += "<img alt='live' style='width:15px; height:15px; cursor:pointer; float: right; padding-right:00px;' title='live' src='" + basePath + "images/livetou.png' onclick=\"openPoliceCamWin(\'" + '${t.targetName}' + "\')\">";
                }
                content += "<img id='treeLight_${t.targetName}' style='float: right;' src='" + basePath + "images/fangweijiao/offline.png' width='18px' height='15px' /> ";
            }
            d.add("${t.targetID}", "${t.fatherID}", content, "");
            </s:iterator>
            document.write(d);
        </script>
    </div>
    <div id="targetButtonDiv">
        <input type="button" id="search_show_all" value=" Show All " onclick="doSelectTarget('all');"/>
        <input type="button" id="search_show_info" value=" Show/Hide Info " onclick="doShowInfo();"/>
    </div>
</div>
<div id="rightPartDiv">
    <div id="basicMap" style="width:100%; height:95%;">
        <div id="topLeftAlertDiv">
            <div style="float: left;">
                <img alt="alert" src="<%=basePath%>images/55033.gif" width="30px" height="30px"
                     style="margin-left: 3px;margin-top: 2px">
            </div>
            <div id="topLeftContent" style="float: left;"></div>
        </div>
        <div id="topRightAlertDiv">
            <div style="margin-top: 5px">
                <a href="#" style="color: white;text-decoration: none;" onclick="showOrHideAlertList();">Alert</a>
            </div>
            <div id="alertCountDiv"> 0</div>
        </div>
        <div id="rightAlertListDiv"></div>
    </div>
    <div id="bottomMarkerDiv">
        <table style="width: 100%;">
            <tr>
                <td><img src="<%=basePath%>images/fangweijiao/34fb.png" style="height: 31px;" align="absmiddle"/></td>
                <td>Vehicle</td>
                <td><img src="<%=basePath%>images/fangweijiao/police.png" style="height: 31px;" align="absmiddle"/></td>
                <td>Police</td>
                <td><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-magenta.png" style="height: 30px;"
                         align="absmiddle"/></td>
                <td>Sudden <br/>Braking</td>
                <td><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-teal.png" style="height: 30px;"
                         align="absmiddle"/></td>
                <td>Sudden <br/>Acceleration</td>
                <td><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-yellow.png" style="height: 30px;"
                         align="absmiddle"/></td>
                <td>Sudden <br/>Left</td>
                <td><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-orange.png" style="height: 30px;"
                         align="absmiddle"/></td>
                <td>Sudden <br/>Right</td>
                <td><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-purple.png" style="height: 30px;"
                         align="absmiddle"/></td>
                <td>Speeding</td>
                <td><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-gray.png" style="height: 30px;"
                         align="absmiddle"/></td>
                <td>Neutral <br/>Slide</td>
                <td><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-blue.png" style="height: 30px;"
                         align="absmiddle"/></td>
                <td>Idle</td>
                <td><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-white.png" style="height: 30px;"
                         align="absmiddle"/></td>
                <td>Engine <br/>Overspeed</td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>