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
		url : basePath + "comparison/driver-scorecard!getChartData",
		success : function(dataList) {
			if(dataList == null || dataList == "") {
				alert('No data!');
				return;
			}
			tooltips = [];		//tooltip context arr
			tickArr = [];
			tickAndValue = new Object();	//X轴刻度实际数值与显示值关系对象
			var dateStrArr = getDateTick($('#select_date_begin_date').val(), $('#select_date_end_date').val());	//获取日期刻度内容
			for ( var i = 0; i < dateStrArr.length; i ++) {	//生成日期刻度
				tickArr[i] = [(i + 1), dateStrArr[i]];
				tickAndValue[dateStrArr[i]] = i + 1;	//保存对应的实际X值
			}
			var mapData = [];
			var driverData = [];
			var driverID = dataList[0].driverID;	//初始化司机ID
			var driverName = dataList[0].driverName;		//初始化司机名称
			var driverIndex = 0, driverDataIndex = 0, dateIndex = 0;	//司机计数器, 司机数据计数器, 总数据计数器, 时间轴计数器
			driverData[driverIndex] = [];	//初始化二维数组
			for (var i = 0, j = dataList.length; i < j; i++) {	//迭代生成图表数据
				if(driverID != dataList[i].driverID) {	//数据转到另一个司机
					tooltips[driverIndex] = driverName;	//保存数据，以供tooltip显示使用
					mapData[driverIndex] = {	//生成数据
						label: driverName,
						data: driverData[driverIndex],
				    	bars : {
				    		order: driverIndex + 1
						}
					}
					driverID = dataList[i].driverID;	//更新司机ID
					driverName = dataList[i].driverName;	//更新司机名称
					driverDataIndex = 0;	//司机数据计数器重置为0
					driverIndex ++;	//司机计数加1，以新建数据数组
					driverData[driverIndex] = [];	//初始化二维数组
				}
				driverDataIndex ++;	//司机数据计数器累加
				driverData[driverIndex][driverDataIndex - 1] = [tickAndValue[dataList[i].riqi], dataList[i].score];	//X轴通过tickAndValue关系对象取实际数值
			}
			tooltips[driverIndex] = driverName;	//保存最后一条数据，以供tooltip显示使用
			mapData[driverIndex] = {	//生成最后一条数据
				label: driverName,
				data: driverData[driverIndex],
				xaxis: 1,
				bars : {
		    		order: driverIndex + 1
				}
			}
			var options = {
				series: {
					bars: {
						show: true,
						fill: 1,
						barWidth: 0.1
					}
				},
				xaxis: {
			        position: 'bottom',
					tickLength: 0,
					autoscaleMargin: 0.05,
					show: true,
					ticks: tickArr
				},
				yaxis: {
					min: 0,
			        max: 100
				},
				colors: [ '#4F81BD', '#C0504D', '#9BBB59', '#8064A2', '#4BACC6', '#F79646' ],
				grid: { hoverable: true }		// 开启鼠标在上面的效果和事件绑定
			};
			$.plot('#barChart', mapData, options);	//mapData
			var container = $("#barChart");
			var yaxisLabel = $("<div class='axisLabel yaxisLabel'></div>")
			.text("Violations Index")
			.appendTo(container);
			var axisLabel = $("<div class='axisLabel xaxisLabel'></div>")
			.text("Date")
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
				showTooltip(item.pageX, item.pageY, 'Driver: ' + tooltips[item.series.bars.order - 1] +
																							'<br/>Violations Index: ' + y);
			}
		} else {
			$("#tooltip").remove();
			previousPoint = null;
		}
	});
});

function getDateTick(beginDate, endDate) {
	if(beginDate == null || beginDate == '' || endDate == null || endDate == '') {
		return null;
	}
	var dateStrArr = [];
	var dayObj = [];
	var m, d;
	beginMillSec = new Date(beginDate).getTime();	//开始时间毫秒数
	endMillSec = new Date(endDate).getTime();	//结束时间毫秒数
	var dayCount = ((endMillSec - beginMillSec) / 86400000) + 1;	//结果天数
	for ( var i = 0; i < dayCount; i++) {
		dayObj = new Date(beginMillSec + 86400000 * i);
		m = parseInt(dayObj.getMonth() + 1);
		m = (m < 10 ? '0' + m : m);
		d = parseInt(dayObj.getDate());
		d = (d < 10 ? '0' + d : d);
		dateStrArr[i] = dayObj.getFullYear() + '-' + m + '-' + d;
	}
	return dateStrArr;
}