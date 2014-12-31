var timer;	//定时器
var interval = 15; //间隔数
var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
var OSM_toProjection   = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
var BING_toProjection   = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
var Google_toProjection   = new OpenLayers.Projection("EPSG:3857"); // to Spherical Mercator Projection
var map, vectorLayer, marker, markers, popup, draw, polygonControl, points, point;
var lineFeature = [], markerArr = [];

//var google_layers = [
//	 new OpenLayers.Layer.Google(
//	     "Google Physical",
//	     {type: google.maps.MapTypeId.TERRAIN}
//	 ),
//	 new OpenLayers.Layer.Google(
//	     "Google Streets", // the default
//	     {numZoomLevels: 20}
//	 ),
//	 new OpenLayers.Layer.Google(
//	     "Google Hybrid",
//	     {type: google.maps.MapTypeId.HYBRID, numZoomLevels: 20}
//	 ),
//	 new OpenLayers.Layer.Google(
//	     "Google Satellite",
//	     {type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 22}
//     )
//];

var apiKey = 'Apvm1LsjBXQ8LjXgKrgVyQXd5SI_j6MYyJ7_EmfhPaI7ROe90G_W8tCx7Ddoj75m';	//bing map api key
var bing_layers = [
	new OpenLayers.Layer.Bing({
		name: "Bing Road",
		key: apiKey,
		type: "Road"
	}),
	new OpenLayers.Layer.Bing({
	    name: "Bing Hybrid",
	    key: apiKey,
	    type: "AerialWithLabels"
	}),
	new OpenLayers.Layer.Bing({
	    name: "Bing Aerial",
	    key: apiKey,
	    type: "Aerial"
	})
];

$(function() {
	$( "#slider" ).slider({	//进度条
		min: 0,	//最大值
		max: nop,	//最小值
		value:0,	//初始值
		step: 1,
		animate:true,	//开动画
		slide: function(event, ui) {	//拖动触发
			vectorLayer.removeFeatures(lineFeature);	//删除轨迹对象
			addRouteAll($("#slider").slider("option", "value"));	//重新画出轨迹
		}
	});
});



/**
 * 计算秒数
 * @param	beginStr	开始时间串
 * @param	endStr		终止时间串
 */
function getSeconds(beginStr, endStr) {
	var beginDate = new Date(2000, 1, 1, beginStr[0], beginStr[1], beginStr[2]).getTime();
	var endStr = new Date(2000, 1, 1, endStr[0], endStr[1], endStr[2]).getTime();
	return (endStr - beginDate)/1000;
}

//滑动条前进同时画出轨迹
function moveSlider() {
	var newValue = $("#slider").slider("option", "value") + 1;
	if(newValue > nop + 1) {
		window.clearInterval(timer);
		return;
	}
	$("#slider").slider("option", "value", newValue);
	vectorLayer.removeFeatures(lineFeature);	//删除轨迹对象
	addRouteAll(newValue);	//重新画出轨迹
}

//开始动画
function doRun() {
	timer = window.setInterval(moveSlider, 500);
}

//停止动画
function doStop() {
	window.clearInterval(timer); 
}

//删除轨迹
function clearRoute() {
	for (var key in lineFeature) {
		vectorLayer.removeFeatures(lineFeature[key]);	//删除轨迹对象
	}
}

var style_green = {	//轨迹样式
	strokeColor: "#339933",
    strokeOpacity: 1,
    strokeWidth: 3,
    pointRadius: 6,
    pointerEvents: "visiblePainted"
};

/**
*	增量
*/
function addRoute(begin, end) {
	points = [];
	var max = (end*perData >= dataList.length ? dataList.length : end*perData);
	var i = begin*perData;
	var beginPoint = new OpenLayers.Geometry.Point(parseFloat(dataList[i][0]).toFixed(6), parseFloat(dataList[i][1]).toFixed(6)).transform(fromProjection, toProjection);	//根据坐标取点，并计算投影偏移量
	var endPoint = new OpenLayers.Geometry.Point(parseFloat(dataList[max-1][0]).toFixed(6), parseFloat(dataList[max-1][1]).toFixed(6)).transform(fromProjection, toProjection);	//根据坐标取点，并计算投影偏移量
	for(; i <= max; i++) {
		point = new OpenLayers.Geometry.Point(parseFloat(dataList[i][0]).toFixed(6), parseFloat(dataList[i][1]).toFixed(6)).transform(fromProjection, toProjection);	//根据坐标取点，并计算投影偏移量
		points.push(point);
		$('#pos').text('份数：' + nop + '; index:' + i + '; 每份个数: ' + perData);
	}
	lineFeature[end] = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(points), null, style_green);	//轨迹对象
	$('#dataP').text('原值：起点：index:' + begin*perData + '; ' + parseFloat(dataList[begin*perData][0]).toFixed(6) + ', ' + parseFloat(dataList[begin*perData][1]).toFixed(6) + ';终点：' + (i-1) + '; ' + parseFloat(dataList[i-1][0]).toFixed(6) + ', ' + parseFloat(dataList[i-1][1]).toFixed(6));
	$('#point').text('点值：起点：' + beginPoint + '; 终点:' + endPoint);
	vectorLayer.addFeatures(lineFeature[end]);		//将轨迹对象加入容器图层
	map.setCenter(endPoint);	//中心点
}

/**
  * 全增全删
  */
function addRouteAll(pointIndex) {
	points = [];
	var i = 0;
	var max = (pointIndex*perData >= dataList.length ? dataList.length : pointIndex*perData);
	var endPoint = new OpenLayers.LonLat(parseFloat(dataList[max-1][0]).toFixed(6), parseFloat(dataList[max-1][1]).toFixed(6)).transform(fromProjection, BING_toProjection);	//根据坐标取点，并计算投影偏移量
	for(; i < max; i++) {
		point = new OpenLayers.Geometry.Point(parseFloat(dataList[i][0]).toFixed(6), parseFloat(dataList[i][1]).toFixed(6)).transform(fromProjection, BING_toProjection);	//根据坐标取点，并计算投影偏移量
		points.push(point);
	}
	lineFeature[0] = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(points), null, style_green);	//轨迹对象
	$('#pointTime').text('时间：' + dataList[max-1][2] + '; index: ' + (max-1) + '; count:' + $("#slider").slider("option", "value") + '/' + nop + '; LonLat: ' + parseFloat(dataList[max-1][0]).toFixed(6) + ',' + parseFloat(dataList[max-1][1]).toFixed(6));
	vectorLayer.addFeatures(lineFeature[0]);		//将轨迹对象加入容器图层
	map.setCenter(endPoint);	//中心点跟随轨迹
}

/**
 * 增加标记点
 */
function addMarkers(id, point, content) {
	var size = new OpenLayers.Size(20, 25);
	var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
	var icon = new OpenLayers.Icon('http://www.openlayers.org/dev/img/marker.png', size, offset);
	//Note that if you pass an icon into the Marker constructor, it will take that icon and use it.
	//This means that you should not share icons between markers -- 
	//you use them once, but you should clone() for any additional markers using that same icon.
	marker = new OpenLayers.Marker(point, icon);
	markers.addMarker(marker);	//增加标记到标记图层
	marker.events.register('click', marker, function(evt) {	//单击事件
		//增加弹出框
		if(null != content && "" != content) {
			popup = new OpenLayers.Popup.FramedCloud(id, point, null, content, null, true);
			map.addPopup(popup);
		}
	});
}

/**
 * 删除标记点
 */
function delMarkers(id) {
	markers.removeMarker(marker);	//增加起点标记到标记图层
}

function initMap() {
	var options = {	//设置选项
		controls: [
			new OpenLayers.Control.Navigation(),	//箭头
			new OpenLayers.Control.PanZoomBar(),	//缩放条
        	new OpenLayers.Control.Attribution()
      	],
		zoom: 10
    };
	map = new OpenLayers.Map("basicMap", options);	//创建地图
    map.addControl(new OpenLayers.Control.MousePosition());	//鼠标坐标
    map.addControl(new OpenLayers.Control.LayerSwitcher());	//图层选择
    var mapnik = new OpenLayers.Layer.OSM();	//默认图层
    map.addLayer(mapnik);	//增加默认图层到地图
//    map.addLayers(google_layers);	//google地图
    map.addLayers(bing_layers);	//bing地图
    var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;	//画图渲染器
    renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;
    vectorLayer = new OpenLayers.Layer.Vector("轨迹容器", {renderers: renderer});	//多边形容器图层
    markers = new OpenLayers.Layer.Markers( "Markers" );	//标记图层
    map.addLayer(vectorLayer);	//增加容器图层到地图
    map.addLayer(markers);	//增加标记图层到地图
    var startPoint = new OpenLayers.LonLat(parseFloat(dataList[0][0]).toFixed(6), parseFloat(dataList[0][1]).toFixed(6)).transform(fromProjection, BING_toProjection);	//根据坐标取点，并计算投影偏移量.transform(fromProjection, BING_toProjection)
    var endPoint = new OpenLayers.LonLat(parseFloat(dataList[totalCount-1][0]).toFixed(6), parseFloat(dataList[totalCount-1][1]).toFixed(6)).transform(fromProjection, BING_toProjection);	//根据坐标取点，并计算投影偏移量.transform(fromProjection, toProjection)
    //增加起点终点并加入单击弹窗事件
    addMarkers('startP', startPoint, '起点:lon:' + parseFloat(dataList[0][0]).toFixed(6) + ', lat:' + parseFloat(dataList[0][1]).toFixed(6));	
    addMarkers('endP', endPoint, '终点:lon:' + parseFloat(dataList[totalCount-1][0]).toFixed(6) + ', lat:' + parseFloat(dataList[totalCount-1][1]).toFixed(6));
    map.setCenter(startPoint);	//中心点
    //多边形
    draw = new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Polygon, {
    	handlerOptions: {
    		holeModifier: "ctrlKey"
    	},
    	featureAdded: function() {
    		//取多边形定点集合
    		var pointsArr = arguments[0].geometry.components[0].components;
    		console.log(pointsArr); //看看参数是啥
    		for(var i = 0; i < pointsArr.length; i++) {
    			$('#pointTime').append(i + ': ' + pointsArr[i].x + ',' + pointsArr[i].y + '; ');
    		}
    	}
    });
    map.addControl(draw);
    
    //指定图形
    polygonControl = new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.RegularPolygon, {handlerOptions: {sides: 4}});
    document.getElementById('noneToggle').checked = true;
    document.getElementById('irregularToggle').checked = false;
    map.addControl(polygonControl);
    draw.activate();
}

function setOptions(options) {
	polygonControl.handler.setOptions(options);
}

OpenLayers.Event.observe(document, "keydown", function(evt) {
    var handled = false;
    switch (evt.keyCode) {
        case 90: // z
            if (evt.metaKey || evt.ctrlKey) {
                draw.undo();
                handled = true;
            }
            break;
        case 89: // y
            if (evt.metaKey || evt.ctrlKey) {
                draw.redo();
                handled = true;
            }
            break;
        case 27: // esc
            draw.cancel();
            polygonControl.cancel();
            handled = true;
            break;
    }
    if (handled) {
        OpenLayers.Event.stop(evt);
    }
});