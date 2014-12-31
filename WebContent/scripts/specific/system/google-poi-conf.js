var timer;	//定时器
var interval = 15; //间隔数
var map, vectorLayer, marker, markers, popup, draw, polygonControl, points, point;
var iconx;//图片名称
var firstload=false;//变量，用于判断页面是否为第一次载入


/**
 * 创建google map
 * @returns
 */
function initMap() {
	var options = {
			//中心点北京(39.90923, 116.397428)
			center: new google.maps.LatLng(1.348128,103.842033),// new google.maps.LatLng(39.90923, 116.397428),
			zoom: 8,
			mapTypeId:google.maps.MapTypeId.ROADMAP
			};
	map = new google.maps.Map(document.getElementById("basicMap"), options);
	
	//绑定地图点击事件
	google.maps.event.addListener(map, 'click', function(event) {
	    // 经纬度  
	    var lat = event.latLng.lat();  
	    var lng = event.latLng.lng(); 
	    $("#showlongitude").html((lng).toFixed(6));
        $("#showlatitude").html((lat).toFixed(6));
        $("#longitude").val((lng).toFixed(6));
        $("#latitude").val((lat).toFixed(6));
        if(firstload){
        	//删除最后一个标记点,如果是第一次载入页面,则不删除
        	deleteOverlays();
        }
        //新增当前标记点
        addPoiMarkers(lng, lat, '', iconx, 'newMarker');
        firstload=true;
	}); 
    
}

/**
 * 删除最后一个标记点
 */
function deleteOverlays() {
	if (markerArr) {
	   markerArr[markerArr.length-1].setMap(null);
	}
}

/**
 * 创建标记点
 * @returns
 */
function addPoiMarkers(x, y, content, ico, temp) {
	var ico_url = null;
	if(temp == 'newMarker'){
		//当前标记点的图标路径
		ico_url = basePath + 'images/' + ico + '_.png';
	} else if(temp == 'oldMarkers'){
		//历史标记点的图标路径
		ico_url = basePath + 'images/' + ico + '.png';
	}
	var poiMarker = new google.maps.Marker({
	      position: new google.maps.LatLng(y, x),
	      map: map,
	      icon: ico_url
	});
	markerArr.push(poiMarker);
	
	//为弹窗赋值
	var infowindow = new google.maps.InfoWindow({
	    content: content
	});
	
	//为标记点绑定点击事件
	google.maps.event.addListener(poiMarker, 'click', function() {
		infowindow.open(map, poiMarker);
	});
	
}

/**
 * 经纬度不变的情况下更换图片URL
 * @param iconx
 * @returns
 */
function selectIcon(icon){
	iconx=icon;
	markerp(iconx);
	var name= "#"+icon;
	$(name).attr("src",basePath+"images/"+icon+"_.png");
	$("#icon").val(icon);
	var url_03 = basePath+"images/tu_03.png";
	var url_05 = basePath+"images/tu_05.png";
	var url_07 = basePath+"images/tu_07.png";
	var url_09 = basePath+"images/tu_09.png";
	if(icon!='tu_03'){
		$("#tu_03").attr("src",url_03);
	}
    if(icon!='tu_05'){
    	$("#tu_05").attr("src",url_05);
	}
    if(icon!='tu_07'){
    	$("#tu_07").attr("src",url_07);
	}
    if(icon!='tu_09'){
	    $("#tu_09").attr("src",url_09);
	}
}

/**
 * 编辑时初始化标记点/为标记点更换图标
 * @param iconx
 * @returns
 */
function markerp(iconx){
	var lon = $("#longitude").val();
	var lat = $("#latitude").val();
	if(lon !=""&&lon!=null){
		 if(firstload){
	        deleteOverlays();
	     }
	     addPoiMarkers(lon, lat, '', iconx, 'newMarker');
	     firstload=true;
    }
}

/**
 * 点击复选框时为isvisible赋值
 * @returns
 */
function setvis(){
	var temp = document.getElementById("cbox").checked;
	if(temp){
		$("#isvisible").val("T");
	} else {
		$("#isvisible").val("F");
	}
}

/**
 * 保存
 * @returns
 */
function subx(){
	//标记点名称不能为空
	if("" == $.trim($("#caption").val())){
		alert("Please enter a POI!");
		$("#caption").select();
		return;
	}
	if($.trim($("#comid").val())==""){
		alert("Please select a company!");
		return;
	}
	//经纬度不能为空，点击地图自动读取经纬度
	if("" == $.trim($("#longitude").val()) || "" == $.trim($("#latitude").val()) ){
		alert("Please click on the map to choose the longitude and latitude!");
		return;
	}
	var isPass = true;
	$(".novalid").each(function() {
		if($(this).length > 0) {
			alert("Illegal content entered, please verify!");
			isPass = false;
			return false;
		}
	});
	if(!isPass) {
		return;
	}
	checkDuplicate();
}

/**
 * check if the police name is duplicate
 */
function checkDuplicate() {
	$.ajax({
	    type: "POST",
	    async: false,
	    dataType: 'JSON',
	    data :{
	    	'vo.comID' : $("#comid").val(),
	    	'vo.poiID' : $.trim($("#poiid").val()),
	    	'vo.caption' : $.trim($("#caption").val())
	    },
	    url : basePath + "system/google-poi-conf!checkDuplicate",
	    success : function(d) {
			if("pass" != d.result){
				alert("The POI name already exists!");
				return;
			} else {
				document.poiForm.submit();
			}
	    },
	    error : function(d) {
			alert("ERROR!");
			window.history.back();
	    }
	});
}