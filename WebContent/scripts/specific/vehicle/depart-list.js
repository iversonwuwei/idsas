function doSave() {
	if($('#orgName').val() == "") {
		alert("Department Name should not be empty!");
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
	    	'parentID' : '1',
	    	'orgName' : $.trim($("#orgName").val())
	    },
	    url : basePath + "vehicle/depart-list!checkDuplicate",
	    success : function(d) {
			if("pass" != d.result){
				alert("The department already exists!");
				return;
			} else {
				document.queryResult.submit();
			}
	    },
	    error : function(d) {
			alert("ERROR!");
			window.history.back();
	    }
	});
}