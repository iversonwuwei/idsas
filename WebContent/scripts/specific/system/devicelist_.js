$(document).ready(function() {
	$(".eee").each(function(i,t){
		inputNum_($(t).attr("id"));
	});
});

function del(id){
	if(confirm('Be sure you want to delete this data?')){
		$.ajax({
			type: "POST",
			async: false,
			dataType:'text',
			data :{
				id : id,
			},
			url : basePath + "system/device-list!checkDel",
			success : function(dd) {
				if("true" == dd) {
					alert("The device is using");
					return;
				}else{
					location=basePath+"system/device-list/"+id+"/delete?editble=1";
				} 
			},
			error : function(dd) {
				alert("Exception occurs!");
				window.history.back();
			}
		});
	}
}