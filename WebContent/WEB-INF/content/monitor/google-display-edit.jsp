<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
	<link type="text/css" href="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/css/le-frog/jquery-ui-1.9.2.custom.min.css" rel="stylesheet" />
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
		<script charset="UTF-8" type="text/javascript" src="http://maps.googleapis.com/maps/api/js?libraries=drawing&key=AIzaSyAs2HwT6PEJzn2P8WYiSScJpq0oigo_pGA&sensor=false"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.core.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.widget.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.mouse.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.slider.js"></script>
		<%-- <script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/OpenLayers-2.12/OpenLayers.js"></script> --%>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/specific/monitor/google-display.js"></script>
<%--		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.min.js"></script> --%>
	</head>
	<button id="play_video" style="height: 50px; width: 50px; z-index: 1500; position: absolute;display: none;">play</button>
	<button id="voice_video" style="height: 50px; width: 50px; z-index: 1500; position: absolute;display: none;">voice</button>
	<body onload="initMap();" onbeforeunload="closeVLC();" height="100%">
		<custom:navigation father="Monitor" model="Playback-G"  />
		<s:hidden id="hid_scheduleID" value="%{scheduleID}"></s:hidden>
		<form action="" target="hidden_frame" id="dlForm" name="dlForm" method="post">
			<iframe name='hidden_frame' id="hidden_frame" style="display: none;"></iframe>
		</form>
		<div class="pop_main">
			<table cellspacing="0" cellpadding="0" height="100%">
				<tr>
				<td class="text_bg" width="10%">Fleet:</td>
					<td width="20%" style="text-align: left;">
						<custom:selTeam name="vo.fleetID" value="${vo.fleetID}" />
					</td>
					<td class="text_bg" width="10%">Vehicle:</td>
					<td width="20%" style="text-align: left;">
						<custom:selVehName name="vehicleid" value="${vehicleid}" />
					</td>
					<td class="text_bg" width="10%">Date:</td>
					<td width="20%" style="text-align: left;">
<input type="text" id="startDate" style="width: 127px;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', readOnly:true});this.blur();" />					
					<%--
						<select id="sel_schedule" onchange="setTimes();" style="width: 310px;">
							<option value="-1">Please choose schedule...</option>
						</select>
						<s:hidden id="channel_value" theme="simple" value="0100"/>
					 --%>
					</td>
				</tr>
				<tr>
					<!-- <td class="text_bg">StartDate:</td>
					<td style="text-align: left;">
						<label class="input_date">
							<input type="text" id="startDate" style="width: 127px;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:&quot;#F{$dp.$D('endDate')||'%y-%M-%d'}&quot;, readOnly:true});this.blur();" />
						</label>
					</td> -->
					<td class="text_bg">StartTime:</td>
					<td style="text-align: left;">
						<label class="input_date">
							<input type="text" id="startTime" style="width: 127px;" onfocus="WdatePicker({dateFmt:'HH:mm:ss', readOnly:true, maxDate:&quot;#F{$dp.$D('endTime')}&quot;});this.blur();" />
						</label>
						<div id="pointTime"></div>
					</td>
					<td class="text_bg">EndTime:</td>
					<td style="text-align: left;" colspan="3">
								<label class="input_date">
									<input type="text" id="endTime" style="width: 127px;" onfocus="WdatePicker({dateFmt:'HH:mm:ss', readOnly:true, minDate:&quot;#F{$dp.$D('startTime')}&quot;});this.blur();" />
								</label>
								<button class="button_input" onclick="checkSearch();" style="width: 60px;">Search</button>
						
					</td>
				</tr>
				<tr>
					<td colspan="6" height="80%">
						<div id="basicMap" style="width:100%; height:100%"></div>
						<div id="operDiv" style="padding-bottom: 5px; "><!-- style="position:absolute;width:550px; height:25px;z-index:1100; left:20px; bottom:70px; visibility:hidden;" -->
							<div id="slider" style="float:left; width:200px; height: 18px"></div>
							<div style="float:left; width:10px;">&nbsp;&nbsp;</div>
							<button id="play" style="float: left; height: 20px; visibility: hidden;">Play</button>
							<div id="sTime" style="float:left; width:200px; font-weight: bolder;padding-top: 3px;"></div>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="6" height="30px;">
						<div id="markerDiv" style="overflow:auto;">
							<ul style="width: 1250px;">
								<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-green.png" style="height: 30px;"/>Starting location</li>
								<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-red.png" style="height: 30px;"/>Ending location</li>
								<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-magenta.png" style="height: 30px;"/>Sudden braking</li>
								<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-teal.png" style="height: 30px;"/>Sudden acceleration</li>
								<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-yellow.png" style="height: 30px;"/>Sudden left</li>
								<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-orange.png" style="height: 30px;"/>Sudden right</li>
								<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-purple.png" style="height: 30px;"/>Speeding</li>
								<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-gray.png" style="height: 30px;"/>Neutral slide</li>
								<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-blue.png" style="height: 30px;"/>Idle</li>
								<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-lime.png" style="height: 30px;"/>Idle air conditioning</li>
								<li style="display:inline;"><img src="<%=basePath%>scripts/common/OpenLayers-2.12/img/m-white.png" style="height: 30px;"/>Engine overspeed</li>
							</ul>
						</div>
					</td>
				</tr>
			</table>
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
		<script type="text/javascript">
		
		/**
		 * Click the video button
		 */
		function clickVideo1(date,time) {
			openProgressWin();
			setProgressText('Verifying user identity and vehicle status...');
			var mrl="";
			//alert(date+" ;"+time+" ;"+ vehidd);
			$.ajax({
			     type: "POST",
			     async: true,
			     url :basePath+"monitor/ip-cam!mod" ,
			  	 data:{'id':vehidd,'vo.date':date,'vo.time':time},
			  	 dataType : "json",
			  	 success : function(data) {
			  		closeProgressWin();
			  		if(data.result='false'){
			  			alert("Please set the camera device connected");
			  		}else if(data.result=='noip'){
			  			alert("Please set the camera IP");
			  		}else if(data.result =='notype'){
			  			alert("Please set the type of the camera");
			  		}else{
			  		mrl=data.result+',';
			  		setTimeout("openVideoWin(\"" + mrl + "\")", 100);
			  		}
			  		
			  		
			     } 
			 });
			
			
			
		}
		
		</script>
	</body>
</html>