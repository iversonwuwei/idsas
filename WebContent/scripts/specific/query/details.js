function clickVideo(date,time) {
	openProgressWin();
	setProgressText('Verifying user identity and vehicle status...');
	var mrl="";
	//alert(date+" ;"+time+" ;"+ vehidd);
	$.ajax({
	     type: "POST",
	     async: true,
	     url :basePath+"monitor/ip-cam!gvideo" ,
	  	 data:{'id':vehid,'vo.date':date,'vo.time':stim},
	  	 dataType : "json",
	  	 success : function(data) {
	  		closeProgressWin();
	  		 if(data.result=='false'){
	  			alert("no video"); 
	  		 }else{
	  			 for(var i=0;i<data.length;i++){
	  				 mrl+=data[i];
	  				 if(i!=data.length-1){
	  					mrl+=",";
	  				 }
	  			 }
	  			if($("#videoDIV").css("display") == "block") {
	 	  			alert("The playback video already exists!");
	 	  			return;
	 	  		}
	 	  		
//	 	  		mrl = 'http://192.168.18.206:8090/idsas/video/20130823131945268_S.avi';	//6077
//	 	  		mrl += ',http://192.168.18.206:8090/idsas/video/20130823132038277_S.avi';	//6077
//	 	  		mrl += ',http://192.168.18.206:8090/idsas/video/20130823132113293_S.avi';	//6077
	 	  		//mrl = 'http://171.207.29.123/sd/20130827/20130827161836540_S.avi';	//5980
	 	  		//mrl += ',http://192.168.18.206:8090/idsas/video/20130823134059852_S.avi';	//5980
//	 	  		mrl += ',http://192.168.18.206:8090/idsas/video/20130823134137841_S.avi';	//5980
	 	  		setTimeout("openVideoWin(\"" + mrl + "\")", 100);
	  		 }
	     } 
	 });
	
	
	
}

/**
 * Open the progress window
 */
function openProgressWin() {
	$("body").append("<div id='divBackGround' class='divBackGround' ></div>");
	$("body").append("<div id='openProgressDiv' class='openProgressDiv' ></div>");
	$("#openProgressDiv").css("left", ($(window).width()-500)/2).css("top",($(window).height()-110)/2).show("fast");
	//$("body").append("<div id='closeProgressDiv' class='closeDiv' ><img src='" + basePath + "images/button_xx.gif'onmousemove=\"this.src='" + basePath + "images/button_xx2.gif'\" onmouseout=\"this.src='" + basePath + "images/button_xx.gif'\" style='cursor: pointer;' onclick='stopProgress();'/></div>");
	$("#closeProgressDiv").css("left", (($(window).width()-500)/2)+500).css("top",(($(window).height()-110)/2)-10).show("fast");
	$("#openProgressDiv").append("<div id='ProgressTitle' style='font-weight:bold;'></div>" +	//Video being uploaded to the server, please wait...
																"<div id='progressbar'><div id='ProgressText' class='progress-label'>Loading...</div></div>" +
																"<script>" +
																	"$(function() {" +
																		"var progressbar = $('#progressbar')," +
																		"progressLabel = $('.progress-label');" +
																		"progressbar.progressbar({" +
																			"value: 100" +
																		"});" +
																	"});" +
														  		"</script>");
}

/**
 * Close the progress window
 */
function closeProgressWin() {
	$("#divBackGround").remove();
	$("#openProgressDiv").remove();
	$("#closeProgressDiv").remove();
}

function stopProgress() {
	$("#divBackGround").remove();
	$("#openProgressDiv").remove();
	$("#closeProgressDiv").remove();
}

function setProgressText(text) {
	$('#ProgressTitle').text(text);
}

/**
 * Open video window
 */
function openVideoWin(mrl) {
	
	$("#closeVideoDiv1").show();
	$("#videoDIV").show();
	var vlc = document.getElementById("vlc");
	if(vlc) {
		vlc.playlist.clear();
		var videos = mrl.split(',');
		for ( var int = 0; int < videos.length; int++) {
			vlc.playlist.add(videos[int]);
		}
		if(!vlc.playlist.isPlaying) {
			
			vlc.playlist.play();
		}
	}
}

/**
 * Close video window
 */
function closeVideoWin(){
	var vlc = document.getElementById("vlc");
	if(vlc) {
		vlc.playlist.stop();
	}
	$("#videoDIV").hide();
	$("#closeVideoDiv1").hide();
}

function closeVLC() {
	var vlc = document.getElementById("vlc")
	if(vlc) {
		vlc.playlist.stop();
	}
}