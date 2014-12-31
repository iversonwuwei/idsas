$(document).ready(function() {
	$(".eee").each(function(i,t){
		inputNum_($(t).attr("id"));
	});
	for(var i = 0;i<16;i++){
		if($("#cid\\["+i+"\\]").val()==null||$("#cid\\["+i+"\\]").val()==""){
			$("#cid\\["+i+"\\]").val("-1");
		}
	}
	if($('#o_deviceno').length > 0 && $('#o_deviceno').val() != null) {
		showChannels();
	}
});



function sub1() {
	if("" == $.trim($("#o_devicename").val())) {
		alert("please entering the device name!");
		$("#o_devicename").focus();
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
	$.ajax({
		type: "POST",
	    async: false,
	    dataType:'text',
	    data :{
	    	'ids' : $.trim($("#o_deviceno").val()),
	    	'names' : $.trim($("#o_devicename").val()),
	    },
	    url : basePath + "system/device-list!checkEdit",
	    success : function(dd) {
			if("true" == dd) {
				alert("The Name has already exists!");
				$("#o_devicedescript").select();
				return;
			} 
			var str= $("#o_devicedetype").find("option:selected").text();
			$("#o_devicedescript").val(str);
			document.queryResult.submit();
	    },
	    error : function(dd) {
			alert("Exception occurs!");
			window.history.back();
		}
	});
}

/**
 * 新增页切换设备类型
 */
function showChannels_add() {
	if($("[name='o_unittype']:checked").val() == '1' || $("[name='o_unittype']:checked").val() == '2') {	//type BNT5000/A5
		if($("[name='o_unittype']:checked").val() == '1') {	//BNT5000
			$("#deviceTable tr:gt(2)").show();
		} else {
			$("#deviceTable tr:gt(2)").hide();
		}
		$("#vehDeviceName").show();
		$("#vehDeviceName").attr('disabled', false);
		$("#policeDeviceName").hide();
		$("#policeDeviceName").attr('disabled', true);
		$("#policeDeviceName").attr('value', '');
		if($("#policeDeviceNameTip").length > 0) {			//判断提示信息是否存在
			$("#policeDeviceNameTip").remove();				//存在则删除
		}
		$("#policeDeviceName").css("border", "1px solid #A2B3BD");	//输入内容合法，恢复原样式
		$("#policeDeviceName").css("background", "#FFFFFF");
	} else {	//type police
		$("#deviceTable tr:gt(2)").hide();
		$("#policeDeviceName").show();
		$("#policeDeviceName").attr('disabled', false);
		$("#vehDeviceName").hide();
		$("#vehDeviceName").attr('disabled', true);
		$("#vehDeviceName").attr('value', '');
		if($("#vehDeviceNameTip").length > 0) {			//判断提示信息是否存在
			$("#vehDeviceNameTip").remove();				//存在则删除
		}
		$("#vehDeviceName").css("border", "1px solid #A2B3BD");	//输入内容合法，恢复原样式
		$("#vehDeviceName").css("background", "#FFFFFF");
	}
}