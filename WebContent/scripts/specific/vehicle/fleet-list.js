/**
 * save operation
 */
function doSave() {
	if($('#orgName').val() == "") {
		alert("Please enter the Fleet Name!");
		return;
	}
	var parentID = select_company.getSelectedValue();
	if(parentID == null || parentID == "-1" || parentID == "") {
		alert("Place select a department!");
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
	checkDuplicate();
}

/**
 * check if the fleet is duplicate
 */
function checkDuplicate() {
	$.ajax({
	    type: "POST",
	    async: false,
	    dataType: 'JSON',
	    data :{
	    	'orgID' : $.trim($("#orgID").val()),
	    	'parentID' : select_company.getSelectedValue(),
	    	'orgName' : $.trim($("#orgName").val())
	    },
	    url : basePath + "vehicle/fleet-list!checkDuplicate",
	    success : function(d) {
			if("pass" != d.result){
				alert("The fleet under the department already exists!");
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