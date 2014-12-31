var timer;	//定时器
var interval = 15; //间隔数
var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
var OSM_toProjection = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
var map, vectorLayer, marker, markers, popup, points, point;
var perData, totalCount, nop;
//var markStart, markEnd, markIdle, markSpeed, markAcc, markBrake, markLeft, markRight, markSlide, markAir, markEngine, markPOI;
var markLayer =  [];
var lineFeature, markerArr = [];
var sel_lisencePlate, sel_startDate, sel_endDate, sel_startTime, sel_endTime;	//selected data
var v_startTime, v_endTime;	//millonsecond of start time and end time
var iconSize = new OpenLayers.Size(20, 30);
var iconOffset = new OpenLayers.Pixel(-(iconSize.w/2), -iconSize.h+5);
var icon;
var vehidd;
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
$(function() {
	$('#markerDiv').width($(window).width());
	//select_vehicle_name.attachEvent('onChange', getSchedule);
//	cascadeSel_setVehNameValue();
	$( "#play" ).button({
		text: false,
		icons: {
			primary: "ui-icon-play"
		}
    })
	.click(function() {
		var options;
		if ( $( this ).text() === "play" ) {
			options = {
				label: "pause",
				icons: {
					primary: "ui-icon-play"
				}
			};
			doStop();
		} else {
			options = {
				label: "play",
				icons: {
					primary: "ui-icon-pause"
				}
			};
			doRun();
		}
		$( this ).button( "option", options );
	});
	
	$( "#play_video" ).button({
		text: false,
		icons: {
			primary: "ui-icon-play"
		}
    })
	.click(function() {
		var options;
		if ( $( this ).text() === "play" ) {
			options = {
					label: "pause",
					icons: {
						primary: "ui-icon-pause"
					}
			};
			video_play();
		} else {
			options = {
					label: "play",
					icons: {
						primary: "ui-icon-play"
					}
			};
			video_pause();
		}
		$( this ).button( "option", options );
	});
	
	$( "#voice_video" ).button({
		text: false,
		icons: {
			primary: "ui-icon-volume-on"
		}
	})
	.click(function() {
		var options;
		if ( $( this ).text() === "volume-on" ) {
			options = {
					label: "volume-off",
					icons: {
						primary: "ui-icon-volume-off"
					}
			};
			volumeOff();
		} else {
			options = {
					label: "volume-on",
					icons: {
						primary: "ui-icon-volume-on"
					}
			};
			volumeOn();
		}
		$( this ).button( "option", options );
	});
});

/**
 * Get schedule by vehicle
 */
function getSchedule() {
	return;
	$.ajax({
	    type : "POST",
	    async : false,
	    dataType : 'json',
	    data : {
	    	'vo.vehID' : select_vehicle_name.getSelectedValue()
	    },
	    url : basePath + "monitor/display!getSchedule",
	    success : function(d) {
			if("empty" != d.result) {
				$("#sel_schedule option").remove();
				$("#sel_schedule").append("<option value='-1'>Please choose schedule...</option>");
				var hid_scheduleID = $('#hid_scheduleID').val();
				for ( var int = 0; int < d.length; int++) {
					if(hid_scheduleID == d[int].key) {
						$("#sel_schedule").append("<option value='" + d[int].key + "' selected>" + d[int].value + "</option>");
					} else {
						$("#sel_schedule").append("<option value='" + d[int].key + "'>" + d[int].value + "</option>");
					}
				}
				setTimes();
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
//		$("#endDate").val(endDat.split(' ')[0]);
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
			data=[];			
			doStop();	//stop the timer
			$('#play').button('option', {	//change the buttom to pause
				label: "pause",
				icons: {
					primary: "ui-icon-play"
				}
			});
			clearRoute();
			var xxx=cleanAllMarkers();//delete the route
			addRoute2($("#slider").slider("option", "value"));	//redraw the route
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
	var xxx=cleanAllMarkers();
	addRoute2(newValue);	//redraw the route
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
	perData = 1;		//add 10 data per slider's changging
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
	map.setCenter(endPoint, 15);	//move the center to ending location
	$('#operDiv').css('visibility', 'visible');	//the div which the slider on it
	initSlider();	//initial the slider
	//remove all old event markers
	for ( var int = 2; int < 11; int++) {
		delMarkers(markLayer[int]);
	}
	//add new event markers
	getEventData();
}

/**
*	Initial the complete route
*/
function addRouteAll2() {
	var xxx=cleanAllMarkers();
	clearRoute();
	totalCount = dataList.length;	//count of total data
	perData = 1;		//add 10 data per slider's changging
	nop = Math.ceil(totalCount/perData);	//slider's max value
	var endPoint = new OpenLayers.LonLat(parseFloat(dataList[totalCount-1].longitude/60).toFixed(6), parseFloat(dataList[totalCount-1].latitude/60).toFixed(6)).transform(fromProjection, OSM_toProjection);
	points = [];
	var sinobj;
	var location;
	var eventObj;
	var showStr;
	var isFirst = 0;
	var first_lon, first_lat, first_time, last_lon, last_lat, last_time;
	for(var i = 0; i < totalCount; i++) {	//add all data point to the array
		sinobj = dataList[i];
		if(sinobj.beh_type == ""){
			if(isFirst == 0) {
				first_lon = sinobj.longitude;
				first_lat = sinobj.latitude;
				first_time = sinobj.gpsDateTime;
				isFirst = 1;
			}
			last_lon = sinobj.longitude;
			last_lat = sinobj.latitude;
			last_time = sinobj.gpsDateTime;
			point = new OpenLayers.Geometry.Point(parseFloat(sinobj.longitude/60).toFixed(6), parseFloat(sinobj.latitude/60).toFixed(6)).transform(fromProjection, OSM_toProjection);	//根据坐标取点，并计算投影偏移量
			points.push(point);
		}else{
			eventObj = sinobj;
			location = new OpenLayers.LonLat(eventObj.longitude, eventObj.latitude).transform(fromProjection, OSM_toProjection);
			showStr = "Event Name: " + eventObj.beh_name + "</br>" +
			"Event Date: " + eventObj.riqi + "</br>" +
			"Begin Time: " + eventObj.time_begin + "</br>" +
			"End Time: " + eventObj.time_end + "</br>";
			if(eventObj.beh_type >= 15) {
				showStr += "Last: " + eventObj.time_cont + " seconds</br>";
				if(eventObj.beh_type == 15) {	//Speeding
					showStr += "Speed: " + eventObj.veh_speed + " km/h</br>";
				} else if(eventObj.beh_type == 19) {	//engine speeding
					showStr += "Engine Speed: " + eventObj.veh_speed + " r/min</br>";
				}
			}
			showStr += "Longitude:" + eventObj.longitude + "</br>" +
					"Latitude:" + eventObj.latitude + "</br>" +
					"<img onclick='clickVideo(\"" + eventObj.riqi + "\",\"" + eventObj.time_begin + "\");' src='" + basePath + "/images/livetou.png' title='Playback' style='width:20px; height:20px; right:5px; cursor:pointer' alt='Playback'></img>";
			addMarkers2(eventObj.not_id, location, eventObj.beh_type, showStr);
		}
	}
	lineFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(points), null, style_green);	//build the route
	vectorLayer.addFeatures(lineFeature);		//add the route to the layer
	var startPoint = new OpenLayers.LonLat(parseFloat(first_lon/60).toFixed(6), parseFloat(first_lat/60).toFixed(6)).transform(fromProjection, OSM_toProjection);
	var endPoint = new OpenLayers.LonLat(parseFloat(last_lon/60).toFixed(6), parseFloat(last_lat/60).toFixed(6)).transform(fromProjection, OSM_toProjection);
	delMarkers(markLayer[0]);	//remove old starting location's marker
	delMarkers(markLayer[1]);		//remove old ending location's marker
	//add new starting\ending location's marker
	addMarkers2('startP', startPoint, 0, 'Starting location: time: ' +first_time + '</br>Longitude:' + parseFloat(first_lon/60).toFixed(6) + '</br>Latitude:' + parseFloat(first_lat/60).toFixed(6));	
	addMarkers2('endP', endPoint, 1, 'Ending location: Time: ' + last_time + '</br>Longitude:' + parseFloat(last_lon/60).toFixed(6) + '</br>Latitude:' + parseFloat(last_lat/60).toFixed(6));
	map.setCenter(endPoint, 15);	//move the center to ending location
	$('#operDiv').css('visibility', 'visible');	//the div which the slider on it
	initSlider();	//initial the slider
	//remove all old event markers
//	for ( var int = 2; int < 11; int++) {
//		delMarkers(markLayer[int]);
//	}
	//add new event markers
//	getEventData();
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
 * Change route by slider
 */
function addRoute2(pointIndex) {
	points = [];
	var i = 0;
	var max = (pointIndex*perData >= dataList.length ? dataList.length : pointIndex*perData);
	max = (max == 0 ? 1 : max);
//	clearMess();
	var location;
	var eventObj;
	var showStr;
	var sinobj;
	var isFirst = 0;
	var first_lon, first_lat, first_time, last_lon, last_lat, last_time;
	var oldBeginTime, oldBeh;
	for(; i < max; i++) {
		sinobj = dataList[i];
		if(sinobj.beh_type == ""){
			if(isFirst == 0) {
				first_lon = sinobj.longitude;
				first_lat = sinobj.latitude;
				first_time = sinobj.gpsDateTime;
				isFirst = 1;
			}
			last_lon = sinobj.longitude;
			last_lat = sinobj.latitude;
			last_time = sinobj.gpsDateTime;
			point = new OpenLayers.Geometry.Point(parseFloat(sinobj.longitude/60).toFixed(6), parseFloat(sinobj.latitude/60).toFixed(6)).transform(fromProjection, OSM_toProjection);	//根据坐标取点，并计算投影偏移量
			points.push(point);
		}else{
			eventObj = sinobj;
			if(oldBeginTime == eventObj.time_begin && oldBeh == eventObj.beh_type) {	//duplicate event points
				continue;
			}
			oldBeginTime = eventObj.time_begin;
			oldBeh = eventObj.beh_type;
			location = new OpenLayers.LonLat(eventObj.longitude, eventObj.latitude).transform(fromProjection, OSM_toProjection);
			showStr = "Event Name: " + eventObj.beh_name + "</br>" +
			"Event Date: " + eventObj.riqi + "</br>" +
			"Begin Time: " + eventObj.time_begin + "</br>" +
			"End Time: " + eventObj.time_end + "</br>";
			if(eventObj.beh_type >= 15) {
				showStr += "Last: " + eventObj.time_cont + " seconds</br>";
				if(eventObj.beh_type == 15) {	//Speeding
					showStr += "Speed: " + eventObj.veh_speed + " km/h</br>";
				} else if(eventObj.beh_type == 19) {	//engine speeding
					showStr += "Engine Speed: " + eventObj.veh_speed + " r/min</br>";
				}
			}
			showStr += "Longitude:" + eventObj.longitude + "</br>" +
					"Latitude:" + eventObj.latitude + "</br>" +
					"<img onclick='clickVideo(\"" + eventObj.riqi + "\",\"" + eventObj.time_begin + "\");' src='" + basePath + "/images/livetou.png' title='Playback' style='width:20px; height:20px; right:5px; cursor:pointer' alt='Playback'></img>";
			addMarkers2(eventObj.not_id, location, eventObj.beh_type, showStr);
		}
	}
	lineFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(points), null, style_green);	//build the route
	$('#sTime').text('Time：' + dataList[max-1].gpsDateTime/* + '-' + parseFloat(dataList[max-1].longitude/60).toFixed(6) + '-' + parseFloat(dataList[max-1].latitude/60).toFixed(6)*/);
	vectorLayer.addFeatures(lineFeature);		//add the route to the layer
	
	var startPoint = new OpenLayers.LonLat(parseFloat(first_lon/60).toFixed(6), parseFloat(first_lat/60).toFixed(6)).transform(fromProjection, OSM_toProjection);
	var endPoint = new OpenLayers.LonLat(parseFloat(last_lon/60).toFixed(6), parseFloat(last_lat/60).toFixed(6)).transform(fromProjection, OSM_toProjection);
	delMarkers(markLayer[0]);	//remove old starting location's marker
	delMarkers(markLayer[1]);		//remove old ending location's marker
	//add new starting\ending location's marker
	addMarkers2('startP', startPoint, 0, 'Starting location: time: ' +first_time + '</br>Longitude:' + parseFloat(first_lon/60).toFixed(6) + '</br>Latitude:' + parseFloat(first_lat/60).toFixed(6));	
	addMarkers2('endP', endPoint, 1, 'Ending location: Time: ' + last_time + '</br>Longitude:' + parseFloat(last_lon/60).toFixed(6) + '</br>Latitude:' + parseFloat(last_lat/60).toFixed(6));

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
function addMarkers(id, point, flag, content) {
	var eventArr = new Array();
	var img_name = null;
	if(flag == 0) {
		img_name = 'm-green.png';	//Starting location
	} else if(flag == 1) {
		img_name = 'm-red.png';	//Ending location
	} else if(flag == 11) {
		img_name = 'm-teal.png';	//Sudden acceleration
	} else if(flag == 12) {
		img_name = 'm-magenta.png';	//Sudden braking
	} else if(flag == 13) {
		img_name = 'm-yellow.png';	//Sudden left
	} else if(flag == 14) {
		img_name = 'm-orange.png';	//Sudden right
	} else if(flag == 15) {
		img_name = 'm-purple.png';	//Speeding
	} else if(flag == 16) {
		img_name = 'm-gray.png';	//Neutral slide
	} else if(flag == 17) {
		img_name = 'm-blue.png';	//Idle
	} else if(flag == 18) {
		img_name = 'm-lime.png';	//Idle air conditioning
	} else if(flag == 19) {
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
				markLayer[flag-9].addMarker(eventArr[id]);
			} else if(map.getZoom() <= 10  && eventArr[id].isDrawn()) {	//hide markers when map'zoom less  than or equal 12
				markLayer[flag-9].removeMarker(eventArr[id]);
			}
		});
	}
}

/**
 * add event marker
 * 
 */
function addMarkers2(id, point, flag, content) {
	var eventArr = new Array();
	var img_name = null;
	if(flag == 0) {
		img_name = 'm-green.png';	//Starting location
	} else if(flag == 1) {
		img_name = 'm-red.png';	//Ending location
	} else if(flag == 11) {
		img_name = 'm-teal.png';	//Sudden acceleration
	} else if(flag == 12) {
		img_name = 'm-magenta.png';	//Sudden braking
	} else if(flag == 13) {
		img_name = 'm-yellow.png';	//Sudden left
	} else if(flag == 14) {
		img_name = 'm-orange.png';	//Sudden right
	} else if(flag == 15) {
		img_name = 'm-purple.png';	//Speeding
	} else if(flag == 16) {
		img_name = 'm-gray.png';	//Neutral slide
	} else if(flag == 17) {
		img_name = 'm-blue.png';	//Idle
	} else if(flag == 18) {
		img_name = 'm-lime.png';	//Idle air conditioning
	} else if(flag == 19) {
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
		markLayer[flag-9].addMarker(eventArr[id]);
//		map.events.register('zoomend', map, function(evt) {		//zooming map
//			if(map.getZoom() > 10 && !eventArr[id].isDrawn()){	//show markers when map'zoom more than 12
//				markLayer[flag-9].addMarker(eventArr[id]);
//			} else if(map.getZoom() <= 10  && eventArr[id].isDrawn()) {	//hide markers when map'zoom less  than or equal 12
//				markLayer[flag-9].removeMarker(eventArr[id]);
//			}
//		});
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

function cleanAllMarkers(){
	for(var i=0;i<11;i++){
		map.removeLayer(markLayer[i]);
	}
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
	    map.addLayers(markLayer);
	    return true;
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
   // map.addControl(new OpenLayers.Control.LayerSwitcher());	//图层选择
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
    //map.setCenter(new OpenLayers.LonLat(103.842033,1.291128).transform(fromProjection, OSM_toProjection));	//中心点
    map.setCenter(new OpenLayers.LonLat(103.842033,1.291128).transform(fromProjection, OSM_toProjection));	//中心点
    getPOI();
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
	icon = new OpenLayers.Icon(basePath + 'images/'+ico+'_.png', size, offset);
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
//	sel_endDate = $.trim($('#endDate').val());
	sel_endTime = $.trim($('#endTime').val());
	sel_lisencePlate = $.trim(select_vehicle_name.getSelectedText());
	if(sel_lisencePlate == null || sel_lisencePlate == '' || sel_lisencePlate == '-1') {
		alert("Please choose a vehicle!");
		return;
	}
	getRouteDate();
}

function checkSearch2() {
	sel_startDate = $.trim($('#startDate').val());
	sel_startTime = $.trim($('#startTime').val());
//	sel_endDate = $.trim($('#endDate').val());
	sel_endTime = $.trim($('#endTime').val());
	sel_lisencePlate = $.trim(select_vehicle_name.getSelectedText());
	if(sel_lisencePlate == null || sel_lisencePlate == '' || sel_lisencePlate == '-1') {
		alert("Please choose a vehicle!");
		return;
	}
	getRouteDate2();
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

/**
 * Get data of route
 */
function getRouteDate2() {
	$.ajax({
	    type : "POST",
	    async : false,
	    dataType : 'json',
	    data : {
	    	'vo.vehID' : select_vehicle_name.getSelectedValue(),
	    	'vo.plateNumber' : sel_lisencePlate,
	    	'vo.beginDate' : sel_startDate,
	    	'vo.beginTime' : sel_startTime,
//	    	'vo.endDate' : sel_endDate,
	    	'vo.endTime' : sel_endTime
	    },
	    url : basePath + "monitor/display!getGeo",
	    success : function(d) {
			if("empty" != d.result){
				vehidd=select_vehicle_name.getSelectedValue();
				dataList = d;
				addRouteAll2();
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
//---------------------------------------------------Show video---------------------------------------------------/
function openProgressWin2(data,time) {
	$("body").append("<div id='divBackGround' class='divBackGround' ></div>");
	$("body").append("<div id='openProgressDiv' class='openProgressDiv' ></div>");
	$("#openProgressDiv").css("left", ($(window).width()-500)/2).css("top",($(window).height()-110)/2).show("fast");
	//$("body").append("<div id='closeProgressDiv' class='closeDiv' ><img src='" + basePath + "images/button_xx.gif'onmousemove=\"this.src='" + basePath + "images/button_xx2.gif'\" onmouseout=\"this.src='" + basePath + "images/button_xx.gif'\" style='cursor: pointer;' onclick='stopProgress();'/></div>");
	$("#closeProgressDiv").css("left", (($(window).width()-500)/2)+500).css("top",(($(window).height()-110)/2)-10).show("fast");
	$("#openProgressDiv").append("<div id='ProgressTitle' style='font-weight:bold;'></div>" +	//Video being uploaded to the server, please wait...
		"<div id='progressbar'><div id='ProgressText' class='progress-label' style='left:30%'>Channel:&nbsp;<select id='chlid'></select>&nbsp;<input type='button' value='OK' onclick='getchlid(\""+data+"\",\""+time+"\")'/>&nbsp;<input type='button' value='Cancel' onclick='closeProgressWin()'/></div></div>" );
}


function getchlid(date,time){
	var chlid=$("#chlid").val();
	if(chlid==null || chlid==""){
		alert("Please select the channel. If there are no channel options, please set the camera for the device.");
		return;
	}else{
		closeProgressWin();
		openProgressWin();
		setProgressText('Verifying user identity and vehicle status...');
		var mrl="";
		$.ajax({
		     type: "POST",
		     async: true,
		     url :basePath+"monitor/ip-cam!mod1" ,
		  	 data:{'id':vehidd,'vo.date':date,'vo.time':time,'cdid':chlid},
		  	 dataType : "json",
		  	 success : function(data) {
		  		/****
		 		 * false no video
		 		 * false2 The vehicle is powered off, so the related video is unavailable.
		 		 * false3 no ip
		 		 * false4 no type
		 		 * false5 no cam
		 		 */
		  		closeProgressWin();
		  		 if(data.result=='false'){
		  			alert("no video"); 
		  		 }else if(data.result=='false2'){
		  			alert("The vehicle is powered off, so the related video is unavailable.");
		  		 }else if(data.result=='false3'){
		  			alert("Not set ip camera");
		  		 }else if(data.result=='false4'){
		  			alert("Not set type camera");
		  		 }else if(data.result=='false5'){
		  			alert("The vehicle not set the camera");
		  		 }else{
		  			 mrl=data.result;
		  			if($("#videoDIV").css("display") == "block") {
		 	  			alert("The playback video already exists!");
		 	  			return;
		 	  		}
		 	  		
		 	  		setTimeout("openVideoWin(\"" + mrl + "\")", 100);
		  		 }
		     } 
		 });
		
		
	}
}



/**
 * Click the video button
 */
function clickVideo(date,time) {
	$.ajax({
	     type: "POST",
	     async: true,
	     url :basePath+"vehicle/vehicle-list!getchannel" ,
	  	 data:{'id':vehidd},
	  	 dataType : "json",
	  	 success : function(data) {
	  			openProgressWin2(data,time);
	  		for(var i=0;i<data.length;i++){
	  			$("#chlid").append("<option value='"+data[i].coreid+"'>"+data[i].channel+"</option>");
	  		}
	     } 
	 });
}



/**
 * Open the progress window
 */
function openProgressWin() {
	$("body").append("<div id='divBackGround' class='divBackGround' ></div>");
	$("body").append("<div id='openProgressDiv' class='openProgressDiv' ></div>");
	$("#openProgressDiv").css("left", ($(window).width()-500)/2).css("top",($(window).height()-110)/2).show("fast");
	//$("body").append("<div id='closeProgressDiv' class='closeDiv' ><img src='" + basePath + "images/button_xx.gif'onmousemove=\"this.src='" + basePath + "images/button_xx2.gif'\" onmouseout=\"this.src='" + basePath + "images/button_xx.gif'\" style='cursor: pointer;' onclick='stopProgress();'/></div>");
	$("#closeProgressDiv").css("left", (($(window).width()-500)/2)+500).css("top",(($(window).height()-110)/2)-10).show("fast");
	$("#openProgressDiv").append("<div id='ProgressTitle' style='font-weight:bold;'></div>" +	//Video being uploaded to the server, please wait...
																"<div id='progressbar'><div id='ProgressText' class='progress-label'>Loading...</div></div>" +
																"<script>" +
																	"$(function() {" +
																		"var progressbar = $('#progressbar')," +
																		"progressLabel = $('.progress-label');" +
																		"progressbar.progressbar({" +
																			"value: 100" +
																		"});" +
																	"});" +
														  		"</script>");
}

/**
 * Close the progress window
 */
function closeProgressWin() {
	$("#divBackGround").remove();
	$("#openProgressDiv").remove();
	$("#closeProgressDiv").remove();
}

function stopProgress() {
	$("#divBackGround").remove();
	$("#openProgressDiv").remove();
	$("#closeProgressDiv").remove();
}

function setProgressText(text) {
	$('#ProgressTitle').text(text);
}

/**
 * Open video window
 */
function openVideoWin(mrl) {
	
	$("#closeVideoDiv1").show();
	$("#videoDIV").show();
	var vlc = document.getElementById("vlc");
	if(vlc) {
		vlc.playlist.clear();
		var videos = mrl.split(',');
		for ( var int = 0; int < videos.length; int++) {
			vlc.playlist.add(videos[int]);
		}
		if(!vlc.playlist.isPlaying) {
			
			vlc.playlist.play();
		}
	}
}

/**
 * Close video window
 */
function closeVideoWin(){
	var vlc = document.getElementById("vlc");
	if(vlc) {
		vlc.playlist.stop();
	}
	$("#videoDIV").hide();
	$("#closeVideoDiv1").hide();
}

function closeVLC() {
	var vlc = document.getElementById("vlc");
	if(vlc) {
		vlc.playlist.stop();
	}
}