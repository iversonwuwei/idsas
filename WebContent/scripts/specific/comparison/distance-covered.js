var tooltips = new Object();		//tooltip context
var isChartEmpty = true;
//选项设置
var options = {
	series: {
		bars: {
			show: true,
			barWidth: 0.6,
			fill: 1,
			align: "center"
		}
	},
	xaxis: {
		mode: "categories",
		autoscaleMargin: 0.15,
		tickLength: 0
	},
	colors: chartColorArr,
	grid: { hoverable: true }
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
		url : basePath + "comparison/distance-covered!getChartData",
		success : function(dataList) {
			if(dataList == null || dataList == "") {
				isChartEmpty = true;
				alert('No data!');
				return;
			}
			var container = $("#barChart");
			tooltips = new Object();
			mapData = [];
			for (var i = 0, j = dataList.length; i < j; i++) {	//生成原始数据格式，因为图标组件要求每条线数据源为二维数组格式，所以总数据需要3维数组格式
				mapData[i] = [dataList[i].value, parseFloat(dataList[i].key).toFixed(2)];
				tooltips[parseFloat(dataList[i].key).toFixed(2)] = [dataList[i].value];
			}
			$.plot('#barChart', [ mapData ], options);
			isChartEmpty = false;
			var yaxisLabel = $("<div class='axisLabel yaxisLabel'></div>")
			.text("Distance")
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
				showTooltip(item.pageX, item.pageY, 'Date: ' + tooltips[y] + '</br>Distance: ' + y);
			}
		} else {
			$("#tooltip").remove();
			previousPoint = null;
		}
	});
});

function doExport() {
	if(isChartEmpty) {
		alert('No data to export!');
		return;
	}
	document.getElementById('chartForm').submit();
}