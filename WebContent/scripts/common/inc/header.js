/**
 * 页面初始化
 */
$(function(){
	toAutoHeight();	//自动高度
	enterSearch();	//回车触发搜索
	changeTrColor();			//表格奇偶行变色
	if($("#maintable").length > 0) {	//maintable表格如果存在，冻结第一行
		var mysuperTable = new superTable("maintable",{headerRows : 1,fixedCols : 0});
	}
	addValidate();	//输入框验证提示
});

//main div自动高度
function toAutoHeight() {
	var _height = $(window).height()
		-( null == $(".welcome").height() ? 0 :  $(".welcome").height())
		- (null == $(".search_g").height() ? 0 :  $(".search_g").height())
		- (null == $(".page").height() ? 0 :  $(".page").height());
	if (null != $(".main").height()) {
		$(".main").height(_height-20);
		return;
	}
	if (null != $(".pop_main").height()) {
		$(".pop_main").height(_height);
	}
	if (null != $(".chart_main").height()) {
		$(".chart_main").height($(window).height() - 100);
	}
}

// 奇偶行变色以及高亮
function changeTrColor(){
	// id为hidden_input的标签用视为标识段
	// 单元格合并后添加此标签，不用该方法变色
	// 否则乱套了
	if(0 == $("form[name='queryResult']").length || 0 != $("input[id='hidden_input']").length){
		return;
	}
	/** 奇行颜色,偶行颜色,鼠标掠过颜色,高亮颜色* */
	var color = [ "#FFFFFF", "#f7f7f7", "#EBF2F9", "#6495DC" ];
	var bg = "background";
	// 奇数
	$(".main table tbody tr:even").css(bg, color[0])
	.bind("mouseover", function() {
		$(this).css(bg, color[2]);
	}).bind("mouseout", function() {
		$(this).css(bg, color[0]);
	});
	//偶数
	$(".main table tbody tr:odd").css(bg, color[1])
	.bind("mouseover", function() {
		$(this).css(bg, color[2]);
	}).bind("mouseout", function() {
		$(this).css(bg, color[1]);
	});
}

//回车搜索
function enterSearch(){
	$(document).bind("keydown",function(e){
		e = e ? e : window.event;
		var code = e.which ? e.which : e.keyCode;
		if(code == 13){
			$("a[title='search']").click();
		}
	});
}

/**
 * 注销
 */
function logout() {
	if (confirm("Are you sure to exit GY-Track ?")) {
		$.ajax( {
			type : "POST",
			async : false,
			url : basePath + "login!logout",
			success : function() {
				window.parent.location = basePath + "login";
			},
			error : function() {
				window.parent.location = basePath + "login";
			}
		});
	}
}

$(document).ajaxStart(function () {
    $.blockUI({
    	message : "<img id='displayImg' src='" + basePath + "images/loading.gif' alt='x' />",
		overlayCSS : {
			opacity : 0.4
		},
		css : {
			border : 'none',
			width : '37px',
			background : 'none',
			left : $(window).width()/2 - 37
		}
    });
});

$(document).ajaxStop(function () {
    // 直接调用，无延时
    $.unblockUI();
});

/**
 * 提交搜索
 */
function sub() {
	beforeSubmit();
	document.queryResult.submit();
}