function doSearch() {
	$('#v_vehicleName').val(select_vehicle_name.getComboText());
	if($('#v_vehicleName').val() == null || $('#v_vehicleName').val() == '') {
		alert('Please select a vehicle!');
		return;
	}
	if($('#select_date').val() == null || $('#select_date').val() == '') {
		alert('Please select a date!');
		return;
	}
	beforeSubmit();
	 $.blockUI({
		 message : "<img id='displayImg' src='" + basePath + "images/loading.gif' alt='x' />",
		 overlayCSS : {
			 opacity : 0.4
		 },
		 css : {
			 border : 'none',
			 width : '37px',
			 background : 'none',
			 left : $(window).width()/2 - 37
		 }
	});
	document.queryResult.submit();
}

var downloadFrame;
function doDownload(destPath) {
	if(destPath == null || destPath == '') {
		alert('Storage path does not exist!');
		return;
	}
	var deviceIP = $('#deviceIP').val();
	if(deviceIP == null || deviceIP == '') {
		alert('Vehicle IP does not exist!');
		return;
	}
	//http://203.117.57.237/cgi-bin/admin/lsctrl.cgi?cmd=search&triggerTime=%272014-06-11%2013:00:00%27+TO+%272014-06-11%2013:59:59%27
	//http://203.117.57.237/cgi-bin/admin/downloadMedias.cgi?/mnt/auto/CF/NCMF//20140612/10/00.mp4
	var urlStr = 'http://' + deviceIP + '/cgi-bin/admin/downloadMedias.cgi?' + destPath;
	if (!downloadFrame) {
		downloadFrame = document.createElement("iframe");
		document.body.appendChild(downloadFrame);
		downloadFrame.style.display = "none";
	}
	downloadFrame.src = urlStr;
}