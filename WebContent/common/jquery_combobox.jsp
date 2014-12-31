<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "No-cache");
	response.setDateHeader("Expires", -10);
	String path = request.getContextPath() + "/";
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<head>
<meta name="author" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/js/jquery-1.8.3.js"></script>
<script src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.core.js"></script>
<script src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.widget.js"></script>
<script src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.position.js"></script>
<script src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.menu.js"></script>
<script src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.autocomplete.js"></script>
<script src="<%=basePath%>scripts/common/jquery-ui-1.9.2.custom/development-bundle/ui/jquery.ui.tooltip.js"></script>
</head>
<select id="${param.name}" name="${param.name}"></select>
<script type="text/javascript">
$.widget("ui.combobox",{
	_create : function() {
		var self = this;
		var select = this.element.hide(), selected = select.children(":selected"), value = selected.val() ? selected.text() : "";
		var input = $("<input />").click(function() {
				input.autocomplete("search", $(input).val());
			}).change(function() {
				input.autocomplete("search", $(input).val());
				}).insertAfter(select).val(value).autocomplete({
					delay : 0,
					minLength : 0,
					source : function(request, response) {
					// 根据输入的搜索字符创建select数据
					initSelected($(input).val(),null);
					var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term),"i");
					response(
							select.children("option").map(
									function() {var text = $(this).text();
										if (this.value&& (!request.term || matcher.test(text)))
												return {
													label : text.replace(
															// 匹配部分加粗
															new RegExp("(?![^&;]+;)(?!<[^<>]*)("+ $.ui.autocomplete.escapeRegex(request.term) + ")(?![^<>]*>)(?![^&;]+;)","gi"),"<strong>$1</strong>"),
													value : text,
													option : this
												};
											}));
										},
								select : function(event, ui) {
										ui.item.option.selected = true;
												self._trigger("selected",event,{
																	item : ui.item.option
												});
											},
								change : function(event, ui) {
											if (!ui.item) {
											var matcher = new RegExp(	"^"+ $.ui.autocomplete.escapeRegex($(this).val()) + "$", "i"), valid = false;
											select.children("option").each(
													function() {
														if (this.value.match(matcher)) {
																	this.selected = valid = true;
																	return false;
																}
															});
													if (!valid) {
														$(this).val("");
														select.val("");
														return false;
													}
												}
											}
										})
								input.data("autocomplete")._renderItem = function(ul,item) {
										return $("<li></li>").data("item.autocomplete", item).append("<a>" + item.label + "</a>").appendTo(ul);
						};
					}
				});
// 创建select数据
// text, 输入的字符  value 数据的value(id)
function initSelected(text, value) {
	$.ajax({
		type : "POST",
		async : false,
		dataType : 'json',
		data : {
			"search_text" : text,
			"search_value" : value
		},
		url : "<%=basePath%>${param.url}",
		success : function(data) {
			$(document.getElementById('${param.name}')).empty();
			$("<option ></option>").appendTo(
					$(document.getElementById('${param.name}')))
			for ( var i = 0; i < data.length; i++) {
				$("<option "+ ((null == value || "" == value) ? "" : "selected='selected'") + " value=" + data[i].key + ">" + data[i].value + "</option>").appendTo($(document.getElementById('${param.name}')))
			}
		}
	});
}
// 初始化select数据
initSelected(null, '${param.value}');
// 创建autocomplate_combox
$(document.getElementById('${param.name}')).combobox();
</script>
