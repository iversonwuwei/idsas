/**
 * Open video window
 */
function openVideoWin() {
	$("body").append("<div id='divBackGround' class='divBackGround' ></div>");
	$("body").append("<div id='openVideoDiv' class='openVideoDiv' ></div>");
	$("#openVideoDiv").css("left",($(window).width()-500)/2).css("top",($(window).height()-410)/2).show("fast");
	$("body").append("<div id='closeVideoDiv' class='closeDiv' ><img src='" + basePath + "images/button_xx.gif'onmousemove=\"this.src='" + basePath + "images/button_xx2.gif'\" onmouseout=\"this.src='" + basePath + "images/button_xx.gif'\" style='cursor: pointer;' onclick='closeVideoWin();'/></div>");
	$("#closeVideoDiv").css("left",(($(window).width()-500)/2)+495).css("top",(($(window).height()-410)/2)-10).show("fast");
	var ieVideoContent = "<div align='center' style='height: 700px;'>"
											+ "<div id='tips' style='left: 0px; width: 100px; height: 24px; text-align: center; color: #515151; font-family: Arial; font-size: 10pt; display: none; position: absolute; z-index: 2; background-image: url(\"" + basePath + "images/video/note-01.gif\");'/>"
											+ "<div style='height: 1px; font-size: 0px;'/>"
											+ "<br/>"
											+ "<table border='0' cellSpacing='0' cellPadding='0'>"
												+ "<tbody>"
													+ "<tr>"
														+ "<td>"
															+ "<table width='500px' border='0' cellSpacing='0' cellPadding='0'>"
																+ "<tbody>"
																	+ "<tr>"
																		+ "<td width='18' height='49' background='" + basePath + "images/video/imagebar-1.gif'></td>"
																		+ "<td background='" + basePath + "images/video/imagebar-2.gif'>"
																			+ "<div style='margin-bottom: -10px;'>"
																				+ "<img id='img0' onmouseover='mouseOver(0);' onmouseout='mouseOut(0);' onmousedown='mouseDown(0);' onmouseup='mouseUp(0);' src='" + basePath + "images/video/snapshot-1.gif' hspace='5' style='cursor: pointer;' />"
																				+ "<img id='img1' onmouseover='mouseOver(1);' onmouseout='mouseOut(1);' onmousedown='mouseDown(1)' onmouseup='mouseUp(1)' src='" + basePath + "images/video/record-1.gif' hspace='5' style='cursor: pointer;' />"
																				+ "<img id='img6' onmouseover='mouseOver(6);' onmouseout='mouseOut(6);' onmousedown='mouseDown(6)' onmouseup='mouseUp(6)' src='" + basePath + "images/video/open-1.gif' hspace='5' style='cursor: pointer;' />"
																				+ "<img id='img2' onmouseover='mouseOver(2);' onmouseout='mouseOut(2);' onmousedown='mouseDown(2)' onmouseup='mouseUp(2)' src='" + basePath + "images/video/full-1.gif' hspace='5' style='cursor: pointer;' />"
																				+ "<img id='img7' onmouseover='mouseOver(7);' onmouseout='mouseOut(7);' onmousedown='mouseDown(7)' onmouseup='mouseUp(7)' src='" + basePath + "images/video/digital_zoom-1.gif' hspace='5' style='cursor: pointer;' />"
																			+ "</div>"
																		+ "</td>"
																		+ "<td width='3' background='" + basePath + "images/video/imagebar-3.gif'></td>"
																	+ "</tr>"
																+ "</tbody>"
															+ "</table>"
														+ "</td>"
													+ "</tr>"
													+ "<tr>"
														+ "<td>"
															+ "<table width='100%' border='0' cellSpacing='0' cellPadding='0' style='height: 380px;'>"
																+ "<tbody>"
																	+ "<tr>"
																		+ "<td width='4' height='4' background='" + basePath + "images/video/a1.gif'></td>"
																		+ "<td id='playerTdWidth' background='" + basePath + "images/video/a2.gif'></td>"
																		+ "<td width='4' background='" + basePath + "images/video/a3.gif'></td>"
																	+ "</tr>"
																	+ "<tr>"
																		+ "<td background='" + basePath + "images/video/a4.gif'></td>"
																		+ "<td align='center' height='375px'>"
																			+ "<object id='RTSPCtl' classid='CLSID:1384A8DE-7296-49DA-B7F8-8A9A5984BE55' codebase='" + basePath + "cabs/AxRTSP.cab#version=1,0,0,190' style='height: 100%; width: 100%;' onfocus='document.body.focus()' />"
																		+ "</td>"
																		+ "<td background='" + basePath + "images/video/a5.gif'></td>"
																	+ "</tr>"
																	+ "<tr>"
																		+ "<td height='4' background='" + basePath + "images/video/a6.gif'></td>"
																		+ "<td background='" + basePath + "images/video/a6.gif'></td>"
																		+ "<td background='" + basePath + "images/video/a8.gif'></td>"
																	+ "</tr>"
																+ "</tbody>"
															+ "</table>"
														+ "</td>"
													+ "</tr>"
												+ "</tbody>"
											+ "</table>"
										+ "</div>";
	$("#openVideoDiv").append(ieVideoContent);
	//initAxRTSP(username, passwd, camIP);
}

/**
 * Close video window
 */
function closeVideoWin() {
	if(isMsie) {
		Disconnect();
	}
	$("#divBackGround").remove();
	$("#openVideoDiv").remove();
	$("#closeVideoDiv").remove();
}

function doSearch() {
	$('#v_select_model').val(select_model.getSelectedValue());
	document.queryResult.submit();
}

function doSub() {
	if($('#camName').val() == null || $('#camName').val() == "") {
		alert('Camera Name should not be empty!');
		return false;
	}
//	if($('#camIP').val() == null || $('#camIP').val() == "") {
//		alert('Camera IP should not be empty!');
//		return false;
//	}
	if($('#adminPass').length > 0) {
		if($('#adminPass').val() == null || $('#adminPass').val() == "") {
			alert('Administrator Password should not be empty!');
			return false;
		}
	}
	$.ajax({
	    type : "POST",
	    async : false,
	    dataType : 'json',
	    data : {
	    	'vo.camID' : $('#camID').val(),
	    	'vo.newIP' : $('#camIP').val(),
	    	'vo.camCode' : $('#camCode').val()
	    },
	    url : basePath + "monitor/ip-cam!checkDuplicate",
	    success : function(d) {
			if("IP" == d.result) {
				alert('Camera IP already exists!');
				return;
			} else if("CODE" == d.result) {
				alert('Camera Code already exists!');
				return;
			} else {
				$('#v_select_model').val(select_model.getSelectedValue());
				document.queryResult.submit();
			}
	    },
	    error : function(d) {
			alert("Query data anomalies!");
			return;
		}
	});
}

function doSubUser() {
	if($('#adminPass').val() == null || $('#adminPass').val() == "") {
		alert('Administrator Password should not be empty!');
		return false;
	}
	if($('#operatorName').val() == null || $('#operatorName').val() == "") {
		alert('Operator Name should not be empty!');
		return false;
	}
	if($('#operatorPass').val() == null || $('#operatorPass').val() == "") {
		alert('Operator Password should not be empty!');
		return false;
	}
	if($('#viewerName').val() == null || $('#viewerName').val() == "") {
		alert('Viewer Name should not be empty!');
		return false;
	}
	if($('#viewerPass').val() == null || $('#viewerPass').val() == "") {
		alert('Viewer Password should not be empty!');
		return false;
	}
	if($('#adminName').val() == $('#operatorName').val()) {
		alert('Administrator Name should not be same with Operator Name!');
		return false;
	}
	if($('#adminName').val() == $('#viewerName').val()) {
		alert('Administrator Name should not be same with Viewer Name!');
		return false;
	}
	if($('#viewerName').val() == $('#operatorName').val()) {
		alert('Operator Name should not be same with Viewer Name!');
		return false;
	}
	document.queryResult.submit();
}

/**-----------------------PlayerArea2.js-------------------------**/
var recording = 0;
var mouseFlag = new Array(0, 0, 0, 0, 0, 0, 0, 0);
var outImg = new Array(basePath + 'images/video/snapshot-1.gif', basePath + 'images/video/record-1.gif',
		basePath + 'images/video/full-1.gif', basePath + 'images/video/digital-1.gif',
		basePath + 'images/video/audio-1.gif', basePath + 'images/video/talk-1.gif',
		basePath + 'images/video/open-1.gif', basePath + 'images/video/digital_zoom-1.gif');
var overImg = new Array(basePath + 'images/video/snapshot-2.gif', basePath + 'images/video/record-2.gif',
		basePath + 'images/video/full-2.gif', basePath + 'images/video/digital-2.gif',
		basePath + 'images/video/audio-2.gif', basePath + 'images/video/talk-2.gif',
		basePath + 'images/video/open-2.gif', basePath + 'images/video/digital_zoom-2.gif');
var downImg = new Array(basePath + 'images/video/snapshot-4.gif', basePath + 'images/video/record-4.gif',
		basePath + 'images/video/full-4.gif', basePath + 'images/video/digital-4.gif',
		basePath + 'images/video/audio-4.gif', basePath + 'images/video/talk-4.gif',
		basePath + 'images/video/open-4.gif', basePath + 'images/video/digital_zoom-4.gif');
var tipsStr = new Array('Snapshot', 'Record', 'Full Screen', 'Manual Trigger',
		'Listen', 'Talk', 'Set Path', 'Digital Zoom');
var configActiveXPath = "imege:\\";
var muteFlag = 1;

function getCookie(c_name) {
	if (document.cookie.length > 0) {
		var c_list = document.cookie.split("\;");
		for (i in c_list) {
			var cook = c_list[i].split("=");
			if (cook[0] == c_name) {
				return unescape(cook[1]);
			}
		}
	}
	return null;
}

function setCookie(c_name, c_value, expiredays) {
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + expiredays);
	document.cookie = c_name + "=" + escape(c_value)
			+ ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString());
}

function workaround() {
	with (document) {
		getElementById("bd").style.display = "none";
		getElementById("bd").style.display = "";
	}
}

function mouseOver(i) {
	with (document) {
		if (mouseFlag[i] == 0 || mouseFlag[i] == -1) {
			getElementById("img" + i).src = overImg[i];
			mouseFlag[i] = 1;
		}
		var e = window.event;
		var posX = e.clientX;
		getElementById("tips").innerHTML = tipsStr[i];
		if (getElementById("tips").style.left == "0px") {
			getElementById("tips").style.left = (posX-370) + "px";
		}
		getElementById("tips").style.display = "block";
	}
}

function mouseOut(i) {
	with (document) {
		if (mouseFlag[i] == 1 || mouseFlag[i] == -2
				|| (mouseFlag[i] == 2 && i == 0)) {
			getElementById("img" + i).src = outImg[i];
			mouseFlag[i] = 0;
		} else if (mouseFlag[i] == -1) { // alert happen
			mouseFlag[i] = -2;
		}
		getElementById("tips").innerHTML = "";
		getElementById("tips").style.display = "none";
		getElementById("tips").style.left = "0px";
	}
}

function mouseDown(i) {
	with (document) {
		if (i == 0) {
			Snapshot();
		} else if (i == 1) {
			StartRecord();
		} else if (i == 2) {
			FullScreen();
			return;
		} else if (i == 6) {
			getElementById("img6").src = downImg[6];
			mouseFlag[i] = 2;
			GetFolder();
		} else if (i == 7) {
			SetDigitalZoom();
		}

		if (mouseFlag[i] == 1 && i != 6) {
			getElementById("img" + i).src = downImg[i];
			mouseFlag[i] = 2;
		} else if (mouseFlag[i] == 2) {
			getElementById("img" + i).src = overImg[i];
			mouseFlag[i] = 1;
		}
	}
}

function mouseUp(i) {
	with (document) {
		if (mouseFlag[i] == 2 && i != 1 && i != 7) {
			if (i == 0) { // snapshot delay 100ms
				setTimeout("mouseUpSnapshot()", 100);
			} else {
				getElementById("img" + i).src = overImg[i];
				mouseFlag[i] = 1;
			}
		}
	}
}

function mouseUpSnapshot() {
	with (document) {
		getElementById("img0").src = overImg[0];
		mouseFlag[0] = 1;
	}
}

function mouseUpOpen() {
	with (document) {
		getElementById("img6").src = overImg[6];
		mouseFlag[6] = 1;
	}
}

function mouseUpDigitalZoom() {
	with (document) {
		getElementById("img7").src = overImg[7];
		mouseFlag[7] = 1;
	}
}

function FullScreen() {
	with (document) {
		var rtsp = getElementById('RTSPCtl');
		rtsp.Set_Full(1);
		rtsp.Set_Fit(0);
	}
}

function StartRecord() {
	with (document) {
		var rtsp = getElementById('RTSPCtl');
		var errorcode;

		if (recording == 0) {
			errorcode = rtsp.StartRecord(configActiveXPath + "\\ipcam.avi", 25);
			if (errorcode == 0) { // fail
				rtsp.StopRecord();
				mouseFlag[1] = -1;
				alert("Record Path not exist");
			} else {
				recording = 1;
			}
		} else {
			recording = 0;
			rtsp.StopRecord();
		}
	}
}

function Snapshot() {
	with (document) {
		var rtsp = getElementById('RTSPCtl');
		var errorcode = rtsp.Snapshot(configActiveXPath + "\\ipcam.jpg");
		if (errorcode != 1) {
			alert("Snapshot Path not exist");
		}
	}
}

function GetFolder() {
	with (document) {
		var rtsp = getElementById("RTSPCtl");
		var str = rtsp.DirectoryBrowse(configActiveXPath);
		if (str == "") {
			return;
		} else {
			configActiveXPath = str;
			setCookie("imegePath", str, 730);
		}
		mouseUpOpen();
	}
}

function SetDigitalZoom() {
	with (document) {
		var rtsp = getElementById('RTSPCtl');
		rtsp.Set_ZoomDialog(1);
		rtsp.Set_Fit(1);
		setTimeout("mouseUpDigitalZoom()", 100);
	}
}

function initAxRTSP(username, passwd, camIP) {
	with (document) {
		setTimeout("Connect('" + username + "','" + passwd + "','" + camIP + "')", 100);
		var imegePath = getCookie("imegePath");
		if (imegePath != null) {
			configActiveXPath = imegePath;
		}
	}
}

function Connect(username, passwd, ip) {
	with (document) {
		var rtsp = getElementById('RTSPCtl');
		rtsp.Set_Path("stream1");
		rtsp.Set_ID(username);
		rtsp.Set_PW(passwd);
		rtsp.Set_URL(ip);
		rtsp.Connect();
		rtsp.Set_Mute(1);
	}
}

function Disconnect() {
	with (document) {
		var rtsp = getElementById('RTSPCtl');
		if(!rtsp) {
			return;
		}
		if (recording == 1) {
			recording = 0;
			rtsp.StopRecord();
		}
		rtsp.Disconnect();
	}
}

function getCamTime(camID, camIP) {
	if(camID == null || camID == "") {
		alert("Camera's ID is null!");
		return;
	}
	if(camIP == null || camIP == "") {
		alert("Camera's IP is null!");
		return;
	}
	var layerIndex = layer.load(0);
	$.ajax({
	    type : "POST",
	    async : true,
	    dataType : 'json',
	    data : {
	    	'vo.camIP' : camIP,
	    },
	    url : basePath + "monitor/ip-cam!getCamTime",
	    success : function(d) {
			if(d.camTime != null) {
				if(d.camTimeErr != null) {
					$('#camTime_' + camID + '_' + camIP).css("color", "red");
				}
				$('#camTime_' + camID + '_' + camIP).text(d.camTime);
			}
	    },
	    error : function(d) {
			alert("Connect error!");
		},
		complete : function(d) {
			layer.close(layerIndex);
		}
	});
}

function getCamTimes() {
	var camInfos = '', camID, camIP,camIPs = new Object();
	$('.camTimeClass').each(function() {
		camID = this.id.split('_')[1];
		camIP = $(this).attr('name');
		if(camIP != null && camIP != '') {
			camInfos += camID + '|' + camIP + ';';
			camIPs[camID] = camIP;
		}
	});
	if(camInfos == "") {
		alert("All cameras' IP in current page are null!");
		return;
	}
	var layerIndex = layer.load(0);
	$.ajax({
	    type : "POST",
	    async : true,
	    dataType : 'json',
	    data : {
	    	'vo.camIP' : camInfos,
	    },
	    url : basePath + "monitor/ip-cam!getCamTimes",
	    success : function(d) {
			if(d.camTimes != null) {
				var camTimes = d.camTimes.split(';');
				var camTime = '';
				for ( var i = 0, j = camTimes.length; i < j; i++) {
					camTime = camTimes[i];
					if(camTime == '') {
						continue;
					}
					var tdObj = $('#camTime_' + camTime.split('_')[0]);
					if(camTime.indexOf('_') > 0) {
						tdObj.css("color", "red");
						tdObj.text(camTime.split('_')[1]);
					} else {
						tdObj.text(camTime.split('|')[1]);
					}
				}
			}
	    },
	    error : function(d) {
			alert("Connect error!");
		},
		complete : function(d) {
			layer.close(layerIndex);
		}
	});
}