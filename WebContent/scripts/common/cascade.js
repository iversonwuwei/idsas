/**
 * 初始化分公司菜单
 */
function cascadeSel_getCom() {
	var com_arr = new Array();
	$.ajax({
		type : 'POST',
		url : basePath + 'cascade!initCom',
		dataType : 'json',
		success : function(data) {
			if(data != null && data.result == "empty") {
				com_arr[0] = {value:'-1',text:''};
			} else {
				for(var i = 0, j = data.length; i < j; i++) {
					com_arr[i] = {value:data[i].key,text:data[i].value};
				}
			}
			select_company.addOption(com_arr);
			cascadeSel_setComValue();	//菜单赋初始值（上次查询内容）
		}
	});
}

/**
 * 选择分公司触发事件
 */
function cascadeSel_comChange() {
	if($('#v_select_team').length > 0) {	//存在选择车队菜单
		cascadeSel_getTeam();	//设置车队菜单内容
	}
	if($('#v_select_driver').length > 0 && $('#v_select_team').length == 0) {	//存在选择司机菜单且不存在车队菜单和线路菜单
		cascadeSel_getDriver();	//设置司机菜单内容
	}
}

/**
 * 获取车队菜单内容
 */
function cascadeSel_getTeam() {
	var team_arr = new Array();
	var orgVal = '-1';
	if($('#v_select_company').length > 0) {	//如果存在车队则取车队ID
		var val = select_company.getSelectedValue();
		if(!cascadeSel_isNULL(val)) {
			orgVal = val;
		}
	}
	$.ajax({
		type : 'POST',
		url : basePath + 'cascade!getTeamByCom',
		data : {
	    	"teamID" : orgVal
	    },
		dataType : 'json',
		success : function(data) {
			cascadeSel_teamClear();	//先清空原有选项
			team_arr[0] = {value:'-1',text:''};
			if(data != null && data.result == "empty") {
				//do nothing
			} else {
				for(var i = 0, j = data.length; i < j; i++) {
					team_arr[i+1] = {value:data[i].key,text:data[i].value};
				}
			}
			select_team.addOption(team_arr);
			cascadeSel_setTeamValue();	//菜单赋初始值（上次查询内容）
		}
	});
}

/**
 * 选择车队触发事件
 */
function cascadeSel_teamChange() {
	if($('#v_select_vehicle').length > 0 && $('#v_select_line').length == 0) {	//存在选择plate number菜单且不存在线路菜单
		cascadeSel_getVeh();	//设置车辆菜单内容
	}
	if($('#v_select_vehicle_name').length > 0 && $('#v_select_line').length == 0) {	//存在选择vehicle name菜单且不存在线路菜单
		cascadeSel_getVehName();	//设置车辆菜单内容
	}
}

/**
 * Plate Number
 */
function cascadeSel_getVeh() {
	var veh_arr = new Array();
	var orgVal = '-1';
	if($('#v_select_team').length > 0) {	//如果存在车队则取车队ID
		var val = select_team.getSelectedValue();
		if(!cascadeSel_isNULL(val)) {
			orgVal = val;
		}
	}
	$.ajax({
		type : "POST",
	    async : false,
	    url : basePath + "cascade!getVehByOrg",
	    data : {
	    	"teamID" : orgVal
	    },
	 	dataType : "json",
		success : function(data) {
			cascadeSel_vehClear();	//清空线路菜单
			veh_arr[0] = {value:'-1',text:''};
			if(data != null && data.result == "empty") {
				//do nothing
			} else {
		    	for(var i = 0; i < data.length; i++) {
		    		veh_arr[i+1]={value:data[i].key,text:data[i].value};
				}
			}
	    	select_vehicle.addOption(veh_arr);
	    	cascadeSel_setVehValue();	//菜单赋初始值（上次查询内容）
	    }
	});
}

/**
 * Vehicle name
 */
function cascadeSel_getVehName() {
	var veh_arr = new Array();
	var orgVal = '-1';
	if($('#v_select_team').length > 0) {	//如果存在车队则取车队ID
		var val = select_team.getSelectedValue();
		if(!cascadeSel_isNULL(val)) {
			orgVal = val;
		}
	}
	$.ajax({
		type : "POST",
		async : false,
		url : basePath + "cascade!getVehNameByOrg",
		data : {
			"teamID" : orgVal
		},
		dataType : "json",
		success : function(data) {
			cascadeSel_vehNameClear();	//清空车辆菜单
			veh_arr[0] = {value:'-1',text:''};
			if(data != null && data.result == "empty") {
				//do nothing
			} else {
				for(var i = 0; i < data.length; i++) {
					veh_arr[i+1]={value:data[i].key,text:data[i].value};
				}
			}
			select_vehicle_name.addOption(veh_arr);
			cascadeSel_setVehNameValue();	//菜单赋初始值（上次查询内容）
		}
	});
}

function cascadeSel_getDriver() {
	var driver_arr = new Array();
	var orgVal = '-1';
	if($('#v_select_company').length > 0) {	//如果存在分公司则取分公司ID
		var val = select_company.getSelectedValue();
		if(!cascadeSel_isNULL(val)) {
			orgVal = val;
		}
	}
	$.ajax({
		type : "POST",
	    async : false,
	    url : basePath + "cascade!getDriverByOrg",
	    data : {
	    	"comID" : orgVal
	    },
	 	dataType : "json",
		success : function(data) {
			cascadeSel_driverClear();	//清空线路菜单
			driver_arr[0] = {value:'-1',text:''};
			if(data != null && data.result == "empty") {
				//do nothing
			} else {
		    	for(var i = 0; i < data.length; i++) {
		    		driver_arr[i+1]={value:data[i].key,text:data[i].value};
				}
			}
	    	select_driver.addOption(driver_arr);
	    	cascadeSel_setDriverValue();	//菜单赋初始值（上次查询内容）
	    }
	});
}

function beforeSubmit() {
	if($('#v_select_company').length > 0) {	//存在分公司菜单
		cascadeSel_getComValue();
	}
	if($('#v_select_team').length > 0) {	//存在车队菜单
		cascadeSel_getTeamValue();
	}
	if($('#v_select_vehicle').length > 0) {	//存在车辆牌照菜单
		cascadeSel_getVehValue();
	}
	if($('#v_select_vehicle_name').length > 0) {	//存在车辆名称菜单
		cascadeSel_getVehNameValue();
	}
	if($('#v_select_driver').length > 0) {	//存在司机菜单
		cascadeSel_getDriverValue();
	}
}

/**
 * 公司getting
 */
function cascadeSel_getComValue() {
	$('#v_select_company').val(select_company.getSelectedValue());
}

/**
 * 公司setting
 */
function cascadeSel_setComValue() {
	var val = $('#v_select_company').val();
	select_company.setComboValue(val);
}

/**
 * 车队getting
 */
function cascadeSel_getTeamValue() {
	$('#v_select_team').val(select_team.getSelectedValue());
}

/**
 * 车队setting
 */
function cascadeSel_setTeamValue() {
	var val = $('#v_select_team').val();
	if(!cascadeSel_isNULL(val) && select_team.getIndexByValue(val) != -1) {
		select_team.setComboValue(val);
	} else {
		select_team.setComboValue("-1");
	}
}

/**
 * 车辆牌照号getting
 */
function cascadeSel_getVehValue() {
	$('#v_select_vehicle').val(select_vehicle.getSelectedValue());
}

/**
 * 车辆牌照号setting
 */
function cascadeSel_setVehValue() {
	var val = $('#v_select_vehicle').val();
	if(!cascadeSel_isNULL(val) && select_vehicle.getIndexByValue(val) != -1) {
		select_vehicle.setComboValue(val);
	} else {
		select_vehicle.setComboValue("-1");
	}
}

/**
 * 车辆名称（自编号）getting
 */
function cascadeSel_getVehNameValue() {
	$('#v_select_vehicle_name').val(select_vehicle_name.getSelectedValue());
}

/**
 * 车辆名称（自编号）setting
 */
function cascadeSel_setVehNameValue() {
	var val = $('#v_select_vehicle_name').val();
	if(!cascadeSel_isNULL(val) && select_vehicle_name.getIndexByValue(val) != -1) {
		select_vehicle_name.setComboValue(val);
	} else {
		select_vehicle_name.setComboValue("-1");
	}
}

/**
 * 司机getting
 */
function cascadeSel_getDriverValue() {
	$('#v_select_driver').val(select_driver.getSelectedValue());
}

/**
 * 司机setting
 */
function cascadeSel_setDriverValue() {
	var val = $('#v_select_driver').val();
	if(!cascadeSel_isNULL(val) && select_driver.getIndexByValue(val) != -1) {
		select_driver.setComboValue(val);
	} else {
		select_driver.setComboValue("-1");
	}
}

/**
 * 清空车队菜单
 */
function cascadeSel_teamClear() {
	select_team.setComboValue("");
	select_team.setComboText("");
	select_team.clearAll();
}

/**
 * 清空车辆牌照号菜单
 */
function cascadeSel_vehClear() {
	select_vehicle.setComboValue("");
	select_vehicle.setComboText("");
	select_vehicle.clearAll();
}

/**
 * 清空车辆名称（自编号）菜单
 */
function cascadeSel_vehNameClear() {
	select_vehicle_name.setComboValue("");
	select_vehicle_name.setComboText("");
	select_vehicle_name.clearAll();
}

/**
 * 清空司机菜单
 */
function cascadeSel_driverClear() {
	select_driver.setComboValue("");
	select_driver.setComboText("");
	select_driver.clearAll();
}

/**
 * 判断空值
 * @param val
 * @returns {Boolean}	true：空值；false：非空
 */
function cascadeSel_isNULL(val) {
	var check = false;
 	if(val == null || val == "" || val == "-1"){
		check=true;
	}
	return check;
}