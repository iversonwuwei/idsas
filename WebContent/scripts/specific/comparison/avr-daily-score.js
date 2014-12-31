var tooltips = new Object();		//tooltip context
//选项设置
var options = {
	series: {
		bars: {
			show: true,
			barWidth: 0.6,
			color: '#FFFFFF',
			fill: 1,
			align: "center"
		}
	},
	xaxis: {
		mode: "categories",
		autoscaleMargin: 0.15,
		tickLength: 0
	},
	colors: [ '#4F81BD', '#C0504D', '#9BBB59', '#8064A2', '#4BACC6', '#F79646' ],
	grid: { hoverable: true }// 开启鼠标在上面的效果和事件绑定
};

function doSearch() {
	if(select_company.getSelectedValue() == null || select_company.getSelectedValue() == "" || select_company.getSelectedValue() == "-1") {
		alert('Please select a department!');
		return;
	}
	if($('#select_date').val() == null || $('#select_date').val() == "") {
		alert('Please select a date!');
		return;
	}
	$.ajax({
		type : "POST",
		async : false,
		dataType : 'json',
		data : {
			'vo.departmentID' : select_company.getSelectedValue(),
			'vo.beginDate' : $('#select_date').val()
		},
		url : basePath + "comparison/avr-daily-score!getChartData",
		success : function(dataList) {
			tooltips = new Object();
			mapDate = [];
			for (var i = 0, j = dataList.length; i < j; i++) {	//生成原始数据格式，因为图标组件要求每条线数据源为二维数组格式，所以总数据需要3维数组格式
				mapDate[i] = [dataList[i].value, parseFloat(dataList[i].key).toFixed(2)];
				tooltips[parseFloat(dataList[i].key).toFixed(2)] = [dataList[i].value];
			}
			$.plot('#barChart', [ mapDate ], options);
			var container = $("#barChart");
			var yaxisLabel = $("<div class='axisLabel yaxisLabel'></div>")
			.text("Score")
			.appendTo(container);
			var axisLabel = $("<div class='axisLabel xaxisLabel'></div>")
			.text("Data")
			.appendTo(container);
		},
		error : function(d) {
			alert("Exception occurs!");
		}
	});
}

$(function() {
	$.plot('#barChart', [], options);	//空表格占位
	var previousPoint = null;
	$("#barChart").bind("plothover", function(event, pos, item) {	//鼠标over事件
		if (item) {
			if (previousPoint != item.dataIndex) {
				previousPoint = item.dataIndex;
				$("#tooltip").remove();
				var x = item.datapoint[0].toFixed(2), y = item.datapoint[1].toFixed(2);
				showTooltip(item.pageX, item.pageY, 'Date: ' + tooltips[y] + '</br>Score: ' + y);
			}
		} else {
			$("#tooltip").remove();
			previousPoint = null;
		}
	});
});