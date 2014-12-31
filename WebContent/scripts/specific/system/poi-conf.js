var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
var OSM_toProjection   = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
var map, vectorLayer, marker, markers, popup, markerID, iconName;
var popArr  =new Array();

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
    var mapnik = new OpenLayers.Layer.OSM();	//默认图层
    markers = new OpenLayers.Layer.Markers( "Markers" );	//标记图层
    map.addLayer(mapnik);	//增加默认图层到地图
    map.addLayer(markers);	//增加标记图层到地图
    var position =new OpenLayers.LonLat(103.842033,1.291128).transform(fromProjection, OSM_toProjection);// 新加坡
    var zoom = 11;
    map.setCenter(position, zoom);	//中心点
    var click = new OpenLayers.Control.Click();
    map.addControl(click);
    click.activate();
}

OpenLayers.Control.Click = OpenLayers.Class(OpenLayers.Control, {                
    defaultHandlerOptions: {
        'single': true,
        'double': false,
        'pixelTolerance': 0,
        'stopSingle': false,
        'stopDouble': false
    },
    initialize: function(options) {
        this.handlerOptions = OpenLayers.Util.extend({}, this.defaultHandlerOptions);
        OpenLayers.Control.prototype.initialize.apply(this, arguments); 
        this.handler = new OpenLayers.Handler.Click(this, { 'click': this.trigger }, this.handlerOptions);
    }, 
    trigger: function(e) {
        var lonlat = map.getLonLatFromPixel(e.xy);
        var p = new OpenLayers.LonLat(lonlat.lon, lonlat.lat).transform(OSM_toProjection, fromProjection);
        $("#longitude").val((p.lon).toFixed(6));
        $("#latitude").val((p.lat).toFixed(6));
        changeMarker(iconName);
    }
});

/**
 * 选择图标事件
 */
function selectIcon(icon) {
	iconName = icon;
	changeMarker(icon);
	$("#" + icon).attr("src", basePath + "images/" + icon + "_.png");
	$("#icon").val(icon);
	if(icon == 'tu_03') {
	    $("#tu_05").attr("src", basePath + "images/tu_05.png");
	    $("#tu_07").attr("src", basePath + "images/tu_07.png");
	}
    if(icon == 'tu_05') {
	    $("#tu_03").attr("src", basePath + "images/tu_03.png");
	    $("#tu_07").attr("src", basePath + "images/tu_07.png");
	}
    if(icon == 'tu_07') {
	    $("#tu_05").attr("src", basePath + "images/tu_05.png");
	    $("#tu_03").attr("src", basePath + "images/tu_03.png");
	}
}

/**
 * 更新POI
 */
function changeMarker(icon) {
	var lon = $("#longitude").val();
	var lat = $("#latitude").val();
	if (lon != "" && lon != null) {
		var lonlat = new OpenLayers.LonLat(lon, lat).transform(fromProjection, OSM_toProjection);
		if (markerID != "" && markerID != null) {
			delMarker();
		}
		 var popContent;
        if($('#poiid').val() == null || $('#poiid').val() == '') {	//新增，marker取'new', popup内容取'new POI'
        	  markerID = 'new';
        	  popContent = ($('#caption').val() == '' ? 'new POI' : $('#caption').val());
        } else {
        	  markerID = $('#poiid').val();
        	  popContent = $('#caption').val();
        }
        addMarker(markerID, lonlat, popContent, icon, true);
	}
}

/**
 * 增加marker
 * @param markerID	ID
 * @param point			坐标点
 * @param content		popup内容
 * @param iconName	icon图片名称
 * @param flag				区别图标明暗标识 true:明 	false:暗
 */
function addMarker(markerID, point, content, iconName, flag) {
	var icon;
	var size = new OpenLayers.Size(30, 30);
	var offset = new OpenLayers.Pixel(-(size.w/2), -size.h + 15);
	if(flag) {
		icon = new OpenLayers.Icon(basePath +'images/' + iconName + '_.png', size, offset);
	} else {
		icon = new OpenLayers.Icon(basePath +'images/' + iconName + '.png', size, offset);
	}
	marker = new OpenLayers.Marker(point, icon);
	markers.addMarker(marker);	//增加标记到标记图层
	marker.events.register('click', marker, function(evt) {	//注册事件
		//增加弹出框
		if(null != content && "" != content) {
			popup = new OpenLayers.Popup.FramedCloud(markerID, point, null, 'POI: ' + content, null, true);
			map.addPopup(popup);
		}
	});
//	marker.events.register('mouseover', marker, function(evt) {	//注册事件
//		//增加弹出框
//		if(null != content && "" != content && typeof(popArr[markerID]) == 'undefined') {
//			popup = new OpenLayers.Popup.FramedCloud(markerID, point, null, 'POI: ' + content, null, true);
//			map.addPopup(popup);
//			popArr[markerID] = popup;
//		}
//	});
//	marker.events.register('mouseout', marker, function(evt) {	//注册事件
//		//增加弹出框
//		if(typeof(popArr[markerID]) != 'undefined') {
//			map.removePopup(popArr[markerID]);
//			delete popArr[markerID];
//		}
//	});
}

/**
 * 删除标记点
 */
function delMarker() {
	markers.removeMarker(marker);	//因为最后赋值的marker为当前操作对象，所以直接针对marker删除即可
}

/**
 *设置isvisible值
 */
function setVisible() {
	var temp = document.getElementById("check_visible").checked;
	if(temp){
		$("#isvisible").val("T");
	} else {
		$("#isvisible").val("F");
	}
}

function doSave() {
	if("" == $.trim($("#caption").val())){
		alert("Please enter the POI Name!");
		$("#caption").select();
		return;
	}
	if($.trim($("#comid").val())==""){
		alert("Please select a company!");
		return;
	}
	if("" == $.trim($("#longitude").val())) {
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
	    url : basePath + "system/poi-conf!checkDuplicate",
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