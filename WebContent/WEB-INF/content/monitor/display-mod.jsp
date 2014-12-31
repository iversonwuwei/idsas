<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
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
				width: 600px;
				height: 300px;
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
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.core.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.widget.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.mouse.js"></script>
		<script charset="UTF-8" type="text/javascript" src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.slider.js"></script>
		<script language="javascript" type="text/javascript" src="<%=basePath%>scripts/common/flot/jquery.flot.min.js"></script>
	</head>
	<button id="play_video" style="height: 50px; width: 50px; z-index: 1500; position: absolute;display: none;">play</button>
	<button id="voice_video" style="height: 50px; width: 50px; z-index: 1500; position: absolute;display: none;">voice</button>
	<body  height="100%">
		<custom:navigation father="Monitor" model="Video Playback" homePath="monitor/display" />
		<form action="" target="hidden_frame" id="dlForm" name="dlForm" method="post">
			<iframe name='hidden_frame' id="hidden_frame" style="display: none;"></iframe>
		</form>
		<div class="pop_main" >
			<table cellspacing="0" cellpadding="0" height="100%">
				<tr >
				<td class="text_bg" width="1%" >Fleet:</td>
					<td width="15%" style="text-align: left;">
<s:select id="fleetid" name="fleetid" list="fleets" listKey="key" listValue="value" theme="simple" headerKey="-1" headerValue="Please select" onChange="sfleet(this.value)" ></s:select>
					
						
					</td>
					<td class="text_bg" width="1%" >Vehicle:</td>
					<td width="15%" style="text-align: left;">
						
						<select id='select_vehicle_name' onChange="vehc(this.value)">
<option value='-1'>Please select</option>
</select>
						
					</td>
					<td class="text_bg" width="10%">Channel:</td>
					<td width="15%" style="text-align: left;">
					<select id="channel">
					<option value='-1'>Please select</option>
					</select>
					</td>
				</tr >
				<tr>
				<td class="text_bg" >Date:</td>
					<td  style="text-align: left;" colspan="5">
<label class="input_date_l" >
<input type="text" style="width: 140px;"  id="startMin" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:&quot;#F{$dp.$D('startMax');}&quot;, minDate:&quot;#F{$dp.$D('startMax',{H:-24});}&quot;,readOnly:true});this.blur()" /></label><label class="input_date_r"><input type="text"  id="startMax" style="width: 140px;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:&quot;#F{$dp.$D('startMin');}&quot;,maxDate:&quot;#F{$dp.$D('startMin',{H:24});}&quot;, readOnly:true});this.blur();"/>
					</label>					
					<button class="button_input" onclick="clickVideo()" style="width: 60px;">Play</button>
								<button class="button_input" onclick="stopp()" style="width: 60px;">Stop</button>
					
					</td>
				</tr>
				
				
			<tr>
			<td colspan="6" style="text-align: center;">
			<div>
<OBJECT  ID="VitaminControlTest" STYLE="width:480px;height:360px" 
CLASSID="CLSID:70EDCF63-CA7E-4812-8528-DA1EA2FD53B6" 
CODEBASE="<%=basePath%>cabs/VitaminCtrl_4.0.0.13.cab#version=4,0,0,13" >

</OBJECT>			
<%--
			<object id='vlc' codebase='<%=basePath%>cabs/axvlc.cab#version=2,0,0,0'
	  							classid='clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921'
	  							style='width: 480px; height: 360px;'>
					<param name='mrl' value='' />
					<param name='volume' value='50' />
					<param name='autoplay' value='false' />
					<param name='controls' value='false' />
			</object>
 --%>
			</div>
			</td>
			</tr>

			</table>
		</div>
		
		
	</body>
<script>
function vehc(id){
	 $("#channel").html("");
	 $("#channel").append("<option value='-1'>Please select</option>");
	if(id!=-1){
		$.ajax({
		     type: "POST",
		     async: true,
		     url :basePath+"vehicle/vehicle-list!getchannel" ,
		  	 data:{'id':id},
		  	 dataType : "json",
		  	 success : function(data) {
		  		
		  		for(var i=0;i<data.length;i++){
		  			$("#channel").append("<option value='"+data[i].coreid+"'>"+data[i].channel+"</option>");
		  		}
		     } 
		 });
	}
	
	
}


				  </script>
<script type="text/javascript">
function sfleet(id){
	$("#select_vehicle_name").html("");
	$("#select_vehicle_name").append("<option value='-1'>Please select</option>");
	if(id!=-1){
		$.ajax({
		     type: "POST",
		     async: true,
		     url :basePath+"vehicle/vehicle-list!getvehbyfleet" ,
		  	 data:{'id':id},
		  	 dataType : "json",
		  	 success : function(data) {
		  		for(var i=0;i<data.length;i++){
$("#select_vehicle_name").append("<option value='"+data[i].key+"'>"+data[i].value+"</option>");
		  		}
		     } 
		 });
	}
}

/**
 * Click the video button
 */
function clickVideo() {
	var vehidd=$("#select_vehicle_name").val();
	var chlid=$("#channel").val();
	var date=$("#startMin").val();
	var date2=$("#startMax").val();
	
	if(vehidd==null || vehidd==""||vehidd==-1){
		alert("Please select vehicles");
		return ;
	}
	if(chlid==null || chlid==""||chlid==-1){
		alert("Please select channel");
		return ;
	}
	if(date==null || date==""){
		alert("Please select date");
		return ;
	}
	if(date2==null || date2==""){
		alert("Please select date");
		return ;
	}
	
	var mrl="";
	$.ajax({
	     type: "POST",
	     async: true,
	     url :basePath+"monitor/ip-cam!mod3" ,
	  	 data:{'id':vehidd,'vo.date':date,'vo.time':date2,'cdid':chlid},
	  	 dataType : "json",
	  	 success : function(data) {
	  		/****
	 		 * false no video
	 		 * false2 The vehicle is powered off, so the related video is unavailable.
	 		 * false3 no ip
	 		 * false4 no type
	 		 * false5 no cam
	 		 */
	  		 if(data.result=='false'){
	  			alert("no video"); 
	  		 }else if(data.result=='false6'){
	  			alert("Please select the corresponding channel video server.");
	  		 }else if(data.result=='false3'){
	  			alert("Not set camera");
	  		 }else if(data.result=='false4'){
	  			alert("Not set type camera");
	  		 }else if(data.result=='false5'){
	  			alert("The vehicle not set the camera");
	  		 }else{
	  			 mrl=data.result;
	  			openVideoWin(mrl);
	 	  		
	 	 // 		setTimeout("openVideoWin(\"" + mrl + "\")", 100);
	  		 }
	     } 
	 });
	
}

function stopp(){
//	var vlc = document.getElementById("vlc");
//	if(vlc) {vlc.playlist.stop();	}
	var VACtrl2=document.getElementById("VitaminControlTest");
	VACtrl2.Disconnect();
}

function openVideoWin(mrl) {
	//alert(mrl);
	/**
	var vlc = document.getElementById("vlc");
	if(vlc) {
		vlc.playlist.stop();
		vlc.playlist.clear();
		var videos = mrl.split(',');
		for ( var int = 0; int < videos.length; int++) {
			vlc.playlist.add(videos[int]);
		}
		if(!vlc.playlist.isPlaying) {
			
			vlc.playlist.play();
		}
	}
	***/
	var VACtrl2=document.getElementById("VitaminControlTest");
	VACtrl2.Disconnect();
	VACtrl2.Password = "root";
	VACtrl2.UserName = "root";
	 	VACtrl2.Url = 	mrl;
	 	VACtrl2.DarwinConnection=-1;
	 	VACtrl2.Connect();
}
window.onbeforeunload = onbeforeunload_handler; 
function onbeforeunload_handler(){   

	stopp();
       return ;
   }   
</script>	
</html>