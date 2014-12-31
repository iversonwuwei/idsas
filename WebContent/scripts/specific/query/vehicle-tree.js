$(function() {
	$.ajaxSetup({cache:false});
	$("#orgTree").jstree({
		"plugins" : [ "themes", "json_data", "ui", "crrm", "dnd", "search", "types" ],
		"json_data" : {
			"ajax" : {
				"url" : basePath + "query/vehicle-tree!getTree",
				"data" : function(n) {
					return {
						"id" : n.attr ? n.attr("id") : -1,
						"type" : n.attr ? n.attr("rel") : "default"
					};
				}
			}
		},
 		"search" : {
			"case_insensitive" : true,
			"ajax" : {
				"type": "POST",
				"url" : basePath + "query/vehicle-tree!search"
      		}
		},
		"core" : {
			"animation" : 150,
			"strings" : {
				loading : "正在加载，请稍后 ...",
				new_node : "新机构"
			}
		},
		"sort" : function (a, b) {
		    return (jQuery(a).attr ? parseFloat(jQuery(a).attr("sort")) : 0) > (jQuery(b).attr ? parseFloat(jQuery(b).attr("sort")) : 0) ? 1 : -1;
		},
		"types" : {
			"valid_children" : [ "root" ],
			"types" : {
				"root" : {
					"valid_children" : [ "default" ],
					"hover_node" : false,
					"move_node" : false,
					"delete_node" : false
				},
				"last" : {
					"valid_children" : [ "none" ],
					"create_node" : false,
					"move_node" : false
				},
				"default" : {
					"valid_children" : [ "default" ],
					"move_node" : false
				},
				"valid_children" : [ "default" ],
				"move_node" : false
			}
		}
	})
});


//搜索功能
function search() {
	$("#orgTree").jstree("close_all");// 关闭全部
	if ("" == $.trim($("#search_text").val())) {
		return;
	}
	$("#orgTree").jstree("search", $("#search_text").val());
}