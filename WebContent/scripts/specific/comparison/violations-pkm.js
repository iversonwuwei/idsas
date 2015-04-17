var tooltips = [];		//tooltip context arr
var tickArr = [];
var tickAndValue = new Object();

function doSearch() {
	if(select_company.getSelectedValue() == null || select_company.getSelectedValue() == "" || select_company.getSelectedValue() == "-1") {
		alert('Please select a department!');
		return;
	}
	if($('#select_date_begin_date').val() == null || $('#select_date_begin_date').val() == "") {
		alert('Please select a starting date!');
		return;
	}
	if($('#select_date_end_date').val() == null || $('#select_date_end_date').val() == "") {
		alert('Please select a endding date!');
		return;
	}
	var begin_date = $('#select_date_begin_date').val();
	var end_date = $('#select_date_end_date').val();
	if(new Date(end_date).getTime() - new Date(begin_date).getTime() > 604800000) {
		alert('You can select up to seven days of data!');
		return;
	}
	$.ajax({
		type : "POST",
		async : false,
		dataType : 'json',
		data : {
			'vo.departmentID' : select_company.getSelectedValue(),
			'vo.beginDate' : $('#select_date_begin_date').val(),
			'vo.endDate' : $('#select_date_end_date').val()
		},
		url : basePath + "comparison/violations-pkm!getChartData",
		success : function(data) {
			if(data.dataList == null || data.dataList == "") {
				alert('No data!');
				return;
			}
			if(data.drivers == null || data.drivers == "") {
				alert('No data!');
				return;
			}
			var dataList = data.dataList;
			var driverStr = data.drivers;
			var drivers = driverStr.split('|');
			tooltips = [];		//reset tooltip context
			tickArr = [];			//reset x-tick array
			tickAndValue = new Object();	//X轴刻度实际数值与显示值关系对象
			for ( var i = 0; i < drivers.length; i++) {
				tickArr[i] = [(i + 1), drivers[i]];
				tickAndValue[drivers[i]] = i + 1;	//保存对应的实际X值
			}
			var mapData = [];
			var dateData = [];
			var riqi = dataList[0].riqi;	//初始化日期
			var dateIndex = 0, dateDataIndex = 0;	//日期计数器, 日期数据计数器
			dateData[dateIndex] = [];	//初始化二维数组
			for (var i = 0, j = dataList.length; i < j; i++) {	//迭代生成图表数据
				if(riqi != dataList[i].riqi) {	//数据转到另一个日期
					tooltips[dateIndex] = riqi;	//保存数据，以供tooltip显示使用
					mapData[dateIndex] = {	//生成数据
						label: riqi,
						data: dateData[dateIndex],
				    	bars : {
				    		order: dateIndex + 1
						}
					}
					riqi = dataList[i].riqi;	//更新日期
					dateDataIndex = 0;	//日期数据计数器重置为0
					dateIndex ++;	//日期计数加1，以新建数据数组
					dateData[dateIndex] = [];	//初始化二维数组
				}
				dateDataIndex ++;	//日期数据计数器累加
				dateData[dateIndex][dateDataIndex - 1] = [tickAndValue[dataList[i].driverName], dataList[i].behCount];
			}
			tooltips[dateIndex] = riqi;	//保存最后数据，以供tooltip显示使用
			mapData[dateIndex] = {	//生成最后一条数据
				label: riqi,
				data: dateData[dateIndex],
				xaxis: 1,
				bars : {
		    		order: dateIndex + 1
				}
			}
			var options = {
				series: {
					bars: {
						show: true,
						fill: 1,
						barWidth: 0.1,
					}
				},
				xaxis: {
			        position: 'bottom',
					tickLength: 0,
					autoscaleMargin: 0.05,
					show: true,
					ticks: tickArr
				},
				colors: [ '#4F81BD', '#C0504D', '#9BBB59', '#8064A2', '#4BACC6', '#F79646' ],
				grid: { hoverable: true }		// 开启鼠标在上面的效果和事件绑定
			};
			$.plot('#barChart', mapData, options);	//mapData
			var container = $("#barChart");
			var yaxisLabel = $("<div class='axisLabel yaxisLabel'></div>")
			.text("Times Per KM")
			.appendTo(container);
			var axisLabel = $("<div class='axisLabel xaxisLabel'></div>")
			.text("Driver")
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
				showTooltip(item.pageX, item.pageY, 'Date: ' + tooltips[item.series.bars.order - 1] +
																							'<br/>Times Per KM: ' + y);
			}
		} else {
			$("#tooltip").remove();
			previousPoint = null;
		}
	});
});