var mapDate = [];
var mark=[];
var pointa=[];
var pointb=[];
var pointe=[];
var pointl=[];
var pointr=[];
var points=[];
function buildMap() {
	var ticks = [];
	var map = new Map();
	var distination = $("#line_chart");	//目标载体div
	var markvalue = 50;
	//存在数据集则处理
	var min=0;
	var max=100;
	if($("#dataList").val() != null && $("#dataList").val() != "" && $("#dataList").val() != "[]") {
		var dataList = eval($("#dataList").val());	//获得数据,并转成对象
		var siz = parseInt(dataList.length/5);
		min = dataList[0].otime*1;
		max = dataList[dataList.length-1].otime*1;
		mark[0]=[min,markvalue];
		mark[1]=[max,markvalue];
		var dis = max - min;
		var sz =  parseInt(dis/5);
		var b = false;
		var coun = 1;
		for (var i = 0, j = dataList.length; i < j; i++) {	//生成原始数据格式，因为图标组件要求每条线数据源为二维数组格式，所以总数据需要3维数组格式
			if(dataList[i].speed==''||dataList[i].speed==null||dataList[i].speed=='null'){
				mapDate[i] = [dataList[i].otime, 0];
			}else{
				mapDate[i] = [dataList[i].otime, dataList[i].speed];
				
				if(dataList[i].otime*1>(coun*sz+min)){
					ticks[i] = [dataList[i].otime,formatSeconds(dataList[i].otime)];
					coun++;
				}
				if(dataList[i].memo=='a'){
					pointa.push([dataList[i].otime, dataList[i].speed]);
				}
				if(dataList[i].memo=='b'){
					pointb.push([dataList[i].otime, dataList[i].speed]);
				}
				if(dataList[i].memo=='e'){
					pointe.push([dataList[i].otime, dataList[i].speed]);
				}
				if(dataList[i].memo=='l'){
					pointl.push([dataList[i].otime, dataList[i].speed]);
				}
				if(dataList[i].memo=='r'){
					pointr.push([dataList[i].otime, dataList[i].speed]);
				}
//				if(dataList[i].memo=='s'){
//					points.push([dataList[i].otime, dataList[i].speed]);
//				}
			}
		}
	}
	//选项设置
    var options = {
       xaxis: { //横轴设置
        	autoscaleMargin: 0.02,
        	min:min,
        	max:max,
        	tickSize:1,
        	ticks:ticks
        },
        grid: { 
        	hoverable: true,
        	clickable:true
        }
    };
    var data1_points = {
			show: true,
			fillColor: "blue", 
			radius: 2
		};
    var data2_points = {
    		show: true,
    		fillColor: "black", 
    		radius: 2
    };
    var data3_points = {
    		show: true,
    		fillColor: "red", 
    		radius: 2
    };
    var data4_points = {
    		show: true,
    		fillColor: "#00FF00", 
    		radius: 2
    };
    var data5_points = {
    		show: true,
    		fillColor: "#875bb2", 
    		radius: 2
    };
    var data6_points = {
    		show: true,
    		fillColor: "#993355", 
    		radius: 2
    };
    var plot = $.plot(distination,[
                       			{lines:{show:true} ,data: mapDate},
                    			{lines:{show:true}, data: mark,color:"#f00" },
                    			{color: "blue", points: data1_points,lines:{show:false}, data: pointa, label: "Sudden Acceleration"},
                    			{color: "black", points: data2_points,lines:{show:false}, data: pointb, label: "Sudden Braking"},
                    			{color: "red", points: data3_points,lines:{show:false}, data: pointe, label: "Engine Overspeed"},
                    			{color: "#00FF00", points: data4_points,lines:{show:false}, data: pointl, label: "Sudden Left"},
                    			{color: "#875bb2", points: data5_points,lines:{show:false}, data: pointr, label: "Sudden Right"}
                    		//	{color: "#993355", points: data6_points,lines:{show:false}, data: points, label: "Speeding"}
                    		], options);		//生成图形
    var previousPoint = null;
    distination.bind("plotclick", function (event, pos, item) {
		var ot =item.datapoint[0];
		 if(item.series.label!=null){
			 dosome(formatSeconds(ot));
		 }
    });
	distination.bind("plothover", function (event, pos, item) {
	    if (item) {
	        if (previousPoint != item.dataIndex) {
	            previousPoint = item.dataIndex;
	            $("#tooltip").remove();
	            var y = item.datapoint[1];
	            //标点详细信息
	            var ot =item.datapoint[0];
	            var details =  y ;
	            var pname ="";
	            if(item.series.label!=null){
	            	pname = item.series.label+"<br>";
	            }
	            showTooltip(item.pageX, item.pageY, pname+details+" , "+formatSeconds(ot));
			}
	    } else {
	        $("#tooltip").remove();
	        previousPoint = null;            
	    }
	});
	
	var container = $("#line_chart");
	var yaxisLabel = $("<div class='axisLabel yaxisLabel'></div>")
	.text("Speed")
	.appendTo(container);
	var axisLabel = $("<div class='axisLabel xaxisLabel'></div>")
	.text("Time")
	.appendTo(container);
}
function showTooltip(x, y, contents) {	
	   $('<div id="tooltip">' + contents + '</div>').css( {
	       position: 'absolute',
	       display: 'none',
	       top: y - 20,
	       left: x + 5,
	       border: '1px solid #fdd',
	       padding: '2px',
	       'background-color': '#fee',
	       opacity: 0.80
	   }).appendTo("body").fadeIn(200);
	}

function Map() {
	var struct = function(key, value) {
		this.key = key;
		this.value = value;
	}
	var put = function(key, value) {
		for ( var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				this.arr[i].value = value;
				return;
			}
		}
		this.arr[this.arr.length] = new struct(key, value);
	}

	var get = function(key) {
		for ( var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				return this.arr[i].value;
			}
		}
		return null;
	}

	var size = function() {
		return this.arr.length;
	}

	var isEmpty = function() {
		return this.arr.length <= 0;
	}

	this.arr = new Array();
	this.get = get;
	this.put = put;
	this.size = size;
	this.isEmpty = isEmpty;
}
function formatSeconds(value) {
	   var theTime = parseInt(value);// 秒
var theTime1 = 0;// 分
var theTime2 = 0;// 小时
// alert(theTime);
	   if(theTime > 60) {
	      theTime1 = parseInt(theTime/60);
	      theTime = parseInt(theTime%60);
	      // alert(theTime1+"-"+theTime);
	      if(theTime1 > 60) {
	         theTime2 = parseInt(theTime1/60);
	         theTime1 = parseInt(theTime1%60);
	       }
	   }
	   var result="";
	if(parseInt(theTime)<10){
		result = "0"+parseInt(theTime);
	}else{
		result = ""+parseInt(theTime);
	}
    if(theTime1 > 0) {
    	if(theTime1<10){
    		result = "0"+parseInt(theTime1)+":"+result;
    	}else{
    		result = ""+parseInt(theTime1)+":"+result;
    	}
    }
    if(theTime2 > 0) {
    	if(theTime2<10){
    		result = "0"+parseInt(theTime2)+":"+result;
    	}else{
    		result = ""+parseInt(theTime2)+":"+result;
    	}
    }
    return result;
	   }