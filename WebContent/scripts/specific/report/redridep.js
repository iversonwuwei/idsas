/**
 * 生成图（主方法）
 */
var mapDate = [];
function buildMap() {
	var distination = $("#line_chart");	//目标载体div
	//存在数据集则处理
	if($("#dataList").val() != null && $("#dataList").val() != "" && $("#dataList").val() != "[]") {
		var dataList = eval($("#dataList").val());	//获得数据,并转成对象
		for (var i = 0, j = dataList.length; i < j; i++) {	//生成原始数据格式，因为图标组件要求每条线数据源为二维数组格式，所以总数据需要3维数组格式
			if(dataList[i].count==''||dataList[i].count==null||dataList[i].count=='null'){
				mapDate[i] = [dataList[i].driver, 0];
			}else{
				mapDate[i] = [dataList[i].driver, dataList[i].count];
			}
		}
	}
	//选项设置
    var options = { 
        series: { 
            lines: { show: true, barWidth: 0.66, align: "center", horizontal: false},
            points: { show: true,radius: 2}
        },	//图形样式
        xaxis: { //横轴设置
        	mode: "categories",
        	autoscaleMargin: 0.02,
        	tickLength: 0
        },
        yaxis: { //纵轴设置
        	min: 0,
        	tickSize: 10,
        	max: 110
        },
        colors: chartColorArr,
        grid: { hoverable: true }
    };
    var plot = $.plot(distination, [mapDate], options);		//生成图形
    var previousPoint = null;
	distination.bind("plothover", function (event, pos, item) {
	    if (item) {
	        if (previousPoint != item.dataIndex) {
	            previousPoint = item.dataIndex;
	            $("#tooltip").remove();
	            var y = item.datapoint[1];
	            //标点详细信息
	            var details =  'Score: ' + y;
	            showTooltip(item.pageX, item.pageY, details);
			}
	    } else {
	        $("#tooltip").remove();
	        previousPoint = null;            
	    }
	});
	var container = $("#line_chart");
	var yaxisLabel = $("<div class='axisLabel yaxisLabel'></div>")
	.text("Score")
	.appendTo(container);
	var axisLabel = $("<div class='axisLabel xaxisLabel'></div>")
	.text("Driver")
	.appendTo(container);
}
function showTooltip(x, y, contents) {	
	   $('<div id="tooltip">' + contents + '</div>').css( {
	       position: 'absolute',
	       display: 'none',
	       top: y + 5,
	       left: x + 5,
	       border: '1px solid #fdd',
	       padding: '2px',
	       'background-color': '#fee',
	       opacity: 0.80
	   }).appendTo("body").fadeIn(200);
	}