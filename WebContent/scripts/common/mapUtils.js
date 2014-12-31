var routeObj;	//轨迹查询对象
var mapObj;	//地图对象
var selfObj;		//本帮助类自身引用
/**
 * 地图帮助组件
 * @author zdtx_liujun
 * @param parentID		地图载体DIV的ID值
 */
function MapUtils(parentID, longitude, latitude, zoom) {
	this.parentID = parentID;
	this.longitude = longitude;
	this.latitude = latitude;
	this.zoom = zoom;
	this.baseLayer = new OpenLayers.Layer.OSM('OSM');	//底层图层（地图切片）
	this.fromProjection = new OpenLayers.Projection("EPSG:4326");   //投影计算From值
	this.toProjection = new OpenLayers.Projection("EPSG:900913"); //投影计算To值
	this.event_icon_size = new OpenLayers.Size(20, 30);	//事件图标大小
	this.event_icon_offset = new OpenLayers.Pixel(-(event_icon_size.w/2), -event_icon_size.h + 5);	//事件图标位置调整
	this.map = '';	//地图对象
	this.options = '';	//地图设置对象
	this.vectorLayer = '';	//画图图层
	this.markers = [];	//标记点集合
	this.points = [];	//坐标点集合
	this.point = '';	//坐标点
	this.markStart = '';	//起点标记图层
	this.markEnd = '';	//终点标记图层
	this.markIdle = [];	//事件-空挡标记图层
	this.markSpeed = [];	//事件-超速标记图层
	this.markAcc = [];	//事件-急加速标记图层
	this.markBrake = [];	//事件-急减速标记图层
	this.markLeft = [];	//事件-急左转向标记图层
	this.markRight = [];	//事件-急右转向标记图层
	this.initMap(parentID, longitude, latitude, zoom);	//init map, set center at beijing
}


/**
 * 初始化地图控件
 * @param parentID				配置项 载体DIV的ID值
 * @param centerX					配置项 中心点经度
 * @param centerY					配置项 中心点纬度
 */
MapUtils.prototype.initMap = function(parentID, centerX, centerY, zoom) {
	this.options = {	//设置选项
		controls: [
           new OpenLayers.Control.Navigation(),	//箭头
           new OpenLayers.Control.PanZoomBar(),	//缩放条
           new OpenLayers.Control.Attribution()
       ],
       zoom: zoom
	};
	this.map = new OpenLayers.Map(parentID, this.options);	//创建地图
//	this.mapObj.map.addControl(new OpenLayers.Control.MousePosition());	//鼠标坐标
//	this.mapObj.map.addControl(new OpenLayers.Control.LayerSwitcher());	//图层选择
	this.map.addLayer(this.baseLayer);	//增加默认图层到地图
	this.vectorLayer = new OpenLayers.Layer.Vector("Routes");	//初始化多边形容器图层
	this.map.addLayer(this.vectorLayer);	//增加容器图层到地图
	this.markStart = new OpenLayers.Layer.Markers("Starting location");	//初始化起点标记图层
	this.map.addLayer(this.markStart);	//增加起点标记图层到地图
	this.markEnd = new OpenLayers.Layer.Markers("Ending location");	//初始化终点标记图层
	this.map.addLayer(this.markEnd);	//增加终点标记图层到地图
	this.markIdle = new OpenLayers.Layer.Markers("Idle");	//初始化事件-空挡标记图层
	this.map.addLayer(this.markIdle);	//增加事件-空挡标记图层到地图
	this.markSpeed = new OpenLayers.Layer.Markers("Speeding");	//初始化事件-超速标记图层
	this.map.addLayer(this.markSpeed);	//增加事件-超速标记图层到地图
	this.markAcc = new OpenLayers.Layer.Markers("Sudden acceleration");	//初始化事件-急加速标记图层
	this.map.addLayer(this.markAcc);	//增加事件-急加速标记图层到地图
	this.markBrake = new OpenLayers.Layer.Markers("Sudden braking");	//初始化事件-急减速标记图层
	this.map.addLayer(this.markBrake);	//增加事件-急减速标记图层到地图
	this.markLeft = new OpenLayers.Layer.Markers("Sudden left");	//初始化事件-急左转向标记图层
	this.map.addLayer(this.markLeft);	//增加事件-急左转向标记图层到地图
	this.markRight = new OpenLayers.Layer.Markers("Sudden right");	//初始化事件-急右转向标记图层
	this.map.addLayer(this.markRight);	//增加事件-急右转向标记图层到地图
	this.map.setCenter(this.getLonLatPoint(centerX, centerY));	//初始化中心点
	document.getElementById('#OpenLayers\\.Control\\.Attribution_4').style.display = 'none';
}

/**
 * 根据经纬度获取LonLat点，并计算投影偏移量
 * @param lon 经度
 * @param lat 纬度
 * @return OpenLayers.LonLat
 */
MapUtils.prototype.getLonLatPoint = function(lon, lat) {
	return new OpenLayers.LonLat(lon, lat).transform(this.fromProjection, this.toProjection)
}

/**
 * 根据经纬度获取Geometry点，并计算投影偏移量
 * @param lon 经度
 * @param lat 纬度
 * @return OpenLayers.Geometry.Point
 */
MapUtils.prototype.getGeoPoint = function(lon, lat) {
	return new OpenLayers.Geometry.Point(lon, lat).transform(this.fromProjection, this.toProjection);
}

/**
 * 增加事件标记，并加入弹窗
 * @param	id	标记ID
 * @param	type	标记类型：1，起点；2，终点；3，空挡滑行；4，超速；5，急加速；6，急减速；7，急左转向；8，急右转向；
 * @param	lon	发生时经度
 * @param	lat	发生时纬度
 * @param	time	发生时间
 * @param	speed 发生时速度
 * @param	hasPop	是否有弹窗
 */
MapUtils.prototype.addEventMarker = function(id, type, lon, lat, time, speed, hasPop) {	//增加起点标记到标记图层
	var eMarker = null;
	var eIcon = null;
	var typeName = null;
	var ePoint = this.getLonLatPoint(lon, lat);
	if(type == 1) {
		typeName = 'Starting location';
		eIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-green.png', this.event_icon_size , this.event_icon_offset);
		eMarker = new OpenLayers.Marker(ePoint, eIcon);
		this.markStart.addMarker(eMarker);
	} else if(flag == 2) {
		typeName = 'Ending location';
		eIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-red.png', this.event_icon_size , this.event_icon_offset);
		eMarker = new OpenLayers.Marker(ePoint, eIcon);
		this.markEnd.addMarker(eMarker);
	} else if(flag == 3) {
		typeName = 'Idle';
		eIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-blue.png', this.event_icon_size , this.event_icon_offset);
		eMarker = new OpenLayers.Marker(ePoint, eIcon);
		this.markIdle.addMarker(eMarker);
	} else if(flag == 4) {
		typeName = 'Speeding';
		eIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-purple.png', this.event_icon_size , this.event_icon_offset);
		eMarker = new OpenLayers.Marker(ePoint, eIcon);
		this.markSpeed.addMarker(eMarker);
	} else if(flag == 5) {
		typeName = 'Sudden acceleration';
		eIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-teal.png', this.event_icon_size , this.event_icon_offset);
		eMarker = new OpenLayers.Marker(ePoint, eIcon);
		this.markAcc.addMarker(eMarker);
	} else if(flag == 6) {
		typeName = 'Sudden braking';
		eIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-magenta.png', this.event_icon_size , this.event_icon_offset);
		eMarker = new OpenLayers.Marker(ePoint, eIcon);
		this.markBrake.addMarker(eMarker);
	} else if(flag == 7) {
		typeName = 'Sudden left';
		eIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-yellow.png', this.event_icon_size , this.event_icon_offset);
		eMarker = new OpenLayers.Marker(ePoint, eIcon);
		this.markLeft.addMarker(eMarker);
	} else if(flag == 8) {
		typeName = 'Sudden right';
		eIcon = new OpenLayers.Icon(basePath + 'scripts/common/OpenLayers-2.12/img/m-orange.png', this.event_icon_size , this.event_icon_offset);
		eMarker = new OpenLayers.Marker(ePoint, eIcon);
		this.markRight.addMarker(eMarker);
	}
	if(hasPop) {	//增加弹出框
		var content = 'Event Name: ' + typeName + '</br>Time: ' + time + '</br>Speeding: ' + speed + '</br>Longitude: ' + lon + '</br>Latitude: ' + lat;
		eMarker.events.register('click', eMarker, function(evt) {	//单击事件
			var popup = new OpenLayers.Popup.FramedCloud(id, ePoint, null, content, null, true);
			this.map.addPopup(popup);
		});
	}
}

/**
 * 删除图层上的所有标记
 * @param layer 带删标记的图层
 */
MapUtils.prototype.delMarkers = function(layer) {
	var markerArr = layer.markers;
	var del_mark;
	for ( var int = 0; int < markerArr.length; int++) {
		del_mark = markerArr[int];
		layer.removeMarker(del_mark);	//去掉图层上的标记
		del_mark.destory();	//销毁该标记
	}
}

//删除轨迹
MapUtils.prototype.clearRoute = function() {
	this.vectorLayer.removeFeatures(lineFeature);	//删除轨迹对象
}