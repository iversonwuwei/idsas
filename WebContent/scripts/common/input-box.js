/**
 * 只能输入数字，可按退格键删除数字
 * @param id  页面传过来的id值
 * @return
 */
function inputNum(id) {
	//通过jquery的id选择器绑定keypress事件
	$("#"+id).bind("keypress",function vaildIntegerNumber(evnt){
		//判断浏览器：IE、火狐  获得相对应的事件对象
		evnt = evnt || window.event;
		var keyCode = window.event?evnt.keyCode:evnt.which;
		//键盘0-9 对应的keyCode值为48-57, keyCode==8为"BackSpace"
		return keyCode >= 48 && keyCode <= 57 || keyCode == 8;
	});
}
function inputNum_(id) {
	//通过jquery的id选择器绑定keypress事件
	$("#"+id).bind("keypress",function vaildIntegerNumber(evnt){
		//判断浏览器：IE、火狐  获得相对应的事件对象
		evnt = evnt || window.event;
		var keyCode = window.event?evnt.keyCode:evnt.which;
		//键盘0-9 对应的keyCode值为48-57, keyCode==8为"BackSpace"
		return keyCode >= 48 && keyCode <= 57 || keyCode == 8;
	}).bind("keyup",function(){
		this.value=this.value.replace(/\D/g,'');		
		//或者$(this).val($(this).val().replace(/\D/g,''))
	});
}
/**
 * 只能输入数字和点,可按退格键删除数字或点
 * @param id 页面传过来的id值 
 * @return
 */
function inputFloatNum(id) {
	//通过jquery的id选择器绑定keypress事件
	$("#"+id).bind("keypress", function vaildFloatNumber(evnt){
		//判断浏览器：IE、火狐  获得相对应的事件对象
		evnt = evnt || window.event;
		var keyCode = window.event?evnt.keyCode:evnt.which;
		//键盘0-9 对应的keyCode值为48-57, keyCode==8为"BackSpace", keyCode == 46为"."
		return keyCode >= 48 && keyCode <= 57 || keyCode == 8 || keyCode == 46;
	});
}

/**
 * 只能输入数字和一个点,可按退格键删除数字或点
 * @param id 页面传过来的值
 * @param 
 * @return
 */
function inputOneFloatNum(id) {
	//通过jquery的id选择器绑定keypress事件
	$("#"+id).bind("keypress", function vaildFloatNumberLimitDecimalPoint(evnt){
		//判断浏览器：IE、火狐  获得相对应的事件对象
		evnt = evnt || window.event;
		var keyCode = window.event ? evnt.keyCode : evnt.which;
		//判断输入值是否含有"."   如果含有，返回false 
		if($("#"+id).val().indexOf(".") != -1 && keyCode == 46) {    
			return false;
		}       
	return keyCode >= 48 && keyCode <= 57 || keyCode == 46|| keyCode == 8;
	});
}

/**
 * 只能输入数字和一个点，且输入的第一个字符不能为点，可按退格键删除数字或点
 * @param evnt
 * @param obj
 * @return
 */
function inputFirstNotFloatNum(id){
	//通过jquery的id选择器绑定keypress事件
	$("#"+id).bind("keypress", function vaildFloatNumberLimitDecimalPoint(evnt){
		//判断浏览器：IE、火狐  获得相对应的事件对象
		evnt = evnt || window.event;
		var keyCode = window.event ? evnt.keyCode : evnt.which;
		//判断当值长度为0时，禁止输入"." 返回false。不为0时再判断是否含有”.“，如果含有，返回false	
		if(($("#"+id).val().length==0 || $("#"+id).val().indexOf(".")!=-1) && keyCode==46 ){  
			return false;
		}
	return keyCode >= 48 && keyCode <= 57 || keyCode == 46 || keyCode == 8;
	});
}

/**
 * 输入小写字母自动变成大写
 * 触发事件用onKeyUp="toUpperCase(this)"
 * @param obj 
 * @return
 */
function toUpperCase(obj) {
	obj.value = obj.value.toUpperCase();
}

/**
 * input里输入大写字母自动变成小写
 * 触发事件用onKeyUp="toLowerCase(this);" 
 * @param obj
 * @return
 */
function toLowerCase(obj) {
	obj.value = obj.value.toLowerCase();
}

/**
 * input里只能输入中文
 * 触发事件需要用onKeyUp="inputChinese(this);"
 * @param obj
 * @return
 */
function inputChinese(obj) {
	obj.value=obj.value.replace(/[^\u4E00-\u9FA5]/g,'');
	return;
}