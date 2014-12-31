var timer;	//定时器
var nop = 300;
var markers;
//var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
//var toProjection   = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
var map, vectorLayer;
var totalCount = "<%=list.size()%>";	//总数据个数
var perData = Math.ceil(totalCount/nop);	//分成100份
var lineFeature = [];
var markLayer =  [];
var icon;
var polyline, markerArr = [];
//滑动条前进同时画出轨迹
function moveSlider() {
	var newValue = $("#slider").slider("option", "value") + 1;
	if(newValue > nop) {
		window.clearInterval(timer); 
	}
	$("#slider").slider("option", "value", newValue);
	addRoute(newValue-1, newValue);
}

//开始动画
function doRun() {
	timer = window.setInterval(moveSlider, 500);
}

//停止动画
function doStop() {
	window.clearInterval(timer); 
}

var style_green = {
		strokeColor: "#339933",
	    strokeOpacity: 1,
	    strokeWidth: 5,
	    pointRadius: 6,
	    pointerEvents: "visiblePainted"
	};

/**
 * 地图初始化
 */
var trafficLayer, trafficOn = 0;
function initMap() {
//	var options = {	//设置选项
//		controls: [
//			new OpenLayers.Control.Navigation(),	//箭头
//			new OpenLayers.Control.PanZoomBar(),	//缩放条
 //       	new OpenLayers.Control.Attribution()
//      	],
//		zoom: 10
//    };
	
	var options = {
			center: new google.maps.LatLng(1.348128,103.842033),
			zoom: 12,
			panControl: false,
			mapTypeControl:false,
			zoomControl: true,
			rotateControl: false,
			overviewMapControl: true,
			mapTypeId: google.maps.MapTypeId.ROADMAP
		};
	map = new google.maps.Map(document.getElementById("basicMap2"), options);
//	var trafficLayer = new google.maps.TrafficLayer();
//	trafficLayer.setMap(map);
	  var trafficControlDiv = document.createElement('div');
	    var trafficControl = new TrafficControl(trafficControlDiv, map);
	    trafficControlDiv.index = 1;
	    map.controls[google.maps.ControlPosition.TOP_LEFT].push(trafficControlDiv);
	    
  markLayer[0] = [];	//标记图层
  markLayer[1] = [];	//poi
  markLayer[2] = [];	//Idle layer
  markLayer[3] = [];	//Speeding layer
  markLayer[4] = [];	//Sudden acceleration layer
  markLayer[5] = [];	//Sudden braking layer
  markLayer[6] = [];	//Sudden left layer
  markLayer[7] = [];	//Sudden right layer
  markLayer[8] = [];	//Neutral slide layer
  markLayer[9] = [];	//Idle air conditioning layer
  markLayer[10] = [];	//Engine overspeed layer
  markLayer[11] = [];
  markLayer[12] = [];
  markLayer[13] = [];
  getPOI();
 
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

function addPoiMarkers( x,y, ico, content) {
	var poiMarker = new google.maps.Marker({
	      position: new google.maps.LatLng(y, x),
	      map: map,
	      icon: basePath + 'images/'+ico+'_.gif'
	  });
	console.log(ico);
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

function cleanAllMarkers(){
	for(var i=2;i<14;i++){
		delMarkers(markLayer[i]);
	}
	    return true;
}



/**
 * add event marker
 * 
 */
function addMarkers(id, point, flag, content,title) {
	var eventArr = new Array();
	var img_name = null;
	var img_title = null;
	if(flag == 0) {
		if(id==null){
			img_name = '118.gif';
		}else{
			img_name = id+'.png';//	方位角
		}
		img_title = 'Bus';
	}else if(flag == 3) {
		img_name = 'm-blue.png';	//Starting location
		img_title = 'Starting location';
	} else if(flag == 4) {
		img_name = 'm-purple.png';	//Ending location
		img_title = 'Ending location';
	} else if(flag == 5) {
		img_name = 'm-teal.png';	//Sudden acceleration
		img_title = 'Sudden acceleration';
	} else if(flag == 6) {
		img_name = 'm-magenta.png';	//Sudden braking
		img_title = 'Sudden braking';
	} else if(flag == 7) {
		img_name = 'm-yellow.png';	//Sudden left
		img_title = 'Sudden left';
	} else if(flag == 8) {
		img_name = 'm-orange.png';	//Sudden right
		img_title = 'Sudden right';
	} else if(flag == 9) {
		img_name = 'm-gray.png';	//Speeding
		img_title = 'Speeding';
	} else if(flag == 10) {
		img_name = 'm-lime.png';	//Neutral slide
		img_title = 'Neutral slide';
	} else if(flag == 11) {
		img_name = 'm-white.png';	//Idle
		img_title = 'Idle';
	} else if(flag == 12) {
		img_name = 'm-black.png';	//Idle air conditioning
		img_title = 'Idle air conditioning';
	} else if(flag == 13) {
		img_name = 'm-black.png';	//Engine overspeed
		img_title = 'Engine overspeed';
	}
	if(flag==0){
		if(id==null){
			eventArr[id] = new google.maps.Marker({
				position : point,
				map : map,
				icon : new google.maps.MarkerImage(basePath
						+ 'images/fangweijiao/' + img_name,
						new google.maps.Size(37, 37),
						new google.maps.Point(0, 0),
						new google.maps.Point(18.5, 18.5)),	//image	offset
						title : title
			});
		}else{
			eventArr[id] = new google.maps.Marker({
				position : point,
				map : map,
				icon : new google.maps.MarkerImage(basePath
						+ 'images/fangweijiao/' + img_name,
						new google.maps.Size(48, 56),
						new google.maps.Point(0, 0),
						new google.maps.Point(15, 18),new google.maps.Size(30, 36)),	//image	offset
						title : title
			});
		}
		
		
	}else{
		eventArr[id] = new google.maps.Marker({
			position : point,
			map : map,
			icon : new google.maps.MarkerImage(basePath
					+ 'scripts/common/OpenLayers-2.12/img/' + img_name,
					new google.maps.Size(25, 38), 
					new google.maps.Point(2, 0),
					new google.maps.Point(11, 36.5)),	//image	offset
			title : img_title
		});
	}
	
	
	
	var infowindow = new google.maps.InfoWindow({
	    content: content+"&nbsp;&nbsp;"
	});
	google.maps.event.addListener(eventArr[id], 'click', function() {
		infowindow.open(map, eventArr[id]);
	});
	markLayer[flag].push(eventArr[id]);
	eventArr[id].setMap(map);
	return eventArr[id];
}
