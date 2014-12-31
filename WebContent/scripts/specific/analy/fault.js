function sub2() {
	if("" == $.trim($("#code").val())) {
		alert("please entering the Code!");
		$("#code").focus();
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
	    	'ids' : $.trim($("#faultcodeid").val()),
	    	'names' : $.trim($("#code").val())
	    },
	    url : basePath + "analy/fault-dict!checkEdit",
	    success : function(dd) {
			if("true" == dd) {
				alert("The Code has already exists!");
				$("#code").select();
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
function sub1() {
	if("" == $.trim($("#code").val())) {
		alert("please entering the Code!");
		$("#code").focus();
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
			'names' : $.trim($("#code").val())
		},
		url : basePath + "analy/fault-dict!checkAdd",
		success : function(dd) {
			if("true" == dd) {
				alert("The Code has already exists!");
				$("#code").select();
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