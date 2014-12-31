var timer;	//定时器
var interval = 15; //间隔数
var map, vectorLayer, marker, markers, popup, draw, polygonControl, points, point;
var lineFeature = [], markerArr = [];
var listlength=null;


var apiKey = 'Apvm1LsjBXQ8LjXgKrgVyQXd5SI_j6MYyJ7_EmfhPaI7ROe90G_W8tCx7Ddoj75m';	//bing map api key

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
 


var style_green = {	//轨迹样式
		strokeColor: "#ffa500",
	    strokeOpacity: 0.4,
	    fillColor: "#ffa500",
	    fillOpacity:0.2,
	    strokeWidth: 3,
	    pointRadius: 4.5,
	    pointerEvents: "visiblePainted"
	};






function increate(){
	    
	    var dataList=null;
	    $.ajax({
			url :  basePath +"query/fencquery!getFence",
			type : "POST",
			async : false,
			data : {
				'id' :  c.getSelectedValue()
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
		for(; i < dataList.length; i++) {
			point = new OpenLayers.Geometry.Point(parseFloat(dataList[i][0]).toFixed(6), parseFloat(dataList[i][1]).toFixed(6)).transform(fromProjection, BING_toProjection);	//根据坐标取点，并计算投影偏移量
			points.push(point);
		}
	
		lineFeature[0] = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LinearRing(points), null, style_green);	//轨迹对象
		vectorLayer.addFeatures(lineFeature[0]);	
		map.addLayer(vectorLayer);

}


