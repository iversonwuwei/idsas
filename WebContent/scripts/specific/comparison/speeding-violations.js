var tooltips = [];		//tooltip context
var tickArr = [];

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
		url : basePath + "comparison/speeding-violations!getChartData",
		success : function(dataList) {
			if(dataList == null || dataList == "") {
				alert('No data!');
				return;
			}
			tooltips = [];		//tooltip context
			var spv = dataList[0].spv;	//先获取速度标准值
			tickArr = [ [1, spv + '-' + (spv + 5) + 'km/h'], [2, (spv + 6) + '-' + (spv + 15) + 'km/h'], [3, '>' + (spv + 16) + 'km/h'] ];		//生成速度范围轴刻度
			var mapData = [];
			var dateData = [];
			for (var i = 0, j = dataList.length; i < j; i++) {	//迭代生成图表数据
				dateData[i] = [];	//初始化二维数组
				dateData[i][0] = [1, dataList[i].sp1];	//插入当前日期的sp1数据
				dateData[i][1] = [2, dataList[i].sp2];	//插入当前日期的sp2数据
				dateData[i][2] = [3, dataList[i].sp3];	//插入当前日期的sp3数据
				tooltips[i] = dataList[i].week;
				mapData[i] = {	//打包日期组数据
					label: dataList[i].week,
					data: dateData[i],
					bars : {
						order: i + 1
					}
				}
			}
			var options = {
				series: {
					bars: {
						show: true,
						fill: 1,
						barWidth: 0.15,
					}
				},
				xaxis: {
			        position: 'bottom',
					tickLength: 0,
					autoscaleMargin: 0.15,
					show: true,
					ticks: tickArr
				},
				colors: [ '#4F81BD', '#C0504D', '#9BBB59', '#8064A2', '#4BACC6', '#F79646' ],
				grid: { hoverable: true }		// 开启鼠标在上面的效果和事件绑定
			};
			$.plot('#barChart', mapData, options);	//mapData
			var container = $("#barChart");
			var yaxisLabel = $("<div class='axisLabel yaxisLabel'></div>")
			.text("Times")
			.appendTo(container);
			var axisLabel = $("<div class='axisLabel xaxisLabel'></div>")
			.text("Speed ​​Range")
			.appendTo(container);
			
		},
		error : function(d) {
			alert("Exception occurs!");
		}
	});
}

$(function() {
	$.plot('#barChart', [], {});	//空表格占位
	var previousPoint = null;
	$("#barChart").bind("plothover", function(event, pos, item) {	//鼠标over事件
		if (item) {
			if (previousPoint != item.dataIndex) {
				previousPoint = item.dataIndex;
				$("#tooltip").remove();
				var x = item.datapoint[0].toFixed(2), y = item.datapoint[1].toFixed(2);
				showTooltip(item.pageX, item.pageY, 'Date: ' + tooltips[item.series.bars.order - 1] + '<br/>Speed ​​Range: ' + tickArr[item.dataIndex][1] + '<br/>Times: ' + y);
			}
		} else {
			$("#tooltip").remove();
			previousPoint = null;
		}
	});
});