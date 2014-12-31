<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>

<html>
<head>
<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.core.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.widget.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.mouse.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.slider.js"></script>
<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/OpenLayers-2.12/OpenLayers.js"></script>
<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/specific/map/olayers.js"></script>
<link  href="<%=basePath%>css/main2.css" rel="stylesheet" type="text/css">
<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/specific/query/details.js"></script>	
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<style type="text/css">
			.ui-slider .ui-slider-handle {
				width: 0.8em;
				height: 1.8em;
			}
			.mainInputTreeDiv3 {
				width: 315px;
				height: 160px;
				z-index: 1200;
				position: absolute;
				display: none;
				background-color: white;
				
			}
			.divBackGround {
				filter: alpha(opacity =   70);	/* 修改IE浏览器的透明度 */
				-moz-opacity: 0.7;	/* 修改MOZ浏览器的透明度 */
				opacity: 0.7;	/* 修改MOZ浏览器的透明度 */
				left:0;
				top:0;width:100%; height:100%;
				background: #ccc;
				position: absolute; /* 绝对路径 */
				z-index: 1199;
				text-align: center;
			}
			
			.openVideoDiv {
				width: 640px;
				height: 360px;
				z-index: 1200;
				position: absolute;
				background-color: white;
				padding: 8px;
				border-color:#2383C0; 
				border-style:solid; 
				border-width:2px;
				-moz-border-radius: 10px;      /* Gecko browsers */
				-webkit-border-radius: 10px;   /* Webkit browsers */
				border-radius:10px;            /* W3C syntax */ */
			}
			
			.closeDiv {
				width: 20px;
				height: 20px;
				z-index: 1201;
				position: absolute;
			}
			
			.openTimeDiv {
				width: 300px;
				height: 140px;
				z-index: 1200;
				position: absolute;
				background-color: white;
				padding: 5px;
				border-color:#2383C0; 
				border-style:solid; 
				border-width:2px 2px 2px 2px;
				-moz-border-radius: 10px;      /* Gecko browsers */
				-webkit-border-radius: 10px;   /* Webkit browsers */
				border-radius:10px;            /* W3C syntax */
			}
			
			.openProgressDiv {
				width: 500px;
				height: 50px;
				z-index: 1200;
				position: absolute;
				background-color: white;
				padding: 4px;
				border-color:#2383C0; 
				border-style:solid; 
				border-width:2px 2px 2px 2px;
				-moz-border-radius: 10px;      /* Gecko browsers */
				-webkit-border-radius: 10px;   /* Webkit browsers */
				border-radius:10px;            /* W3C syntax */
			}
			
			.openVideoListDiv {
				width: 640px;
				height: 360px;
				z-index: 1200;
				position: absolute;
				background-color: white;
				padding: 5px;
				border-color:#2383C0; 
				border-style:solid; 
				border-width:2px 2px 2px 2px;
				-moz-border-radius: 10px;      /* Gecko browsers */
				-webkit-border-radius: 10px;   /* Webkit browsers */
				border-radius:10px;            /* W3C syntax */
			}
			
			.ui-progressbar {
				position: relative;
			}
			
		  .progress-label {
		    position: absolute;
		    left: 45%;
		    top: 4px;
		    font-weight: bold;
		    text-shadow: 1px 1px 0 #fff;
		  }
		  
		  .ui-progressbar .ui-progressbar-value { background-image: url(../../../images/pbar-ani.gif); }
		</style>
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
var jingdu='${longitude}';
var weidu='${latitude}';
var beh = '${beh_name}';
var stim = '${timeBegin}';
var etim = '${vehSpeed}';
var vehid='${vehId}'
	var date='${riqi}'
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
    var iocn = new OpenLayers.Marker(position);
    markers.addMarker(iocn);	//增加标记到标记图层
    var content= beh + '</br>Start Time: ' + stim+ '</br>Speed:'+etim+'</br>Longitude:' + jingdu + '</br>Latitude:' + weidu+
    "<img onclick='clickVideo(\"" + date + "\",\"" + vehid + "\");' src='" + basePath + "/images/livetou.png' title='Playback' style='width:20px; height:20px; right:5px; cursor:pointer' alt='Playback'></img>";;
    var popup;
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
    popup = new OpenLayers.Popup.FramedCloud("fir", position, null, content, null, true);
	map.addPopup(popup); 
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
	<custom:navigation father="Driver Report" model="Event List Show" homePath="query/details" />
	<div id="basicMap" style="width:100%; ">
		
	</div>
	<div id="videoDIV" style="position: absolute;right: 0px; bottom: 0px;display: none;">
			<object id='vlc' codebase='<%=basePath%>cabs/axvlc.cab#version=2,0,0,0'
	  							classid='clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921'
	  							style='width: 480px; height: 270px;'>
					<param name='mrl' value='' />
					<param name='volume' value='50' />
					<param name='autoplay' value='false' />
					<param name='controls' value='false' />
			</object>
		</div>
		<div id='closeVideoDiv1' class='closeDiv' style='display: none;right: 475px;bottom: 266px;'>
			<img src='<%=basePath%>images/button_xx.gif' onmousemove="this.src='<%=basePath%>images/button_xx2.gif'" onmouseout="this.src='<%=basePath%>images/button_xx.gif'" style='cursor: pointer;' onclick='closeVideoWin();'/>
		</div>
	</body>
</html>