<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp"%>
<%
String basePathws = "ws://" + request.getServerName() + ":" + request.getServerPort()+ path; 

//String basePathws = "ws://" + request.getServerName() + ":" + request.getServerPort()+ "/idsasws/"; 

%>
<html>
<head>
<script charset="UTF-8" type="text/javascript" src="http://maps.googleapis.com/maps/api/js?libraries=drawing&key=AIzaSyAs2HwT6PEJzn2P8WYiSScJpq0oigo_pGA&sensor=false"></script>
<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/specific/map/olayersg.js"></script>
<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.min.js"></script>
<link  href="<%=basePath%>css/main2.css" rel="stylesheet" type="text/css">
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<style type="text/css">

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
	/**padding: 20px;**/
	display: none;
}
.mainInputTreeDiv {
	width: 350px;
	height: 280px;
	z-index: 1200;
	position: absolute;
	display: none;
	background-color: white;
	
}
.mainInputTreeDiv3 {
	width: 315px;
	height: 160px;
	z-index: 1200;
	position: absolute;
	display: none;
	background-color: white;
	
}
.mainInputTreeDiv2 {
	width: 20px;
	height: 20px;
	z-index: 1201;
	position: absolute;
	display: none;
	
}

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
	    

<script type="text/JavaScript">

var data = [],
totalPoints = 30;
var busna;
var plot;
function getData(sp) {

	if (data.length > 0)
		data = data.slice(1);

	// Do a random walk

	while (data.length < totalPoints) {
		data.push(sp);
	}

	// Zip the generated y values with the x values

	var res = [];
	for (var i = 0; i < data.length; ++i) {
		res.push([i, data[i]])
	}

	return res;
}
//----------
    	var firstbus;
    	var alertcount=0;
    	var points2=[];
		function divdisplay(){
			$("#reddiv").css("display","none");
		}
		function busdivdis(){
			var a=$("#buslis").css("display");
			if(a=='block'){ 
				$("#buslis").slideUp("slow");
			}else{
				$("#buslis").slideDown("slow");
			}
		}
		
		function addLine(x,y){
			var pointb= new google.maps.LatLng(y, x);
			points2.push(pointb);
		//	lineFeature[1] = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.LineString(points2), null, style_green);	//轨迹对象
		//	vectorLayer.addFeatures(lineFeature[1]);
			try{
				polyline.setMap(null);
			}catch(e){
			}
			polyline=[];
			polyline = new google.maps.Polyline({
			    path: points2,
			    strokeColor: "#FF0000",
			    strokeOpacity: 1.0,
			    strokeWeight: 5
			  });	//build the route
			polyline.setMap(map);
		}
		function cleanLine(){
			try{
				polyline.setMap(null);
			}catch(e){
			}
			polyline=[];
			points2=[];
		}
		
		//新建车辆列表数据
		function newbustr(strary){
			var str="<tr id='bustr"+Object[strary[13]]+"'></tr>";
			//var std="<td>"+strary[1]+"</td><td>"+strary[4]+"</td><td>"+strary[5]+"</td><td id='busy"+strary[1]+"'>"+parseFloat(strary[2]).toFixed(3)+"</td><td id='busx"+strary[1]+"'>"+parseFloat(strary[3]).toFixed(3)+"</td>"+
			<%-- "<td id='bussd"+strary[1]+"'>"+strary[9]+"km/h &nbsp; "+"<img alt='chart' style='width: 17px;height: 17px;cursor:pointer' title='chart' src='<%=basePath%>images/chart_c.GIF' onclick=\"ShowChart(\'"+strary[1]+"\')\"></td><td id='busfwj"+strary[1]+"'>"+strary[11]+"</td>"+ --%>
			//"";
			//"<td id='busjg"+strary[1]+"'>"+strary[8]+"</td>";
			var std = "<td>" + strary[1] + "</td>" +	//vehicle name
					  "<td>" + strary[4] + "</td>" +	//driver name
					  "<td>" + strary[5] + "</td>" +		//route
					  "<td id='busy" + strary[1] + "'>" + parseFloat(strary[2]).toFixed(3) + "</td>" +	//Longitude
					  "<td id='busx" + strary[1] + "'>" + parseFloat(strary[3]).toFixed(3) + "</td>";	//Latitude
			if(strary[5] == '1') {	//单兵设备，只显示速度
				std += "<td>" + strary[9] + "km/h</td>";
			} else {	//车辆设备，显示chart图
				std += "<td id='bussd" + strary[1] + "'>" + strary[9] + "km/h &nbsp; <img alt='chart' style='width:17px; height:17px; cursor:pointer' title='chart' src='" + basePath + "images/chart_c.GIF' onclick=\"ShowChart(\'" + strary[1] + "\')\"></td>"; //Speeding
			}
			std += "<td id='busfwj" + strary[1] + "'>" + strary[11] + "</td>";	//Azimuth
			var std2="<td id='busvideo"+strary[1]+"'>";
			if(strary[13] != "") {
				if(strary[5] == '1') {	//单兵设备
					std2 += "<img alt='live' style='width:20px; height:20px; cursor:pointer' title='live' src='" + basePath + "images/livetou.png' onclick=\"ShowVideoOCX(\'" + police[strary[1]] + "\')\">";	//小窗口
				} else {
					std2 += "<img alt='live' style='width:20px; height:20px; cursor:pointer' title='live' src='" + basePath + "images/livetou.png' onclick=\"ShowVideoIPCam(\'" + Object[strary[13]] + "\')\">";	//小窗口
				}
			}
			<%-- if(strary[13]!=""){
			//	 std2+="<img alt='live' style='width: 20px;height: 20px;cursor:pointer' title='live' src='<%=basePath%>images/livetou.png' onclick=\"ShowVideo(\'"+Object[strary[13]]+"\',1)\">&nbsp;<img alt='live' style='width: 20px;height: 20px;cursor:pointer' title='live' src='<%=basePath%>images/livetou.png' onclick=\"ShowVideo(\'"+Object[strary[13]]+"\',2)\">&nbsp;<img alt='live' style='width: 20px;height: 20px;cursor:pointer' title='live' src='<%=basePath%>images/livetou.png' onclick=\"ShowVideo(\'"+Object[strary[13]]+"\',4)\">&nbsp;<img alt='live' style='width: 20px;height: 20px;cursor:pointer' title='live' src='<%=basePath%>images/livetou.png' onclick=\"ShowVideo(\'"+Object[strary[13]]+"\',8)\">";
		//		std2+="<img alt='live' style='width: 20px;height: 20px;cursor:pointer' title='live' src='<%=basePath%>images/livetou.png' onclick=\"ShowVideoIp(\'"+Object[strary[13]]+"\')\">";
			std2+="<img alt='live' style='width: 20px;height: 20px;cursor:pointer' title='live' src='<%=basePath%>images/livetou.png' onclick=\"ShowVideo3(\'"+Object[strary[13]]+"\')\">";//小窗口
			} --%>
			std2+="&nbsp; </td>";
			
			if($("#bustr"+Object[strary[13]]).length==0){
				$("#bustbody").append(str);
				$("#bustr"+Object[strary[13]]).append(std+std2);
			}else{
				$("#bustr"+Object[strary[13]]).remove();
				//$("#bustbody").prepend(str);
				
				if(busname=='' || busname=='all'|| busname==Object[strary[13]]){
					$("#bustbody").prepend(str);
				}else{
					$("#bustr"+busname).after(str);
				}
				$("#bustr"+Object[strary[13]]).prepend(std+std2);
			}		
		
			if(Object[strary[13]]==busname){
				$("#bustr"+busname).css("background-color","#6495DC");
			}
			addBusFilter(strary[1],Object[strary[13]]);
		}
		function showalertno(){
			$("#yuandian").html(alertcount);
			if(alertcount>99){
				$("#yuandian").css("width","40px");
			}else{
				$("#yuandian").css("width","20px");
			}
			$("#yuandian").css("display","block");
		}
		//关闭alert列表
		function displayalertno(){
			if($("#yuandian").css("display")=='none'){
				$("#alertdiv").slideUp("slow");
				//$("#alertdiv").css("display","none");
				$("#alertdiv").html("");
				cleanAllMarkers();
				$("#leftalertdvi").css("display","none");
				$("#downrightalertdvi").css("display","none");
			}else{
			$("#yuandian").css("display","none");
			$("#alertdiv").slideDown("slow");
			alertcount=0;
			}
		}
		
		function creatAlertNo(){
			if($("#alertdiv").css("display")=='none'){
			alertcount=alertcount+1;
			showalertno();
			}
		}
		function creatAlert(time,name,x,y,flag,text){
			//flag 4 speeding
			var alerttext="";
			if(flag==3){
				alerttext="Idle";
			}else if(flag==4){
				alerttext="Speeding";
			}else if(flag==5){
				alerttext="Sudden acceleration";
			}else if(flag==6){
				alerttext="Sudden braking";
			}else if(flag==7){
				alerttext="Sudden left";
			}else if(flag==8){
				alerttext="Sudden right";
			}else if(flag==10){
				alerttext="Idle air conditioning";
			}else if(flag==11){
				alerttext="Engine overspeed";
			}else if(flag==12){
				alerttext="Into the Geo Fencing";
			}else if(flag==13){
				alerttext="Exit the Geo Fencing";
			}else if(flag==9){
				alerttext="Neutral slide";
			}
			
			
			
			var alertstr='Vehicle Name:'+name+'</br>Event Name: '+alerttext+'</br>Time: '+time+'</br>Longitude:'+parseFloat(x).toFixed(3)+'</br>Latitude:'+parseFloat(y).toFixed(3)
			if(flag==4){
				alertstr+="</br>Speed: "+text+"km/h"
			}
			
			leftalert(alertstr);
			if(flag==12 || flag==13){//出入围栏 右下角弹出
				downrightalert(alertstr)
			}
			addAlertDiv(alertstr);
			creatAlertNo();
			//creatAlertDot(116.415698, 39.967851,'3',str1+'<br/>'+str2);
			creatAlertDot(x, y,flag,alertstr);
		}
		//增加左侧alertdiv
		function leftalert(str1){
			$("#left1span").html(str1);
			//$("#left2span").text(str2);
			$("#leftalertdvi").css("display","block");
			$("#leftalertdvi").fadeOut(300).fadeIn(300).fadeOut(300).fadeIn(300); 
			
		}
		
		//增加右下角 围栏使用
		function downrightalert(str1){
			$("#downrightspan").html(str1);
			$("#downrightalertdvi").css("display","block");
			$("#downrightalertdvi").fadeOut(300).fadeIn(300).fadeOut(300).fadeIn(300); 
			
		}
		//增加右侧alert列表
		function addAlertDiv(str1){
			//alert(str1+":"+str2);
			//var divstr="<div style='margin-left: 5px;width: 95%;height: 40px;background-color: white;box-shadow:0px 2px 1px 2px #dddddd;margin-top: 4px'>"+str+"</div>";
			var divstr2="<div style='margin-left: 5px;height: 90px; width: 250px;background-color: white;left: 70px;box-shadow:0px 0px 5px 3px #919191;margin-top: 4px'>"
			+"<div style='float: left;'>"+"<img src='<%=basePath%>images/55033.gif' width='30px' height='30px' style='margin-left: 3px;margin-top: 2px'></div>"+
			"<div style='float: left;'>"+str1+"</div></div>";
			$("#alertdiv").prepend(divstr2);
		}
		
		function creatAlertDot(x,y,flag,str){
			var position2=new google.maps.LatLng(y, x);
			addMarkers("speed", position2, flag, str,str);
		}

		var busname="all";
		var socket;
		var layersObje=new Array();
		var position1s=new Array();
		//开启一个websocket
		var chat=function() {
			
			var strid="?ids="+'${mainUser.loginName}'+"&pw=${mainUser.password}";
			socket = new WebSocket('<%=basePathws%>busmap.do'+strid);
			//打开Socket 
			socket.onopen = function(event) {
			};
			socket.onmessage = function(event) {
				var strary=(""+event.data).split(",");
				if(strary[0]=="bus"){
					if(typeof(Object[strary[13]])=='undefined'){
						return;
					}
					/////////////////////////////////////////
					if(strary[1] == '53002') {
						strary[1] = 'police1';
						strary[13] = 'police1';
						strary[5] = '1';
					}
					/////////////////////////////////////////
					if(busname=='all' || busname==Object[strary[13]]){
					//接到gps数据
					if(firstbus==null){
						firstbus=Object[strary[13]];
					}
					if(firstbus!=null && firstbus!="true" && firstbus!=Object[strary[13]]){
						firstbus="true";
						cleanLine();
					}
					if(busname !=null && busname !="" && busname !="all"){
						if(Object[strary[13]]==busname){
							newMacket(strary,true);
							addLine(parseFloat(strary[2]).toFixed(10), parseFloat(strary[3]).toFixed(10));
							//居中
						}
					
					}else{
						if(firstbus=="true"){
							newMacket(strary,false);
						}else{
							newMacket(strary,true);
							if(parseFloat(strary[2])!=0 && parseFloat(strary[3])!=0){
							addLine(parseFloat(strary[2]).toFixed(10), parseFloat(strary[3]).toFixed(10));
							}
						}
					}
					}
					newbustr(strary);
					if(busna==strary[1]){
						update(strary[9])
					}
				}else if(strary[0]=="busalert"&&(busname=='all' || busname==Object[strary[7]])){
					if(typeof(Object[strary[7]])=='undefined'){
						return
					}
					//接到警告数据
					creatAlert(strary[1],strary[2],strary[3],strary[4],strary[5],strary[6]);
					
				}else if(strary[0]=="busout"&&(busname=='all' || busname==Object[strary[2]])){
					if(typeof(Object[strary[2]])=='undefined'){
						return
					}
			//		if(layersObje[Object[strary[2]]]!=null){
						
				//	layersObje[Object[strary[2]]].setMap(null);
				//	}
				//	if($("#buslist").val()==Object[strary[2]]){
				//		$("#buslist").val('all');
				//		send(this);
				//	}
				//	$("#bustr"+Object[strary[2]]).remove();
				//	$("option[id^='bus'][value='"+Object[strary[2]]+"']").remove();
					
					
				
					var straryv=new Array();
					straryv[1]=strary[2];
					straryv[4]="";
					straryv[5]="";
					straryv[11]="";
					straryv[8]="";
					straryv[10]=1;
					straryv[2]=strary[3];
					straryv[3]=strary[4];
					straryv[9]=strary[5];
					straryv[11]=strary[6];
					straryv[13]=strary[2];
					busred(straryv);
					newbustr(straryv);
				}
				
				
				
			};

			socket.onclose = function(event) {
			};
			if(socket==null){
			}
			
		};
		var socketv;
		var chatv=function() {
			
			socketv = new WebSocket('<%=basePathws%>video.do');
			//打开Socket 
			socketv.onopen = function(event) {
				socketv.send('11');
				//addText("连接成功！");
				//document.getElementById("send_btn").disabled=false;
			};
			socketv.onmessage = function(event) {
				//event.data
				
			};
			socketv.onclose = function(event) {
			};
			
		};
		function stopv(){
			socketv.send("stop");
			//socket.close();
		}
		
		var send=function(obj){//过滤
			cleanAllMarkers();
			$("#yuandian").css("display","none");
			alertcount=0;
			$("#leftalertdvi").css("display","none");
		
		//	busname=$.trim($("#busname").val());
		if($("#buslist").val()!=null){
			if((firstbus!=$("#buslist").val()) && ($("#buslist").val()!=busname)){
			cleanLine();
			}
			if($("#buslist").val()=="all"){
				sendall();
				$("#bustr"+busname).css("background-color","#FFFFFF");
				
			}else{
				$("#bustr"+busname).css("background-color","#FFFFFF");
			socket.send("busname:"+ $("#buslist").val());
			cleanOther($("#buslist").val());
			if(position1s[$("#buslist").val()]!=null){
			map.setCenter(position1s[$("#buslist").val()])
			}
			if(layersObje[$("#buslist").val()]!=null){
			layersObje[$("#buslist").val()].setMap(map);
			}
			$("#bustr"+$("#buslist").val()).css("background-color","#6495DC");
			var xtr=$("#bustr"+$("#buslist").val());
			$(xtr).remove();
			$("#bustbody").prepend(xtr);
			}
			busname=$("#buslist").val();
		}
		}
		function stop(){
			socket.send("stop");
		}
		function chats(obj){
			for(var i=0;i<10;i++){
			chat(obj);
			}
		}
		var sendall=function(obj){
			socket.send("busname:all");
		}
		function cleanAll(){
			delMarkers(markLayer[0]);
		}
		function cleanOther(busname){
			for ( var ai = 0; ai < layersObje.length; ai++) {
				if('undefined'!=typeof(layersObje[ai])){
					if(ai!=Number(busname)){
						layersObje[ai].setMap(null);
					}
					
				}
			}
		}
		
function addBusFilter(busname,busid){
			
			//$("#bus"+busname)
		if($("option[id^='bus'][value='"+busid+"']").val()==undefined){
		/**过滤下拉菜单*/
		$("#buslist").append("<option value='"+busid+"' id='"+"bus"+busname+"'>"+busname+"</option>");
		}
		}
		//在地图上增加车辆图标、方位角
		function newMacket(strary,flag2){
				if(parseFloat(strary[2])!=0 && parseFloat(strary[3])!=0){
				var position1 = new google.maps.LatLng( parseFloat(strary[3]).toFixed(5),parseFloat(strary[2]).toFixed(5));
				//var position1 = new google.maps.LatLng(12, 12);
				var layersObjb;
				if(strary[9]==0){
					layersObjb= addMarkers(null, position1, 0, strary[1],""+strary[1]) 
				}else{
				layersObjb= addMarkers(strary[10], position1, 0, strary[1],""+strary[1]) 
				}
				if(flag2==true){
					map.setCenter(position1)//中心点
				}
				position1s[Object[strary[13]]]=position1;
				if(layersObje[Object[strary[13]]]!=null){
				//	markLayer[0].removeMarker(layersObje[strary[1]]);
				layersObje[Object[strary[13]]].setMap(null);
				}
				layersObje[Object[strary[13]]]=layersObjb;
				
			}
		}
		function	busred(strary){
			if(parseFloat(strary[2])!=0 && parseFloat(strary[3])!=0){
				var position1 = new google.maps.LatLng( parseFloat(strary[3]).toFixed(5),parseFloat(strary[2]).toFixed(5));
				//var position1 = new google.maps.LatLng(12, 12);
				var layersObjb;
					layersObjb= addMarkers1(null, position1, 0,strary[1],""+strary[1]) 
			
			
				position1s[Object[strary[13]]]=position1;
				if(layersObje[Object[strary[13]]]!=null){
				//	markLayer[0].removeMarker(layersObje[strary[1]]);
				layersObje[Object[strary[13]]].setMap(null);
				}
				layersObje[Object[strary[13]]]=layersObjb;
				
			}
		}
		
		/**
		 * add event marker
		 * 
		 */
		function addMarkers1(id, point, flag, content,title) {
			var eventArr = new Array();
			var img_name = null;
			var img_title = null;
			if(flag == 0) {
				if(id==null){
					img_name = '119.gif';
				}else{
					img_name = id+'.png';//	方位角
				}
				img_title = 'Bus';
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
		
		function playback(id){
			$.ajax({
			    type: "POST",
			    async: false,
			    url : "<%=basePath%>monitor/real-time!vehip",
			 	 dataType : "text",
			 	 data:{
			 		   "id":id} ,
				success : function(data) {		 
					if(data=='noip'){
						alert("no ip");
					}else{
						var urll='http://'+data+"/setup/localstorage.html";
						window.open(urll);
					}
			    
			    } 
			});
		}
	
		function closeOCX() {
			console.log('closeOCX()');
			zdcomaudio.CloseOCX();
			$("#beginButtonOCX").attr("src", basePath + "images/video/1.png");
			$("#divBackGroundOCX").css("display","none");
			$("#mainInputTreeDivOCX").css("display","none");
			$("#mainInputTreeDiv2OCX").css("display","none");
		}
		
	
	/**
	 * create ocx video div
	 * closeAll();
	 */
	function CreatVideoDivOCX() {
		console.log('CreatVideoDivOCX()');
		$("body").append("<div id='divBackGroundOCX' class='divBackGround'></div>");
		$("body").append("<div id='mainInputTreeDivOCX' class='mainInputTreeDiv'></div>");
		$("#mainInputTreeDivOCX").css("right", 0).css("bottom", 0);
		$("body").append("<div id='mainInputTreeDiv2OCX' class='mainInputTreeDiv2'><img src='" + basePath + "images/button_xx.gif' onmousemove=\"this.src='" + basePath + "images/button_xx2.gif'\" onmouseout=\"this.src='" + basePath + "images/button_xx.gif'\" style='cursor: pointer;' onclick='closeOCX();'/></div>");
		$("#mainInputTreeDiv2OCX").css("right",345).css("bottom",275);
		$("#mainInputTreeDivOCX").append("<div style='background-color:white;width:100%;height:90%'><object name='zdcomaudio' classid='clsid:7E6CC141-11E8-41AE-A1BE-CF6606B1DB48' codebase='" + basePath + "cabs/ZdcomAudio.cab#version=1,0,9,0' width='100%' height='100%'></object></div>");
		$("#mainInputTreeDivOCX").append("<div style='background-color:#419CD8;width:100%;height:40px'><img id='beginButtonOCX' src='" + basePath + "images/video/1.png' height='39px' style='margin-left:175px; cursor:pointer;'></img></div>")
	}
	
	 /**
	 * ocx 视频
	 */
	function ShowVideoOCX(deviceID) {	//ocx 视频
		console.log('ShowVideoOCX()');
		$("#mainInputTreeDivOCX").css("display", "block");
		$("#mainInputTreeDiv2OCX").css("display", "block");
		$("#beginButtonOCX").attr("onclick", "playOCX(\'" + deviceID + "\')");
		//playOCX(deviceID);
	}
	
	function update(sp) {
		plot.setData([getData(sp)]);
		plot.draw();
	}
	
	/**
	 * 曲线图
	 */
	function CreatChartDiv() {
		console.log('CreatChartDiv()');
		$("body").append("<div id='divBackGround22' style='display:none;background:white; height: 175px;width: 315px;position: absolute;z-index: 1199;bottom: 83px;'><span>Vehicle Name:</span><span id='charspan'></span> <div id='mainInputTreeDiv1' class='mainInputTreeDiv3' ></div></div>");
		$("#mainInputTreeDiv1").css("left",0).css("top",15);
		$("body").append("<div id='mainInputTreeDiv3' class='mainInputTreeDiv2' ><img src='<%=basePath%>images/button_xx.gif'onmousemove=\"this.src='<%=basePath%>images/button_xx2.gif'\" onmouseout=\"this.src='<%=basePath%>images/button_xx.gif'\" style='cursor: pointer;' onclick='closeChart()'/></div>");
		$("#mainInputTreeDiv3").css("left",310).css("bottom",250);
	}
	
	function ShowChart(vehCode) {
		console.log('ShowChart()');
		$("#charspan").text(vehCode);
		$("#divBackGround22").css("display", "block");
		$("#mainInputTreeDiv1").css("display", "block");
		$("#mainInputTreeDiv3").css("display", "block");
		data = [];
		plot = $.plot("#mainInputTreeDiv1", [ getData() ], {
			series: {
				shadowSize: 0	// Drawing is faster without shadows
			},
			yaxis: {
				min: 0,
				max: 125,
				ticks:6
			},
			xaxis: {
				show: false
			}
		});
		busna = vehCode;
	}
	
	function closeChart(){
		console.log('closeChart()');
		$("#divBackGround22").css("display","none");
		$("#mainInputTreeDiv1").css("display","none");
		$("#mainInputTreeDiv3").css("display","none");
		data=[];
		busna="";
	}
	
	$(function (){
		$('#markerDiv').width($(window).width());
		$("#basicMap2").height($(window).height()-$("#botdiv").height())
		initMap();
		<%--
		<s:iterator value="vehDevlist" var="vv" >
		var straryv=new Array();
		straryv[1]='${vv.key}';
		straryv[4]="";
		straryv[5]="";
		straryv[11]="";
		straryv[8]="";
		straryv[2]=0;
		straryv[3]=0;
		straryv[9]=0;
		straryv[13]='${vv.value}';
		newbustr(straryv);
		addBusFilter('${vv.key}')
		</s:iterator>
		
		--%>
		<%--
		CreatVideoDiv();//ocx视频
		--%>
		<s:iterator value="vehDevlist" var="vv" >
			Object['${vv.key}']='${vv.value}'; 
		</s:iterator>
		<s:iterator value="policeDevList" var="p">
			police['${p.key}']='${p.value}'; 
		</s:iterator>
		<s:iterator value="lasts" var="vv" >
		
		var straryv=new Array();
	straryv[1]='${vv.o_busname}';
	straryv[4]="";
	straryv[5]="";
	straryv[11]="";
	straryv[8]="";
	straryv[10]=1;
	
//	 parseFloat(strary[3]).toFixed(5)
	straryv[2]=parseFloat(Number('${vv.o_longitude}')/60 ).toFixed(5) ;
	straryv[3]=parseFloat(Number('${vv.o_latitude}')/60 ).toFixed(5);
	straryv[9]='${vv.o_speed}';
	straryv[11]='${vv.o_direction}';
	straryv[13]='${vv.o_busname}';
if(typeof(Object[straryv[1]])=='undefined'){
}else{
	busred(straryv);
	newbustr(straryv);
}
</s:iterator>
CreatVideoDivIPCam();	//ipcam视频（车辆）
CreatVideoDivOCX();		//ocx视频(单兵)

		CreatChartDiv();
		if($("#stable").length > 0) {	//maintable表格如果存在，冻结第一行
		//	var mysuperTable = new superTable("stable",{headerRows : 1,fixedCols : 0});
		}
		busdivdis();
        chat();
	})
	/**
	 * 播放视频
	 */
	function playOCX(deviceID) {
		console.log('playOCX(' + deviceID + ')');
		//返回值：0--正常， 1--网络连接错误, 2--参数错误
		var r = zdcomaudio.PlayNet("172.100.100.35", 18026, "${mainUser.loginName}", 2, deviceID, 1, 350, 250);//大连本地  
		if(r == 0) {
			$("#beginButtonOCX").attr("src", basePath + "images/video/2.png");
			$("#beginButtonOCX").attr("onclick", "stopOCX(" + deviceID + ")");
		} else {
			alert("Video service has been shut down");
		}
	}
	function stopOCX(deviceID) {
		console.log('stopOCX()');
		zdcomaudio.CloseOCX();
		$("#beginButtonOCX").attr("src", basePath + "images/video/1.png");
		$("#beginButtonOCX").attr("onclick", "playOCX(" + deviceID + ")");
	}
	
	/**
	 * create IP cam video div
	 * closeIPCam();
	 */
	function CreatVideoDivIPCam() {
		console.log('CreatVideoDivIPCam()');
		$("body").append("<div id='divBackGround' class='divBackGround'></div>");
		$("body").append("<div id='mainInputTreeDiv' class='mainInputTreeDiv'></div>");
		$("#mainInputTreeDiv").css("right",0).css("bottom",0);
		$("body").append("<div id='mainInputTreeDiv2' class='mainInputTreeDiv2'><img src='" + basePath + "images/button_xx.gif' onmousemove=\"this.src='" + basePath + "images/button_xx2.gif'\" onmouseout=\"this.src='" + basePath + "images/button_xx.gif'\" style='cursor:pointer;' onclick='closeIPCam()'/></div>");
		$("#mainInputTreeDiv2").css("right",345).css("bottom",275);
		$("#mainInputTreeDiv").append("<div id='ipcamdiv' style='background-color:white;width:100%;height:100%'></div>");
	}
	
	 /**
	 * ipcam 小窗 视频
	 */
	function ShowVideoIPCam(vehcode) {
		console.log('ShowVideoIPCam()');
		$("#mainInputTreeDiv").css("display","block");
		$("#mainInputTreeDiv2").css("display","block");
		$("#ipcamdiv").html("<iframe name='ipframe' height='280px' width='350px'  frameborder='0'  src='" + basePath + "vehicle/vehicle-list/" + vehcode + "/showliveonly'></iframe>");
	}

	function closeIPCam() {
		console.log('closeIPCam()');
		try {
			ipframe.window.disconn();  
		} catch (e) {
		}
		$("#ipcamdiv").html("");
		$("#divBackGround").css("display","none");
		$("#mainInputTreeDiv").css("display","none");
		$("#mainInputTreeDiv2").css("display","none");
	}
</script>

</head>

	<body >
	<div id="basicMap" style="width:100%; height:95%;">
		<div id="basicMap2" style="width:100%; height:90%;"></div>
		<div id="leftalertdvi" style="top:0px;z-index: 1005; position: absolute;height:90px; width: 250px;background-color: white;left: 70px;box-shadow:0px 0px 1px 3px #919191;display: none;">
			<div style="float: left;">
				<img alt="alert" src="<%=basePath%>images/55033.gif" width="30px" height="30px" style="margin-left: 3px;margin-top: 2px">
			</div>
			<div id="left1span"  style="float: left; ">
				
				
			</div>
		</div>
					<div id="downrightalertdvi" style="z-index: 1400; position: absolute;height:90px; width: 250px;background-color: white;right: 0px;box-shadow:0px 0px 1px 3px #919191;display: none;bottom: 86px">
			<div style="float: left;">
				<img alt="alert" src="<%=basePath%>images/55033.gif" width="30px" height="30px" style="margin-left: 3px;margin-top: 2px">
			</div>
			<div id="downrightspan"  style="float: left; ">
				
				
			</div>
		</div>
		
		
		
		<div id="topdiv" style="top:0px;box-shadow:3px 1px 1px 2px #424242;text-align:center;width: 100px;height: 27px;background-color: #337DAF;z-index: 1005; position: absolute;right: 0px;border-radius:0px 0px 0px 15px">
			<div style="margin-top: 5px">
			<a href="#" style="color: white;text-decoration:none;" onclick="displayalertno()">Alert</a>
			</div>
			<div id="yuandian" style="box-shadow:0px 0px 1px 2px #424242;z-index: 1025; position: absolute;background-color: red;color:white;width: 20px;height: 20px;border-radius:10px 10px 10px 10px;color: white;display: none;font-size: 15px"> 0</div>
		</div>
		<div id="alertdiv" style="width: 275px;height: 500px;z-index: 1015; position: absolute;right: 0px;top: 27px;display: none;overflow: auto;">
		
		</div>
		
		
		
		
		<div id="buslis" style="width: 100%;height:150px;background-color:white;z-index: 1005; position: absolute;bottom: 80px;box-shadow:0px 0px 1px 3px #919191;overflow: auto;">
		<table class="zhzd" style="width: 100%;" id="stable">
		<thead class="Reprot_Head">
		<tr>
		<td>Vehicle Name</td>
		<td>Driver Name</td>
		<td>Route</td>
		<td>Longitude</td>
		<td>Latitude</td>
		<td>Speeding</td>
		<td>Azimuth</td>
		<%--
		<td>Engine Temperature</td>
		<td>Interior Temperature</td>
		<td>Warning Status</td>
		 --%>
		<td>Live Video</td>
		</tr>
		</thead>
		<tbody id="bustbody" style="text-align: center;">
		
		
		</tbody>
		
		
		</table>
		
		</div>
		<div id="botdiv" style="width: 100%;height:83px;background-color: #337DAF;z-index: 1005; position: absolute;bottom: 0px;min-width: 1050px" >
		<div style="margin-top: 10px;margin-left: 20px;width: 250px;float: left;" >
		<a href="#" style="color: white;text-decoration:none;font-size: 15px;text-align:center; padding:2px;border:solid 1px #E9F0F9; border-bottom:solid 1px #8599AD;border-right:solid 1px #8599AD; background:#337DAF;" onclick="busdivdis()">Summary</a>|
		 <select id="buslist" onchange="send(this.value)" >
		<option value="all">All</option>
		</select>
		
		 <%-- 
		|<a href="#" style="color: white;text-decoration:none;font-size: 15px" onclick="send(this);">Filter</a>
		|<a href="#" style="color: white;text-decoration:none;font-size: 15px" onclick="ShowVideo(11923);">ShowVideo</a> 
		|<a href="#" style="color: white;text-decoration:none;font-size: 15px" onclick="stopv();">停</a>
		|<a href="#" style="color: white;text-decoration:none;font-size: 15px" onclick="ShowVideo(11923);">show video</a>
		|<a href="#" style="color: white;text-decoration:none;font-size: 15px" onclick="dotest(11923);">show video</a>
		|<a href="#" style="color: white;text-decoration:none;font-size: 15px" onclick="openVideo();">video</a>
		|
		<a href="#" style="color: white;text-decoration:none;font-size: 15px" onclick="sendall(this);">停止过滤</a> 
		|<a href="#" style="color: white;text-decoration:none;font-size: 15px" onclick="chat();">链接</a>
		  |<a href="#" style="color: white;text-decoration:none;font-size: 15px" onclick="stop(this);">停</a> |
		|
		<a href="#" style="color: white;text-decoration:none;font-size: 15px" onclick="creatAlert('2013-15-15','有问题');">alertNO</a>|
		<a href="#" style="color: white;text-decoration:none;font-size: 15px" onclick="leftalert('a','b');">leftdivcome</a>
		|<a href="#" style="color: white;text-decoration:none;font-size: 15px" onclick=" creatAlert('2013-15-15','0542','12','12',4,'abc');">alertNO</a>|
		 --%>
		
		
		
		</div>
		
		<div id="markerDiv" style="margin-top: 5px;float:left;color: white;overflow:auto;">
		<ul style="width: 1250px;">
		<li style="display:inline;"><img src="<%=basePath%>images/fangweijiao/34fb.png" style="height: 31px; " align="absmiddle" />Vehicle</li>
		<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-magenta.png" style="height: 30px;" align="absmiddle"/>Sudden braking</li>
		<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-teal.png" style="height: 30px;" align="absmiddle"/>Sudden acceleration</li>
		<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-yellow.png" style="height: 30px;" align="absmiddle"/>Sudden left</li>
		<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-orange.png" style="height: 30px;" align="absmiddle"/>Sudden right</li>
		<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-purple.png" style="height: 30px;" align="absmiddle"/>Speeding</li>
		<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-gray.png" style="height: 30px;" align="absmiddle"/>Neutral slide</li>
		<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-blue.png" style="height: 30px;" align="absmiddle"/>Idle</li>
		<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-lime.png" style="height: 30px;" align="absmiddle"/>Idle air conditioning</li>
		<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-white.png" style="height: 30px;" align="absmiddle"/>Engine overspeed</li>		
		<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-black.png" style="height: 30px;" align="absmiddle"/>Geo Fencing notice</li>	
		</ul>
		</div>
		
		</div>
		</div>
	
	
	</body>
</html>