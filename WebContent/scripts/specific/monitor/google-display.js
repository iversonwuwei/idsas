var timer;	//定时器
var interval = 15; //间隔数
var map, vectorLayer, marker, markers, popup, points, point;
var perData, totalCount, nop;
var markLayer = [];
var polyline, markerArr = [];
var sel_lisencePlate, sel_startDate, sel_endDate, sel_startTime, sel_endTime;	//selected data
var v_startTime, v_endTime;	//millonsecond of start time and end time
var icon;
var vehidd;

/**
 * css style of the route
 */
var style_poly = {
    strokeColor: '#339933',
    strokeOpacity: 1.0,
    strokeWeight: 5
}

/**
 * On window initial
 */
$(function() {
	$('#markerDiv').width($(window).width());
	//select_vehicle_name.attachEvent('onChange', getSchedule);
	//cascadeSel_setVehNameValue();
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
			clearRoute();
			cleanAllMarkers();//delete the route
			addRoute($("#slider").slider("option", "value"));	//redraw the route
		}
	});
	$('#play').css("visibility", "visible");
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
	cleanAllMarkers();
	addRoute(newValue);	//redraw the route
}

/**
 * Begin moving slider
 */
function doRun() {
	timer = window.setInterval(moveSlider, 3000);
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
	cleanAllMarkers();
	clearRoute();
	totalCount = dataList.length;	//count of total data
	perData = 1;		//add 10 data per slider's changging
	nop = Math.ceil(totalCount/perData);	//slider's max value
	var endPoint = new google.maps.LatLng(parseFloat(dataList[totalCount-1].latitude/60).toFixed(6), parseFloat(dataList[totalCount-1].longitude/60).toFixed(6));
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
			point = new google.maps.LatLng(parseFloat(sinobj.latitude/60).toFixed(6), parseFloat(sinobj.longitude/60).toFixed(6));	//根据坐标取点，并计算投影偏移量
			points.push(point);
		} else {
			eventObj = sinobj;
			location = new google.maps.LatLng(eventObj.latitude, eventObj.longitude);
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
			addMarkers(eventObj.not_id, location, eventObj.beh_type, showStr);
		}
	}
	polyline = new google.maps.Polyline({
	    path: points,
	    strokeColor: "#FF0000",
	    strokeOpacity: 1.0,
	    strokeWeight: 5
	  });	//build the route
	polyline.setMap(map);
	var startPoint = new google.maps.LatLng(parseFloat(first_lat/60).toFixed(6), parseFloat(first_lon/60).toFixed(6));
	var endPoint = new google.maps.LatLng(parseFloat(last_lat/60).toFixed(6), parseFloat(last_lon/60).toFixed(6));
	delMarkers(markLayer[0]);	//remove old starting location's marker
	delMarkers(markLayer[1]);		//remove old ending location's marker
	//add new starting\ending location's marker
	addMarkers('startP', startPoint, 0, 'Starting location: time: ' +first_time + '</br>Longitude:' + parseFloat(first_lon/60).toFixed(6) + '</br>Latitude:' + parseFloat(first_lat/60).toFixed(6));	
	addMarkers('endP', endPoint, 1, 'Ending location: Time: ' + last_time + '</br>Longitude:' + parseFloat(last_lon/60).toFixed(6) + '</br>Latitude:' + parseFloat(last_lat/60).toFixed(6));
	map.setCenter(endPoint, 15);	//move the center to ending location
	$('#operDiv').css('visibility', 'visible');	//the div which the slider on it
	initSlider();	//initial the slider
}

/**
 * Change route by slider
 */
function addRoute(pointIndex) {
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
			point = new google.maps.LatLng(parseFloat(sinobj.latitude/60).toFixed(6), parseFloat(sinobj.longitude/60).toFixed(6));	//根据坐标取点，并计算投影偏移量
			points.push(point);
		}else{
			eventObj = sinobj;
			location = new google.maps.LatLng(eventObj.latitude, eventObj.longitude);
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
			addMarkers(eventObj.not_id, location, eventObj.beh_type, showStr);
		}
	}
	polyline = new google.maps.Polyline({
	    path: points,
	    strokeColor: "#FF0000",
	    strokeOpacity: 1.0,
	    strokeWeight: 5
	  });	//build the route
	polyline.setMap(map);
	var startPoint = new google.maps.LatLng(parseFloat(first_lat/60).toFixed(6), parseFloat(first_lon/60).toFixed(6));
	var endPoint = new google.maps.LatLng(parseFloat(last_lat/60).toFixed(6), parseFloat(last_lon/60).toFixed(6));
	delMarkers(markLayer[0]);	//remove old starting location's marker
	delMarkers(markLayer[1]);		//remove old ending location's marker
	//add new starting\ending location's marker
	addMarkers('startP', startPoint, 0, 'Starting location: time: ' +first_time + '</br>Longitude:' + parseFloat(first_lon/60).toFixed(6) + '</br>Latitude:' + parseFloat(first_lat/60).toFixed(6));	
	addMarkers('endP', endPoint, 1, 'Ending location: Time: ' + last_time + '</br>Longitude:' + parseFloat(last_lon/60).toFixed(6) + '</br>Latitude:' + parseFloat(last_lat/60).toFixed(6));
	$('#sTime').text('Time：' + dataList[max-1].gpsDateTime/* + '-' + parseFloat(dataList[max-1].longitude/60).toFixed(6) + '-' + parseFloat(dataList[max-1].latitude/60).toFixed(6)*/);
	//change the center of the map when the ending lication outsides the current view
	if (endPoint.lng() <= map.getBounds().getSouthWest().lng()
			|| endPoint.lng() >= map.getBounds().getNorthEast().lng()
			|| endPoint.lat() <= map.getBounds().getSouthWest().lat()
			|| endPoint.lat() >= map.getBounds().getNorthEast().lat()) {
		map.panTo(endPoint);
	}
}

/**
 * Remove the route
 */
function clearRoute() {
	if(polyline) {
		polyline.setMap(null);
	}
}

/**
 * add event marker
 * 
 */
function addMarkers(id, point, flag, content) {
	var eventArr = new Array();
	var img_name = null;
	var img_title = null;
	if(flag == 0) {
		img_name = 'm-green.png';	//Starting location
		img_title = 'Starting location';
	} else if(flag == 1) {
		img_name = 'm-red.png';	//Ending location
		img_title = 'Ending location';
	} else if(flag == 11) {
		img_name = 'm-teal.png';	//Sudden acceleration
		img_title = 'Sudden acceleration';
	} else if(flag == 12) {
		img_name = 'm-magenta.png';	//Sudden braking
		img_title = 'Sudden braking';
	} else if(flag == 13) {
		img_name = 'm-yellow.png';	//Sudden left
		img_title = 'Sudden left';
	} else if(flag == 14) {
		img_name = 'm-orange.png';	//Sudden right
		img_title = 'Sudden right';
	} else if(flag == 15) {
		img_name = 'm-purple.png';	//Speeding
		img_title = 'Speeding';
	} else if(flag == 16) {
		img_name = 'm-gray.png';	//Neutral slide
		img_title = 'Neutral slide';
	} else if(flag == 17) {
		img_name = 'm-blue.png';	//Idle
		img_title = 'Idle';
	} else if(flag == 18) {
		img_name = 'm-lime.png';	//Idle air conditioning
		img_title = 'Idle air conditioning';
	} else if(flag == 19) {
		img_name = 'm-white.png';	//Engine overspeed
		img_title = 'Engine overspeed';
	}
	eventArr[id] = new google.maps.Marker({
		position : point,
		map : map,
		icon : new google.maps.MarkerImage(basePath
				+ 'scripts/common/OpenLayers-2.12/img/' + img_name,
				new google.maps.Size(25, 38), 
				new google.maps.Point(2, 0),
				new google.maps.Point(11, 36.5)),	//image	offset
//		anchorPoint : new google.maps.Point(-100, 200),
		title : img_title
	});
	var infowindow = new google.maps.InfoWindow({
	    content: content
	});
	google.maps.event.addListener(eventArr[id], 'click', function() {
		infowindow.open(map, eventArr[id]);
	});
//	google.maps.event.addListener(map, 'zoom_changed', function() {
//		if(map.getZoom() > 10) {
//			eventArr[id].setMap(map);
//		} else if(map.getZoom() <= 10) {
//			eventArr[id].setMap(null);
//		}
//	});
	if(flag <= 1) {
		markLayer[flag].push(eventArr[id]);
	} else {
		markLayer[flag - 9].push(eventArr[id]);
	}
	eventArr[id].setMap(map);
}

/**
 * Remove markers
 */
function delMarkers(markersArray) {
	if (markersArray) {
		for (i in markersArray) {
			markersArray[i].setMap(null);
		}
		markersArray.length = 0;
	}
}

function cleanAllMarkers() {
	for ( var int = 2; int < 11; int++) {	//remove all old event markers
		delMarkers(markLayer[int]);
	}
}

/**
 * Initial the map
 */
var trafficLayer, trafficOn = 0;
function initMap() {
	var options = {
		center: new google.maps.LatLng(1.348128,103.842033),
		zoom: 12,
		panControl: false,
		zoomControl: true,
		rotateControl: false,
		overviewMapControl: true,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("basicMap"), options);
//	trafficLayer = new google.maps.TrafficLayer();
//	trafficLayer.setMap(map);
    markLayer[0] = [];	//Starting location layer
    markLayer[1] = [];	//Ending location layer
    markLayer[2] = [];	//Sudden acceleration layer
    markLayer[3] = [];	//Sudden braking layer
    markLayer[4] = [];	//Sudden left layer
    markLayer[5] = [];	//Sudden right layer
    markLayer[6] = [];	//Speeding layer
    markLayer[7] = [];	//Neutral slide layer
    markLayer[8] = [];	//Idle layer
    markLayer[9] = [];	//Idle air conditioning layer
    markLayer[10] = [];	//Engine overspeed layer
    markLayer[11] = [];	//POI layer
    getPOI();
    map.controls[google.maps.ControlPosition.BOTTOM_LEFT].push(document.getElementById('operDiv'));	//add the slider on map
    var trafficControlDiv = document.createElement('div');
    var trafficControl = new TrafficControl(trafficControlDiv, map);
    trafficControlDiv.index = 1;
    map.controls[google.maps.ControlPosition.TOP_RIGHT].push(trafficControlDiv);
}

function TrafficControl(controlDiv, map) {
	controlDiv.style.padding = '5px';
	var controlUI = document.createElement('div');
	controlUI.style.backgroundColor = 'white';
	controlUI.style.borderStyle = 'solid';
	controlUI.style.borderWidth = '1px';
	controlUI.style.cursor = 'pointer';
	controlUI.style.textAlign = 'center';
	controlUI.title = 'Click to show the traffic';
	controlDiv.appendChild(controlUI);

	var controlText = document.createElement('div');
	controlText.style.fontFamily = 'Arial,sans-serif';
	controlText.style.fontSize = '13px';
	controlText.style.paddingLeft = '4px';
	controlText.style.paddingRight = '4px';
	controlText.style.width = '50px';
	controlText.style.height = '17px';
	controlText.innerHTML = 'Traffic';
	controlUI.appendChild(controlText);

	// Setup the click event listeners: simply set the map to Chicago.
	google.maps.event.addDomListener(controlUI, 'click', function() {
		if(trafficOn == 1) {
			trafficLayer.setMap(null);
			trafficOn = 0;
			controlText.innerHTML = 'Traffic';
			controlUI.title = 'Click to show the traffic';
		} else {
			trafficLayer = new google.maps.TrafficLayer();
			trafficLayer.setMap(map);
			trafficOn = 1;
			controlText.innerHTML = '<strong>Traffic</strong>';
			controlUI.title = 'Click to hide the traffic';
		}
	});
}

/**
 * Get POI data, and draw the markers
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
	var poiMarker = new google.maps.Marker({
	      position: new google.maps.LatLng(y, x),
	      map: map,
	      icon: basePath + 'images/'+ico+'_.gif'
	  });
	var infowindow = new google.maps.InfoWindow({
	    content: content
	});
	google.maps.event.addListener(poiMarker, 'click', function() {
		infowindow.open(map, poiMarker);
	});
	google.maps.event.addListener(map, 'zoom_changed', function() {
		if(map.getZoom() > 10) {
			poiMarker.setMap(map);
		} else if(map.getZoom() <= 10) {
			poiMarker.setMap(null);
		}
	});
}

function checkSearch() {
	sel_startDate = $.trim($('#startDate').val());
	sel_startTime = $.trim($('#startTime').val());
	sel_endTime = $.trim($('#endTime').val());
	sel_lisencePlate = $.trim(select_vehicle_name.getSelectedText());
	if(sel_lisencePlate == null || sel_lisencePlate == '' || sel_lisencePlate == '-1') {
		alert("Please choose a vehicle!");
		return;
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
	    	'vo.vehID' : select_vehicle_name.getSelectedValue(),
	    	'vo.plateNumber' : sel_lisencePlate,
	    	'vo.beginDate' : sel_startDate,
	    	'vo.beginTime' : sel_startTime,
	    	'vo.endTime' : sel_endTime
	    },
	    url : basePath + "monitor/google-display!getGeo",
	    success : function(d) {
			if("empty" != d.result){
				vehidd=select_vehicle_name.getSelectedValue();
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
	var vlc = document.getElementById("vlc")
	if(vlc) {
		vlc.playlist.stop();
	}
}