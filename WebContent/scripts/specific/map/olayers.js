var timer;	//定时器
var nop = 300;
var markers;
var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
var toProjection   = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
var map, vectorLayer;
var totalCount = "<%=list.size()%>";	//总数据个数
var perData = Math.ceil(totalCount/nop);	//分成100份
var lineFeature = [];
var markLayer =  [];

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
    //map.addControl(new OpenLayers.Control.LayerSwitcher());	//图层选择
    var mapnik = new OpenLayers.Layer.OSM();	//默认图层
   // markers = new OpenLayers.Layer.Markers( "Markers" );	//标记图层
    
    markLayer[0] = new OpenLayers.Layer.Markers( "Bus" );	//标记图层
    markLayer[1] = new OpenLayers.Layer.Markers( "Poi" );	//poi
    markLayer[2] = new OpenLayers.Layer.Markers("Idle");	//Idle layer
    markLayer[3] = new OpenLayers.Layer.Markers("Speeding");	//Speeding layer
    markLayer[4] = new OpenLayers.Layer.Markers("Sudden acceleration");	//Sudden acceleration layer
    markLayer[5] = new OpenLayers.Layer.Markers("Sudden braking");	//Sudden braking layer
    markLayer[6] = new OpenLayers.Layer.Markers("Sudden left");	//Sudden left layer
    markLayer[7] = new OpenLayers.Layer.Markers("Sudden right");	//Sudden right layer
    markLayer[8] = new OpenLayers.Layer.Markers("Neutral slide");	//Neutral slide layer
    markLayer[9] = new OpenLayers.Layer.Markers("Idle air conditioning");	//Idle air conditioning layer
    markLayer[10] = new OpenLayers.Layer.Markers("Engine overspeed");	//Engine overspeed layer
    markLayer[11] = new OpenLayers.Layer.Markers("Into the Geo Fencing");
    markLayer[12] = new OpenLayers.Layer.Markers("Exit the Geo Fencing");
    vectorLayer = new OpenLayers.Layer.Vector("Routes");	//容器图层
    map.addLayer(vectorLayer);
    map.addLayer(mapnik);	//增加默认图层到地图
    map.addLayers(markLayer);
     var position = new OpenLayers.LonLat(103.842033,1.291128).transform(fromProjection, toProjection);// 新加坡
    var zoom = 14;
    map.setCenter(position, zoom);	//中心点
    var controls = {	//画图器
 	       'point': new OpenLayers.Control.DrawFeature(vectorLayer,
 	                    OpenLayers.Handler.Point),
 	        'line': new OpenLayers.Control.DrawFeature(vectorLayer,
 	                    OpenLayers.Handler.Path),
 	        'polygon': new OpenLayers.Control.DrawFeature(vectorLayer,
 	                    OpenLayers.Handler.Polygon),
 	        'drag': new OpenLayers.Control.DragFeature(vectorLayer)
 	    };

 	    for(var key in controls) {	//添加画图器到地图
 	        map.addControl(controls[key]);
 	    }
}

function cleanAllMarkers(){
	for(var i=2;i<13;i++){
		map.removeLayer(markLayer[i]);
	}
	markLayer[2] = new OpenLayers.Layer.Markers("Idle");	//Idle layer
    markLayer[3] = new OpenLayers.Layer.Markers("Speeding");	//Speeding layer
    markLayer[4] = new OpenLayers.Layer.Markers("Sudden acceleration");	//Sudden acceleration layer
    markLayer[5] = new OpenLayers.Layer.Markers("Sudden braking");	//Sudden braking layer
    markLayer[6] = new OpenLayers.Layer.Markers("Sudden left");	//Sudden left layer
    markLayer[7] = new OpenLayers.Layer.Markers("Sudden right");	//Sudden right layer
    markLayer[8] = new OpenLayers.Layer.Markers("Neutral slide");	//Neutral slide layer
    markLayer[9] = new OpenLayers.Layer.Markers("Idle air conditioning");	//Idle air conditioning layer
    markLayer[10] = new OpenLayers.Layer.Markers("Engine overspeed");	//Engine overspeed layer
    markLayer[11] = new OpenLayers.Layer.Markers("Into the Geo Fencing");
    markLayer[12] = new OpenLayers.Layer.Markers("Exit the Geo Fencing");
    for(var i=2;i<13;i++){
    	 map.addLayer(markLayer[i]);
	}
    return true;
}