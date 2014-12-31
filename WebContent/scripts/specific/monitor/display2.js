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
					"<img onclick='clickVideo(\"" + eventObj.riqi + " " + eventObj.time_begin + "\");' src='" + basePath + "/images/livetou.png' title='Playback' style='width:20px; height:20px; right:5px; cursor:pointer' alt='Playback'></img>";
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
	for ( var int = 2; int < 11; int++) {
		delMarkers(markLayer[int]);
	}
	//add new event markers
//	getEventData();
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
					"<img onclick='clickVideo(\"" + eventObj.riqi + " " + eventObj.time_begin + "\");' src='" + basePath + "/images/livetou.png' title='Playback' style='width:20px; height:20px; right:5px; cursor:pointer' alt='Playback'></img>";
			addMarkers2(eventObj.not_id, location, eventObj.beh_type, showStr);
		}
	}
	lineFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(points), null, style_green);	//build the route
	$('#sTime').text('Time：' + dataList[max-1].gpsDateTime/* + '-' + parseFloat(dataList[max-1].longitude/60).toFixed(6) + '-' + parseFloat(dataList[max-1].latitude/60).toFixed(6)*/);
	update(dataList[max-1].veh_speed);
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
				dataList = d;
				CreatChartDiv();
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


//=====================create chart 
var data = [],
totalPoints = 30;
var plot;
function CreatChartDiv(){
	$("body").append("<div id='mainInputTreeDiv1' class='mainInputTreeDiv3' ></div>");
	$("#mainInputTreeDiv1").css("right",0).css("bottom",50);
	$("#mainInputTreeDiv1").css("display","block");
	data=[];
	plot = $.plot("#mainInputTreeDiv1", [ getData() ], {
		series: {
			shadowSize: 0	// Drawing is faster without shadows
		},
		yaxis: {
			min: 0,
			max: 150,
			ticks:6
		},
		xaxis: {
			show: false
		}
	});
}
function getData(sp) {
	if(data.length==0){
		while (data.length < totalPoints) {
			data.push(null);
		}
	}
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
function update(sp) {
	plot.setData([getData(sp)]);
	plot.draw();
}