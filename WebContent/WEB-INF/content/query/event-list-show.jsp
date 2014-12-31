<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>

<html>
<head>
<link rel="stylesheet" href="<%=basePath%>scripts/common/jquery-ui-1.8.18.custom/development-bundle/themes/base/jquery.ui.all.css">
<script charset="UTF-8"  src="<%=basePath%>scripts/common/jquery-ui-1.8.18.custom/js/jquery-1.7.1.min.js"></script>
<script charset="UTF-8"  src="<%=basePath%>scripts/common/jquery-ui-1.8.18.custom/development-bundle/ui/jquery.ui.core.js"></script>
<script charset="UTF-8"  src="<%=basePath%>scripts/common/jquery-ui-1.8.18.custom/development-bundle/ui/jquery.ui.widget.js"></script>
<script charset="UTF-8"  src="<%=basePath%>scripts/common/jquery-ui-1.8.18.custom/development-bundle/ui/jquery.ui.mouse.js"></script>
<script charset="UTF-8"  src="<%=basePath%>scripts/common/jquery-ui-1.8.18.custom/development-bundle/ui/jquery.ui.slider.js"></script>
<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/OpenLayers-2.12/OpenLayers.js"></script>
<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/specific/map/olayers.js"></script>
<link  href="<%=basePath%>css/main2.css" rel="stylesheet" type="text/css">

<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<style type="text/css">
html { height: 100% }
body { height: 100%; margin: 0; padding: 0 }
#map_canvas { height: 100% }
		      
.buslis {
    border-bottom: 1px solid #DDDDDD;
    border-collapse: collapse;
    border-left: 1px solid #DDDDDD;
    width: 97.5%;
}
.buslis thead {
    background: none repeat scroll 0 0 #65A7D3;
    font-weight: bolder;
    height: 25px;
    text-align: center;
}
.buslis td {
    border-right: 1px solid #DDDDDD;
    border-top: 1px solid #DDDDDD;
    empty-cells: show;
    height: 20px;
    text-align: center;
    white-space: nowrap;
}
</style>
<script type="text/javascript">
var jingdu='${lng}';
var weidu='${lat}';
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
   // map.addControl(new OpenLayers.Control.MousePosition());	//鼠标坐标
    map.addControl(new OpenLayers.Control.LayerSwitcher());	//图层选择
    var mapnik = new OpenLayers.Layer.OSM();	//默认图层
    vectorLayer = new OpenLayers.Layer.Vector("轨迹容器");	//容器图层
    markers = new OpenLayers.Layer.Markers( "Markers" );	//标记图层
    map.addLayer(mapnik);	//增加默认图层到地图
    map.addLayer(vectorLayer);	//增加容器图层到地图
    map.addLayer(markers);	//增加标记图层到地图
    
    var position = new OpenLayers.LonLat(jingdu, weidu).transform(fromProjection, toProjection);	//根据坐标取点，并计算投影偏移量
    markers.addMarker(new OpenLayers.Marker(position));	//增加标记到标记图层
    var zoom = 11;
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
    //controls['polygon'].activate();	//使生效
}
$(function(){
	$("#basicMap").height($(window).height()-$(".welcome").height()-2);
	initMap();
	$("#OpenLayers\\.Control\\.Attribution_4").css("display","none");
	$("#OpenLayers_Control_MaximizeDiv").css("display","none");
	
})

</script>	    



</head>
	<body >
	<custom:navigation father="Data Query" model="Event List Show"/>
	<div id="basicMap" style="width:100%; ">
		
	</div>
	
	</body>
</html>