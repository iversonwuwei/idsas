var isPass = true;

/**
 * save operation
 */
function doSave() {
	if($('#policeName').val() == "") {
		alert("Please enter the Police Name!");
		return;
	}
	var deptID = select_company.getSelectedValue();
	if(deptID == null || deptID == "-1" || deptID == "") {
		alert("Please select a department!");
		return;
	}
	$('#deptName').val(select_company.getComboText());
	var fleetID = select_team.getSelectedValue();
	if(fleetID == null || fleetID == "-1" || fleetID == "") {
		alert("Please select a fleet!");
		return;
	}
	$('#fleetName').val(select_team.getComboText());
//	var deviceID = $('#deviceID').val();
//	if(deviceID == null || deviceID == "-1" || deviceID == "") {
//		alert("Please select a device!");
//		return;
//	}
	$('#deviceName').val($('#deviceID').find('option:selected').text());
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
	checkDuplicate();
}

function getDeviceByDept() {
	$.ajax({
	    type: "POST",
	    async: false,
	    dataType: 'JSON',
	    data :{
	    	'vo.deptID' : select_company.getSelectedValue()
	    },
	    url : basePath + "vehicle/police!getDeviceByDept",
	    success : function(data) {
	    	$('#deviceID').empty();
	    	$('#deviceID').append("<option value='-1'>Please select</option>");
			if(null != data){
				for (var int = 0; int < data.length; int++) {
					$('#deviceID').append("<option value='" + data[int].key + "'>" + data[int].value + "</option>");
				}
			}
			if($('#v_deviceID').val() != null && $('#v_deviceID').val() != "") {	//edit condition inital
				$('#deviceID').append("<option value='" + $('#v_deviceID').val() + "'>" + $('#deviceName').val() + "</option>");
				$('#deviceID').val($('#v_deviceID').val());
			}
	    },
	    error : function(d) {
			alert("ERROR!");
	    }
	});
}

/**
 * check if the police name is duplicate
 */
function checkDuplicate() {
	$.ajax({
	    type: "POST",
	    async: false,
	    dataType: 'JSON',
	    data :{
	    	'vo.fleetID' : select_team.getSelectedValue(),
	    	'vo.policeID' : $.trim($("#policeID").val()),
	    	'vo.policeName' : $.trim($("#policeName").val())
	    },
	    url : basePath + "vehicle/police!checkDuplicate",
	    success : function(d) {
			if("pass" != d.result){
				alert("The police name already exists!");
				return;
			} else {
				beforeSubmit();
				document.queryResult.submit();
			}
	    },
	    error : function(d) {
			alert("ERROR!");
			window.history.back();
	    }
	});
}