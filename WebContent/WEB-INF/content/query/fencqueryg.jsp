<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.core.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.widget.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.mouse.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.slider.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/specific/query/fencquery2g.js"></script>
		<script charset="UTF-8" type="text/javascript" src="http://maps.googleapis.com/maps/api/js?libraries=drawing&key=AIzaSyAs2HwT6PEJzn2P8WYiSScJpq0oigo_pGA&sensor=false"></script>
		
	<script type="text/javascript">
	var points2;
	var markLayer =  [];
	lineFeature[1]=[];
	var style_green2 = {
			strokeColor: "#339933",
		    strokeOpacity: 1,
		    strokeWidth: 5,
		    pointRadius: 6,
		    pointerEvents: "visiblePainted"
		};
	var trafficLayer, trafficOn = 0;
	function initMap() {
		var options = {
				center: new google.maps.LatLng(1.348128,103.842033),
				zoom: 10,
				mapTypeId: google.maps.MapTypeId.ROADMAP
			};
			map = new google.maps.Map(document.getElementById("basicMap"), options);
			
			//var trafficLayer = new google.maps.TrafficLayer();
			//trafficLayer.setMap(map);
			
			  var trafficControlDiv = document.createElement('div');
			    var trafficControl = new TrafficControl(trafficControlDiv, map);
			    trafficControlDiv.index = 1;
			    map.controls[google.maps.ControlPosition.TOP_RIGHT].push(trafficControlDiv);
	    //controls['polygon'].activate();	//使生效
			 markLayer[0] = [];	//标记图层
  markLayer[1] = [];	//poi
  markLayer[2] = [];	//Idle layer
  markLayer[3] = [];	//Speeding layer
  markLayer[4] = [];	//Sudden acceleration layer
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
	
	function increate(){
		points = [];
		points2 = [];
		
		if(lineFeature[0]!=null){
		lineFeature[0].setMap(null);//map.removeLayer(vectorLayer);
		}
		if(lineFeature[1]!=null){
			
		for(var i=0;i<lineFeature[1].length;i++){
			lineFeature[1][i].setMap(null);
		}
		lineFeature[1]=[]
		}
	//	renderer = OpenLayers.Util.getParameters(window.location.href).renderer;	
	//	renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;
	//	vectorLayer = new OpenLayers.Layer.Vector("base layer", {renderers: renderer});	
		//vectorLayer.removeFeatures(lineFeature[0]);
		//vectorLayer.removeFeatures(lineFeature[1]);
	//	 map.removeLayer(markers);
	//	 markers = new OpenLayers.Layer.Markers( "Markers" );	//标记图层
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
			if(dataList!=null){
			for(; i < dataList.length; i++) {
				points = [];
				var datain=dataList[i]
				for(var j=0;j<datain.length;j++){
				point =new google.maps.LatLng(parseFloat(datain[j][1]).toFixed(6), parseFloat(datain[j][0]).toFixed(6));
				points.push(point);
				var pointaaaa=new google.maps.LatLng(parseFloat(datain[j][1]).toFixed(6), parseFloat(datain[j][0]).toFixed(6));
				map.setCenter(pointaaaa);
				}
			
		lineFeature[1][i]  = new google.maps.Polygon({
				    paths: points,
				    strokeColor: "#ffa500",
				    strokeOpacity: 0.4,
				    strokeWeight: 2,
				    fillColor: "#ffa500",
				    fillOpacity: 0.35
				  });
			      
			  lineFeature[1][i].setMap(map);
			
		
			}
			}
			
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
			var pointb= new google.maps.LatLng((parseFloat(parry[i][1])/3600).toFixed(10), (parseFloat(parry[i][0])/3600).toFixed(10));
			points2.push(pointb);
		
		}
		lineFeature[0] = new google.maps.Polyline({
		    path: points2,
		    strokeColor: "#FF0000",
		    strokeOpacity: 1.0,
		    strokeWeight: 5
		  });	//build the route
		  lineFeature[0].setMap(map);
	}
	function addline2(listdate){
		parry=listdate;
		for(var i=0;i<parry.length;i++ ){
			var pointb= new google.maps.LatLng((parseFloat(parry[i].latitude)/60).toFixed(10), (parseFloat(parry[i].longitude)/60).toFixed(10));
			points2.push(pointb);
		
		}
		if(parry!=null && parry.length!=0){
		var startPoint = new google.maps.LatLng(parseFloat(parry[0].latitude/60).toFixed(6), parseFloat(parry[0].longitude/60).toFixed(6));
		var endPoint =  new google.maps.LatLng(parseFloat(parry[parry.length-1].latitude/60).toFixed(6), parseFloat(parry[parry.length-1].longitude/60).toFixed(6));
		addMarkers('startP',  3, 'Starting location: time: ' +parry[0].gpsDateTime + '</br>Longitude:' + parseFloat(parry[0].longitude/60).toFixed(6) + '</br>Latitude:' + parseFloat(parry[0].latitude/60).toFixed(6),parseFloat(parry[0].longitude/60).toFixed(6), parseFloat(parry[0].latitude/60).toFixed(6));	
		addMarkers('endP',  4, 'Ending location: Time: ' + parry[parry.length-1].gpsDateTime + '</br>Longitude:' + parseFloat(parry[parry.length-1].longitude/60).toFixed(6) + '</br>Latitude:' + parseFloat(parry[parry.length-1].latitude/60).toFixed(6),parseFloat(parry[parry.length-1].longitude/60).toFixed(6), parseFloat(parry[parry.length-1].latitude/60).toFixed(6));
		}
			lineFeature[0] = new google.maps.Polyline({
		    path: points2,
		    strokeColor: "#FF0000",
		    strokeOpacity: 1.0,
		    strokeWeight: 5
		  });	//build the route
		  lineFeature[0].setMap(map);
	}
	
	function addMarkers(id, flag, content,x,y) {
		
		var point=new google.maps.LatLng(y,x);
		var eventArr = new Array();
		var img_name = null;
		var img_title = null;	
		
		if(flag == 1) {
			img_name = 'm-blue.png';	//Starting location
			img_title = 'Into';
		} else if(flag == 2) {
			img_name = 'm-black.png';	//Ending location
			img_title = 'Exit';
		} else if(flag == 3) {
			img_name = 'm-green.png';	//Sudden acceleration
			img_title = 'Starting';
		} else if(flag == 4) {
			img_name = 'm-red.png';	//Sudden braking
			img_title = 'Ending braking';
		} 
		
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
		
		var infowindow = new google.maps.InfoWindow({
		    content: content
		});
		google.maps.event.addListener(eventArr[id], 'click', function() {
			infowindow.open(map, eventArr[id]);
		});
		markLayer[flag].push(eventArr[id]);
		eventArr[id].setMap(map);
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