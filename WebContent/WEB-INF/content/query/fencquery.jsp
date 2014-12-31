<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.core.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.widget.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.mouse.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.slider.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/OpenLayers-2.12/OpenLayers.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/specific/query/fencquery2.js"></script>
	<script type="text/javascript">
	var points2;
	var style_green2 = {
			strokeColor: "#339933",
		    strokeOpacity: 1,
		    strokeWidth: 5,
		    pointRadius: 6,
		    pointerEvents: "visiblePainted"
		};
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
	   //map.addControl(new OpenLayers.Control.MousePosition());	//鼠标坐标
	    map.addControl(new OpenLayers.Control.LayerSwitcher());	//图层选择
	    var mapnik = new OpenLayers.Layer.OSM();	//默认图层
	    vectorLayer = new OpenLayers.Layer.Vector("Routes");	//容器图层
	    markers = new OpenLayers.Layer.Markers( "Markers" );	//标记图层
	    map.addLayer(mapnik);	//增加默认图层到地图
	    map.addLayer(vectorLayer);	//增加容器图层到地图
	    map.addLayer(markers);	//增加标记图层到地图
	    
	    var position = new OpenLayers.LonLat(103.842033,1.291128).transform(fromProjection, BING_toProjection);
	   // markers.addMarker(new OpenLayers.Marker(position));	//增加标记到标记图层
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
	    //controls['polygon'].activate();	//使生效
	}
	function check(){
		//$("#kindForm").submit();
		//alert();
		if(c.getSelectedValue()==-1){
			alert("Please Select Vehicle!");
			return;
		}
		if($("#startDate").val()==null || $("#startDate").val()==""){
			alert("Please Select Date!");
			return;
		}
		increate();
	}
	$(function() {
		
	});
	
	function increate(){
		
		points = [];
		points2 = [];
		map.removeLayer(vectorLayer);
		renderer = OpenLayers.Util.getParameters(window.location.href).renderer;	
		renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;
		vectorLayer = new OpenLayers.Layer.Vector("base layer", {renderers: renderer});	
		//vectorLayer.removeFeatures(lineFeature[0]);
		//vectorLayer.removeFeatures(lineFeature[1]);
		 map.removeLayer(markers);
		 markers = new OpenLayers.Layer.Markers( "Markers" );	//标记图层
		 	//增加标记图层到地图
		  
		    var dataList=null;
		    $.ajax({
				url :  basePath +"query/fencquery!getFence",
				type : "POST",
				async : false,
				data : {
					'id' :  c.getSelectedValue(),
					'fenVo.bdate' : $("#startDate").val(),
					'fenVo.edate' :  $("#endDate").val()
				},
				dataType : "json",
				success : function(data) {
					var str="";
					if(0 != data.length){
						if(0 != data[1].length){
						dataList = data[1];
						}
						if(0 != data[0].length){
							var flog=data[0];
							for(var i = 0; i < flog.length; i++){
							 creatAlert(flog[i].o_time,flog[i].o_date,(parseFloat(flog[i].o_ptx)/36000).toFixed(10),(parseFloat(flog[i].o_pty)/36000).toFixed(10),flog[i].o_curstate);
							}
						}
						if(0 != data[2].length){
							var flog=data[2];
							
							for(var i = 0; i < flog.length; i++){
								//addLine(flog[i]);
							}
						}
						if(0 != data[3].length){
						addline2(data[3]);
						}
						
					}
				},
				error : function(json) {
					window.history.back();
				}
			});
			
			var i = 0;
			for(; i < dataList.length; i++) {
				points = [];
				var datain=dataList[i]
				for(var j=0;j<datain.length;j++){
				point = new OpenLayers.Geometry.Point(parseFloat(datain[j][0]).toFixed(6), parseFloat(datain[j][1]).toFixed(6)).transform(fromProjection, BING_toProjection);	//根据坐标取点，并计算投影偏移量
				points.push(point);
				var pointaaaa=new OpenLayers.LonLat((parseFloat(datain[j][0])).toFixed(10),(parseFloat(datain[j][1])).toFixed(10)).transform(fromProjection, BING_toProjection)
				map.setCenter(pointaaaa, 14);
				}
			
			lineFeature[1] = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LinearRing(points), null, style_green);	//轨迹对象
			vectorLayer.addFeatures(lineFeature[1]);
		    map.addLayer(vectorLayer);
		    map.addLayer(markers);
			}
			//addLine();
	//		lineFeature[0] = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LinearRing(points), null, style_green);	//轨迹对象
	//		vectorLayer.addFeatures(lineFeature[0]);
	//	    map.addLayer(vectorLayer);
	//	    map.addLayer(markers);
			//creatAlert("15:25:31","2013-05-30",116.70387,39.635967,12)
			
	}
	function creatAlert(time,date,x,y,flag){
		var alerttext="";
		if(flag==1){
			alerttext="Into the Geo Fencing";
		}else if(flag==2){
			alerttext="Exit the Geo Fencing";
		}
		var alertstr='Event Name: '+alerttext+'</br>Date:'+date+'</br>Time: '+time+'</br>Longitude:'+parseFloat(x).toFixed(3)+'</br>Latitude:'+parseFloat(y).toFixed(3)
		addMarkers("speed",flag,alertstr,x,y);
	}
	function addLine(parry){
		var points2=[];
		for(var i=0;i<parry.length;i++ ){
			var pointb= new OpenLayers.Geometry.Point((parseFloat(parry[i][0])/3600).toFixed(10), (parseFloat(parry[i][1])/3600).toFixed(10)).transform(fromProjection, BING_toProjection);
			points2.push(pointb);
		
		}
		lineFeature[0] = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(points2), null, style_green2);	//轨迹对象
		vectorLayer.addFeatures(lineFeature[0]);
	}
	function addline2(listdate){
		parry=listdate;
		for(var i=0;i<parry.length;i++ ){
			var pointb= new OpenLayers.Geometry.Point((parseFloat(parry[i].longitude)/60).toFixed(10), (parseFloat(parry[i].latitude)/60).toFixed(10)).transform(fromProjection, BING_toProjection);
			points2.push(pointb);
		
		}
		if(parry!=null && parry.length!=0){
		var startPoint = new OpenLayers.LonLat(parseFloat(parry[0].longitude/60).toFixed(6), parseFloat(parry[0].latitude/60).toFixed(6)).transform(fromProjection, OSM_toProjection);
		var endPoint = new OpenLayers.LonLat(parseFloat(parry[parry.length-1].longitude/60).toFixed(6), parseFloat(parry[parry.length-1].latitude/60).toFixed(6)).transform(fromProjection, OSM_toProjection);

		addMarkers('startP',  3, 'Starting location: time: ' +parry[0].gpsDateTime + '</br>Longitude:' + parseFloat(parry[0].longitude/60).toFixed(6) + '</br>Latitude:' + parseFloat(parry[0].latitude/60).toFixed(6),parseFloat(parry[0].longitude/60).toFixed(6), parseFloat(parry[0].latitude/60).toFixed(6));	
		addMarkers('endP',  4, 'Ending location: Time: ' + parry[parry.length-1].gpsDateTime + '</br>Longitude:' + parseFloat(parry[parry.length-1].longitude/60).toFixed(6) + '</br>Latitude:' + parseFloat(parry[parry.length-1].latitude/60).toFixed(6),parseFloat(parry[parry.length-1].longitude/60).toFixed(6), parseFloat(parry[parry.length-1].latitude/60).toFixed(6));
		}

		
		lineFeature[0] = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(points2), null, style_green2);	//轨迹对象
		vectorLayer.addFeatures(lineFeature[0]);
	}
	
	function addMarkers(id, flag, content,x,y) {
		
		var point=new OpenLayers.LonLat(x,y).transform(fromProjection, BING_toProjection)
		var size = new OpenLayers.Size(20, 30);
		var offset = new OpenLayers.Pixel(-(size.w/2), -(size.h*(4/5)));
		var icon;
		var marker1 = null;
		if(flag == 1) {
			icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-blue.png', size, offset);
			marker1 = new OpenLayers.Marker(point, icon);
		}else if(flag == 2) {
			icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-black.png', size, offset);
			marker1 = new OpenLayers.Marker(point, icon);
		}else if(flag == 3) {
			
			icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-green.png', size, offset);
			marker1 = new OpenLayers.Marker(point, icon);
		}else if(flag == 4) {
			icon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-red.png', size, offset);
			marker1 = new OpenLayers.Marker(point, icon);
		}
		
		markers.addMarker(marker1);
		marker1.events.register('click', marker1, function(evt) {
			//单击事件
			//增加弹出框
			if(null != content && "" != content) {
				var popup = new OpenLayers.Popup.FramedCloud(id, point, null, content, null, true);
				map.addPopup(popup);
			}
		});
	}
	</script>
	
	</head>
	<body onload="initMap();" height="100%">
	
	
	
	
	<custom:navigation father="Data Query" model="Geo Fencing Query" />	
		<div class="pop_main">
			<table cellspacing="0" cellpadding="0" height="100%">
				<tr>
					<td class="text_bg" width="13%">Vehicle:</td>
					<td width="27%" style="text-align: left;">
<s:select list="vehlist" listKey="key" listValue="value" theme="simple" id="vehid" name="fenVo.vehid" headerKey="-1" headerValue=""></s:select>


					</td>
					<td class="text_bg" width="13%">Date:</td>
					<td width="47%" style="text-align: left;">
<label class="input_date">
<input type="text" id="startDate" name="fenVo.bdate" value="${fenVo.bdate}" style="width: 127px;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', readOnly:true});this.blur();" /></label>
						<input type="button"  onclick="check();" style="width: 60px;"  value="Search"/>
						
						
					</td>
				</tr>
				
				<tr>
					<td colspan="4" height="92%">
						<div id="basicMap" style="width:100%; height:100%">
							<div id="operDiv" style="position:absolute;width:550px; height:25px;z-index:1100; left:20px; bottom:70px; visibility: hidden;">
								<div id="slider" style="float:left; width:200px; height: 18px"></div>
								<div style="float:left; width:10px;">&nbsp;&nbsp;</div>
								<button id="play" style="float:left; height: 20px;">Play</button>
								<div id="sTime" style="float:left; width:200px; font-weight: bolder;padding-top: 3px;"></div>
							</div>
						</div>
					</td>
				</tr>
			</table>
			 <script type="text/JavaScript">
   var c=dhtmlXComboFromSelect("vehid");
	c.enableFilteringMode(true);
	</script>
		</div>
	</body>
</html>