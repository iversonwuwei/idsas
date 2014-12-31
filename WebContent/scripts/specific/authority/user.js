function doSave() {
	//$("#orgstr").val(getDate('orgTree'));
	if("" == $.trim($("#loginName").val())) {
		alert("Please enter the Account!");
		$("#loginName").focus();
		return;
	}
	if("" == $.trim($("#userName").val())) {
		alert("Please enter the Name!");
		$("#userName").focus();
		return;
	}
	//test();
	/*if("" == $("#orgstr").val() || null == $("#orgstr").val()) {
		alert("请选择所在部门！");
		return;
	}*/
	
	var t = $("#e_mail").val();
	//对电子邮件的验证
	if ("" != t) {
		var myreg = /^([a-za-z0-9]+[_|_|.]?)*[a-za-z0-9]+@([a-za-z0-9]+[_|_|.]?)*[a-za-z0-9]+.[a-za-z]{2,3}$/;
		if(!myreg.test(t)) {
	       alert('Email is wrong');
	       $("#e_mail").select();
	       return;
		}
	}
	
	if("" == getDate('departmentTree') || null == getDate('departmentTree')) {
		alert("Please select the Department!");
		return;
	}
	if("" == getDate('dataTree') || null == getDate('dataTree')) {
		alert("Please select the Visible!");
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
	    	'userID' : $.trim($("#userID").val()),
	    	'names' : $.trim($("#loginName").val()),
	    	'code' : $.trim($("#userCode").val()),
	    	'userName' : $.trim($("#userName").val())
	    },
	    url : basePath + "authority/user!check",
	    success : function(dd) {
			if("loginNameRep" == dd) {
				alert("Account repetition!");
				$("#loginName").select();
				return;
			} 
			if("userCodeRep" == dd) {
				alert("User Code repetition");
				$("#userCode").select();
				return;
			}
			if("userNameRep" == dd) {
				alert("Name repetition,Requests to be added!");
				$("#userName").select();
				return;
			}
			if("success" == dd){
				$('#userDepart').val(getDate('departmentTree'));
				$('#visibleStr').val(getDate('dataTree'));
	    		document.queryResult.submit();
	    	}
	    },
	    error : function(dd) {
			alert("Exception occurs!");
			window.history.back();
		}
	});
}