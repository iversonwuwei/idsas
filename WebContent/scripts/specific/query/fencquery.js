var timer;	//定时器
var interval = 15; //间隔数
var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
var OSM_toProjection = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
var map, vectorLayer, marker, markers, popup, points, point;
var perData, totalCount, nop;
//var markStart, markEnd, markIdle, markSpeed, markAcc, markBrake, markLeft, markRight, markSlide, markAir, markEngine, markPOI;
var markLayer = [];
var lineFeature, markerArr = [];
var sel_lisencePlate, sel_startDate, sel_endDate, sel_startTime, sel_endTime;	//selected data
var v_startTime, v_endTime;	//millonsecond of start time and end time
var iconSize = new OpenLayers.Size(20, 30);
var iconOffset = new OpenLayers.Pixel(-(iconSize.w/2), -iconSize.h+5);
var icon;

/**
 * css style of the route
 */
var style_green = {
	strokeColor: "#339933",
    strokeOpacity: 1,
    strokeWidth: 5,
    pointRadius: 6,
    pointerEvents: "visiblePainted"
};

/**
 * On window initial
 */


/**
 * Get schedule by vehicle
 */
function getSchedule() {
	$.ajax({
	    type : "POST",
	    async : false,
	    dataType : 'json',
	    data : {
	    	'vo.vehID' : select_vehicle.getSelectedValue()
	    },
	    url : basePath + "monitor/display!getSchedule",
	    success : function(d) {
			if("empty" != d.result) {
				$("#sel_schedule option").remove();
				$("#sel_schedule").append("<option value='-1'>Please choose schedule...</option>");
				for ( var int = 0; int < d.length; int++) {
					$("#sel_schedule").append("<option value='" + d[int].key + "'>" + d[int].value + "</option>");
				}
			}
	    },
	    error : function(d) {
			alert("Exception occurs!");
		}
	});
}

/**
 * Process the date time before submit search
 */
function setTimes() {
	var schTime = $("#sel_schedule").val();
	if(schTime != -1) {
		var schStr = $("#sel_schedule").find('option:selected').text();
		var beginDat = $.trim(schStr.split(',')[0]);
		var endDat = $.trim(schStr.split(',')[1]);
		$("#startDate").val(beginDat.split(' ')[0]);
		$("#startTime").val(beginDat.split(' ')[1]);
		$("#endDate").val(endDat.split(' ')[0]);
		$("#endTime").val(endDat.split(' ')[1]);
	}
}

/**
 * Initial the route time slider
 */
function initSlider() {
	$( "#slider" ).slider({	//ui slider
		min: 0,	//min value
		max: nop,	//max value
		value:nop,	//init value
		step: 1,
		range: "min",
		animate:true,	
		slide: function(event, ui) {	//slide event
			doStop();	//stop the timer
			$('#play').button('option', {	//change the buttom to pause
				label: "pause",
				icons: {
					primary: "ui-icon-play"
				}
			});
			clearRoute();	//delete the route
			addRoute($("#slider").slider("option", "value"));	//redraw the route
		}
	});
}

/**
 * Calculate the seconds
 * @param	beginStr	begin time string
 * @param	endStr		end time string
 */
function getSeconds(beginStr, endStr) {
	var beginDate = new Date(2000, 1, 1, beginStr[0], beginStr[1], beginStr[2]).getTime();
	var endStr = new Date(2000, 1, 1, endStr[0], endStr[1], endStr[2]).getTime();
	return (endStr - beginDate)/1000;
}

//Move the slider and draw the route
function moveSlider() {
	var newValue = $("#slider").slider("option", "value") + 1;
	if(newValue > nop + 1) {
		window.clearInterval(timer);
		return;
	}
	$("#slider").slider("option", "value", newValue);
	clearRoute();	//delete the route
	addRoute(newValue);	//redraw the route
}

/**
 * Begin moving slider
 */
function doRun() {
	timer = window.setInterval(moveSlider, 500);
}

/**
 * Stop moving slider
 */
function doStop() {
	window.clearInterval(timer); 
}

/**
*	Initial the complete route
*/
function addRouteAll() {
	totalCount = dataList.length;	//count of total data
	perData = 10;		//add 10 data per slider's changging
	nop = Math.ceil(totalCount/perData);	//slider's max value
	var endPoint = new OpenLayers.LonLat(parseFloat(dataList[totalCount-1].longitude/60).toFixed(6), parseFloat(dataList[totalCount-1].latitude/60).toFixed(6)).transform(fromProjection, OSM_toProjection);
	points = [];
//	clearMess();
	var sinobj;
	for(var i = 0; i < totalCount; i++) {	//add all data point to the array
		sinobj = dataList[i];
		point = new OpenLayers.Geometry.Point(parseFloat(sinobj.longitude/60).toFixed(6), parseFloat(sinobj.latitude/60).toFixed(6)).transform(fromProjection, OSM_toProjection);	//根据坐标取点，并计算投影偏移量
		points.push(point);
//		addMess(i, 'Time: ' + sinobj.gpsDateTime + 
//				'; Longitude: ' + parseFloat(sinobj.longitude/60).toFixed(6) + 
//				'; Latitude: ' + parseFloat(sinobj.latitude/60).toFixed(6) + 
//				'; Speed: ' + sinobj.speed + 
//				'; Direction: ' + sinobj.direction
//				);
	}
	clearRoute();	//delete old route
	lineFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(points), null, style_green);	//build the route
	vectorLayer.addFeatures(lineFeature);		//add the route to the layer
	var startPoint = new OpenLayers.LonLat(parseFloat(dataList[0].longitude/60).toFixed(6), parseFloat(dataList[0].latitude/60).toFixed(6)).transform(fromProjection, OSM_toProjection);
	var endPoint = new OpenLayers.LonLat(parseFloat(dataList[totalCount-1].longitude/60).toFixed(6), parseFloat(dataList[totalCount-1].latitude/60).toFixed(6)).transform(fromProjection, OSM_toProjection);
	delMarkers(markLayer[0]);	//remove old starting location's marker
	delMarkers(markLayer[1]);		//remove old ending location's marker
	//add new starting\ending location's marker
	addMarkers('startP', startPoint, 0, 'Starting location: time: ' +dataList[0].gpsDateTime + '</br>Longitude:' + parseFloat(dataList[0].longitude/60).toFixed(6) + '</br>Latitude:' + parseFloat(dataList[0].latitude/60).toFixed(6));	
	addMarkers('endP', endPoint, 1, 'Ending location: Time: ' + dataList[totalCount-1].gpsDateTime + '</br>Longitude:' + parseFloat(dataList[totalCount-1].longitude/60).toFixed(6) + '</br>Latitude:' + parseFloat(dataList[totalCount-1].latitude/60).toFixed(6));
	map.setCenter(endPoint, 9);	//move the center to ending location
	$('#operDiv').css('visibility', 'visible');	//the div which the slider on it
	initSlider();	//initial the slider
	//remove all old event markers
	for ( var int = 2; int < 11; int++) {
		delMarkers(markLayer[int]);
	}
	//add new event markers
	addEventMarkers();
}

/**
  * Change route by slider
  */
function addRoute(pointIndex) {
	points = [];
	var i = 0;
	var max = (pointIndex*perData >= dataList.length ? dataList.length : pointIndex*perData);
	max = (max == 0 ? 1 : max);
	var endPoint = new OpenLayers.LonLat(parseFloat(dataList[max-1].longitude/60).toFixed(6), parseFloat(dataList[max-1].latitude/60).toFixed(6)).transform(fromProjection, OSM_toProjection);	//根据坐标取点，并计算投影偏移量
//	clearMess();
	var sinobj;
	for(; i < max; i++) {
		sinobj = dataList[i];
		point = new OpenLayers.Geometry.Point(parseFloat(sinobj.longitude/60).toFixed(6), parseFloat(sinobj.latitude/60).toFixed(6)).transform(fromProjection, OSM_toProjection);	//根据坐标取点，并计算投影偏移量
		points.push(point);
//		addMess(i, 'Time: ' + sinobj.gpsDateTime + 
//							'; Longitude: ' + parseFloat(sinobj.longitude/60).toFixed(6) + 
//							'; Latitude: ' + parseFloat(sinobj.latitude/60).toFixed(6) + 
//							'; Speed: ' + sinobj.speed + 
//							'; Direction: ' + sinobj.direction
//							);
	}
	lineFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(points), null, style_green);	//build the route
	$('#sTime').text('Time：' + dataList[max-1].gpsDateTime/* + '-' + parseFloat(dataList[max-1].longitude/60).toFixed(6) + '-' + parseFloat(dataList[max-1].latitude/60).toFixed(6)*/);
	vectorLayer.addFeatures(lineFeature);		//add the route to the layer
	delMarkers(markLayer[1]);
	addMarkers('endP', endPoint, 1, 'Ending location: Time: ' +dataList[max-1].gpsDateTime + '</br>Longitude:' + parseFloat(dataList[max-1].longitude/60).toFixed(6) + '</br>Latitude:' + parseFloat(dataList[max-1].latitude/60).toFixed(6));
	//change the center of the map when the ending lication outsides the current view
	if(endPoint.lon <=  map.getExtent().left || endPoint.lon >= map.getExtent().right || endPoint.lat <= map.getExtent().bottom || endPoint.lat >= map.getExtent().top) {
		map.setCenter(endPoint);
	}
}

/**
 * Remove the route
 */
function clearRoute() {
	vectorLayer.removeFeatures(lineFeature);
}

/**
 * add event marker
 * 
 */
function addMarkers(id, point, flag, content) {	//增加起点标记到标记图层
	var eventArr = new Array();
	var img_name = null;
	if(flag == 0) {
		img_name = 'm-green.png';	//Starting location
	} else if(flag == 1) {
		img_name = 'm-red.png';	//Ending location
	} else if(flag == 2) {
		img_name = 'm-magenta.png';	//Sudden braking
	} else if(flag == 3) {
		img_name = 'm-teal.png';	//Sudden acceleration
	} else if(flag == 4) {
		img_name = 'm-yellow.png';	//Sudden left
	} else if(flag == 5) {
		img_name = 'm-orange.png';	//Sudden right
	} else if(flag == 6) {
		img_name = 'm-purple.png';	//Speeding
	} else if(flag == 7) {
		img_name = 'm-gray.png';	//Neutral slide
	} else if(flag == 8) {
		img_name = 'm-blue.png';	//Idle
	} else if(flag == 9) {
		img_name = 'm-lime.png';	//Idle air conditioning
	} else if(flag == 10) {
		img_name = 'm-white.png';	//Engine overspeed
	}
	icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/' + img_name, iconSize, iconOffset);
	eventArr[id] = new OpenLayers.Marker(point, icon);
	//markLayer[flag].addMarker(eventArr[id]);
	eventArr[id].events.register('click', eventArr[id], function(evt) {	//onclick event open pop window
		if(null != content && "" != content) {
			popup = new OpenLayers.Popup.FramedCloud(id, point, null, content, null, true);
			map.addPopup(popup);
		}
	});
	if(flag <= 1) {
		markLayer[flag].addMarker(eventArr[id]);
	} else {
		map.events.register('zoomend', map, function(evt) {		//zooming map
			if(map.getZoom() > 10 && !eventArr[id].isDrawn()){	//show markers when map'zoom more than 12
				markLayer[flag].addMarker(eventArr[id]);
			} else if(map.getZoom() <= 10  && eventArr[id].isDrawn()) {	//hide markers when map'zoom less  than or equal 12
				markLayer[flag].removeMarker(eventArr[id]);
			}
		});
	}
}

/**
 * Remove markers
 */
function delMarkers(layer) {
	var markerArr = layer.markers;
	for ( var int = 0; int < markerArr.length; int++) {
		layer.removeMarker(markerArr[int]);
	}
}

/**
 * Initial the map
 */
function initMap() {
	var options = {
		controls: [
			new OpenLayers.Control.Navigation(),	//箭头
			new OpenLayers.Control.PanZoomBar(),	//缩放条
        	new OpenLayers.Control.Attribution()
      	],
		zoom: 11
    };
	map = new OpenLayers.Map("basicMap", options);	//创建地图
//	map.addControl(new OpenLayers.Control.MousePosition());
    map.addControl(new OpenLayers.Control.LayerSwitcher());	//图层选择
    var osm_layer = new OpenLayers.Layer.OSM('OSM');	//默认图层
    map.addLayer(osm_layer);	//增加默认图层到地图
    vectorLayer = new OpenLayers.Layer.Vector("Routes");	//多边形容器图层
    map.addLayer(vectorLayer);	//增加容器图层到地图
    markLayer[0] = new OpenLayers.Layer.Markers("Starting location");	//Starting location layer
    markLayer[1] = new OpenLayers.Layer.Markers("Ending location");	//Ending location layer
    markLayer[2] = new OpenLayers.Layer.Markers("Sudden acceleration");	//Sudden acceleration layer
    markLayer[3] = new OpenLayers.Layer.Markers("Sudden braking");	//Sudden braking layer
    markLayer[4] = new OpenLayers.Layer.Markers("Sudden left");	//Sudden left layer
    markLayer[5] = new OpenLayers.Layer.Markers("Sudden right");	//Sudden right layer
    markLayer[6] = new OpenLayers.Layer.Markers("Speeding");	//Speeding layer
    markLayer[7] = new OpenLayers.Layer.Markers("Neutral slide");	//Neutral slide layer
    markLayer[8] = new OpenLayers.Layer.Markers("Idle");	//Idle layer
    markLayer[9] = new OpenLayers.Layer.Markers("Idle air conditioning");	//Idle air conditioning layer
    markLayer[10] = new OpenLayers.Layer.Markers("Engine overspeed");	//Engine overspeed layer
    markLayer[11] = new OpenLayers.Layer.Markers("POI");	//POI layer
    map.addLayers(markLayer);
    map.setCenter(new OpenLayers.LonLat(116.396713, 39.919216).transform(fromProjection, OSM_toProjection));	//中心点
    $('#OpenLayers\\.Control\\.Attribution_4').hide();
}

/**
 *  Get POI data, and draw the markers
 */
function getPOI() {
	$.ajax({
	    type : "POST",
	    async : false,
	    dataType : 'json',
	    url : basePath + "monitor/display!getPOI",
	    success : function(d) {
			if("empty" != d.result) {
				for ( var int = 0; int < d.length; int++) {
					addPoiMarkers(d[int].longitude, d[int].latitude, d[int].icon, d[int].caption);
				}
			}
	    },
	    error : function(d) {
			alert("Exception occurs!");
		}
	});
}

/**
 * Add POI markers to map
 * @param x	lon
 * @param y	lat
 * @param ico	icon name
 * @param content	content in popup
 */
function addPoiMarkers(x, y, ico, content) {
	var point=new OpenLayers.LonLat(x,y).transform(fromProjection, OSM_toProjection);
	var size = new OpenLayers.Size(30, 30);
	var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
	var icon;
	var markerPOI = null;
	icon = new OpenLayers.Icon(basePath + 'images/'+ico+'_.gif', size, offset);
	markerPOI = new OpenLayers.Marker(point, icon);
	markerPOI.events.register('click', markerPOI, function(evt) {	//click marker show pop message
		if(null != content && "" != content) {
			var popup = new OpenLayers.Popup.FramedCloud('click', point, null, content, null, true);
			map.addPopup(popup);
		}
	});
	map.events.register('zoomend', markerPOI, function(evt) {		//zooming map
		//show markers when map'zoom more than 12
		if(map.getZoom() > 10 && !markerPOI.isDrawn()){
			markLayer[11].addMarker(markerPOI);
		//hide markers when map'zoom less  than or equal 12
		} else if(map.getZoom() <= 10  && markerPOI.isDrawn()) {
			markLayer[11].removeMarker(markerPOI);
		}
	});
}

function checkSearch() {
	sel_startDate = $.trim($('#startDate').val());
	sel_startTime = $.trim($('#startTime').val());
	sel_endDate = $.trim($('#endDate').val());
	sel_endTime = $.trim($('#endTime').val());
	sel_lisencePlate = $.trim(select_vehicle.getSelectedText());
	if(sel_lisencePlate == null || sel_lisencePlate == '' || sel_lisencePlate == '-1') {
		alert("Please choose a vehicle!");
		return;
	}
	if(sel_startDate == '') {	//对不输入的选项做默认处理
		if(sel_endDate == '') {
			var myDate = new Date();
			sel_endDate = myDate.getFullYear() + '-' + (myDate.getMonth() + 1) + '-' + myDate.getDate();
		}
		sel_startDate = sel_endDate;
	} else if(sel_endDate == '') {
		sel_endDate = sel_startDate;
	}
	var arrDate = sel_startDate.split('-');
	var arrTime = sel_startTime == '' ? ['00', '00', '00'] : sel_startTime.split(':');
	v_startTime = new Date(arrDate[0], arrDate[1], arrDate[2], arrTime[0], arrTime[1], arrTime[2]).getTime();
	arrDate = sel_endDate.split('-');
	arrTime = sel_endTime == '' ? ['23', '59', '59'] : sel_endTime.split(':');
	v_endTime = new Date(arrDate[0], arrDate[1], arrDate[2], arrTime[0], arrTime[1], arrTime[2]).getTime();
	var d1 = new Date(v_startTime);
	var d2 = new Date(v_endTime);
	if(v_startTime > v_endTime) {
		alert("The StartTime can't be latter than the EndTime!");
	}
	getRouteDate();
}

/**
 * Get data of route
 */
function getRouteDate() {
	$.ajax({
	    type : "POST",
	    async : false,
	    dataType : 'json',
	    data : {
	    	'vo.plateNumber' : sel_lisencePlate,
	    	'vo.beginDate' : sel_startDate,
	    	'vo.beginTime' : sel_startTime,
	    	'vo.endDate' : sel_endDate,
	    	'vo.endTime' : sel_endTime
	    },
	    url : basePath + "monitor/display!getGeo",
	    success : function(d) {
			if("empty" != d.result){
				dataList = d;
				addRouteAll();
			} else {
				alert('no data!');
			}
	    },
	    error : function(d) {
			alert("Exception occurs!");
		}
	});
}

function showDiv() {
	$('#contextDiv').show();
	$('#maxDiv').hide();
	$('#minDiv').show();
}

function hidDiv() {
	$('#contextDiv').hide();
	$('#maxDiv').show();
	$('#minDiv').hide();
}

function addMess(id, context) {
	$('#contextDiv').prepend("<span onclick='localTo(" + id + ")' style='cursor:pointer;' onmouseover='messHighLight(this);' onmouseout='messNoHighLight(this)'>" + context + "</span><br/>");
}

function messHighLight(obj) {
	$(obj).css('font-weight', 'bold');
}

function messNoHighLight(obj) {
	$(obj).css('font-weight', 'normal');
}

function clearMess() {
	$("#contextDiv").html('');
}

var lmarker;
function localTo(id) {
	if(typeof(lmarker) != "undefined") {
		markEnd.removeMarker(lmarker);
		lmarker.destroy();
	}
	var lPoint = new OpenLayers.LonLat(parseFloat(dataList[id].longitude/60).toFixed(6), parseFloat(dataList[id].latitude/60).toFixed(6)).transform(fromProjection, OSM_toProjection);
	map.setCenter(lPoint);
	icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-black.png', iconSize, iconOffset);
	lmarker = new OpenLayers.Marker(lPoint, icon);
	markLayer[1].addMarker(lmarker);
}

//---------------------------------------------------Show video---------------------------------------------------/
function addEventMarkers() {
	//Idle
	var location = new OpenLayers.LonLat(116.455396, 39.899105).transform(fromProjection, OSM_toProjection);
	addMarkers('Idle1', location, 8, "Event Name: Idle</br>Time: 2013-04-27 17:27:43</br>Longitude:116.455396</br>Latitude:39.899105</br><img onclick='clickVideo(\"2013-04-27 17:27:43\");' src='" + basePath + "/images/livetou.png' title='Playback' style='width:20px; height:20px; right:5px; cursor:pointer' alt='Playback'></img>");
	location = new OpenLayers.LonLat(116.294263, 39.981885).transform(fromProjection, OSM_toProjection);
	addMarkers('Idle2', location, 8, "Event Name: Idle</br>Time: 2013-04-27 18:05:29</br>Longitude:116.294263</br>Latitude:39.981885</br><img onclick='clickVideo(\"2013-04-27 18:05:29\");' src='" + basePath + "/images/livetou.png' title='Playback' style='width:20px; height:20px; right:5px; cursor:pointer' alt='Playback'></img>");
	//speeding
	location = new OpenLayers.LonLat(116.452913, 39.953023).transform(fromProjection, OSM_toProjection);
	addMarkers('Speed1', location, 6, "Event Name: Speeding</br>Time: 2013-04-27 07:10:28</br>Speeding: 87</br>Longitude:116.452913</br>Latitude:39.953023</br><img onclick='clickVideo(\"2013-04-27 07:10:28\");' src='" + basePath + "/images/livetou.png' title='Playback' style='width:20px; height:20px; right:5px; cursor:pointer' alt='Playback'></img>");
	//sudden acc
	location = new OpenLayers.LonLat(116.415698, 39.967851).transform(fromProjection, OSM_toProjection);
	addMarkers('Acc1', location, 2, "Event Name: Sudden acceleration</br>Time: 2013-04-27 07:00:05</br>Longitude:116.415698</br>Latitude:39.967851</br><img onclick='clickVideo(\"2013-04-27 07:00:05\");' src='" + basePath + "/images/livetou.png' title='Playback' style='width:20px; height:20px; right:5px; cursor:pointer' alt='Playback'></img>");
	//Sudden braking
	location = new OpenLayers.LonLat(116.455493, 39.915356).transform(fromProjection, OSM_toProjection);
	addMarkers('Braking1', location, 3, "Event Name: Sudden braking</br>Time: 2013-04-27 17:30:43</br>Speeding: 83</br>Longitude:116.455493</br>Latitude:39.915356</br><img onclick='clickVideo(\"2013-04-27 17:30:43\");' src='" + basePath + "/images/livetou.png' title='Playback' style='width:20px; height:20px; right:5px; cursor:pointer' alt='Playback'></img>");
	//sudden left
	location = new OpenLayers.LonLat(116.365723, 39.966321).transform(fromProjection, OSM_toProjection);
	addMarkers('Left1', location, 4, "Event Name: Sudden left</br>Time:2013-04-27 06:35:45</br>Longitude:116.365723</br>Latitude:39.966321</br><img onclick='clickVideo(\"2013-04-27 06:35:45\");' src='" + basePath + "/images/livetou.png' title='Playback' style='width:20px; height:20px; right:5px; cursor:pointer' alt='Playback'></img>");
	//sudden right
	location = new OpenLayers.LonLat(116.509570, 39.790198).transform(fromProjection, OSM_toProjection);
	addMarkers('Right1', location, 5, "Event Name: Sudden right</br>Time: 2013-04-27 16:06:47</br>Longitude:116.509570</br>Latitude:39.790198</br><img onclick='clickVideo(\"2013-04-27 16:06:47\");' src='" + basePath + "/images/livetou.png' title='Playback' style='width:20px; height:20px; right:5px; cursor:pointer' alt='Playback'></img>");
}

/**
 * Click the video button
 */
function clickVideo(time) {
	if(!checkVehIsOnLine(time)) {
		alert('You can not view the video because the vehicle is offline!');
		return;
	} else {
		openTimeWin(time);
	}
}

/**
 * Check whether the vehicle is online or not
 * @returns {Boolean} true: online; false: offline
 */
function checkVehIsOnLine(time) {
	return time != '2013-04-27 07:00:05';
}

/**
 * Open the select time window
 */
function openTimeWin(time) {
	$("body").append("<div id='divBackGround' class='divBackGround' ></div>");
	$("body").append("<div id='openTimeDiv' class='openTimeDiv' ></div>");
	$("#openTimeDiv").css("left", ($(window).width()-300)/2).css("top",($(window).height()-110)/2).show("fast");
	$("body").append("<div id='closeTimeDiv' class='closeDiv' ><img src='" + basePath + "images/button_xx.gif'onmousemove=\"this.src='" + basePath + "images/button_xx2.gif'\" onmouseout=\"this.src='" + basePath + "images/button_xx.gif'\" style='cursor: pointer;' onclick='closeTimeWin();'/></div>");
	$("#closeTimeDiv").css("left", (($(window).width()-300)/2) + 304).css("top",(($(window).height()-110)/2)-10).show("fast");
	$("#openTimeDiv").append("<div class='pop_main' style='height: 100%;'>" +
																	"<table height='100%' cellspacing='0' cellpadding='0' style='border-left:1px solid rgb(192, 201, 207);'>" +
																		"<tr>" +
																			"<td class='text_bg'>Event time:</td>" +
																			"<td style='text-align: left;'>" +
																				time +
																			"</td>" +
																		"</tr>" +
																		"<tr>" +
																			"<td class='text_bg'>Starting time:</td>" +
																			"<td style='text-align: left;'>" +
																				"<label class='input_date'><input id='open_startTime' type='text' style='width: 127px;' onfocus=\"WdatePicker({dateFmt:'HH:mm:ss', maxDate:&quot;#F{$dp.$D('open_endTime')}&quot;, readOnly:true});this.blur();\"></input></label>" +
																			"</td>" +
																		"</tr>" +
																		"<tr>" +
																			"<td class='text_bg'>Ending time:</td>" +
																			"<td style='text-align: left;'>" +
																				"<label class='input_date'><input id='open_endTime' type='text' style='width: 127px;' onfocus=\"WdatePicker({dateFmt:'HH:mm:ss', minDate:&quot;#F{$dp.$D('open_startTime')}&quot;, readOnly:true});this.blur();\"></input></label>" +
																			"</td>" +
																		"</tr>" +
																		"<tr>" +
																			"<td style='text-align: center;'><input id='clearTimeWin' type='button' value='Clear' onclick='clearTimeWin();' /></td>" +
																			"<td style='text-align: center;'><input id='subTimeWin' type='button' value='Submit' onclick='submitTimeWin(\"" + time + "\");' /></td>" +
																		"</tr>" +
																	"</table>" +
																"</div>");
	$('#open_startTime').val(getTenMin(time)[0]);
	$('#open_endTime').val(getTenMin(time)[1]);
}

/**
 * get 5 min before and 5 min after
 * @param time
 * @returns {Array} [5 min before, 5 min after]
 */
function getTenMin(time) {
	var ten_time = [];
	var yyyyMMdd = time.split(' ')[0].split('-');
	var HHmmss = time.split(' ')[1].split(':');
	var begin_date = new Date(yyyyMMdd[0], yyyyMMdd[1], yyyyMMdd[2], HHmmss[0], parseInt(HHmmss[1]) - 5, HHmmss[2]);
	var end_date = new Date(yyyyMMdd[0], yyyyMMdd[1], yyyyMMdd[2], HHmmss[0], parseInt(HHmmss[1]) + 5, HHmmss[2]);
	ten_time[0] = (begin_date.getHours() < 10 ? "0" + begin_date.getHours() : begin_date.getHours()) + ':' + (begin_date.getMinutes() < 10 ? "0" + begin_date.getMinutes() : begin_date.getMinutes()) + ':' + begin_date.getSeconds();
	ten_time[1] = (end_date.getHours() < 10 ? "0" + end_date.getHours() : end_date.getHours()) + ':' + (end_date.getMinutes() < 10 ? "0" + end_date.getMinutes() : end_date.getMinutes()) + ':' + end_date.getSeconds();
	return ten_time;
}

function clearTimeWin() {
	$('#open_startTime').val('');
	$('#open_endTime').val('');
}

function submitTimeWin(time) {
	closeTimeWin();
	if(!checkVideoIsExist(time)) {
		alert('The video segment does not exist!');
		return;
	} else {
		openProgressWin();
	}
}

/**
 * Close the select time window
 */
function closeTimeWin() {
	$("#divBackGround").remove();
	$("#openTimeDiv").remove();
	$("#closeTimeDiv").remove();
}

function checkVideoIsExist(time) {
	return time != '2013-04-27 18:05:29';
}

function openProgressWin() {
	$("body").append("<div id='divBackGround' class='divBackGround' ></div>");
	$("body").append("<div id='openProgressDiv' class='openProgressDiv' ></div>");
	$("#openProgressDiv").css("left", ($(window).width()-500)/2).css("top",($(window).height()-110)/2).show("fast");
	$("body").append("<div id='closeProgressDiv' class='closeDiv' ><img src='" + basePath + "images/button_xx.gif'onmousemove=\"this.src='" + basePath + "images/button_xx2.gif'\" onmouseout=\"this.src='" + basePath + "images/button_xx.gif'\" style='cursor: pointer;' onclick='stopProgress();'/></div>");
	$("#closeProgressDiv").css("left", (($(window).width()-500)/2)+500).css("top",(($(window).height()-110)/2)-10).show("fast");
	$("#openProgressDiv").append("<div style='font-weight:bold;'>Video being uploaded to the server, please wait...</div>" +
																"<div id='progressbar'><div class='progress-label'>Uploading...</div></div>" +
																"<script>" +
																	"$(function() {" +
																		"var progressbar = $('#progressbar')," +
																		"progressLabel = $('.progress-label');" +
																		"progressbar.progressbar({" +
																			"value: 100" +
//																			"change: function() {" +
//																				"progressLabel.text( progressbar.progressbar( 'value' ) + '%' );" +
//																			"}," +
//																			"complete: function() {" +
//																				"progressLabel.text('Complete!');" +
//																				"closeProgressWin();" +
//																			"}" +
																		"});" +
																		"setTimeout(closeProgressWin, 1000);" +
//																		"function progress() {" +
//																			"var val = progressbar.progressbar('value') || 0;" +
//																			"progressbar.progressbar('value', val + 1);" +
//																			"if (val < 99) {" +
//																				"if(val == 26) {" +
//																					"setTimeout(progress, 2000);" +
//																				"} else if(val == 67) {" +
//																					"setTimeout(progress, 1000);" +
//																					"} else if(val == 98) {" +
//																					"setTimeout(progress, 2600);" +
//																				"} else {" +
//																					"setTimeout(progress, 100);" +
//																				"}" +
//																			"}" +
//																		"}" +
//																		"setTimeout(progress, 3000);" +
																	"});" +
														  		"</script>");
}

function stopProgress() {
	$("#divBackGround").remove();
	$("#openProgressDiv").remove();
	$("#closeProgressDiv").remove();
}

function closeProgressWin() {
	$("#divBackGround").remove();
	$("#openProgressDiv").remove();
	$("#closeProgressDiv").remove();
	openVideoWin();
}

/**
 * Open video window
 */
function openVideoWin() {
	$("body").append("<div id='divBackGround' class='divBackGround' ></div>");
	$("body").append("<div id='openVideoDiv' class='openVideoDiv' ></div>");
	$("#openVideoDiv").css("left",($(window).width()-500)/2).css("top",($(window).height()-410)/2).show("fast");
	$("body").append("<div id='closeVideoDiv' class='closeDiv' ><img src='" + basePath + "images/button_xx.gif'onmousemove=\"this.src='" + basePath + "images/button_xx2.gif'\" onmouseout=\"this.src='" + basePath + "images/button_xx.gif'\" style='cursor: pointer;' onclick='closeVideoWin();'/></div>");
	$("#closeVideoDiv").css("left",(($(window).width()-500)/2)+495).css("top",(($(window).height()-410)/2)-10).show("fast");
	$("#openVideoDiv").append("<object id='zdcomaudio' classid='clsid:7E6CC141-11E8-41AE-A1BE-CF6606B1DB48' codebase='" + basePath + "video/ZdcomAudio.cab#version=1,0,0,1' style='height: 100%; width: 100%;' />");
	$("#play_video").css("left",(($(window).width()-500)/2)+225).css("top",(($(window).height()-410)/2)+400).show("fast");
}

function video_play() {
	var res = document.getElementById("zdcomaudio").StartOCX("211.140.202.4", 9026, "ctx", 0, 11923, 1, 400, 300);
	alert(res == 0 ? 'success' : 'error');
}

function video_pause() {
	document.getElementById("zdcomaudio").CloseOCX();
}

/**
 * Close video window
 */
function closeVideoWin(){
	$("#divBackGround").remove();
	document.getElementById("zdcomaudio").CloseOCX();
	$("#zdcomaudio").remove();
	$("#openVideoDiv").remove();
//	$("#openVideoDiv").hide();
	$("#play_video").hide();
	$("#closeVideoDiv").remove();
}
/**-------------------------------**/