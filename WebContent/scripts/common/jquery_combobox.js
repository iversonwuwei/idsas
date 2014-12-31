$.widget("ui.combobox",{
	_create : function() {
		var self = this;
		var select = this.element.hide(), selected = select.children(":selected"), value = selected.val() ? selected.text() : "";
		var input = $("<input />").click(function() {
				$(input).val("");//点击清空输入框
				input.autocomplete("search", $(input).val());
			}).change(function() {
				input.autocomplete("search", $(input).val());
				}).insertAfter(select).val(value).autocomplete({
					delay : 0,
					minLength : 0,
					source : function(request, response) {
					var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term),"i");
					response(
							select.children("option").map(
									function() {var text = $(this).text();
										if (this.value&& (!request.term || matcher.test(text)))
												return {
													label : text.replace(
													// 匹配部分加粗
													new RegExp("(?![^&;]+;)(?!<[^<>]*)("+ $.ui.autocomplete.escapeRegex(request.term) + ")(?![^<>]*>)(?![^&;]+;)","gi"),"<strong>$1</strong>"
													),
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
				
function toAutoComplate(id){
	$(document.getElementById(id)).combobox();
}