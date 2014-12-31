var timer;	//定时器
var interval = 15; //间隔数
var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
var OSM_toProjection   = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
var BING_toProjection   = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
var Google_toProjection   = new OpenLayers.Projection("EPSG:3857"); // to Spherical Mercator Projection
var map, vectorLayer, marker, markers, popup, draw, polygonControl, points, point;
var lineFeature = [], markerArr = [];
var listlength=null;

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

function  remov(){
     var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;	
     renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;
	 map.removeLayer(vectorLayer);
	 vectorLayer = new OpenLayers.Layer.Vector("base layer", {renderers: renderer});	
	 map.addLayer(vectorLayer);
}

function addRouteAll() {
	points = [];
	var i = 0;
	var dataList = $("#listobj").val();
	for(; i < dataList.length; i++) {
		alert(dataList[i][0]);
		point = new OpenLayers.Geometry.Point(parseFloat(dataList[i][0]).toFixed(6), parseFloat(dataList[i][1]).toFixed(6)).transform(fromProjection, BING_toProjection);	//根据坐标取点，并计算投影偏移量
		points.push(point);
	}
	lineFeature[0] = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(points), null, style_green);	//轨迹对象
	vectorLayer.addFeatures(lineFeature[0]);		//将轨迹对象加入容器图层
	map.setCenter(endPoint);	//中心点跟随轨迹
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
    var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;	
    renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;
    vectorLayer = new OpenLayers.Layer.Vector("base layer", {renderers: renderer});	
    map.addLayer(vectorLayer);
    //var startPoint = new OpenLayers.LonLat(116.8, 39.6).transform(fromProjection, BING_toProjection);	
    var startPoint = new OpenLayers.LonLat(103.842033,1.291128).transform(fromProjection, BING_toProjection);
    map.setCenter(startPoint);	//涓績鐐�
   //多边形
    draw = new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Polygon, {
    	handlerOptions: {
    		holeModifier: "ctrlKey"
    	},
    	featureAdded: function() {
    		//取多边形定点集合
    		var pointsArr = arguments[0].geometry.components[0].components;
 //   		console.log(pointsArr); //看看参数是啥
    		var str = '';
    		listlength = pointsArr;
    		for(var i = 0; i < pointsArr.length; i++) {
    			point = new OpenLayers.Geometry.Point(parseFloat(pointsArr[i].x).toFixed(6), parseFloat(pointsArr[i].y).toFixed(6)).transform(BING_toProjection, fromProjection);	//根据坐标取点，并计算投影偏移量
    			str = str+(point.x+"-"+point.y) + ';'
    		}
    		$("#fenname").val(str);
    	   draw.deactivate();
    	}
    });
    map.addControl(draw);
   
}

var style_green = {	//轨迹样式
		strokeColor: "#ffa500",
	    strokeOpacity: 0.4,
	    fillColor: "#ffa500",
	    fillOpacity:0.2,
	    strokeWidth: 3,
	    pointRadius: 4.5,
	    pointerEvents: "visiblePainted"
	};

function initMap1() {
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
    var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;	
    renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;
    vectorLayer = new OpenLayers.Layer.Vector("base layer", {renderers: renderer});	
    map.addLayer(vectorLayer);
    var startPoint = new OpenLayers.LonLat(103.842033,1.291128).transform(fromProjection, BING_toProjection);	
    map.setCenter(startPoint);	//涓績鐐�
   //多边形
    draw = new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Polygon, {
    	handlerOptions: {
    		holeModifier: "ctrlKey"
    	},
    	featureAdded: function() {
    		//取多边形定点集合
    		var pointsArr = arguments[0].geometry.components[0].components;
//    		console.log(pointsArr); //看看参数是啥
    		var str = '';
    		$("#fenname").val('');
    		listlength = pointsArr;
    		for(var i = 0; i < pointsArr.length; i++) {
    			point = new OpenLayers.Geometry.Point(parseFloat(pointsArr[i].x).toFixed(6), parseFloat(pointsArr[i].y).toFixed(6)).transform(BING_toProjection, fromProjection);	//根据坐标取点，并计算投影偏移量
    			str = str+(point.x+"-"+point.y) + ';'
    		}
    		$("#fenname").val(str);
    	   draw.deactivate();
    	}
    });
    map.addControl(draw);
    draw.activate();
   
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
            handled = true;
            break;
    }
    if (handled) {
        OpenLayers.Event.stop(evt);
    }
});


function draw1(){
	    remov();
	    draw = new OpenLayers.Control.DrawFeature(vectorLayer, OpenLayers.Handler.Polygon, {
	    	handlerOptions: {
	    		holeModifier: "ctrlKey"
	    	},
	    	featureAdded: function() {
	    		  
	    		//取多边形定点集合
	    		var pointsArr = arguments[0].geometry.components[0].components;
//	    		console.log(pointsArr); //看看参数是啥
	    		var str = '';
	    		$("#fenname").val('');
	    		listlength = pointsArr;
	    		for(var i = 0; i < pointsArr.length; i++) {
//	    			point = new OpenLayers.Geometry.Point(parseFloat(pointsArr[i].x).toFixed(6), parseFloat(pointsArr[i].y).toFixed(6)).transform(BING_toProjection, fromProjection);	//根据坐标取点，并计算投影偏移量
//	    			str = str+(point.lon+"-"+point.lat) + ';'
	    			point = new OpenLayers.Geometry.Point(parseFloat(pointsArr[i].x).toFixed(6), parseFloat(pointsArr[i].y).toFixed(6)).transform(BING_toProjection, fromProjection);	//根据坐标取点，并计算投影偏移量
	    			str = str+(point.x+"-"+point.y) + ';'
	    			
	    		}
	    		$("#fenname").val(str);
	    	   draw.deactivate();
	    	}
	    });
	    map.addControl(draw);
	    draw.activate();
}



function increate(){
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
	    
	    var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;	
	    renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;
	    vectorLayer = new OpenLayers.Layer.Vector("base layer", {renderers: renderer});	
//	    vectorLayer = new OpenLayers.Layer.Vector("base layer", {style: layer_style}); 

	    var startPoint = new OpenLayers.LonLat(103.842033,1.291128).transform(fromProjection, BING_toProjection);	
	    map.setCenter(startPoint);	//涓績鐐�
	    
	    var dataList=null;
	    $.ajax({
			url :  basePath +"task/fencing!edit1",
			type : "POST",
			async : false,
			data : {
				'id' :  $("#geofencingid").val()
			},
			dataType : "json",
			success : function(data) {
				var str="";
				 
				if(0 != data.length){
					 
					dataList = data;
					for(var i = 0; i < data.length; i++) {
						 str =str +data[i][0]+"-"+data[i][1]+";"
				   }
				}
			  $("#fenname").val(str);
			},
			error : function(json) {
				window.history.back();
			}
		});
		points = [];
		var i = 0;
		
		if(null != dataList){
			for(; i < dataList.length; i++) {
				point = new OpenLayers.Geometry.Point(parseFloat(dataList[i][0]).toFixed(6), parseFloat(dataList[i][1]).toFixed(6)).transform(fromProjection, BING_toProjection);	//根据坐标取点，并计算投影偏移量
				points.push(point);
				 var startPointa = new OpenLayers.LonLat(parseFloat(dataList[i][0]).toFixed(6), parseFloat(dataList[i][1]).toFixed(6)).transform(fromProjection, BING_toProjection);	
				  map.setCenter(startPointa);
			}
		}
		
	
		lineFeature[0] = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LinearRing(points), null, style_green);	//轨迹对象
		vectorLayer.addFeatures(lineFeature[0]);	
		map.addLayer(vectorLayer);
		

}


function increate1(skid){
	    $("#basicMap").show();
	    var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;	
	     renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;
		 map.removeLayer(vectorLayer);
		 vectorLayer = new OpenLayers.Layer.Vector("base layer", {renderers: renderer});
	    
	    var dataList=null;
	
	    $.ajax({
			url :  basePath +"task/fencing!edit1",
			type : "POST",
			async : false,
			data : {
				'id' :  skid
			},
			dataType : "json",
			success : function(data) {
				if(0 != data.length){
					dataList = data;
				}
			},
			error : function(json) {
				window.history.back();
			}
		});
		points = [];
		var i = 0;
		if(null != dataList){
			for(; i < dataList.length; i++) {
				point = new OpenLayers.Geometry.Point(parseFloat(dataList[i][0]).toFixed(6), parseFloat(dataList[i][1]).toFixed(6)).transform(fromProjection, BING_toProjection);	//根据坐标取点，并计算投影偏移量
				points.push(point);
				 var startPointa = new OpenLayers.LonLat(parseFloat(dataList[i][0]).toFixed(6), parseFloat(dataList[i][1]).toFixed(6)).transform(fromProjection, BING_toProjection);	
				  map.setCenter(startPointa);
			}
		}
		
		lineFeature[0] = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LinearRing(points), null, style_green);	//轨迹对象
		vectorLayer.addFeatures(lineFeature[0]);	
		map.addLayer(vectorLayer);

}