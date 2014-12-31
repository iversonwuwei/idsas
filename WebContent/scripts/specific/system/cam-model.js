function doSave() {
	if($('#modelName').val() == null || $('#modelName').val() == "") {
		alert('Model Name should not be empty!');
		return false;
	}
	$.ajax({
	    type : "POST",
	    async : false,
	    dataType : 'json',
	    data : {
	    	'model_id' : $('#modelID').val(),
	    	'model_name' : $('#modelName').val()
	    },
	    url : basePath + "system/cam-model!isDuplicate",
	    success : function(d) {
			if("DUP" == d.result) {
				alert('Model Name already exists!');
				return;
			} else {
				document.queryResult.submit();
			}
	    },
	    error : function(d) {
			alert("Exception occurs!");
			return;
		}
	});
}
