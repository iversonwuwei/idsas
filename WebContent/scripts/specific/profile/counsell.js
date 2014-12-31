function sub1() {
	if("" == $.trim($("#startime").val())) {
		alert("please entering the Start Time!");
		$("#startime").focus();
		return;
	}
	if("" == $.trim($("#endtime").val())) {
		alert("please entering the End Time!");
		$("#endtime").focus();
		return;
	}
	if("" == $.trim($("#remark").val())) {
		alert("please entering the Remark!");
		$("#remark").focus();
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
	var str1= $("#userid").find("option:selected").text();
	$("#username").val(str1);
	var str2= $("#driverid").find("option:selected").text();
	$("#drivername").val(str2);
	document.queryResult.submit();
}
function sub2() {
	if("" == $.trim($("#startime").val())) {
		alert("please entering the Start Time！");
		$("#startime").focus();
		return;
	}
	if("" == $.trim($("#endtime").val())) {
		alert("please entering the End Time!");
		$("#endtime").focus();
		return;
	}
	if("" == $.trim($("#remark").val())) {
		alert("please entering the Remark!");
		$("#remark").focus();
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
	var str2= $("#driverid").find("option:selected").text();
	$("#drivername").val(str2);
	document.queryResult.submit();
}