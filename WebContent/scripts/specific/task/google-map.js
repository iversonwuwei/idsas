
var point;
//var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
//var Google_toProjection   = new OpenLayers.Projection("EPSG:3857"); // to Spherical Mercator Projection
//var BING_toProjection   = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
var overlay;
var map;
var drawingManager;
var polylon;
var bermudaTriangle;
var listlength=null;
var points = '';
/**
 * Initial the map
 */
function initMap() {
	 
	
	var options = {
		center: new google.maps.LatLng(1.348128,103.842033),
		zoom: 10,
		mapTypeId: google.maps.MapTypeId.TERRAIN
	};
	map = new google.maps.Map(document.getElementById("basicMap"), options);
	
	var trafficLayer = new google.maps.TrafficLayer();
	trafficLayer.setMap(map);
}
 
function draw1(){
	
	if(null != polylon && "" != polylon){
		remov();
	}
	
	drawingManager = new google.maps.drawing.DrawingManager({
		drawingMode : google.maps.drawing.OverlayType.POLYGON,
		drawingControl : false,
		drawingControlOptions: {
		    drawingModes: [google.maps.drawing.OverlayType.POLYGON],
		    position: google.maps.ControlPosition.TOP_CENTER
		},
		polygonOptions : {
			 strokeColor: "#ffa500",
			 strokeOpacity: 1,
			 strokeWeight: 2,
			 fillColor: "#ffa500",
			 fillOpacity: 0.35,
			 draggable : true,
			 editable : true
		}
	});
	 
	drawingManager.setMap(map);
	google.maps.event.addListener(drawingManager, 'overlaycomplete', function(event) {
		polylon = event.overlay;
		drawingManager.setDrawingMode(null);
		drawingManager.setOptions({drawingControl : false});
	});
}
function save1() {
	
	if(null != polylon && "" != polylon){
	  for(var i = 0; i < polylon.getPath().getLength(); i ++) {
		var point = polylon.getPath().getArray()[i];
		points +=  point.lng()+ ',' + point.lat() + ';'
		
	  }
	}
	 
	listlength = points.split(";").length;
     $("#fenname").val(points);
	
	
}

function remov() {
	polylon.setMap(null);
	polylon = null;
}
 
function increate(){
     
         this.initMap();
		var bermudaTriangle;
		 
	    var dataList=null;
		      $.ajax({
		  		url :  basePath +"task/fencing!edit1",
		  		type : "POST",
		  		async : false,
		  		data : {
		  			'id' :  $("#geofencingid").val()
		  		},
		  		dataType : "json",
		  		success : function(data) {
		  			if(0 != data.length){
		  				dataList = data;
		  			}
		  		},
		  		error : function(json) {
		  			window.history.back();
		  		}
		  	});
		    triangleCoords = [];
		    
		  	var i = 0;
		  	if(null != dataList){
		  		for(; i < dataList.length; i++) {
		  			point = new google.maps.LatLng(parseFloat(dataList[i][1]).toFixed(6), parseFloat(dataList[i][0]).toFixed(6));	//根据坐标取点，并计算投影偏移量
		  			triangleCoords.push(point);
		  			var startPointa = new google.maps.LatLng(parseFloat(dataList[i][1]).toFixed(6), parseFloat(dataList[i][0]).toFixed(6));	
		  			map.setCenter(startPointa);
		  		}
		  	}
		  	
		   
		  bermudaTriangle = new google.maps.Polygon({
		    paths: triangleCoords,
		    strokeColor: "#ffa500",
		    strokeOpacity: 0.4,
		    strokeWeight: 2,
		    fillColor: "#ffa500",
		    fillOpacity: 0.35,
		    draggable : true,
			editable : true
		  });
          
		  bermudaTriangle.setMap(map);
		  
		  polylon = bermudaTriangle;
		  
		  google.maps.event.addListener(bermudaTriangle.getPath(), 'insert_at', function(event) {
			  polylon = bermudaTriangle;
				 
		  });
		  
		  google.maps.event.addListener(bermudaTriangle.getPath(), 'set_at', function(event) {
			  polylon = bermudaTriangle;
				 
		  });
		 
}
 
function increate1(skid){
	 $("#basicMap").show();
      
	if(null != bermudaTriangle && "" != bermudaTriangle){
		 bermudaTriangle.setMap(null);
	}
	 
    var dataList=null;
	      $.ajax({
	  		url :  basePath +"task/fencing!edit1",
	  		type : "POST",
	  		async : false,
	  		data : {
	  			'id' :  skid
	  		},
	  		dataType : "json",
	  		success : function(data) {
	  			if(0 != data.length){
	  				dataList = data;
	  			}
	  		},
	  		error : function(json) {
	  			window.history.back();
	  		}
	  	});
	    triangleCoords = [];
	    
	  	var i = 0;
	  	if(null != dataList){
	  		for(; i < dataList.length; i++) {
	  			point = new google.maps.LatLng(parseFloat(dataList[i][1]).toFixed(6), parseFloat(dataList[i][0]).toFixed(6));	//根据坐标取点，并计算投影偏移量
	  			triangleCoords.push(point);
	  			 
	  			var startPointa = new google.maps.LatLng(parseFloat(dataList[i][1]).toFixed(6), parseFloat(dataList[i][0]).toFixed(6));	
	  			map.setCenter(startPointa);
	  		}
	  	}
	   
	  bermudaTriangle = new google.maps.Polygon({
	    paths: triangleCoords,
	    strokeColor: "#ffa500",
	    strokeOpacity: 0.4,
	    strokeWeight: 2,
	    fillColor: "#ffa500",
	    fillOpacity: 0.35
	  });
      
	  bermudaTriangle.setMap(map);
	   
 

}