function del(id){
	if(confirm('Be sure you want to delete this data?')){
		$.ajax({
			type: "POST",
			async: false,
			dataType:'text',
			data :{
				id : id,
			},
			url : basePath + "system/device-status!checkDel",
			success : function(dd) {
				if("true" == dd) {
					alert("The status is using！");
					return;
				}else{
					location=basePath+"system/device-status/"+id+"/delete?editble=1";
				} 
			},
			error : function(dd) {
				alert("Exception occurs!");
				window.history.back();
			}
		});
	}
}