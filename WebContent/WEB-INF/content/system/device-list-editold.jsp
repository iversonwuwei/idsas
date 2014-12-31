<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/content/inc/header.jsp" %>
<html>
	<head>
	<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/specific/system/devicelist.js"></script>
	<script type="text/javascript" charset="UTF-8" src="<%=basePath%>scripts/common/input-box.js"></script>
	<script src="<%=basePath%>scripts/common/dhtmlxCombo/dhtmlxcombo_5.js" type="text/javascript"></script>
	</head>
	<body>
		<custom:navigation father="Organization Management" model="Device" operate="Edit" saveMethod="sub1();" homePath="system/device-list" />
		<form action="<%=basePath%>system/device-list!create" name="queryResult" method="post">
			<input type="hidden" name="o_deviceno" id="o_deviceno" value="${o_deviceno}" /> 
			<input type="hidden" name="o_busid" id="o_busid" value="${o_busid}" /> 
			<input type="hidden" name="o_busname" id="o_busname" value="${o_busname}" /> 
			<div class="pop_main">
				<table cellspacing="0" cellpadding="0">
					<tr>
						<td class="text_bg" width="20%">Name:</td>
						<td colspan="5" style="text-align: left;" class="valid"><input type="text" id="o_devicename" name="o_devicename" maxlength="20" value="${o_devicename}" class="5,1,20"></td>
					</tr>
					<tr>
						<td class="text_bg">Type:</td>
						<td style="text-align: left;"  colspan="5">
							&nbsp;Vehicle terminal
						</td>
					</tr>
					<tr>
						<td class="text_bg">Status:</td>
						<td style="text-align: left;" class="valid"  colspan="5"> 
							<s:select list="searchList" listKey="key" listValue="value" name="o_devicedetype" id="o_devicedetype" theme="simple" />
							<input type="hidden" name="o_devicedescript" id="o_devicedescript" value="${o_devicedescript}" /> 
						</td>
					</tr>
					<tr>
						<td class="text_bg">Channel set:</td>
						<td style="width: 20%;border-right: 0px"> 
						&nbsp;channel：&nbsp;<s:select list="countList" listKey="key" listValue="value" id="chans" theme="simple"  cssStyle="width:110px"/>
						</td>
						<td style="width: 6%;text-align: right;border-left: 0px;border-right: 0px">
							camera:
						</td>
						<td colspan="3">
							<s:select id="videolist" list="channelList" listKey="key" listValue="value" headerKey="-1" headerValue="" theme="simple" cssStyle="width:150px;" onChange="change();"/> 
						</td>
					</tr>
					<tr>
						<td rowspan="4" class="text_bg" style="height: 88px">Video List:</td>
						<td id="td1" >&nbsp;&nbsp;&nbsp;1.&nbsp;
							<span id="sp1">
								<s:if test="%{cameras[0].cameracode!=null}">
								${cameras[0].cameracode}(${cameras[0].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[0]" name="cid[0]" value="${cameras[0].cameraid}"/>
							<a href="#" style="float: right;"> <img id="del1" onclick="del(1)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
						<td id="td2" style="width: 20%" colspan="2">&nbsp;&nbsp;&nbsp;2.&nbsp;
						 	<span id="sp2">
						 		<s:if test="%{cameras[1].cameracode!=null}">
								${cameras[1].cameracode}(${cameras[1].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
						 	</span>
						 		<input type="hidden" id="cid[1]" name="cid[1]" value="${cameras[1].cameraid}"/>
						 	<a href="#" style="float: right;"> <img id="del2" onclick="del(2)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						 </td>
						<td id="td3" style="width: 20%">&nbsp;&nbsp;&nbsp;3.&nbsp;
							<span id="sp3">
								<s:if test="%{cameras[2].cameracode!=null}">
								${cameras[2].cameracode}(${cameras[2].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[2]" name="cid[2]" value="${cameras[2].cameraid}"/>
							<a href="#" style="float: right;"> <img id="del3" onclick="del(3)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
						<td id="td4" style="width: 20%">&nbsp;&nbsp;&nbsp;4.&nbsp;
							<span id="sp4">
								<s:if test="%{cameras[3].cameracode!=null}">
								${cameras[3].cameracode}(${cameras[3].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[3]" name="cid[3]" value="${cameras[3].cameraid}"/>
							<a href="#" style="float: right;"> <img id="del4" onclick="del(4)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
					</tr>
					<tr>
						<td id="td5" >&nbsp;&nbsp;&nbsp;5.&nbsp;
							<span id="sp5">
								<s:if test="%{cameras[4].cameracode!=null}">
								${cameras[4].cameracode}(${cameras[4].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[4]" name="cid[4]" value="${cameras[4].cameraid}"/>
							<a href="#" style="float: right;"><img id="del5" onclick="del(5)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
						<td id="td6" colspan="2">&nbsp;&nbsp;&nbsp;6.&nbsp;
							<span id="sp6">
								<s:if test="%{cameras[5].cameracode!=null}">
								${cameras[5].cameracode}(${cameras[5].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[5]" name="cid[5]" value="${cameras[5].cameraid}"/>
							<a href="#" style="float: right;"> <img id="del6" onclick="del(6)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
						<td id="td7" >&nbsp;&nbsp;&nbsp;7.&nbsp;
							<span id="sp7">
								<s:if test="%{cameras[6].cameracode!=null}">
								${cameras[6].cameracode}(${cameras[6].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[6]" name="cid[6]" value="${cameras[6].cameraid}"/>
							<a href="#" style="float: right;"> <img id="del7" onclick="del(7)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
						<td id="td8" >&nbsp;&nbsp;&nbsp;8.&nbsp;
							<span id="sp8">
								<s:if test="%{cameras[7].cameracode!=null}">
								${cameras[7].cameracode}(${cameras[7].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[7]" name="cid[7]" value="${cameras[7].cameraid}"/>
							<a href="#" style="float: right;"> <img id="del8" onclick="del(8)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
					</tr>
					<tr>
						<td id="td9" >&nbsp;&nbsp;&nbsp;9.&nbsp;
							<span id="sp9">
								<s:if test="%{cameras[8].cameracode!=null}">
								${cameras[8].cameracode}(${cameras[8].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[8]" name="cid[8]" value="${cameras[8].cameraid}"/>
							<a href="#" style="float: right;"> <img id="del9" onclick="del(9)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
						<td id="td10" colspan="2">&nbsp;10.&nbsp;
							<span id="sp10">
								<s:if test="%{cameras[9].cameracode!=null}">
								${cameras[9].cameracode}(${cameras[9].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[9]" name="cid[9]" value="${cameras[9].cameraid}"/>
							<a href="#" style="float: right;"> <img id="del10" onclick="del(10)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
						<td id="td11" >&nbsp;11.&nbsp;
							<span id="sp11">
								<s:if test="%{cameras[10].cameracode!=null}">
								${cameras[10].cameracode}(${cameras[10].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[10]" name="cid[10]" value="${cameras[10].cameraid}"/>
							<a href="#" style="float: right;"> <img id="del11" onclick="del(11)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
						<td id="td12" >&nbsp;12.&nbsp;
							<span id="sp12">
								<s:if test="%{cameras[11].cameracode!=null}">
								${cameras[11].cameracode}(${cameras[11].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[11]" name="cid[11]" value="${cameras[11].cameraid}"/>
							<a href="#" style="float: right;"> <img id="del12" onclick="del(12)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
					</tr>
					<tr>
						<td id="td13" >&nbsp;13.&nbsp;
							<span id="sp13">
								<s:if test="%{cameras[12].cameracode!=null}">
								${cameras[12].cameracode}(${cameras[12].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[12]" name="cid[12]" value="${cameras[12].cameraid}"/>
							<a href="#" style="float: right;"> <img id="del13" onclick="del(13)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
						<td id="td14" colspan="2">&nbsp;14.&nbsp;
							<span id="sp14">
								<s:if test="%{cameras[13].cameracode!=null}">
								${cameras[13].cameracode}(${cameras[13].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[13]" name="cid[13]" value="${cameras[13].cameraid}"/>
							<a href="#" style="float: right;"> <img id="del14" onclick="del(14)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
						<td id="td15" >&nbsp;15.&nbsp;
							<span id="sp15">
								<s:if test="%{cameras[14].cameracode!=null}">
								${cameras[14].cameracode}(${cameras[14].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[14]" name="cid[14]" value="${cameras[14].cameraid}"/>
							<a href="#" style="float: right;"> <img id="del15" onclick="del(15)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
						<td id="td16" >&nbsp;16.&nbsp;
							<span id="sp16">
								<s:if test="%{cameras[15].cameracode!=null}">
								${cameras[15].cameracode}(${cameras[15].cameraname})
								</s:if>
								<s:else>
									NULL
								</s:else>
							</span>
								<input type="hidden" id="cid[15]" name="cid[15]" value="${cameras[15].cameraid}"/>
							<a href="#" style="float: right;"> <img id="del16" onclick="del(16)" src="<%=basePath%>images/dtree/ico_delete.gif" title="删除" /> </a>
						</td>
					</tr>
				</table>
			</div>
<script type="text/JavaScript">
	var video = dhtmlXComboFromSelect_5("videolist");//姓名
	video.enableFilteringMode(true);
	function change(){
		
		var camid=video.getSelectedValue();
		var index=$("#chans").val();
		//----
		$("#chans option[value=\'"+index+"\']").remove();
		
		
		//----
		var ind = index-1;
		var initcid ;
		var initcode;
		var newop;
		if($("#cid\\["+ind+"\\]").val()!=null&&$("#cid\\["+ind+"\\]").val()!=""&&$("#cid\\["+index+"\\]").val()!="-1"){
			initcid = $("#cid\\["+ind+"\\]").val();
			initcode = $("#sp"+index).html();
			initcode = initcode.split("(")[0];
			newop = {value : initcid, text : initcode};
		}
		$.ajax({
			url :  basePath +"system/device-list!getCam",
			type : "POST",
			async : false,
			data : {
				"cname" : camid
			},
			dataType : "json",
			success : function(data) {
				$("#sp"+index).html(data[0]+"("+data[1]+")");
				$("#cid\\["+ind+"\\]").val(data[2]);
				if(index<16){
					$("#chans").val(index*1+1);
				}
				video.deleteOption(camid);
			},
			error : function(json) {
				window.history.back();
			}
		});
		if(newop!=null){
			video._addOption(newop);
		}
	}
	function del(ind){
		
		var index=ind*1-1;
		var initcid ;
		var initcode;
		var newop;
		if($("#cid\\["+index+"\\]").val()!=null&&$("#cid\\["+index+"\\]").val()!=""&&$("#cid\\["+index+"\\]").val()!="-1"){
			if($("#chans option[value='"+index+"']").size() != 0){
				$("#chans option[value=\'"+index+"\']").after("<option value=\'"+ind+"\'>"+ind+"</option>");
			}else{
				$($('#chans option')[0]).before("<option value=\'"+ind+"\'>"+ind+"</option>");
			}
			$("#chans").val(ind);
			initcid = $("#cid\\["+index+"\\]").val();
			initcode = $("#sp"+ind).html();
			initcode = initcode.split("(")[0];
			newop = {value : initcid, text : initcode};
			video._addOption(newop);
			$("#sp"+ind).html("NULL");
			$("#cid\\["+index+"\\]").val(null);
		}
	}
</script>
		</form>
	</body>
</html>