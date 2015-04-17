var map, markerLayer, routeLayer, poiLayer;
var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
var toProjection   = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
var pointArr = new Array();
var popupArr = new Array();
var alertCount = 0;	//报警数量
var markerArr = new Array();	//markerArr[车牌号/Police名称] = marker
var targetArr = new Array();	//targetArr[车牌号/Police名称] = 车辆ID/Police设备ID
var lonLatArr = new Array();	//lonLatArr[车牌号/Police名称] = markerLonLat
var policeArr = new Array();	//policeArr[Police名称] = 设备名称
var selectedTarget = 'all';						//被选中的显示对象
var iconSize = new OpenLayers.Size(36, 36);
var iconOffset = new OpenLayers.Pixel(-(iconSize.w / 2), -(iconSize.h / 2));
var isOnline = false;
var route_style = {
		strokeColor: "#339933",
	    strokeOpacity: 1,
	    strokeWidth: 5,
	    pointRadius: 6,
	    pointerEvents: "visiblePainted"
	};

/**
 * 地图初始化
 */
function initMap() {
	var options = {	//设置选项
		controls: [
			new OpenLayers.Control.Navigation(),		//箭头
			new OpenLayers.Control.PanZoomBar(),	//缩放条
        	new OpenLayers.Control.Attribution()
      	],
		zoom: 10
    };
	map = new OpenLayers.Map("basicMap", options);								//创建地图
    var osmLayer = new OpenLayers.Layer.OSM();										//默认图层
    markerLayer = new OpenLayers.Layer.Markers("markers");			//marker图层
    routeLayer = new OpenLayers.Layer.Vector("routes");						//轨迹图层
    poiLayer = new OpenLayers.Layer.Markers( "poi" );								//poi图层
    map.addLayer(osmLayer);																					//增加默认图层到地图
    map.addLayer(routeLayer);
    map.addLayer(markerLayer);
    map.addLayer(poiLayer);
	var centerPosition = new OpenLayers.LonLat(103.842033,1.291128).transform(fromProjection, toProjection);	// 默认中心点新加坡
	//var initZoom = 14;	//新加坡地图初始缩放
	var initZoom = 12;	//尼日利亚地图初始缩放
    map.setCenter(centerPosition, initZoom);	//初始化地图
}

/**
 * 增加轨迹(轮询)
 */
function addRoute(lon, lat) {
	routeLayer.removeAllFeatures();	//清除之前轨迹数据
	var point = new OpenLayers.Geometry.Point(lon, lat).transform(fromProjection, toProjection);
	pointArr.push(point);	//保存轨迹点集合
	var lineFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(pointArr), null, route_style);	//轨迹对象
	routeLayer.addFeatures(lineFeature);
}
//Added by Walden in order to filter the data which is over 5 mins
function addRouteIn5mins(lon, lat, duration){
	routeLayer.removeAllFeatures();
	var point = new OpenLayers.Geometry.Point(lon, lat).transform(fromProjection, toProjection);
	pointArr.push(point);
	if(duration > 5){
		pointArr = [];
	}
	var lineFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(pointArr), null, route_style);	//轨迹对象
	routeLayer.addFeatures(lineFeature);
}

/**
 * 删除所有轨迹
 */
function cleanAllRoute() {
	pointArr = new Array();		//清空轨迹点集合
	routeLayer.removeAllFeatures();	//清除之前轨迹数据
}

/**
 * 删除所有标记点
 */
function cleanAllMarker() {
	map.removeLayer(markerLayer);
	markerLayer = new OpenLayers.Layer.Markers( "mainLayer" );	
	map.addLayer(markerLayer);
}

/**
 * 选中对象
 * @param target 车牌号/Police名称
 */
function doSelectTarget(targetName) {	//过滤
	if(selectedTarget == targetName) {	//选中了上一次的选项直接返回
		return;
	}
	alertCount = 0;
	$("#alertCountDiv").css("display", "none");	//隐藏
	$("#topLeftAlertDiv").css("display", "none");//隐藏
	cleanAllMarker();
	cleanAllRoute();
	if(targetName != 'all') {
		map.setCenter(lonLatArr[targetName], 11);	//lonLatArr[车牌号/Police名称] = markerLonLat
		if(typeof(markerArr[targetName]) != 'undefined') {
			markerLayer.addMarker(markerArr[targetName]);	//初始定位，先取出保存的最后定位数据，markerArr[车牌号/Police名称] = marker
		}
		$('#treeContent_' + targetArr[targetName] + '88000').css('background-color', 'rgb(35, 131, 192)');	//选中树项背景色加深
		$('#treeContent_' + targetArr[selectedTarget] + '88000').css('background-color', 'white');	//恢复之前选中项背景色
	} else {
		for(var key in markerArr) {
			markerLayer.addMarker(markerArr[key]);	
		}
	}
	selectedTarget = targetName;
}

var isPopup = false;
function doShowInfo() {
	if(isPopup) {
		for(var key in popupArr) {
			map.removePopup(popupArr[key]);
		}
		isPopup = false;
	} else {
		for(var key in popupArr) {
			map.addPopup(popupArr[key]);
		}
		isPopup = true;
	}
}

/**
 * 在地图上增加车辆图标、方位角
 * @param gpsDataArr	定位数据	"gpsdata, 车辆名称, 经度, 纬度, 驾驶员名称, 车辆所属(1为单兵设备，其他为车载设备), 车辆告警状态, 速度, 处理后方位角, 发送时系统时间"
 * @param isCenter		true: 设定数据坐标为中心点; false: 不更新中心点
 */
function addOnLineMarker(gpsDataArr, isCenter) {
	if(parseFloat(gpsDataArr[2]) != 0 && parseFloat(gpsDataArr[3]) != 0) {
		var onlineLonLat = new OpenLayers.LonLat(parseFloat(gpsDataArr[2]).toFixed(10), parseFloat(gpsDataArr[3]).toFixed(10)).transform(fromProjection, toProjection);
		var onlineIconSize = new OpenLayers.Size(36, 36);
		var onlineIconOffset = new OpenLayers.Pixel(-(onlineIconSize.w / 2), -(onlineIconSize.h / 2));
		var markerIcon;
		var content;
		if(gpsDataArr[5] == '1') {	//police设备
			markerIcon = new OpenLayers.Icon(basePath + 'images/fangweijiao/police.png', onlineLonLat, onlineIconOffset);	//改为police图标
			content = 'Police Name: ' + gpsDataArr[1] + '</br>' + 'Device Name:' + policeArr[gpsDataArr[1]];
		} else {	//车载设备
			markerIcon = new OpenLayers.Icon(basePath + 'images/fangweijiao/' + gpsDataArr[8] + '.png', onlineIconSize, onlineIconOffset);
			if(gpsDataArr[7] == 0) {	//如果速度为0，显示圆点
				markerIcon = new OpenLayers.Icon(basePath + 'images/fangweijiao/118.gif', onlineLonLat, onlineIconOffset);
			}
			content = 'Plate Number: ' + gpsDataArr[1] + '</br>' + 'Driver:' + gpsDataArr[4];
		}
		var gpsMarker = new OpenLayers.Marker(onlineLonLat, markerIcon);
		if(isCenter == true){
			map.setCenter(onlineLonLat);	//中心点
		}
		//弹出框初始化
		if(null != gpsDataArr[1] && "" != gpsDataArr[1]) {
			if(typeof(popupArr[gpsDataArr[1]]) != 'undefined') {
				map.removePopup(popupArr[gpsDataArr[1]]);
			}
			popupArr[gpsDataArr[1]] = new OpenLayers.Popup.FramedCloud(gpsDataArr[1], onlineLonLat, onlineIconSize, content, null, false);
			popupArr[gpsDataArr[1]].panMapIfOutOfView = false;
			if(isPopup) {
				map.addPopup(popupArr[gpsDataArr[1]]);
			}
			gpsMarker.events.register('click', gpsMarker, function(evt) {	//单击事件
				if(!isPopup) {
					var regPopup = new OpenLayers.Popup.FramedCloud(gpsDataArr[1], onlineLonLat, onlineIconSize, content, null, true);
					map.addPopup(regPopup);
				}
			});
		}
		//targetArr[车牌号/Police名称] = 车辆ID/Police设备ID
		//lonLatArr[车辆ID/Police设备ID] = markerLonLat
		lonLatArr[gpsDataArr[1]] = onlineLonLat;	//保存当前位置(doSelectTarget中用到)
		markerLayer.addMarker(gpsMarker);	//增加gps箭头marker到marker图层
		//markerArr[车牌号/Police名称] = marker
		if(markerArr[gpsDataArr[1]] != null) {		//如果'车牌号/Police名称'对应marker存在，则删除旧数据
			markerLayer.removeMarker(markerArr[gpsDataArr[1]]);
		}
		markerArr[gpsDataArr[1]] = gpsMarker;	//保存新的'车牌号/Police名称'对应marker(doSelectTarget中用到)
		if($('#treeLight_' + gpsDataArr[1]).attr('src') == basePath + 'images/fangweijiao/offline.png') {
			$('#treeLight_' + gpsDataArr[1]).attr('src', basePath + 'images/fangweijiao/online.png');
		}
	}
}

/**
 * 增加离线数据地图标记
 */
function addOffLineMarker(targetName, lon, lat) {
	if(parseFloat(lon) != 0 && parseFloat(lat) != 0) {
		var offLineLonLat = new OpenLayers.LonLat(parseFloat(lon).toFixed(10), parseFloat(lat).toFixed(10)).transform(fromProjection, toProjection);
		var offLineIconSize = new OpenLayers.Size(36, 36);
		var offLineIconOffset = new OpenLayers.Pixel(-(offLineIconSize.w / 2), -(offLineIconSize.h / 2));
		var offLineIcon = new OpenLayers.Icon(basePath + 'images/fangweijiao/119.gif', offLineIconSize, offLineIconOffset);
		var offLineMarker = new OpenLayers.Marker(offLineLonLat, offLineIcon);
		var content;
		if(typeof(policeArr[targetName]) != 'undefined') {	//police设备
			content = 'Police Name: ' + targetName + '</br>' + 'Device Name:' + policeArr[targetName];
		} else {	//vehicle设备
			content = 'Plate Number: ' + targetName + '</br>';
		}
		if(null != targetName && "" != targetName) {
			if(typeof(popupArr[targetName]) != 'undefined') {
				map.removePopup(popupArr[targetName]);
			}
			popupArr[targetName] = new OpenLayers.Popup.FramedCloud(targetName, offLineLonLat, offLineIconSize, content, null, false);
			popupArr[targetName].panMapIfOutOfView = false;
			if(isPopup) {
				map.addPopup(popupArr[targetName]);
			}
			offLineMarker.events.register('click', offLineMarker, function(evt) {	//单击事件
				if(!isPopup) {
					var regPopup = new OpenLayers.Popup.FramedCloud(targetName, offLineLonLat, offLineIconSize, content, null, true);
					map.addPopup(regPopup);
				}
			});
		}
		//targetArr[车牌号/Police名称] = 车辆ID/Police设备ID
		//lonLatArr[车牌号/Police名称] = markerLonLat
		lonLatArr[targetName] = offLineLonLat;		//保存当前位置(doSelectTarget中用到)
		markerLayer.addMarker(offLineMarker);	//增加离线marker到marker图层
		if(markerArr[targetName] != null) {
			markerLayer.removeMarker(markerArr[targetName]);	//如果'车牌号/Police名称'对应marker存在，则删除旧数据
		}
		markerArr[targetName] = offLineMarker;	//保存新的'车牌号/Police名称'对应marker(doSelectTarget中用到)
		if($('#treeLight_' + targetName).attr('src') == basePath + 'images/fangweijiao/online.png') {
			$('#treeLight_' + targetName).attr('src', basePath + 'images/fangweijiao/offline.png');
		}
	}
}

/**
 * 增加报警信息
 * @param 车辆/单兵名称, 报警类型, 经度, 纬度, 时间, 报警值
 */
function creatAlert(name, flag, lon, lat, time, content) {
	var alertType = "";
	if(flag == 3) {
		alertType = "Idle";
	} else if(flag == 4) {
		alertType = "Speeding";
	} else if(flag == 5) {
		alertType = "Sudden Acceleration";
	} else if(flag == 6) {
		alertType = "Sudden Braking";
	} else if(flag == 7) {
		alertType = "Sudden Left";
	} else if(flag == 8) {
		alertType = "Sudden Right";
	} else if(flag == 9){
		alertType = "Neutral Slide";
	} else if(flag == 11) {
		alertType = "Engine Overspeed";
	}
	var alertContent = 'Name:' + name + '</br>' +
				   'Event Name: ' + alertType + '</br>' +
				   'Time: ' + time + '</br>' +
				   'Longitude:' + parseFloat(lon).toFixed(3) + '</br>' +
				   'Latitude:' + parseFloat(lat).toFixed(3);
	if(flag == 4) {
		alertContent += "</br>Speed: " + content + "km/h";
	}
	topLeftAlert(alertContent);			//其他报警左上角弹出报警
	addAlertCount();	//增加右上角报警计数
	addToRightAlertList(alertContent);		//增加右侧报警队列
	addAlertMarker(lon, lat, flag, alertContent);	//增加报警marker到地图
}

/**
 * 弹出左上角报警
 */
function topLeftAlert(alertContent) {
	$("#topLeftContent").html(alertContent);
	$("#topLeftAlertDiv").css("display", "block");
	$("#topLeftAlertDiv").fadeOut(300).fadeIn(300).fadeOut(300).fadeIn(300); 
}

/**
 * alert计数器自增
 */
function addAlertCount() {
	if ($("#rightAlertListDiv").css("display") == 'none') {
		alertCount = alertCount + 1;
		$("#alertCountDiv").html(alertCount);
		if (alertCount > 99) {
			$("#alertCountDiv").css("width", "40px");
		} else {
			$("#alertCountDiv").css("width", "20px");
		}
		$("#alertCountDiv").css("display", "block");
	}
}

/**
 * 增加右侧报警列表项
 */
function addToRightAlertList(alertContent){
	var divContent = "<div style='margin-left: 5px;height: 90px; width: 250px;background-color: white;left: 70px;box-shadow:0px 0px 5px 3px #919191; margin-top:4px'>" +
					"<div style='float: left;'>" +
					  "<img src='" + basePath + "images/55033.gif' width='30px' height='30px' style='margin-left:3px; margin-top:2px;'>" +
				  	"</div>" +
				  	"<div style='float:left;'>" + alertContent + "</div>" +
				  "</div>";
	$("#rightAlertListDiv").prepend(divContent);
}

/**
 * 增加报警事件Marker点
 */
function addAlertMarker(lon, lat, flag, content) {
	var alertMarkerLonLat = new OpenLayers.LonLat(lon, lat).transform(fromProjection, toProjection)
	var alertIconSize = new OpenLayers.Size(20, 30);
	var alertIconOffset = new OpenLayers.Pixel(-(alertIconSize.w / 2), -alertIconSize.h + 5);
	var alertIcon;
	var alertMarker;
	if(flag == 3) {
		alertIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-blue.png', alertIconSize, alertIconOffset);
	} else if(flag == 4) {
		alertIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-purple.png', alertIconSize, alertIconOffset);
	} else if(flag == 5) {
		alertIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-teal.png', alertIconSize, alertIconOffset);
	} else if(flag == 6) {
		alertIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-magenta.png', alertIconSize, alertIconOffset);
	} else if(flag == 7) {
		alertIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-yellow.png', alertIconSize, alertIconOffset);
	} else if(flag == 8) {
		alertIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-orange.png', alertIconSize, alertIconOffset);
	} else if(flag == 9) {
		alertIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-gray.png', alertIconSize, alertIconOffset);
	} else if(flag == 10) {
		alertIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-lime.png', alertIconSize, alertIconOffset);
	} else if(flag == 11) {
		alertIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-white.png', alertIconSize, alertIconOffset);
	} else if(flag == 12) {
		alertIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-black.png', alertIconSize, alertIconOffset);
	} else if(flag == 13) {
		alertIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-black.png', alertIconSize, alertIconOffset);
	}
	alertMarker = new OpenLayers.Marker(alertMarkerLonLat, alertIcon);
	markerLayer.addMarker(alertMarker);
//	alertMarker.events.register('click', alertMarker, function(evt) {	//单击事件
//		//增加弹出框
//		if(null != content && "" != content) {
//			var alertPopup = new OpenLayers.Popup.FramedCloud('alertPopup', alertMarkerLonLat, null, content, null, true);
//			map.addPopup(alertPopup);
//		}
//	});
}

/**
 * 增加POImarkers
 * @param lon		经度
 * @param lat		纬度
 * @param icoName	图形名称
 * @param content		弹出泡内容
 */
function addPoiMarkers(lon, lat, icoName, content) {
	var poiLonLat = new OpenLayers.LonLat(lon, lat).transform(fromProjection, toProjection);
	var poiIconSize = new OpenLayers.Size(30, 30);
	var poiIconOffset = new OpenLayers.Pixel(-(poiIconSize.w/2), -(poiIconSize.h/2));
	var poiMarker;
	var poiIcon = new OpenLayers.Icon(basePath + 'images/' + icoName + '_.png', poiIconSize, poiIconOffset);
	poiMarker = new OpenLayers.Marker(poiLonLat, poiIcon);
	poiLayer.addMarker(poiMarker);
	var poiPopLonLat = new OpenLayers.LonLat(lon, lat)
	poiMarker.events.register('click', poiMarker, function(evt) {	//单击事件
		//增加弹出框
		if(null != content && "" != content) {
			var popup = new OpenLayers.Popup.FramedCloud(lon, poiLonLat, null, content, null, true);
			map.addPopup(popup);
		}
	});
	map.events.register('zoomend', poiMarker, function(evt) {	//缩放事件
		if(map.getZoom() > 10 && !poiMarker.isDrawn()){
			poiLayer.addMarker(poiMarker);
		} else if(map.getZoom() <= 10 && poiMarker.isDrawn()){
			poiLayer.removeMarker(poiMarker);
		}
	});
}

/**
 * 显示或隐藏右侧alert列表
 */
function showOrHideAlertList() {
	if ($("#alertCountDiv").css("display") == 'none') {
		$("#rightAlertListDiv").slideUp("slow");
		$("#rightAlertListDiv").html("");
		cleanAllMarker();
		$("#topLeftAlertDiv").css("display", "none");
	} else {
		$("#alertCountDiv").css("display", "none");
		$("#rightAlertListDiv").slideDown("slow");
		alertCount = 0;
	}
}

/**
 * 点击弹出车辆实时视频窗口
 * @param vehName		车辆名称
 */
var winWidth = 600;
var winHeight = 600;
var winLeft = (window.screen.availWidth - winWidth) / 2;
var winTop = (window.screen.availHeight - winHeight) / 2;

function openVehCamWin(vehicleName) {
	if($('#treeLight_' + vehicleName).attr('src') == basePath + 'images/fangweijiao/offline.png') {
		alert('The vehicle is not online!');
		return;
	}
	if(typeof(targetArr[vehicleName]) == 'undefined') {
		alert('The vehicle does not exist!');
		return;
	}
	//window.open(basePath + 'monitor/real-time-new!openVehCamWin?targetID=' + targetArr[vehicleName], '_blank', 'help:off; resizable:off; scroll:no; status:off');
	window.open(basePath + 'monitor/real-time-new!openVehCamWin?targetID=' + targetArr[vehicleName], 'camWin_' + vehicleName, 'help=off, height=' + winHeight + ', width=' + winWidth + ', top=' + winTop + ', left=' + winLeft + ', toolbar=no, menubar=no, scrollbars=no, resizable=no, titlebar=no, location=no, status=no');
}

/**
 * 点击弹出车辆实时视频窗口
 * @param policeName		单兵名称
 */
function openPoliceCamWin(policeName) {
	if($('#treeLight_' + policeName).attr('src') == basePath + 'images/fangweijiao/offline.png') {
		alert('The police device is not online!');
		return;
	}
	if(typeof(policeArr[policeName]) == 'undefined') {
		alert('The police device does not exist!');
		return;
	}
	window.open(basePath + 'monitor/real-time-new!openPoliceCamWin?deviceName=' + policeArr[policeName], 'camWin' + policeName, 'help=off, height=' + winHeight + ', width=' + winWidth + ', top=' + winTop + ', left=' + winLeft + ', toolbar=no, menubar=no, scrollbars=no, resizable=no, titlebar=no, location=no, status=no');
}