// JavaScript Document	js通用工具
/**
 * 修改所有checkbox状态
 * @param name	checkbox name
 * @param state	checkbox state
 */
function setAllCheckboxState(name,state) {
	var elms = document.getElementsByName(name);
	for(var i = 0; i < elms.length; i++) {
		elms[i].checked = state;
	}
}

/**
 * 扫描并增加验证功能
 * @return
 */
function addValidate() {
	var elm = null;
	var args = new Object();
	var eID = new Object();
	$(".valid").each(function(){	//遍历所有增加验证的组件
		$(this).children(":text").each(function() {	//查找所有text 类型的input标签
			args[$(this).attr("id")] = $(this).attr("class");
			eID[$(this).attr("id")] = $(this).attr("id");
			//增加title属性提示信息
			if(typeof($(this).attr("id")) != "undefined") {
				$(this).attr("title" , getMess($(this).attr("class").split(',')[0], $(this).attr("class").split(',')[1], $(this).attr("class").split(',')[2], $(this).attr("class").split(',').length == 4 ? "" : $(this).attr("class").split(',')[4]));
			}
//			$(this).focus(function(event){
//				roleMess(eID[event.target.id], args[event.target.id].split(',')[0], args[event.target.id].split(',')[1], args[event.target.id].split(',')[2], args[event.target.id].split(',').length == 4 ? "" : args[event.target.id].split(',')[4]);
//			});
			$(this).blur(function(event){
				checkContext(eID[event.target.id], args[event.target.id].split(',')[0], args[event.target.id].split(',')[1], args[event.target.id].split(',')[2], args[event.target.id].split(',').length == 4 ? "" : args[event.target.id].split(',')[3]);
			});
			$(this).keyup(function(event){
				pressLimit(args[event.target.id].split(',')[0],this);
			});
		});
	});
}


function getMess(role, minLength, maxLength, message) {
	switch(role) {	//根据规则写入提示信息
		case '0':		//正整数（不包括0）
			return (minLength == maxLength ? minLength : "(" + minLength +" ~ " + maxLength + ")") + "-digit positive integer!";
			break;
		case '1':		//整数（包括0）
			return (minLength == maxLength ? minLength : "(" + minLength +" ~ " + maxLength + ")") + "-digits integer!";
			break;
		case '2':		//小数
			return (minLength == maxLength ? minLength : "(" + minLength +" ~ " + maxLength + ")") + "-digit positive number,up to two decimal places!";
			break;
		case '3':		//数字或字母
			return "Length of " + (minLength == maxLength ? minLength : "(" + minLength +" ~ " + maxLength + ")") + " numbers or letters!";
			break;
		case '4':		//自定义提示内容
			return message;
			break;
		default:	//只限制长度
			return "Length of " + (minLength == maxLength ? minLength : "（" + minLength +" ~ " + maxLength + "）") + " contents";
	}
}

/**
 * 提示规则信息
 * @param elemID	元素ID
 * @param role		提示规则
 * @param maxLength	内容限制最大长度
 * @param minLength	内容限制最小长度
 * @param message	自定义提示内容
 * @return	show message
 */
function roleMess(elemID, role, minLength, maxLength, message) {
	if($("#" + elemID + "Tip").length > 0) {			//判断提示信息是否存在
		$("#" + elemID + "Tip").remove();				//存在则删除
	}
	switch(role) {	//根据规则写入提示信息
		case '0':		//正整数（不包括0）
			$("#" + elemID).parent().append("<span id=\"" + elemID + "Tip\" class=\"vspan\"><font color=\"red\">&nbsp;" + (minLength == maxLength ? minLength : "（" + minLength +" ~ " + maxLength + "）") + "&nbsp;-digit positive integer!</font></span>");
			break;
		case '1':		//整数（包括0）
			$("#" + elemID).parent().append("<span id=\"" + elemID + "Tip\" class=\"vspan\"><font color=\"red\">&nbsp;" + (minLength == maxLength ? minLength : "（" + minLength +" ~ " + maxLength + "）") + "&nbsp;-digits integer!</font></span>");
			break;
		case '2':		//小数
			$("#" + elemID).parent().append("<span id=\"" + elemID + "Tip\" class=\"vspan\"><font color=\"red\">&nbsp;" + (minLength == maxLength ? minLength : "（" + minLength +" ~ " + maxLength + "）") + "&nbsp;-digit positive number,up to two decimal places!</font></span>");
			break;
		case '3':		//数字或字母
			$("#" + elemID).parent().append("<span id=\"" + elemID + "Tip\" class=\"vspan\"><font color=\"red\">Length of&nbsp;" + (minLength == maxLength ? minLength : "（" + minLength +" ~ " + maxLength + "）") + "&nbsp;numbers or letters!</font></span>");
			break;
		case '4':		//自定义提示内容
			$("#" + elemID).parent().append("<span id=\"" + elemID + "Tip\" class=\"vspan\"><font color=\"red\">" + message + "</font></span>");
			break;
		default:	//只限制长度
			$("#" + elemID).parent().append("<span id=\"" + elemID + "Tip\" class=\"vspan\"><font color=\"red\">&nbsp;" + (minLength == maxLength ? minLength : "（" + minLength +" ~ " + maxLength + "）") + "&nbsp;length!</font></span>");
	}
}

/**
 * 验证id控件内容是否为空  2012-2-20增加trim去空格功能
 * @param elemID	元素ID
 * @return true:not empty;	false:empty
 */
function checkEmpty(elemID) {
	
	if("-1" == jQuery.trim($("#" + elemID).val()) || "" == jQuery.trim($("#" + elemID).val()) || null == jQuery.trim($("#" + elemID).val())) {
		return false;
	}
	return true;
}

/**
 * 验证输入内容是否合法，非法则警告
 * @param elemID	元素ID
 * @param role		验证规则
 * @param maxLength	限制最大长度
 * @param minLength	限制最小长度
 * @param expression 自定义规则
 */
function checkContext(elemID, role, minLength, maxLength, expression) {
	var value = $("#" + elemID).val();
	if($("#" + elemID + "Tip").length > 0) {			//判断提示信息是否存在
		$("#" + elemID + "Tip").remove();				//存在则删除
	}
	if(!validate(value, role, minLength, maxLength, expression) && value != "") {
		$("#" + elemID).css("border", "2px solid #c00");	//输入内容非法，变色警告
		$("#" + elemID).css("background", "#DDC7C7");
		$("#" + elemID).parent().append("<span id=\"" + elemID + "Tip\" class=\"novalid\"></span>");
	} else {
		$("#" + elemID).css("border", "1px solid #A2B3BD");	//输入内容合法，恢复原样式
		$("#" + elemID).css("background", "#FFFFFF");
	}
}

/**
 * 正则验证
 * @param num	验证内容
 * @param isDouble	是否是小数（保留5位） true:小数;	false:整数
 * @return
 */
function validate(value, role, minLength, maxLength, expression) {
	switch(role) {
		case '0':		//正整数（不包括0）
			var reg = "/^\\+?[1-9][0-9]{" + (minLength < 1 ? 0 : minLength - 1) + "," + (maxLength-1) + "}$/";
			break;
		case '1':		//整数（包括0）
			var reg = "/^\\+?[0-9]{" + minLength + "," + maxLength + "}$/";
			break;
		case '2':		//正小数
			var reg = "/^\\+?\\d{" + (minLength < 0 ? 0 : minLength) + "," + (maxLength < 0 ? 0 : maxLength) + "}(\\.\\d{1,2})?$/";
			break;
		case '3':		//数字或字母或下划线
			var reg = "/^[A-Za-z0-9]{" + minLength + "," + maxLength + "}$/";
			break;
		case '4':		//自定义规则
			var reg = expression;
			break;
		default:	//只限制长度
			var reg = "/^.{" + minLength + "," + maxLength + "}$/";
	}
	reg = eval(reg);	//变字符串为js对象
	if(!reg.test(value)) {
		return false;
	}
	return true;
}
/**
 * 添加输入限制
 * @param limID
 * @param dom
 * @return
 */
function pressLimit(limID,dom){
	if('0'==limID||'1'==limID){
		dom.value=dom.value.replace(/\D/g,'');		
	}
}
/**
 * 显示当前时间
 * @param domID(节点ID)
 * @return
 */
function showToday(domID){
	var today=new Date();
	$("#"+domID).val(today.getFullYear()+"-"+((today.getMonth()+1)<10?"0"+(today.getMonth()+1):today.getMonth()+1)+"-"+(today.getDate()<10?"0"+today.getDate():today.getDate()));
}
/**
 * 空值验证(对象验证)
 * @return 空 返回true 非空 返回false
 */
function isNULL(_var){
	var check=false;
	if(null==_var||""==_var||"-1"==_var||typeof(_var)=='undefined'||0==_var){
		check=true;
	}
	return check;
}
/**
 * 空值验证(参数：DOM id )
 * @param domID
 * @return 空 返回true 非空 返回false
 */
function $isNULL(domID){
	var dom=$("#"+domID).val(),check=false;
	if(document.getElementById(domID).nodeName=="SPAN"){
		dom=$("#"+domID).html();
	}
	if(null==dom||""==dom||"-1"==dom||typeof(document.getElementById(domID))=='undefined'){
		check=true;
	}
	return check;
}

/**
 * @param 用于截取掉名字后面的说明
 * @param name
 * @return
 */
function getName(name) {
	var _, __;
	_ = [ "-", "(", "（", ")", "）" ];
	__ = null == name ? "" : name;
	for ( var i = 0; i < _.length; i++) {
		if (1 != __.split(_[i])) {
			__ = __.split(_[i])[0];
		}
	}
	return __;
}
/**
 * @param 添加验证
 * @param type 验证类型(目前只有整数和小数)
 * @param id Domid
 * @param msg 提示信息
 * @return
 */
function inputCheck(type,id,msg){
	if("" == $("#"+id).val()){
		return;
	}
	if(1 == type){//15位纯数字
		if(!/^\d{0,15}$/.test($("#"+id).val())){
			alert("你输入的"+((""==msg)?"信息":msg))+"不合法！";
			$("#"+id).val($("#"+id).val().replace(/\D/g,""));
		}
	}
	if(2 == type){//小数
		if(!/^\d{0,15}(\.\d{1,5})?$/.test($("#"+id).val())){
			alert("你输入的"+((""==msg)?"信息":msg))+"不合法！";
			$("#"+id).val($("#"+id).val().replace(/\D/g,""));
		}
	}
}
// Email的正则
var emailReg = /^([a-za-z0-9]+[_|_|.]?)*[a-za-z0-9]+@([a-za-z0-9]+[_|_|.]?)*[a-za-z0-9]+.[a-za-z]{2,3}$/i;
// 显示类似title那样的标签
function showTooltip(x, y, contents) {
	$("<div id='tooltip'>" + contents + "</div>").css({
		position: "absolute",
		display: "none",
		top: y + 5,
		left: x + 5,
		border: "1px solid #fdd",
		padding: "4px",
		"background-color": "#fee",
		opacity: 0.80
	}).appendTo("body").fadeIn(200);
}