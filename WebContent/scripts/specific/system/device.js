function sub1() {
	if("" == $.trim($("#statusname").val())) {
		alert("please entering the status name!");
		$("#statusname").focus();
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
	    	'ids' : $.trim($("#statusid").val()),
	    	'names' : $.trim($("#statusname").val()),
	    },
	    url : basePath + "system/device-status!checkEdit",
	    success : function(dd) {
			if("true" == dd) {
				alert("The Name has already exists!");
				$("#statusname").select();
				return;
			} 
			document.queryResult.submit();
	    },
	    error : function(dd) {
			alert("Exception occurs!");
			window.history.back();
		}
	});
}
function sub2() {
	if("" == $.trim($("#statusname").val())) {
		alert("please entering the statusname!");
		$("#statusname").focus();
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
			'names' : $.trim($("#statusname").val()),
		},
		url : basePath + "system/device-status!checkAdd",
		success : function(dd) {
			if("true" == dd) {
				alert("The Name has already exists!");
				$("#statusname").select();
				return;
			} 
			document.queryResult.submit();
		},
		error : function(dd) {
			alert("Exception occurs!");
			window.history.back();
		}
	});
}
